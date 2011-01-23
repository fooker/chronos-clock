package chronos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.CommPortIdentifier;

public class Controller {

  static {
    System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
  }

  public static enum LogLevel {

    ERROR("Error"),
    WARNING("Warning"),
    INFO("Info"),
    DEBUG("Debug");

    private String text;

    private LogLevel(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    @Override
    public String toString() {
      return this.text;
    }
  }

  public static interface Listener {

    public abstract void synced();

    public abstract void connectionChanged(boolean connected);

    public abstract void log(LogLevel level, String message);
  }

  private static Controller instance;

  public static Controller getInstance() {
    if (instance == null) {
      instance = new Controller();
      instance.init();
    }

    return instance;
  }

  private Controller() {
  }

  private List<Listener> listeners = new ArrayList<Listener>();

  private String device;

  private Connection connection;

  private Date syncLast;

  private long syncDuration;

  private long syncPkgSend;

  private long syncPkgReceived;

  private Date measurementStart;

  private Date measurementStop;

  private long measurementDuration;

  private long measurementRecords;

  private long measurementGaps;

  private List<Record> records;

  private Connection.Dumper dumper = new Connection.Dumper() {

    private void dump(String prefix, byte[] pkg) {
      StringBuilder out = new StringBuilder();
      out.append(prefix);
      out.append(": ");

      for (int i = 0; i < 3; i++) {
        out.append(String.format("%02X ", pkg[i]));
      }

      out.append(" | ");

      for (int i = 3; i < pkg.length; i++) {
        out.append(String.format("%02X ", pkg[i]));
      }

      Controller.getInstance().log(Controller.LogLevel.DEBUG, "%s", out.toString());
    }

    public void send(byte[] pkg) {
      this.dump("SEND", pkg);
    }

    public void recv(byte[] pkg) {
      this.dump("RECV", pkg);
    }

    public void log(String message) {
      Controller.getInstance().log(Controller.LogLevel.DEBUG, "Connection: %s", message);
    }
  };

  private void init() {
  }

  private static List<String> availablePortIdentifiers = null;

  public static List<String> getAvailablePortIdentifiers() {
    if (availablePortIdentifiers == null) {
      availablePortIdentifiers = new ArrayList<String>();

      Enumeration<CommPortIdentifier> e = CommPortIdentifier.getPortIdentifiers();
      while (e.hasMoreElements()) {
        CommPortIdentifier identifier = e.nextElement();
        if (identifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
          availablePortIdentifiers.add(identifier.getName());
        }
      }
    }

    return availablePortIdentifiers;
  }

  public Connection getConnection() {
    return this.connection;
  }

  public String getDevice() {
    return this.device;
  }

  public void setDevice(String device) {
    this.device = device;
  }

  public boolean isConnected() {
    return this.getConnection() != null;
  }

  public void setConnected(boolean connected) {

    if (connected) {
      if (this.connection != null) {
        return;
      }
      try {
        this.connection = new Connection(this.device, this.dumper);
        this.connection.startAP();

        this.log(LogLevel.INFO, "Connection estalished.");

      } catch (Exception ex) {
        Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }

    } else {
      if (this.connection == null) {
        return;
      }

      try {
        this.connection.stopAP();
        this.connection.close();
        this.connection = null;

        this.log(LogLevel.ERROR, "Connection closed.");

      } catch (Exception ex) {
        Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    for (Listener l : this.listeners) {
      l.connectionChanged(this.isConnected());
    }
  }

  public void sync() {
    Controller.getInstance().log(Controller.LogLevel.INFO, "================================================================================");

    long ts_start = System.currentTimeMillis();

    this.records = new ArrayList<Record>(360);

    this.measurementRecords = 0;
    this.measurementGaps = 0;

    try {
      // Read data from watch
      Connection.Data data = this.connection.fetchData();
      int byte_index = 0;

      // Print status
      Controller.getInstance().log(Controller.LogLevel.INFO, "Status: Date: %d %d %d", data.status.day, data.status.month, data.status.year);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Status: Time: %d %d %d", data.status.hour, data.status.minute, data.status.second);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Status: Mode: %d", data.status.mode);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Status: Interval: %d", data.status.interval);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Status: Bytes: %d", data.status.bytes);

      // Parse recording infos
      byte_index++; // pkg[0x06] unused (magic bytes)
      byte_index++; // pkg[0x07] unused (magic bytes)

      int mode = (data.data[byte_index++] & 0xFF);
      int interval = (data.data[byte_index++] & 0xFF);

      int day = (data.data[byte_index++] & 0xFF);
      int month = (data.data[byte_index++] & 0xFF);
      int year = (data.data[byte_index++] & 0xFF) | (data.data[byte_index++] & 0xFF) << 8;

      int hour = (data.data[byte_index++] & 0x7F);
      int minute = (data.data[byte_index++] & 0xFF);
      int second = (data.data[byte_index++] & 0xFF);

      long timestamp = new Date(year, month, day, hour, minute, second).getTime();

      Controller.getInstance().log(Controller.LogLevel.INFO, "Recording: Date: %d %d %d", day, month, year);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Recording: Time: %d %d %d", hour, minute, second);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Recording: Mode: %d", mode);
      Controller.getInstance().log(Controller.LogLevel.INFO, "Recording: Interval: %d", interval);

      int accl_value_old = 0;

      // Parse accl values and generate records
      while (byte_index < data.status.bytes - 3) {
        int accl_value = (data.data[byte_index++] & 0xFF);
        int accl_count = (data.data[byte_index++] & 0xFF);

        // Create a record for each count
        for (int i = 0; i < accl_count; i++) {
          int accl_value_diff = Math.abs(accl_value - accl_value_old);
          accl_value_old = accl_value;

          Record record = new Record(timestamp, accl_value_diff);
          this.records.add(record);

          // Update timestamp to next interval
          timestamp += data.status.interval * 1000;
        }

        this.measurementGaps += accl_count - 1;
        this.measurementRecords++;
      }

      byte_index++; // pkg[0x??] unused
      byte_index++; // pkg[0x??] unused
      byte_index++; // pkg[0x??] unused

      long ts_end = System.currentTimeMillis();

      // Update statistic fields
      this.syncLast = new Date(ts_start);
      this.syncDuration = (ts_end - ts_start) / 1000;

      this.syncPkgSend = 0;
      this.syncPkgReceived = 0;

      this.measurementStart = new Date(data.status.year, data.status.month, data.status.day, data.status.hour, data.status.minute, data.status.second);
      this.measurementDuration = this.records.size() * data.status.interval;
      this.measurementStop = new Date(this.measurementStart.getTime() + this.measurementDuration * 1000);

    } catch (Exception ex) {
      Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);

      return;
    }

    this.log(LogLevel.INFO, "Synced.");

    for (Listener l : this.listeners) {
      l.synced();
    }
  }

  public void clear() {
    try {
      this.getConnection().clearData();

    } catch (IOException ex) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void exportFile(File f) {
    FileWriter fw = null;
    BufferedWriter w = null;

    try {
      fw = new FileWriter(f);
      w = new BufferedWriter(fw);

      for (Record r : this.records) {
        w.append(Long.toString(r.getTimestamp())).append('\t');
        w.append(Double.toString(r.getMovement())).append('\t');
        w.newLine();
      }

    } catch (IOException ex) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);

    } finally {
      try {
        if (w != null) {
          w.close();
        } else {
          fw.close();
        }

      } catch (IOException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public void importFile(File f) {
    FileReader fr = null;
    BufferedReader r = null;

    long ts_start = System.currentTimeMillis();

    this.measurementGaps = 0;
    this.measurementRecords = 0;

    try {
      fr = new FileReader(f);
      r = new BufferedReader(fr);

      this.records = new ArrayList<Record>();

      Record rec_last = null;

      String line;
      while ((line = r.readLine()) != null) {
        String[] fields = line.split("[ \t]+");
        assert fields.length >= 2;

        Record rec = new Record(Long.parseLong(fields[0]), Double.parseDouble(fields[1]));
        this.records.add(rec);

        this.measurementRecords++;
        if (rec_last != null && rec.getMovement() == rec_last.getMovement()) {
          this.measurementGaps++;
        }
      }

      long ts_end = System.currentTimeMillis();

      // Update statistic fields
      this.syncLast = new Date(ts_start);
      this.syncDuration = (ts_end - ts_start) / 1000;

      this.syncPkgSend = 0;
      this.syncPkgReceived = 0;

      this.measurementStart = new Date(records.get(0).getTimestamp());
      this.measurementStop = new Date(records.get(records.size() - 1).getTimestamp());
      this.measurementDuration = (this.measurementStop.getTime() - this.measurementStart.getTime()) / 1000;

    } catch (IOException ex) {
      Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);

    } finally {
      try {
        if (r != null) {
          r.close();
        } else {
          fr.close();
        }
      } catch (IOException ex) {
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    for (Listener l : this.listeners) {
      l.synced();
    }
  }

  public Date getSyncLast() {
    return this.syncLast;
  }

  public long getSyncDuration() {
    return this.syncDuration;
  }

  public long getSyncPkgSend() {
    return this.syncPkgSend;
  }

  public long getSyncPkgReceived() {
    return this.syncPkgReceived;
  }

  public Date getMeasurementStart() {
    return this.measurementStart;
  }

  public Date getMeasurementStop() {
    return this.measurementStop;
  }

  public long getMeasurementDuration() {
    return this.measurementDuration;
  }

  public long getMeasurementRecords() {
    return this.measurementRecords;
  }

  public long getMeasurementGaps() {
    return this.measurementGaps;
  }

  public List<Record> getRecords() {
    return this.records;
  }

  public void addListener(Listener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    this.listeners.remove(listener);
  }

  public void log(LogLevel level, String format, Object... args) {
    String message = String.format(format, args);
    System.out.println("LOG: " + message);

    for (Listener l : this.listeners) {
      l.log(level, message);
    }
  }
}

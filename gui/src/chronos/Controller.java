package chronos;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

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

  private void init() {
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
        this.connection = new Connection(this.device);
        this.log(LogLevel.WARNING, "Connection estalished.");

      } catch (Exception ex) {
        Controller.getInstance().log(Controller.LogLevel.ERROR, "Exception: %s", ex.getMessage());
        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
      }

    } else {
      if (this.connection == null) {
        return;
      }

      this.connection = null;
      this.log(LogLevel.ERROR, "Connection closed.");
    }

    for (Listener l : this.listeners) {
      l.connectionChanged(this.isConnected());
    }
  }

  public void sync() {
    this.records = new ArrayList<Record>(360);

    long start = System.currentTimeMillis();

    this.syncLast = new Date(start);

    this.measurementStart = new Date(start - 12 * 60 * 60 * 1000);
    this.measurementStop = new Date(start - 12 * 60 * 60 * 1000 + 360 * 5000);


    for (int i = 0; i < 360; i++) {
      long timestamp = start - 12 * 60 * 60 * 1000 + i * 5000;
      int movement = (int) (Math.abs(Math.sin(i * Math.PI / 180)) * Math.random() * 256);

      final Record record = new Record(new Date(timestamp), movement);

      this.records.add(record);
    }

    this.measurementDuration = 360 * 5000;

    this.measurementRecords = 360;
    this.measurementGaps = 2;

    this.syncDuration = System.currentTimeMillis() - start;

    this.log(LogLevel.INFO, "Synced.");

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

    for (Listener l : this.listeners) {
      l.log(level, message);
    }
  }
}

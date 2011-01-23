package chronos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

public class Connection {

  private static final int FLASH_SIZE = 0x2000;

  private static final int FLASH_PAGES = FLASH_SIZE / 16;

  private final static int SYNC_AP_CMD_NOP = 0x01;

  private final static int SYNC_AP_CMD_GET_STATUS = 0x02;

  private final static int SYNC_AP_CMD_SET_WATCH = 0x03;

  private final static int SYNC_AP_CMD_GET_MEMORY_BLOCKS_MODE_1 = 0x04;

  private final static int SYNC_AP_CMD_GET_MEMORY_BLOCKS_MODE_2 = 0x05;

  private final static int SYNC_AP_CMD_ERASE_MEMORY = 0x06;

  private final static int SYNC_AP_CMD_EXIT = 0x07;

  private final static int BM_SYNC_START = 0x30;

  private final static int BM_SYNC_SEND_COMMAND = 0x31;

  private final static int BM_SYNC_GET_BUFFER_STATUS = 0x32;

  private final static int BM_SYNC_READ_BUFFER = 0x33;

  private final static int SYNC_ED_TYPE_R2R = 0x01;

  private final static int SYNC_ED_TYPE_MEMORY = 0x02;

  private final static int SYNC_ED_TYPE_STATUS = 0x03;

  private final static int BM_SYNC_BURST_PACKETS_IN_DATA = 0x09;

  public static interface Dumper {

    public abstract void send(byte[] pkg);

    public abstract void recv(byte[] pkg);

    public abstract void log(String message);
  }

  public static class Data {

    public static class Status {

      public int hour;

      public int minute;

      public int second;

      public int year;

      public int month;

      public int day;

      public int mode;

      public int interval;

      public int bytes;

      public Status(byte[] pkg) {
        this.hour = (pkg[0x04] & 0x7F);
        this.minute = (pkg[0x05] & 0xFF);
        this.second = (pkg[0x06] & 0xFF);

        this.year = (pkg[0x07] & 0xFF) << 8 | (pkg[0x08] & 0xFF);
        this.month = (pkg[0x09] & 0xFF);
        this.day = (pkg[0x0A] & 0xFF);

        // pkg[0x0B] unused
        // pkg[0x0C] unused
        // pkg[0x0D] unused
        // pkg[0x0E] unused
        // pkg[0x0F] unused
        // pkg[0x10] unused

        this.mode = (pkg[0x11] & 0xFF);

        this.interval = (pkg[0x12] & 0xFF);
        this.bytes = (pkg[0x13] & 0xFF) << 8 | (pkg[0x14] & 0xFF);
      }
    }

    public Status status;

    public byte[] data;

    public Data(Status status, byte[] data) {
      this.status = status;
      this.data = data;
    }
  }

  private String device;

  private CommPortIdentifier portIdentifier;

  private SerialPort serialPort;

  private InputStream inputStream;

  private OutputStream outputStream;

  private Dumper dumper = null;

  public Connection(String device) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
    this(device, null);
  }

  public Connection(String device, Dumper dumper) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
    this.device = device;
    this.dumper = dumper;

    this.portIdentifier = CommPortIdentifier.getPortIdentifier(this.device);

    this.serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), 2000);
    this.serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

    this.inputStream = this.serialPort.getInputStream();
    this.outputStream = this.serialPort.getOutputStream();
  }

  public final void close() {
    this.serialPort.close();
  }

  public final String getDevice() {
    return this.device;
  }

  public final CommPortIdentifier getPortIdentifier() {
    return this.portIdentifier;
  }

  public final SerialPort getSerialPort() {
    return this.serialPort;
  }

  private void dumpSend(byte[] pkg) {
    if (this.dumper != null) {
      this.dumper.send(pkg);
    }
  }

  private void dumpRecv(byte[] pkg) {
    if (this.dumper != null) {
      this.dumper.recv(pkg);
    }
  }

  private void dumpLog(String format, Object... args) {
    if (this.dumper != null) {
      this.dumper.log(String.format(format, args));
    }
  }

  private void send(int cmd, int... data) throws IOException {
    byte[] pkg = new byte[3 + data.length];
    pkg[0] = (byte) 0xFF;
    pkg[1] = (byte) cmd;
    pkg[2] = (byte) (3 + data.length);
    for (int i = 0; i < data.length; i++) {
      pkg[3 + i] = (byte) data[i];
    }

    this.dumpSend(pkg);

    this.outputStream.write(pkg);
    this.outputStream.flush();
  }

  private byte[] recv() throws IOException {
    byte[] header = new byte[3];
    this.inputStream.read(header);

    int len = header[2];

    byte[] pkg;
    if (len > 3) {
      byte[] data = new byte[len - 3];
      this.inputStream.read(data);

      pkg = new byte[header.length + data.length];
      System.arraycopy(header, 0, pkg, 0, header.length);
      System.arraycopy(data, 0, pkg, header.length, data.length);

    } else {
      pkg = header;
    }

    this.dumpRecv(pkg);

    return pkg;
  }

  private void assertReadOk(int... data) throws IOException {
    byte[] pkg = this.recv();

    if (pkg[0] != (byte) 0xFF) {
      throw new RuntimeException();
    }
    if (pkg[1] != (byte) 0x06) {
      throw new RuntimeException();
    }

    for (int i = 0; i < data.length; i++) {
      if (pkg[i + 3] != (byte) data[i]) {
        throw new RuntimeException();
      }
    }
  }

  public final void startAP() throws IOException {
    this.send(0x07);
    this.assertReadOk();

    this.dumpLog("AP started");
  }

  public final void stopAP() throws IOException {
    this.send(0x09);
    this.assertReadOk();

    this.dumpLog("AP stoped");
  }

  private void sleep() {
    try {
      Thread.sleep(250);
    } catch (InterruptedException ex) {
    }
  }

  private byte[] fetchDataPage(int command, int... address) throws IOException {
    byte[] response;

    // Request status
    this.send(BM_SYNC_SEND_COMMAND, catPkg(command, address));
    this.assertReadOk(command);

    // Get response status
    do {
      this.sleep();
      this.send(BM_SYNC_GET_BUFFER_STATUS);
      response = this.recv();
    } while (response[0x03] != 0x01);

    // Get response
    this.send(BM_SYNC_READ_BUFFER);
    response = this.recv();

    return response;
  }

  public final Data fetchData() throws IOException {
    byte[] response;

    // Start sync mode
    this.send(BM_SYNC_START);
    this.assertReadOk();
    this.dumpLog("Switched to sync mode");

    // Fetch watch status
    response = this.fetchDataPage(SYNC_AP_CMD_GET_STATUS);
    this.dumpLog("Status fetched");

    // Parse status
    Data.Status status = new Data.Status(response);

    // Build return structure
    byte[] data = new byte[FLASH_SIZE];
    int data_offset = 0;

    // Fetch data for all pages
    for (int i = 0; i < Math.ceil(((float)status.bytes) / 16.0f); i++) {
      
      // Request data from watch
      response = this.fetchDataPage(SYNC_AP_CMD_GET_MEMORY_BLOCKS_MODE_1, catPkg(mkPkgShort(i), mkPkgShort(i)));

      // Copy fetched pages to output structure
      System.arraycopy(response, 6, data, data_offset, 16);
      data_offset += 16;
    }

    this.dumpLog("Data fetched");

    return new Data(status, data);
  }

  public final void clearData() throws IOException {
    this.dumpLog("Cleaning data");

    this.send(BM_SYNC_SEND_COMMAND, SYNC_AP_CMD_ERASE_MEMORY);
    this.assertReadOk(SYNC_AP_CMD_ERASE_MEMORY);

    this.dumpLog("Data cleaned");
  }

  private static int[] mkPkgShort(int i) {
    return new int[]{
              (i >> 8) & 0xFF,
              (i) & 0xFF
            };
  }

  private static int[] catPkg(int[] i1, int[] i2) {
    int[] i = new int[i1.length + i2.length];
    System.arraycopy(i1, 0, i, 0, i1.length);
    System.arraycopy(i2, 0, i, i1.length, i2.length);
    return i;
  }

  private static int[] catPkg(int[] i1, int i2) {
    return catPkg(i1, new int[]{i2});
  }

  private static int[] catPkg(int i1, int... i2) {
    return catPkg(new int[]{i1}, i2);
  }
}

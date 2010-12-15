package chronos;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection {

  private String device;

  private CommPortIdentifier portIdentifier;

  private SerialPort serialPort;

  private InputStream inputStream;

  private OutputStream outputStream;

  public Connection(String device) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
    this.device = device;

//    this.portIdentifier = CommPortIdentifier.getPortIdentifier(this.device);
//
//    this.serialPort = (SerialPort) portIdentifier.open(this.getClass().getName(), 2000);
//    this.serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//
//    this.inputStream = this.serialPort.getInputStream();
//    this.outputStream = this.serialPort.getOutputStream();
  }

  public String getDevice() {
    return this.device;
  }

  public CommPortIdentifier getPortIdentifier() {
    return this.portIdentifier;
  }

  public SerialPort getSerialPort() {
    return this.serialPort;
  }

  public InputStream getInputStream() {
    return this.inputStream;
  }

  public OutputStream getOutputStream() {
    return this.outputStream;
  }
}

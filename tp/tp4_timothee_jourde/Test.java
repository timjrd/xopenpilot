import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import gnu.io.*;

public class Test
{
   static public void main(String[] args) throws IOException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException
   {
      CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(args[0]);
      SerialPort port = (SerialPort) id.open("reader", 1000);
      port.setSerialPortParams(19200,
                               SerialPort.DATABITS_8,
                               SerialPort.STOPBITS_1,
                               SerialPort.PARITY_NONE);

      InputStream in = port.getInputStream();
      OutputStream out = port.getOutputStream();

      out.write(0xFD);
   }
}

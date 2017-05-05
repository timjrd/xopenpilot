package fr.ubordeaux.xopenpilot;

import java.util.Scanner;
import com.fazecast.jSerialComm.*;

public class SerialComm
{
   public static void main(String[] args)
   {
      SerialPort ports[] = SerialPort.getCommPorts();

      if (ports.length > 0)
      {
         System.out.println("Serial ports:");
         
         int i = 1;
         for (SerialPort port : ports)
         {
            System.out.println(i + ") " + port.getSystemPortName());
            i++;
         }
         
         SerialPort port = ports[0];

         if (port.openPort())
         {
            System.out.println("Port 0 opened. Reading as text...");

            port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

            Scanner data = new Scanner(port.getInputStream());
            while (data.hasNextLine())
            {
               System.out.println(data.nextLine());
            }
         }
         else
         {
            System.out.println("Cannot open port 0.");
         }
      }
      else
      {
         System.out.println("No serial ports.");
      }
   }
}

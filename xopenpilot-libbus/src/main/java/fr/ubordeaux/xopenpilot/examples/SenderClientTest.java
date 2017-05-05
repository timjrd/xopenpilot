package fr.ubordeaux.xopenpilot.examples;

import java.util.Scanner;
import javax.json.*;

import java.io.IOException;

import fr.ubordeaux.xopenpilot.libbus.client.*;

public class SenderClientTest
{
   static public void main(String[] args)
   {
      System.out.println("\n\n");
         
      if (args.length != 1)
      {
         System.out.println("Please provide the bus hostname as argument.\n\n");
         return;
      }
      
      try
      {
         RemoteBus bus    = new RemoteBus("localhost", 7182);
         Sender    sender = bus.registerSender("Test", "test");
         Scanner   in     = new Scanner(System.in);

         if (sender != null)
         {
            String line = "line";
            while (! "".equals(line))
            {
               System.out.print("\nPlease enter a message or a blank line to terminate\n> ");
               line = in.nextLine();

               JsonObject msg = Json.createObjectBuilder()
                  .add("message", line)
                  .build();

               sender.sendMessage(msg);
            }
         }

         in.close();
         sender.deregister();
         bus.close();
      }
      catch(IOException e)
      {
         System.out.println("\n\nConnection error. Please make sure the bus is running at the specified address.\n\n");
      }
   }
}

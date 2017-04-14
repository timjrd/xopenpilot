package fr.ubordeaux.xopenpilot.libbus;

import java.util.Scanner;
import javax.json.*;

import fr.ubordeaux.xopenpilot.libbus.client.*;

public class SenderClientTest
{
   static public void main(String[] args) throws java.io.IOException
   {
      RemoteBus bus    = new RemoteBus("localhost", 7182);
      Sender    sender = bus.registerSender("test", "test");
      Scanner   in     = new Scanner(System.in);

      if (sender != null)
      {
         while (true)
         {
            System.out.print("enter message>");
            String line = in.nextLine();

            JsonObject msg = Json.createObjectBuilder()
               .add("message", line)
               .build();

            sender.sendMessage(msg);
         }
      }

      in.close();
      bus.close();
   }
}

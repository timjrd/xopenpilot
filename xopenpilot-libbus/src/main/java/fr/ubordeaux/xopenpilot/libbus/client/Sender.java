// Sender.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.io.IOException;
import javax.json.*;

public class Sender
{
   private RemoteServer remoteServer;
   private int id;
   
   Sender(RemoteServer rs, int id)
   {
      this.id           = id;
      this.remoteServer = rs;
   }
   
   public void deregister() throws IOException
   {
      JsonObject message = Json.createObjectBuilder()
         .add("type", "deregister")
         .add("sender_id", id)
         .build();

      String response = remoteServer.send(message.toString());
   }

   public void sendMessage(JsonValue messageContent) throws IOException
   {
      JsonObject message = Json.createObjectBuilder()
         .add("type", "send")
         .add("sender_id", id)
         .add("contents", messageContent)
         .build();

      String response = remoteServer.send(message.toString());
   }
}

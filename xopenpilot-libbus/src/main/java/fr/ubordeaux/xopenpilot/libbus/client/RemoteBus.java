// RemoteBus.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.json.*;

public class RemoteBus
{
   static public final int DEFAULT_PORT = 7182;
   
   private RemoteServer remoteServer;

   static boolean responseError(JsonObject response)
   {
      return response.getJsonObject("ack").getString("resp").equals("error");
   }
   
   public RemoteBus(String hostname, int port) throws IOException
   {
      remoteServer = new RemoteServer(hostname, port);
   }
   public RemoteBus(String hostname) throws IOException
   {
      this(hostname, DEFAULT_PORT);
   }
   
   public void close() throws IOException
   {
      remoteServer.close();
   }

   public Sender registerSender(String senderClass, String senderName) throws IOException
   {
      JsonObject message = Json.createObjectBuilder()
         .add("type", "register")
         .add("sender_class", senderClass)
         .add("sender_name", senderName)
         .build();

      String response = remoteServer.send(message.toString());

      JsonObject responseObject = Json.createReader(new StringReader(response)).readObject();

      if (responseError(responseObject))
         return null;
      else
      {
         int id = responseObject.getInt("sender_id");
         return new Sender(remoteServer, id);
      }
   }

   
   /* Méthode de découverte (pour la réception) */
   /*
     Exemples d'appels :
     
     listSenders(null, null);      // liste tout les Sender
     listSenders("GPS", null);     // liste les Sender de classe "GPS"
     listSenders(null, "device");  // liste les Sender de nom "device"
     listSenders("GPS", "device"); // liste les Sender de classe "GPS" ET de nom "device"
    */
   public SenderInfoClient[] listSenders(String senderClass, String senderName) throws IOException
   {
      JsonObjectBuilder builder = Json.createObjectBuilder()
         .add("type", "list");

      if (senderClass != null)
         builder.add("sender_class", senderClass);

      if (senderName != null)
         builder.add("sender_name", senderName);

      String response = remoteServer.send(builder.build().toString());

      JsonObject responseObject = Json.createReader(new StringReader(response)).readObject();
      JsonArray  results        = responseObject.getJsonArray("results");

      ArrayList<SenderInfoClient> senders = new ArrayList<>();
      for (JsonValue result_ : results)
      {
         JsonObject result = (JsonObject) result_;
         
         int    id        = result.getInt("sender_id");
         String name      = result.getString("sender_name");
         String class_    = result.getString("sender_class");
         int    messageId = result.getInt("last_message_id");

         senders.add(new SenderInfoClient(remoteServer, id, name, class_, messageId));
      }

      SenderInfoClient[] result = new SenderInfoClient[senders.size()];
      return senders.toArray(result);
   }
}


// SenderInfoClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.io.IOException;

import java.util.Date;
import java.io.StringReader;
import javax.json.*;

public class SenderInfoClient
{
   private RemoteServer remoteServer;
   
   private int    senderId;
   private String senderName;
   private String senderClass;
   private int    nextMessageId;
   //private int    lastMessageId;


   SenderInfoClient(RemoteServer rs, int id, String name, String class_, int messageId)
   {
      remoteServer  = rs;
      senderId      = id;
      senderName    = name;
      senderClass   = class_;
      nextMessageId = messageId;
      //lastMessageId = messageId;
   }
   
   public String getSenderName()
   {
      return senderName;
   }

   public String getSenderClass()
   {
      return senderClass;
   }

   public MessageClient getNextMessage() throws IOException
   {
      JsonObject message = Json.createObjectBuilder()
         .add("type", "get")
         .add("sender_id", senderId)
         .add("msg_id", nextMessageId)
         .build();
      
      String response = remoteServer.send(message.toString());

      JsonObject responseObject = Json.createReader(new StringReader(response)).readObject();

      if (RemoteBus.responseError(responseObject))
         return null;
      else
      {
         int       messageId = responseObject.getInt("msg_id");
         long      date      = responseObject.getJsonNumber("date").longValue();
         JsonValue content   = responseObject.get("contents");

         nextMessageId = messageId + 1;

         return new MessageClient(new Date(date), content);
      }
   }

   public MessageClient getLastMessage() throws IOException
   {
      JsonObject message = Json.createObjectBuilder()
         .add("type", "get_last")
         .add("sender_id", senderId)
         .build();

      String response = remoteServer.send(message.toString());

      JsonObject responseObject = Json.createReader(new StringReader(response)).readObject();

      if (RemoteBus.responseError(responseObject))
         return null;
      else
      {
         long      date    = responseObject.getJsonNumber("date").longValue();
         JsonValue content = responseObject.get("contents");
      
         return new MessageClient(new Date(date), content);
      }
   }
}

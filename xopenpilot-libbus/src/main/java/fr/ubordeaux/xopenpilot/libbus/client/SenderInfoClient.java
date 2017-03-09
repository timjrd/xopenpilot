// SenderInfoClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

public class SenderInfoClient
{
   public String getSenderName()
   {
      return "senderName";
   }

   public String getSenderClass()
   {
      return "senderClass";
   }

   public MessageClient getNextMessage()
   {
      return new MessageClient();
   }

   public MessageClient getLastMessage()
   {
      return new MessageClient();
   }
}

// SenderInfoServer.java
package fr.ubordeaux.xopenpilot.libbus.server;

public class SenderInfoServer
{
   private int senderId;
   private int lastMessageId;
   
   private String senderClass;
   private String senderName;

   public SenderInfoServer(String class_, String name, int id, int lastId)
   {
      senderId      = id;
      lastMessageId = lastId;
      senderClass   = class_;
      senderName    = name;
   }
   
   public String getSenderName()
   {
      return senderName;
   }

   public String getSenderClass()
   {
      return senderClass;
   }

   public int getSenderId()
   {
      return senderId;
   }

   public int getLastMessageId()
   {
      return lastMessageId;
   }
}

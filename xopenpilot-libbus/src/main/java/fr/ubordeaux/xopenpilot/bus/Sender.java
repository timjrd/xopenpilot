package fr.ubordeaux.xopenpilot.bus;

import java.util.Date;
import java.util.ArrayList;
import javax.json.*;

import fr.ubordeaux.xopenpilot.libbus.server.*;

class Sender
{
   private int keepTime = 60; // seconds
   
   private int senderId;
   
   private String senderName;
   private String senderClass;

   private ArrayList<MessageServer> messages = new ArrayList<>();
   
   Sender(String class_, String name, int id)
   {
      senderId    = id;
      senderClass = class_;
      senderName  = name;
   }

   boolean match(String class_, String name)
   {
      boolean matchClass = class_ == null || class_.equals(senderClass);
      boolean matchName  = name   == null || name.equals(senderName);

      return matchClass && matchName;
   }

   SenderInfoServer senderInfo()
   {
      return new SenderInfoServer(senderClass, senderName, senderId, lastMessageId());
   }

   void addMessage(JsonValue msg)
   {
      if (! messages.isEmpty())
      {
         long time = messages.get(0).getDate().getTime();
         long now  = new Date().getTime();

         if (now - time > keepTime * 1000)
         {
            log("removing old message " + messages.get(0).getId() + ".");
            messages.remove(0);
         }
      }

      int newId = lastMessageId() + 1;
      log("adding new message " + newId + ".");
      messages.add(new MessageServer(new Date(), msg, newId));
   }
   
   MessageServer getMessage(int messageId)
   {
      if (messages.isEmpty())
      {
         log("requesting inexistent message " + messageId + ", message queue is empty, sending back nothing.");
         return null;
      }
      else
      {
         int firstId = messages.get(0).getId();
         int lastId  = messages.get(messages.size() - 1).getId();

         if (messageId > lastId)
         {
            log("requesting inexistent message " + messageId + ", last message is " + lastId + ", sending back nothing.");
            return null;
         }
         else if (messageId < firstId)
         {
            log("requesting removed message " + messageId + ", sending back message " + firstId + ".");
            return messages.get(0);
         }
         else
         {
            log("requesting valid message " + messageId + ".");
            return messages.get(messageId - firstId);
         }
      }
   }

   MessageServer getLastMessage()
   {
      if (messages.isEmpty())
      {
         log("requesting last message, message queue is empty, sending back nothing.");
         return null;
      }
      else
      {
         MessageServer result = messages.get( messages.size() - 1 );
         log("requesting last message, sending back message " + result.getId() + ".");
         return result;
      }
   }

   private int lastMessageId()
   {
      if (messages.isEmpty())
         return 0;
      else
         return messages.get( messages.size() - 1 ).getId();
   }

   private MessageServer get(int index)
   {
      if (index < 0 || index >= messages.size())
         return null;
      else
         return messages.get(index);
   }

   private void log(String str)
   {
      System.out.println("Sender " + senderId + ": " + str);
   }
}

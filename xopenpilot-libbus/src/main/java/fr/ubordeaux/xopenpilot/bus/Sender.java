package fr.ubordeaux.xopenpilot.bus;

import java.util.Date;
import java.util.ArrayList;
import javax.json.*;

import fr.ubordeaux.xopenpilot.libbus.server.*;

class Sender
{
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
      messages.add(new MessageServer(new Date(), msg, lastMessageId() + 1));
   }
   
   MessageServer getMessage(int messageId)
   {
      return null;
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
}

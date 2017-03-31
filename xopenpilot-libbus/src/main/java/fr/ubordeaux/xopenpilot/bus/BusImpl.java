// BusImpl.java
package fr.ubordeaux.xopenpilot.bus;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.json.*;

import fr.ubordeaux.xopenpilot.libbus.server.*;

public class BusImpl implements Bus
{
   private int maxSenderId  = 2000000000;
   private int nextSenderId = 0;
   private Map<Integer, Sender> senderById = new HashMap<>();
   
   /*
     Retourne un identifiant unique.
   */
   @Override
   public int  registerSender(String senderClass, String senderName)
   {
      nextSenderId++;
      
      if (nextSenderId >= maxSenderId)
      {
         return -1;
      }
      else
      {
         senderById.put(nextSenderId, new Sender(senderClass, senderName, nextSenderId));
         return nextSenderId;
      }
   }

   @Override
   public void deregisterSender(int senderId)
   {
      senderById.remove(senderId);
   }

   /*
     Invoqué lors de la réception d'un message par un Sender.
   */
   @Override
   public void messageReceived(int senderId, JsonValue messageContent)
   {
      Sender sender = senderById.get(senderId);

      if (sender != null)
         sender.addMessage(messageContent);
   }

   
   /*
     Voir RemoteBus.java
   */
   @Override
   public SenderInfoServer[] listSenders(String senderClass, String senderName)
   {
      ArrayList<SenderInfoServer> result = new ArrayList<>();
      
      for (Sender sender : senderById.values())
         if (sender.match(senderClass, senderName))
            result.add( sender.senderInfo() );

      return (SenderInfoServer[]) result.toArray();
   }

   /*
     Voir SenderInfoClient.java
   */
   @Override
   public MessageServer getMessage(int senderId, int messageId)
   {
      return null;
   }

   @Override
   public MessageServer getLastMessage(int senderId)
   {
      return null;
   }

}

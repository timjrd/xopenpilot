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
      if (nextSenderId >= maxSenderId)
      {
         log("unable to register new sender: too much senders.");
         return -1;
      }
      else
      {
         int newId = nextSenderId;
         log("registering new sender " + newId + ".");
         senderById.put(newId, new Sender(senderClass, senderName, newId));
         
         nextSenderId++;
         return newId;
      }
   }

   @Override
   public void deregisterSender(int senderId)
   {
      log("deregistering sender " + senderId + ".");
      senderById.remove(senderId);
   }

   /*
     Invoqué lors de la réception d'un message par un Sender.
   */
   @Override
   public void messageReceived(int senderId, JsonValue messageContent)
   {
      Sender sender = senderById.get(senderId);

      if (sender == null)
      {
         log("message received from unknown sender " + senderId + ".");
      }
      else
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

      String senderClassStr = (senderClass == null) ? "any" : "\"" + senderClass + "\"";
      String senderNameStr  = (senderName  == null) ? "any" : "\"" + senderName + "\"";
      log("requesting sender list with class=" + senderClassStr + " and name=" + senderNameStr + ", sending back " + result.size() + " results.");

      SenderInfoServer[] result_ = new SenderInfoServer[result.size()];
      return result.toArray(result_);
   }

   /*
     Voir SenderInfoClient.java
   */
   @Override
   public MessageServer getMessage(int senderId, int messageId)
   {
      Sender sender = senderById.get(senderId);

      if (sender == null)
      {
         log("requesting message from inexistent sender " + senderId + ". sending back nothing.");
         return null;
      }
      else
      {
         return sender.getMessage(messageId);
      }
   }

   @Override
   public MessageServer getLastMessage(int senderId)
   {
      Sender sender = senderById.get(senderId);

      if (sender == null)
      {
         log("requesting last message from inexistent sender " + senderId + ". sending back nothing.");
         return null;
      }
      else
      {
         return sender.getLastMessage();
      }
   }

   private void log(String str)
   {
      System.out.println("Bus: " + str);
   }
}

// BusImpl.java
package fr.ubordeaux.xopenpilot.libbus.server;

public class BusImpl implements Bus
{
   /*
     Retourne un identifiant unique.
   */
   @Override
   public int  registerSender(String senderClass, String senderName)
   {
   }

   @Override
   public void deregisterSender(int senderId)
   {
   }

   /*
     Invoqué lors de la réception d'un message par un Sender.
   */
   @Override
   public void messageReceived(int senderId, JsonValue messageContent)
   {
   }

   
   /*
     Voir RemoteBus.java
   */
   @Override
   public SenderInfoServer[] listSenders(String senderClass, String senderName)
   {
   }

   /*
     Voir SenderInfoClient.java
   */
   @Override
   public MessageServer getMessage(int senderId, int messageId)
   {
   }

   @Override
   public MessageServer getLastMessage(int senderId)
   {
   }

}

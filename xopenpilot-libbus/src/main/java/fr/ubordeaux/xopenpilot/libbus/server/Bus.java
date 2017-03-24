// Bus.java
package fr.ubordeaux.xopenpilot.libbus.server;

import javax.json.*;

/*
  Une implémentation de bus doit implémenter cette interface.
 */
public interface Bus
{
   /*
     Retourne un identifiant unique.
    */
   public int  registerSender(String senderClass, String senderName);
   public void deregisterSender(int senderId);

   /*
     Invoqué lors de la réception d'un message par un Sender.
    */
   public void messageReceived(int senderId, JsonValue messageContent);

   
   /*
     Voir RemoteBus.java
    */
   public SenderInfoServer[] listSenders(String senderClass, String senderName);

   /*
     Voir SenderInfoClient.java
    */
   public MessageServer getMessage(int senderId, int messageId);
   public MessageServer getLastMessage(int senderId);
}

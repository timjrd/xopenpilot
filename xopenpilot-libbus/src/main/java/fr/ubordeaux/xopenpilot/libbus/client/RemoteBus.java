// RemoteBus.java
package fr.ubordeaux.xopenpilot.libbus.client;

public class RemoteBus
{
   public RemoteBus(String hostname, int port)
   {
   }

   public Sender registerSender(String senderClass, String senderName)
   {
      return new Sender();
   }

   
   /* Méthode de découverte (pour la réception) */
   /*
     Exemples d'appels :
     
     listSenders(null, null);      // liste tout les Sender
     listSenders("GPS", null);     // liste les Sender de classe "GPS"
     listSenders(null, "device");  // liste les Sender de nom "device"
     listSenders("GPS", "device"); // liste les Sender de classe "GPS" ET de nom "device"
    */
   public SenderInfoClient[] listSenders(String senderClass, String senderName)
   {
      return new SenderInfoClient[0];
   }
}


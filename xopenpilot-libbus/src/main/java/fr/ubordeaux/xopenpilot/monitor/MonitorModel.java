package fr.ubordeaux.xopenpilot.monitor;

import fr.ubordeaux.xopenpilot.libbus.client.*;
import javax.json.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;

public class MonitorModel
{
   static public final int MAX_MESSAGES_SIZE        = 100;
   static public final int NEW_MESSAGES_PER_REFRESH = 30;
   
   private SenderInfoClient lastSelectedSender = null;
   
   private SenderInfoClient[]       senders  = new SenderInfoClient[0];
   private ArrayList<MessageClient> messages = new ArrayList<MessageClient>();
   private MessageClient            selectedMessage = null;
   private MessageClient            lastMessage     = null;
   
   private RemoteBus bus = null;
   
   public MonitorModel(String hostname) throws IOException
   {
      bus = new RemoteBus(hostname);
   }

   public void close() throws IOException
   {
      bus.close();
   }

   public void refresh(int selectedSenderId, int selectedMessageId) throws IOException
   {
      /* On met à jour la liste des émetteurs */
      senders = bus.listSenders(null, null);
      Arrays.sort(senders, (a,b) -> a.getSenderId() - b.getSenderId());

      /* On cherche l'émetteur sélectionné */
      SenderInfoClient selectedSender = null;
      if (selectedSenderId < 0)
         selectedSender = null;
      else if (lastSelectedSender != null && selectedSenderId == lastSelectedSender.getSenderId())
         selectedSender = lastSelectedSender;
      else
         selectedSender = findSender(selectedSenderId);
      
      
      if (selectedSender != lastSelectedSender)
         messages.clear();
      
      if (selectedSender != null)
      {
         /* On récupère les nouveaux messages */
         boolean hasNext = true;
         for (int n = 0; hasNext && n < NEW_MESSAGES_PER_REFRESH; n++)
         {
            MessageClient msg = selectedSender.getNextMessage();
            if (msg == null)
               hasNext = false;
            else
               addMessage(msg);
         }
      
         /* On cherche le message sélectionné */
         if (selectedMessageId >= 0)
            selectedMessage = findMessage(selectedMessageId);
         else
            selectedMessage = null;

         lastMessage = selectedSender.getLastMessage();
      }
      
      /* On mémorise l'émetteur sélectionné */
      lastSelectedSender = selectedSender;
   }
   
   private SenderInfoClient findSender(int id)
   {
      SenderInfoClient result = null;
      for (SenderInfoClient sender : senders)
         if (id == sender.getSenderId())
         {
            result = sender;
            break;
         }

      return result;
   }
   
   private MessageClient findMessage(int id)
   {
      MessageClient result = null;
      for (MessageClient msg : messages)
         if (id == msg.getId())
         {
            result = msg;
            break;
         }

      return result;
   }
   
   private void addMessage(MessageClient msg)
   {
      if (messages.size() == MAX_MESSAGES_SIZE)
         messages.remove(0);

      messages.add(msg);
   }

   public SenderInfoClient[] getSenders()
   {
      return senders;
   }
   public ArrayList<MessageClient> getMessages()
   {
      return messages;
   }
   public MessageClient getSelectedMessage()
   {
      return selectedMessage;
   }
   public MessageClient getLastMessage()
   {
      return lastMessage;
   }
}


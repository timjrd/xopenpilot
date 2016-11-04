import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Date;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Server extends Thread
{
   private ServerSocket serverSocket;

   private List<Message> messages = new ArrayList<Message>();

   private volatile boolean running = false;

   public Server(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }

   public void terminate()
   {
      running = false;
   }

   @Override
   public void run()
   {
      running = true;
      
      while (running)
      {
         try
         {
            // on initialise la connection avec un client
            Socket socket = serverSocket.accept();

            // on récupère les flux d'entrée/sortie de la connexion
            InputStream  in  = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            ObjectInputStream  oin  = new ObjectInputStream(in);
            ObjectOutputStream oout = new ObjectOutputStream(out);

            accept(oin, oout);

            oout.flush();
            
            socket.close();
         }
         catch (IOException|ClassNotFoundException e)
         {
            e.printStackTrace();
         }
      }
   }

   private void accept(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException
   {
      Protocol query = (Protocol) in.readObject();

      if (query == Protocol.SEND_MESSAGE)
      {
         insertMessage( (Message) in.readObject() );
         out.writeObject(Protocol.SENT);
      }
      else if (query == Protocol.RETRIEVE_MESSAGES_SINCE_DATE)
      {
         List<Message> response = getMessagesSinceDate( (Date) in.readObject() );
         out.writeObject(response);
      }
   }

   private void insertMessage(Message msg)
   {
      for (int i = messages.size() - 1; i >= 0; i--)
      {
         Date date = messages.get(i).getDate();

         if (msg.getDate().after(date) || msg.getDate().equals(date))
         {
            messages.add(i+1, msg);
         }
      }

      messages.add(0, msg);
   }

   private List<Message> getMessagesSinceDate(Date date)
   {
      List<Message> result = new ArrayList<Message>();
      
      for (Message msg : messages)
      {
         if (msg.getDate().after(date) || msg.getDate().equals(date))
            result.add(msg);
      }

      return result;
   }
}

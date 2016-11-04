import java.util.Scanner;
import java.util.Date;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

class Connexion
{
   private Socket socket;
   
   public final ObjectInputStream in;
   public final ObjectOutputStream out;

   public Connexion(Socket s, ObjectInputStream in_, ObjectOutputStream out_)
   {
      socket = s;
      in  = in_;
      out = out_;
   }

   public void close() throws IOException
   {
      out.flush();
      socket.close();
   }
}

public class Client
{
   private String author;
   private String host;
   private int port;
   
   public Client(String aut, String h, int p)
   {
      author = aut;
      host = h;
      port = p;
   }

   public Connexion connect()
   {
      try
      {
         // on initialise la connection
         Socket socket = new Socket(host, port);

         // on récupère les flux d'entrée/sortie de la connection avec le server
         ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream());
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

         return new Connexion(socket, in, out);
      }
      catch(IOException e)
      {
         return null;
      }
   }
   
   public boolean sendMessage(String msg) throws IOException, ClassNotFoundException
   {
      Connexion connexion = connect();

      if (connexion == null)
         return false;

      connexion.out.writeObject(Protocol.SEND_MESSAGE);
      connexion.out.writeObject(new Message(msg, author, new Date()));

      Protocol response = (Protocol) connexion.in.readObject();
      return (response == Protocol.SENT);
   }
}

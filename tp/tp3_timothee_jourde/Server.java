import java.util.Set;
import java.util.HashSet;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Server
{
   private Set<Long> idsInside = new HashSet<Long>();

   private ServerSocket serverSocket;

   public Server(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }

   public void listen(int port) throws IOException
   {
      while (true)
      {
         // on initialise la connection avec un client
         Socket socket = serverSocket.accept();

         // on récupère les flux d'entrée/sortie de la connexion
         InputStream  in  = socket.getInputStream();
         OutputStream out = socket.getOutputStream();

         accept(new ObjectInputStream(in), new ObjectOutputStream(out));
         
         socket.close();
      }
   }

   private void accept(ObjectInputStream in, ObjectOutputStream out) throws IOException
   {
   }
}

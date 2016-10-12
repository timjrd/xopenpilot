import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Server
{
   static public void main(String[] args) throws IOException, ClassNotFoundException
   {
      ServerSocket serverSocket = new ServerSocket(9742);

      boolean stop = false;
      while(! stop)
      {
         // on initialise la connection avec un client
         Socket socket = serverSocket.accept();

         // on récupère les flux d'entrée/sortie de la connection
         InputStream  in  = socket.getInputStream();
         OutputStream out = socket.getOutputStream();

         // on récupère les 2 entiers envoyés par le client
         int a = in.read();
         int b = in.read();

         // on envoi la réponse au client
         out.write( a + b );

         ObjectInputStream oin = new ObjectInputStream(in);
         Object o = oin.readObject();

         System.out.println("[SERVER] " + o.toString());
         
         socket.close();
      }

      serverSocket.close();
   }
}

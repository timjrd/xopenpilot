import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Client
{
   static public void main(String[] args) throws IOException, UnknownHostException
   {
      if (args.length != 2)
      {
         System.out.println("[CLIENT] 2 arguments requis: entier_a entier_b");
         return;
      }
      
      // on initialise la connection
      Socket socket = new Socket("localhost", 9042);

      // on récupère les flux d'entrée/sortie de la connection avec le server
      InputStream  in  = socket.getInputStream();
      OutputStream out = socket.getOutputStream();

      // on envoi au server les 2 entiers
      out.write( Integer.parseInt(args[0]) );
      out.write( Integer.parseInt(args[1]) );

      // on récupère la réponse
      int response = in.read();

      // on affiche la réponse
      System.out.println( "[CLIENT] réponse: " + response );

      Object o = new MyObject("client0");
      ObjectOutputStream oout = new ObjectOutputStream(out);

      oout.writeObject(o);
      
      socket.close();
   }
}

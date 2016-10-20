import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

import java.net.Socket;
import java.net.ServerSocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Server extends Thread
{
   private Set<Long> idsInside = new HashSet<Long>();

   private ServerSocket serverSocket;

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

            Scanner scanner = new Scanner(in, "US-ASCII");
            OutputStreamWriter outw = new OutputStreamWriter(out, "US-ASCII");

            accept(scanner, outw);

            outw.flush();
            socket.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }

   private void accept(Scanner in, OutputStreamWriter out) throws IOException
   {
      // on récupère le type de requête
      String msg = in.next();
      if (msg.equals( Protocol.IN ))
      {
         // on récupère l'identifiant
         Long id = in.nextLong();
                  
         String response;
         if (idsInside.contains(id))
            // si l'identifiant est déjà enregistré on envoi un refus
            response = Protocol.UNAUTHORIZED + " id already registered";
         else
         {
            // sinon on enregistre l'identifiant et on authorise l'entrée
            idsInside.add(id);
            response = Protocol.AUTHORIZED;
         }

         response += "\n";
         out.write(response, 0, response.length());
      }
      else if (msg.equals( Protocol.OUT ))
      {
         // on supprime l'identifiant
         Long id = in.nextLong();
         idsInside.remove(id);
         // pas de réponse
      }
      else
      {
         String response = Protocol.UNAUTHORIZED + " invalid query";

         response += "\n";
         out.write(response, 0, response.length());
      }
   }
}

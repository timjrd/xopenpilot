import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Server
{
   private Set<Long> idsInside = new HashSet<Long>();

   private ServerSocket serverSocket;

   public Server(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }

   public void listen() throws IOException
   {
      while (true)
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
   }

   private void accept(Scanner in, OutputStreamWriter out) throws IOException
   {
      String msg = in.next();
      if (msg.equals( Protocol.IN ))
      {
         Long id = in.nextLong();
                  
         String response;
         if (idsInside.contains(id))
            response = Protocol.UNAUTHORIZED + " id already registered";
         else
         {
            idsInside.add(id);
            response = Protocol.AUTHORIZED;
         }

         response += "\n";
         out.write(response, 0, response.length());
      }
      else if (msg.equals( Protocol.OUT ))
      {
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

   static public void main(String[] args) throws IOException
   {
      Server server = new Server(9743);
      server.listen();
   }
}

import java.util.Scanner;

import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class InClient
{
   private String host;
   private int port;
   
   public InClient(String h, int p)
   {
      host = h;
      port = p;
   }

   // interroge le serveur et retourne vrai si l'identifiant est autorisé à entrer, faux sinon
   public boolean queryIn(long id) throws IOException, UnknownHostException
   {
      // on initialise la connection
      Socket socket = new Socket(host, port);

      // on récupère les flux d'entrée/sortie de la connection avec le server
      Scanner in = new Scanner(socket.getInputStream(), "US-ASCII");
      OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "US-ASCII");

      // on envoi la requête
      String msg = Protocol.IN + " " + id + "\n";
      out.write(msg, 0, msg.length());
      out.flush();

      // on récupère la réponse
      String response = in.next();

      boolean authorized;
      if (response.equals(Protocol.AUTHORIZED))
         authorized = true;
      else
         authorized = false;

      socket.close();

      return authorized;
   }
}

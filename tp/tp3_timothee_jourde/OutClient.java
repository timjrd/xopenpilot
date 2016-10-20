import java.net.Socket;
import java.net.UnknownHostException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class OutClient
{
   private String host;
   private int port;
   
   public OutClient(String h, int p)
   {
      host = h;
      port = p;
   }

   public void sendOut(long id) throws IOException, UnknownHostException
   {
      // on initialise la connection
      Socket socket = new Socket(host, port);

      // on récupère le flux de sortie de la connection avec le server
      OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "US-ASCII");
      
      // on envoi la notification de sortie
      String msg = Protocol.OUT + " " + id + "\n";
      out.write(msg, 0, msg.length());
      out.flush();

      socket.close();
   }
}

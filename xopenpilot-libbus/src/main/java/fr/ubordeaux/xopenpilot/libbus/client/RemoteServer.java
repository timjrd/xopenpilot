package fr.ubordeaux.xopenpilot.libbus.client;

import java.net.*;
import java.io.*;

public class RemoteServer
{
   private Socket socket = null;
   private BufferedReader in = null;
   private OutputStreamWriter out = null;
   
   public RemoteServer(String hostname, int port) throws IOException, UnknownHostException
   {
      socket = new Socket(hostname, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new OutputStreamWriter(socket.getOutputStream());
   }

   public String send(String message) throws IOException
   {
      System.out.println("[Client] sending message: \"" + message + "\"");
      out.write(message + "\n");
      out.flush();

      String response = in.readLine();
      System.out.println("[Client] response: \"" + response + "\"");
      return response;
   }
   
   public void close() throws IOException
   {
      socket.close();
   }
}

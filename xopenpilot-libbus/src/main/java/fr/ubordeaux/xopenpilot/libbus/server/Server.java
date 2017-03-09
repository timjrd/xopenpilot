package fr.ubordeaux.xopenpilot.libbus.server;

import java.util.*;
import java.nio.channels.*;
import java.net.*;
import java.io.*;

public class Server
{
   public interface MessageHandler
   {
      public String messageReceived(String messageLine);
   }
   
   public static void serve(int port, MessageHandler handler) throws IOException, ClosedChannelException
   {
      Selector selector = Selector.open();
      
      ServerSocketChannel serverSocket = ServerSocketChannel.open();
      serverSocket.socket().bind( new java.net.InetSocketAddress(port) );
      serverSocket.configureBlocking(false);
      serverSocket.register(selector, SelectionKey.OP_ACCEPT);

      while(true)
      {
         System.out.println("\n[Server] SELECT");
         int n = selector.select();
         if (n > 0)
         {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            for (SelectionKey key : selectedKeys)
            {
               if ( key.isAcceptable() )
               {
                  System.out.println("[Server] ACCEPT");
                  
                  // Incomming connection
                  SocketChannel clientSocket = serverSocket.accept();
                  if (clientSocket != null)
                  {
                     clientSocket.configureBlocking(false);
                     clientSocket.register(selector, SelectionKey.OP_READ);
                  }
               }
               else if ( key.isReadable() )
               {
                  System.out.println("[Server] READ");
                  
                  SocketChannel clientSocket = (SocketChannel) key.channel();
                  key.cancel();
                  clientSocket.configureBlocking(true);
                  Socket s = clientSocket.socket();
                  
                  String message = new BufferedReader( new InputStreamReader(s.getInputStream()) ).readLine();

                  if (message == null)
                  {
                     // Connection closed
                     clientSocket.close();
                  }
                  else
                  {
                     String response = handler.messageReceived(message);
                     OutputStreamWriter out = new OutputStreamWriter( s.getOutputStream() );
                     out.write(response + "\n");
                     out.flush();

                     clientSocket.configureBlocking(false);
                     selector.selectNow();
                     clientSocket.register(selector, SelectionKey.OP_READ);
                  }
               }
            }

            selectedKeys.clear();
         }
      }
   }
}

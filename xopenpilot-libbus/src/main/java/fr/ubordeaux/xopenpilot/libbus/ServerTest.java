package fr.ubordeaux.xopenpilot.libbus;

import fr.ubordeaux.xopenpilot.libbus.server.Server;
import javax.json.*;
import java.io.StringReader;
import java.io.IOException;

public class ServerTest
{
   static public void main(String[] args) throws IOException
   {
      System.out.println("\n\n\n\n");
      
      Server.serve(Integer.parseInt(args[1]), (message) -> {
            JsonReader reader = Json.createReader(new StringReader(message));
            JsonStructure jsonMessage = reader.read();
            reader.close();
            
            System.out.println( "[Server] json message received: " + jsonMessage.toString() );

            return "a response";
         });
   }
}

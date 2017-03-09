package fr.ubordeaux.xopenpilot.libbus;

import fr.ubordeaux.xopenpilot.libbus.client.RemoteServer;
import javax.json.*;
import java.net.UnknownHostException;
import java.io.IOException;

public class ClientTest
{
   static public void main(String[] args) throws UnknownHostException, IOException
   {
      System.out.println("\n\n\n\n");
      
      RemoteServer remoteServer = new RemoteServer(args[1], Integer.parseInt(args[2]));

      JsonObject root = Json.createObjectBuilder()
         .add("id", 34)
         .add("a_string", "I'm a string")
         .add("an_array", Json.createArrayBuilder()
              .add(18)
              .add(20)
              .add(32)
              .add(Json.createObjectBuilder()
                   .add("a_key", "a value")
                   .add("another_key", "another value")))
         .build();

      String response = remoteServer.send(root.toString());

      remoteServer.close();
   }
}

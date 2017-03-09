// MessageServer.java
package fr.ubordeaux.xopenpilot.libbus.server;

import java.util.Date;
import javax.json.*;

public class MessageServer
{
   public Date getDate()
   {
      return new Date();
   }

   public JsonValue getContent()
   {
      return null;
   }

   public int getId()
   {
      return 0;
   }
}

// MessageServer.java
package fr.ubordeaux.xopenpilot.libbus.server;

import java.util.Date;
import javax.json.*;

public class MessageServer
{
   private Date      date;
   private JsonValue content;
   private int       id;

   public MessageServer(Date date, JsonValue content, int id)
   {
      this.date    = date;
      this.content = content;
      this.id      = id;
   }
   
   public Date getDate()
   {
      return date;
   }

   public JsonValue getContent()
   {
      return content;
   }

   public int getId()
   {
      return id;
   }
}

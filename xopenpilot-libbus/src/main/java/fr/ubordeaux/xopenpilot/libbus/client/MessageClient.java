// MessageClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.util.Date;
import javax.json.*;

public class MessageClient
{
   private Date      date;
   private JsonValue content;
   private int       id;

   MessageClient(Date date, JsonValue content, int id)
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

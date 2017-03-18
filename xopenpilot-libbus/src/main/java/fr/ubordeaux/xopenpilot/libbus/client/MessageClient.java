// MessageClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.util.Date;
import javax.json.*;

public class MessageClient
{
   private Date date;
   private JsonValue content;

   MessageClient(Date date, JsonValue content)
   {
      this.date    = date;
      this.content = content;
   }
   
   public Date getDate()
   {
      return date;
   }

   public JsonValue getContent()
   {
      return content;
   }
}

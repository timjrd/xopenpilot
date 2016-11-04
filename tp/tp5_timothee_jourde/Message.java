import java.util.Date;

public class Message
{
   public final String author;
   public final String message;

   private Date date;

   public Message(String msg, String aut, Date d)
   {
      author  = aut;
      message = msg;
      date    = d;
   }

   public Date getDate()
   {
      return date;
   }
}

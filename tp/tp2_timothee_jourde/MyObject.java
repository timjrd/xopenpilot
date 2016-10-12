import java.io.Serializable;

public class MyObject implements Serializable
{
   private String id;

   public MyObject()
   {}
   
   public MyObject(String id_)
   {
      id = id_;
   }
   
   @Override
   public String toString()
   {
      return "MyObject id: " + id;
   }
}

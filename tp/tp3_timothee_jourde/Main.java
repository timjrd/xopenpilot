import java.io.IOException;

public class Main
{
   static public void main(String[] args) throws IOException, InterruptedException
   {
      Server    server    = new Server(9743);
      InClient  inClient  = new InClient("localhost", 9743);
      OutClient outClient = new OutClient("localhost", 9743);

      server.start();

      Thread.sleep(1000);

      queryInLog(inClient,  12);
      queryInLog(inClient,  24);
      queryInLog(inClient,  12);
      
      sendOutLog(outClient, 12);

      queryInLog(inClient,  12);
      
      sendOutLog(outClient, 24);
      
      server.terminate();
   }

   static public void queryInLog(InClient inClient, long id) throws IOException
   {
      if (inClient.queryIn(id))
         System.out.println("IN " + id + " -> Autorisé");
      else
         System.out.println("IN " + id + " -> REFUS");
   }

   static public void sendOutLog(OutClient outClient, long id) throws IOException
   {
      outClient.sendOut(id);
      System.out.println("OUT " + id);
   }
}

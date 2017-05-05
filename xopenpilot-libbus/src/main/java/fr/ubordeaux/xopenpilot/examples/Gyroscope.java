package fr.ubordeaux.xopenpilot.examples;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonObject;

import fr.ubordeaux.xopenpilot.libbus.client.RemoteBus;
import fr.ubordeaux.xopenpilot.libbus.client.Sender;

public class Gyroscope {

   private static volatile boolean keepRunning = true;
   
   private int x,y,z;
   private RemoteBus bus;
   private Sender sender;

   private Random random;

   public Gyroscope(String host) throws IOException {
      this.bus    = new RemoteBus(host);
      this.sender = bus.registerSender("Gyroscope", "random_gyroscope");
      random = new Random();
   }
   
   public double getX() {
      return x;
   }
   public void setX(int x) throws IOException {
      this.x = x;
      changed();
   }
	
   public double getY() {
      return y;
   }
   public void setY(int y) throws IOException {
      this.y = y;
      changed();
   }
	
   public double getZ() {
      return z;
   }
   public void setZ(int z) throws IOException {
      this.z = z;
      changed();
   }

   public void random() throws IOException {
      x = random.nextInt(360);
      y = random.nextInt(360);
      z = random.nextInt(360);
      changed();
   }

   public JsonObject toJson() {
      return Json.createObjectBuilder()
         .add("x", x)
         .add("y", y)
         .add("z", z)
         .build();
   }

   private void changed() throws IOException {
      if (sender != null)
         sender.sendMessage( toJson() );
   }
		
   public void close() throws IOException {
      if (sender != null)
         sender.deregister();
      if (bus != null)
         bus.close();
   }
	
   public static void main(String[] args) throws InterruptedException {

      if (args.length != 1)
      {
         System.out.println("\n\nPlease provide the bus hostname as argument.\n\n");
         return;
      }

      System.out.println("\n\nSending... (press CTRL-C to interrupt)");
      
      Thread mainThread = Thread.currentThread();
      Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
               keepRunning = false;

               try {
                  mainThread.join();
               }
               catch (InterruptedException e) {
                  System.out.println("Could not properly terminate.");
               }
            }
         });

      try
      {
         Gyroscope gyro = new Gyroscope(args[0]);

         while (keepRunning)
         {
            gyro.random();
            Thread.sleep(100);
         }

         System.out.println("\n\nInterrupted, deregistering the sender and closing the connection.\n\n");
         gyro.close();
      }
      catch (IOException e)
      {
         System.out.println("\n\nConnection error. Please make sure the bus is running at the specified address.\n\n");
      }
   }
	
}

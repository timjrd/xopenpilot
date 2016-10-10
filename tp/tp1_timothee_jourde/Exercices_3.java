import java.util.Random;

public class Exercices_3
{
   static public void main(String[] args)
   {
      System.out.println("=== Exercice 3:\n");
      
      Station station = new Station(0,0,0);

      Drone[] drones = new Drone[5];
      
      drones[0] = new Aerien(station);
      drones[1] = new Aerien(station);
      drones[2] = new Marin(station);
      drones[3] = new SousMarin(station);
      drones[4] = new SousMarin(station);

      // on définit aléatoirement la position des drones (intervalles valides)
      drones[0].setPosition( Vector3.random(-100,100, -100,100, -100,100) );
      drones[1].setPosition( Vector3.random(-900,900, -900,900, -900,900) );
      drones[2].setPosition( Vector3.random(-500,500, -500,500, 0,0) );
      drones[3].setPosition( Vector3.random(-500,500, -500,500, 0,0) );
      drones[4].setPosition( Vector3.random(-400,400, -400,400, -800,0) );

      int i = 1;
      for(Drone drone : drones)
      {
         drone.newMission("mission_" + i); // on lance une mission pour chaque drone
         i++;
      }

      Random r = new Random();
      
      for(Drone drone : drones)
         drone.setBatteryLife( r.nextFloat() ); // les batteries se vide
      
      for(Drone drone : drones)
         if (r.nextBoolean())
            drone.endMission(); // certains drones termine leur mission

      i = 1;
      for(Drone drone : drones)
      {
         if (drone.isMissionCompleted())
         {
            if (drone.canReachStation())
               System.out.println("[++] le drone " + i + " a terminé sa mission et il peut retourner à la station.");
            else
               System.out.println("[+-] le drone " + i + " a terminé sa mission mais il ne peut pas retourner à la station.");
         }
         else
            System.out.println("[--] le drone " + i + " n'a pas terminé sa mission.");

         i++;
      }

      i = 1;
      int firstToReach = -1;
      float minTime = Float.MAX_VALUE;
      for (Drone drone : drones)
      {
         if (drone.isMissionCompleted() && drone.canReachStation())
         {
            float t = drone.timeToReachStation();

            if (t < minTime)
            {
               firstToReach = i;
               minTime = t;
            }
         }
         
         i++;
      }

      System.out.println("\nle drone " + firstToReach + " arrivera en premier.");
   }
}

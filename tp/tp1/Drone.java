
public class Drone
{
   // PRIVATE pour garentir la cohérence de la position (doit être conforme à isValidPosition)
   private Vector3 position = new Vector3(0,0,0);

   // PRIVATE : la station ne doit pas être modifié après la construction
   // on pourrai déclarer ce membre STATIC au prix de la flexibilité
   private Station station;

   // PRIVATE : attributs gérés par newMission() et endMission()
   private String  missionName;
   private boolean ongoingMission = false;

   // PRIVATE : attribut géré par get/setBatteryLife
   private float batteryLife = 1;
   
   public Drone(Station s)
   {
      station = s;
   }

   public void setPosition(Vector3 pos)
   {
      if (isValidPosition(pos))
         position = pos;
   }
   public void move(float dx, float dy, float dz)
   {
      move(new Vector3(dx,dy,dz));
   }
   public void move(Vector3 d)
   {
      setPosition( position.add(d) );
   }

   // méthode redéfinit dans les sous classes validant la position du drone dans l'espace
   public boolean isValidPosition(Vector3 pos)
   {
      return ( pos.x >= -1000 && pos.x <= 1000
               && pos.y >= -1000 && pos.y <= 1000
               && pos.z >= -1000 && pos.z <= 1000 );
   }

   // méthodes redéfinit dans les sous classes exposants les caractéristiques des drones (vitesse et endurance)
   public float speed()
   {
      return 0;
   }
   public float endurance()
   {
      return 0;
   }
   
   public boolean canReachStation()
   {
      float dist = station.position.dist( position );

      float maxDist = endurance() * getBatteryLife();

      return (dist <= maxDist);
   }
   public float timeToReachStation()
   {
      float dist = station.position.dist( position );

      return dist / speed();
   }

   public float getBatteryLife()
   {
      return batteryLife;
   }
   public void setBatteryLife(float x)
   {
      if (x >= 0 && x <= 1)
         batteryLife = x;
   }
   
   public void newMission(String name)
   {
      missionName = name;
      ongoingMission = true;
   }
   public void endMission()
   {
      missionName = null;
      ongoingMission = false;
   }
   public String getMissionName()
   {
      return missionName;
   }
   public boolean isMissionCompleted()
   {
      return ! ongoingMission;
   }
}

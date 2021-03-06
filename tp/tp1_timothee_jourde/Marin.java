
public class Marin extends Drone
{
   public Marin(Station s)
   {
      super(s);
   }
   
   @Override
   public boolean isValidPosition(Vector3 pos)
   {
      return super.isValidPosition(pos)
         && (pos.z == 0);
   }

   @Override
   public float speed()
   {
      return 2;
   }
   @Override
   public float endurance()
   {
      return 5000;
   }
}


public class SousMarin extends Drone
{
   @Override
   public boolean isValidPosition(Vector3 pos)
   {
      return super.isValidPosition(pos)
         && (pos.z <= 0 && pos.z >= -1000);
   }

   @Override
   public float speed()
   {
      return 5;
   }
   @Override
   public float endurance()
   {
      return 1500;
   }
}

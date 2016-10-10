import java.util.Random;


public class Vector3
{
   // object immuable, x y z sont accesibles publiquement en lecture seul
   public final float x;
   public final float y;
   public final float z;

   public Vector3(float ox, float oy, float oz)
   {
      x = ox;
      y = oy;
      z = oz;
   }

   public Vector3 add(float ox, float oy, float oz)
   {
      return add(new Vector3(ox,oy,oz));
   }
   public Vector3 add(Vector3 o)
   {
      return new Vector3(x + o.x, y + o.y, z + o.z);
   }
   
   public Vector3 sub(float ox, float oy, float oz)
   {
      return sub(new Vector3(ox,oy,oz));
   }
   public Vector3 sub(Vector3 o)
   {
      return new Vector3(x - o.x, y - o.y, z - o.z);
   }

   public float dist(Vector3 o)
   {
      Vector3 d = sub(o);

      return (float) Math.sqrt(d.x*d.x + d.y*d.y + d.z*d.z);
   }
   
   static public Vector3 random(float xMin, float yMin, float zMin,
                                float xMax, float yMax, float zMax)
   {
      Random r = new Random();

      Vector3 min = new Vector3(xMin, yMin, zMin);
      Vector3 max = new Vector3(xMax, yMax, zMax);

      Vector3 d = max.sub(min);
      
      return new Vector3(
         r.nextFloat() * d.x + min.x,
         r.nextFloat() * d.y + min.y,
         r.nextFloat() * d.z + min.z
         );
   }
}


public class Station
{
   // attribut publique immuable (la station ne peut pas bouger)
   public final Vector3 position;

   public Station(float x, float y, float z)
   {
      position = new Vector3(x,y,z);
   }
}

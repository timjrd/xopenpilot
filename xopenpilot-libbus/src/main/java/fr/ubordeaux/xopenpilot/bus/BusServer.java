package fr.ubordeaux.xopenpilot.bus;

import fr.ubordeaux.xopenpilot.libbus.server.*;

public class BusServer
{
   static public void main(String[] args) throws java.io.IOException
   {
      Server.serve(7182, new MessageHandler( new BusImpl() ));
   }
}

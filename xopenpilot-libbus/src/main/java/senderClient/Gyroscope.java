package senderClient;

import java.io.IOException;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;

import fr.ubordeaux.xopenpilot.libbus.client.RemoteBus;
import fr.ubordeaux.xopenpilot.libbus.client.Sender;

public class Gyroscope {
	private JsonObject contents;
	private double x,y,z;
	private RemoteBus bus;
	private Sender sender; 

	public JsonObject getContents() {
		return contents;
	}

	public void setContents(JsonObject contents) {
		this.contents = contents;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public Gyroscope(String host) throws IOException{
		this.bus = new RemoteBus(host, 7182);
		this.sender = bus.registerSender("Gyroscope", "monGyroscope");
		
	}
	
	public void run() throws IOException{
		boolean running = true; 
		String line = "";
		Scanner in = new Scanner(System.in);
		if (sender != null){
			while (running){
				System.out.println("<send position x y z>");	
				line = in.nextLine();		
				if(line.equals("quit")){
					running = false;
				}			
				else{
					x = Double.parseDouble(line);
					line = in.nextLine();
					y = Double.parseDouble(line);
					line = in.nextLine();
					z = Double.parseDouble(line);				
		
					contents = Json.createObjectBuilder()
							.add("x", x)
							.add("y", y)
							.add("z", z)
							.build();
					
					sender.sendMessage(contents);
				}
			}
		}
	}
	
	public void close() throws IOException{
		if (sender != null){
			sender.deregister();
			bus.close();
		}
	}
	
	public static void main(String[] args) throws IOException{
		Gyroscope gyro = new Gyroscope("10.0.104.10");
		gyro.run();
		gyro.close();
	}
	
}

package senderClient;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
	
	public Gyroscope(String host, int ip) throws IOException{
		this.bus = new RemoteBus(host, ip);
		this.sender = bus.registerSender("Gyroscope", "monGyroscope");
		
	}
	
	public void run() throws IOException, InterruptedException{
		boolean running = true;
		Random random = new Random();
		if (sender != null){
			while (running){
				x = random.nextInt() + random.nextDouble();
				y = random.nextInt() + random.nextDouble();
				z = random.nextInt() + random.nextDouble();
				System.out.println("x :" + x + " y : " + y + " z : " + z);
				System.out.println("sending position...");
				contents = Json.createObjectBuilder()
						.add("x", x)
						.add("y", y)
						.add("z", z)
						.build();
				sender.sendMessage(contents);
				//le moniteur se met en pause pendant 10sec
				TimeUnit.SECONDS.sleep(10);
			}
		}
	}
	
	
	public void close() throws IOException{
		if (sender != null){
			sender.deregister();
			bus.close();
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException{
		Gyroscope gyro = new Gyroscope("10.0.104.10", 7182);
		gyro.run();
		gyro.close();
	}
	
}

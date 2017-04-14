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
	
	public Gyroscope() throws IOException{
		RemoteBus bus = new RemoteBus("localhost", 7182);
		Sender sender = bus.registerSender("Gyroscope", "monGyroscope");
		Scanner in = new Scanner(System.in);
		
		if (sender != null){
			while (true){
				System.out.println("<enter message>");
				String line = in.nextLine();
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

package fr.ubordeaux.xopenpilot.libbus.server;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;

public class MessageHandler implements Server.MessageHandler {
	private Bus bus;

	public MessageHandler(Bus bus){
		this.bus = bus;
	}
	
	public String register(JsonObject messageObject){
		this.bus.registerSender(messageObject.getString("sender_class"), messageObject.getString("sender_name"));
		JsonObject response = Json.createObjectBuilder()
				.add("type", "register")
				.add("sender_id", 1)
				.add("ack", Json.createObjectBuilder()
						.add("resp", "ok"))
				.build();		
		return response.toString();
	}
	
	@Override
	public String messageReceived(String messageLine) {
		String type;
	    JsonObject messageObject = Json.createReader(new StringReader(messageLine)).readObject();
	    //messageObject.getString("type") == "register";
	    type = messageObject.getString("type");
		if (type == "register")
			return register(messageObject);
		/*if type == etc etc return methode*/
			
		return null;
	}
	

}

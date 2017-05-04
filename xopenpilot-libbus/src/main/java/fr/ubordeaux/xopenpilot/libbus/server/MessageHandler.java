package fr.ubordeaux.xopenpilot.libbus.server;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

public class MessageHandler implements Server.MessageHandler {
	private Bus bus;

	public MessageHandler(Bus bus){
		this.bus = bus;
	}
	//test
	public JsonObject ack_ok(){
		JsonObject response = Json.createObjectBuilder()
				.add("resp", "ok")
				.build();
		return response;
	}
	
	public JsonObject ack_error(int error_id){
		JsonObject response = Json.createObjectBuilder()
				.add("resp", "error")
				.add("error_id", error_id)
				.build();
		return response;
	}
	
	public String getStringIfPresent(JsonObject messageObject, String key){
		JsonValue value = messageObject.get(key);
		if(value != null)
			return ((JsonString) value).getString();
		return null;
	}
	
	public String register(JsonObject messageObject){
		int id = this.bus.registerSender(messageObject.getString("sender_class"), messageObject.getString("sender_name"));
		JsonObject response = Json.createObjectBuilder()
				.add("type", "register")
				.add("sender_id", id)
				.add("ack", (id  == -1)? ack_error(400) :ack_ok())
				.build();		
		return response.toString();
	}
	
	public String deregister(JsonObject messageObject){
		this.bus.deregisterSender(messageObject.getInt("sender_id"));
		JsonObject response = Json.createObjectBuilder()
				.add("type", "deregister")
				.add("ack", Json.createObjectBuilder()
						.add("resp", "ok"))
				.build();
		return response.toString();
	}
	
	public String list(JsonObject messageObject){
		String sender_class = getStringIfPresent(messageObject, "sender_class");
		String sender_name = getStringIfPresent(messageObject, "sender_name");
		JsonBuilderFactory factory = Json.createBuilderFactory(messageObject);
		SenderInfoServer[] list_senders = this.bus.listSenders(sender_class, sender_name);
		JsonArrayBuilder result = factory.createArrayBuilder();
		for(SenderInfoServer sender : list_senders){
			result.add(factory.createObjectBuilder()
					.add("sender_id", sender.getSenderId())
					.add("sender_class", sender.getSenderClass())
					.add("sender_name", sender.getSenderName())
					.add("last_message_id", sender.getLastMessageId()));
		}
		JsonObject response = Json.createObjectBuilder()
				.add("type", "list")
				.add("ack", Json.createObjectBuilder()
						.add("resp", "ok"))
				.add("results", result)
				.build();
		return response.toString();
	}
	
	public String send(JsonObject messageObject){
		this.bus.messageReceived(messageObject.getInt("sender_id"), messageObject.get("contents"));
		JsonObject response = Json.createObjectBuilder()
				.add("type", "send")
				.add("ack", Json.createObjectBuilder()
						.add("resp", "ok"))
				.build();
		return response.toString();
	}
	
	public String get(JsonObject messageObject){
		MessageServer messageServer = this.bus.getMessage(messageObject.getInt("sender_id"), messageObject.getInt("msg_id"));
		if(messageServer == null){
			JsonObject response = Json.createObjectBuilder()
					.add("type", "get")
					.add("ack", ack_error(400))
					.build();
			return response.toString();			
		}
		JsonObject response = Json.createObjectBuilder()
				.add("type", "get")
				.add("ack", ack_ok())
				.add("msg_id", messageServer.getId())
				.add("date", messageServer.getDate().getTime())
				.add("contents", messageServer.getContent())
				.build();
		return response.toString();
	}
	
	public String getLast(JsonObject messageObject){
		MessageServer messageServer = this.bus.getLastMessage(messageObject.getInt("sender_id"));
		if(messageServer == null){
			JsonObject response = Json.createObjectBuilder()
					.add("type", "get_last")
					.add("ack", ack_error(400))
					.build();
			return response.toString();
		}
		JsonObject response = Json.createObjectBuilder()
				.add("type", "get_last")
				.add("ack", ack_ok())
				.add("msg_id", messageServer.getId())
				.add("date", messageServer.getDate().getTime())
				.add("contents", messageServer.getContent())
				.build();
		return response.toString();
	}
	
	@Override
	public String messageReceived(String messageLine) {
		String type;
	    JsonObject messageObject = Json.createReader(new StringReader(messageLine)).readObject();
	    type = messageObject.getString("type");
                if (type.equals("register"))
			return register(messageObject);
		if (type.equals("deregister"))
			return deregister(messageObject);
		if(type.equals("list"))
			return list(messageObject);
		if(type.equals("send"))
			return send(messageObject);
		if(type.equals("get"))
			return get(messageObject);
		if(type.equals("get_last"))
			return getLast(messageObject);			
		return null;
	}
	

}

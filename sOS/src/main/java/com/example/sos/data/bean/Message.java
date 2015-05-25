package com.example.sos.data.bean;

public class Message {

	private String id;
	private String message;
	
	public Message(){}
	
	public Message(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
}



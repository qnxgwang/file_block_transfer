package entity;

import java.io.Serializable;

public class Message implements Serializable{
	

	private int messageType ;
	private User user ;
	
	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

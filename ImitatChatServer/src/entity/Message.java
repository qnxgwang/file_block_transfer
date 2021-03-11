package entity;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{
	

	private int messageType ;
	private User user ;
	private List<User> userList ;
	
	public int getMessageType() {
		return messageType;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
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

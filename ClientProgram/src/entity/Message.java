package entity;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int messageType ;
	
	private User user ;
	
	private List<User> userList ;
	
	private FileInformation fileInformation;
	
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
	
	public List<User> getUserList() {
		return userList;
	}
	
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public FileInformation getFileInformation() {
		return fileInformation;
	}

	public void setFileInformation(FileInformation fileInformation) {
		this.fileInformation = fileInformation;
	}	
	
}

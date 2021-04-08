package entity;

import java.io.Serializable;

public class ChatContent implements Serializable{
	
	private String timeStamp ;
	
	private String messageFrom ;
	
	private String messageTo;
	
	private String content ;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getMessageFrom() {
		return messageFrom;
	}

	public void setMessageFrom(String messageFrom) {
		this.messageFrom = messageFrom;
	}

    

	public String getMessageTo() {
		return messageTo;
	}

	public void setMessageTo(String messageTo) {
		this.messageTo = messageTo;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

}

package entity;

import java.io.Serializable;

public class ChatContent implements Serializable{
	
	private String timeStamp ;
	private String From ;
	private String to;
	private String content ;
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getFrom() {
		return From;
	}
	public void setFrom(String from) {
		From = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return new String("(From="+From+",content="+content+")");
	}
	

}

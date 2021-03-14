package entity;

import java.io.Serializable;
import java.util.List;
/**
 * Serializable –Ú¡–ªØ
 * @author 14005
 *
 */
public class User implements Serializable{

	private int id;
	private String username;
	private String pwd;
	private String realname;
	private String photo;
	private ChatContent content;
	private List<ChatContent> contentList;
	private boolean onlineFlag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public ChatContent getChatContent() {
		return content;
	}
	public void setChatContent(ChatContent content) {
		this.content = content;
	}
	public List<ChatContent> getContentList() {
		return contentList;
	}
	public void setContentList(List<ChatContent> contentList) {
		this.contentList = contentList;
	}
	public boolean isOnlineFlag() {
		return onlineFlag;
	}
	public void setOnlineFlag(boolean onlineFlag) {
		this.onlineFlag = onlineFlag;
	}
	@Override
    public String toString() {
		String message = "";
		if(contentList != null) {
			for(ChatContent content: contentList) {
				message += content.toString();
			}
		}
		else message = "NULL";
		
    	return new String("User{username="+username+",onlineFlag="+onlineFlag+",chatContent=["+message+"]}");
    }

}

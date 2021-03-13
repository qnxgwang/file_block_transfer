package Thread;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import entity.Message;
import util.MessageType;
import util.FileUtil;
import util.SocketUtil;
import view.ChatView;
import view.FriendListView;


public class DataThread extends Thread{

	Socket datasocket;
	FriendListView friendListView;
	public DataThread(Socket datasocket,FriendListView friendListView) {
		this.datasocket = datasocket;
		this.friendListView = friendListView;
	}
	@Override
	public void run() {
		while(friendListView != null) {
			try {
				Message reponseMessage = SocketUtil.getSocketUtil().readMessage(datasocket);
				switch(reponseMessage.getMessageType()) {
				case MessageType.MESSAGE_FROM_FRIEND:{		
	                String username = reponseMessage.getUser().getChatContent().getTo();
	                String friendname = reponseMessage.getUser().getChatContent().getFrom();
	                String time = reponseMessage.getUser().getChatContent().getTimeStamp();
	                String content = reponseMessage.getUser().getChatContent().getContent();
	                FileUtil.writeFriendMessageToFile(username, time, friendname, content);
	                JLabel[] jLabel = friendListView.jLabel;
	                ChatView chatView = friendListView.tempChatView;
	                boolean flag = false; 
	                if(chatView != null) {
	                	if(chatView.friendUsername.equals(friendname)) {
	                		Document docs = chatView.recordPanel.getDocument();//获得文本对象
	        			    try {
	        			    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
	        			        docs.insertString(docs.getLength(),new Date().toString()+" "+friendname+" "+content+'\n',attributeSet);//对文本进行追加			    
	        			    } catch (BadLocationException e) {
	        			        e.printStackTrace();
	        			    }	
	        			    flag = true;
	                	}
	                }  
	                if(!flag) {
	                	 for(int i=0;i<jLabel.length-1;i++) {
		                     if(jLabel[i].getText().equals(friendname)) {
		                     	jLabel[i].setForeground(Color.red);
		                     }
	                     }
	                	 friendListView.revalidate();
	                }              
				}
				case MessageType.UPDATE_FRIEND_LIST:{		
					/**
					 * 更新好友在线信息
					 */
				}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		try {
			datasocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
}

package handle;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import entity.FileData;
import entity.Message;
import entity.User;
import util.FileUtil;
import util.MessageType;
import util.SocketUtil;
import view.ChatView;
import view.FriendListView;


public class DataThread extends Thread{
	
	/**
	 * 心跳线程，负责接收数据
	 */
	
	private Socket datasocket;
	
	private FriendListView friendListView;
	
	/**
	 * 构造器
	 * @param datasocket 套接字
	 * @param friendListView 好友列表窗口
	 */
	public DataThread(Socket datasocket,FriendListView friendListView) {
		this.datasocket = datasocket;
		this.friendListView = friendListView;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				//持续接收数据,维持与服务器的通信
				Message reponseMessage = SocketUtil.getSocketUtil().readMessage(datasocket);

				switch(reponseMessage.getMessageType()) {
				/*
				 * 收到服务器发来的信息"好友发送文字消息，需要显示在聊天面板上"
				 */
				case MessageType.MESSAGE_FROM_FRIEND:{		
	                DataHandle.updateTextArea(reponseMessage, friendListView);
	                break;
				}
				/*
				 * 收到服务器发来的信息"有好友登录，需要更新在线列表"
				 */
				case MessageType.UPDATE_FRIEND_LIST_LOGIN:{		
					DataHandle.updateListView(reponseMessage, "src/Image/", friendListView);
					break;
				}
				/*
				 *  收到服务器发来的信息"有好友退出登录，需要更新在线列表"
				 */
				case MessageType.UPDATE_FRIEND_LIST_EXIT:{					
					DataHandle.updateListView(reponseMessage, "src/Image/gray", friendListView);
					break;
				}
				/*
				 *  收到来自服务器的文件，这里只包含"文件名字"
				 */
				case MessageType.FILE_FROM_FRIEND:{
					DataHandle.updateLocalDB(reponseMessage, friendListView);
					break;
				}
				/*
				 * 收到来自服务器的文件，这里包含"文件名字"和"文件数据"
				 */
				case MessageType.FILE_REQUEST:{
					DataHandle.downloadFile(reponseMessage, datasocket);												
					break;					
				}				
				default:break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
}

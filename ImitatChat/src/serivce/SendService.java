package serivce;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.AbstractDocument.Content;

import entity.ChatContent;
import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class SendService {
	

	/**
	 * 发送信息给好友
	 * @param username
	 * @param friendname
	 * @param content
	 */
	public void sendMessage(String username,String friendname,String content) {
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.MESSAGE_TO_FRIEND);
			User user = new User();
			ChatContent chatContent = new ChatContent();
			chatContent.setTimeStamp(new Date().toString());
			chatContent.setFrom(username);
			chatContent.setTo(friendname);
			chatContent.setContent(content);	
			user.setChatContent(chatContent);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}
}

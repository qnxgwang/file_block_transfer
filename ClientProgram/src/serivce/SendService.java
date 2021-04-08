package serivce;

import java.net.Socket;
import java.util.Date;

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
	public static void sendMessage(String messagefrom,String messageto,String content) {
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.MESSAGE_TO_FRIEND);
			User user = new User();
			ChatContent chatContent = new ChatContent();
			chatContent.setTimeStamp(new Date().toString());
			chatContent.setMessageFrom(messagefrom);
			chatContent.setMessageTo(messageto);
			chatContent.setContent(content);	
			user.setChatContent(chatContent);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}
	
}

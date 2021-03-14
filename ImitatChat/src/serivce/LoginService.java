package serivce;

import java.net.Socket;
import java.util.List;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class LoginService {

	private Socket loginSocket = null;
	private List<User> userList = null;
	public Socket getDataSocket() {
		return loginSocket;
	}
	public List<User> getUserList() {
		return userList;
	}
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	public boolean login(User user) {	    
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getLoginPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.LOGIN);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//向服务端发送登录请求
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//从服务端接收反馈		
			if(responsemessage.getMessageType() == MessageType.LOGIN_SUCCESS) {		
				userList = responsemessage.getUserList();
				loginSocket = socket;
				return true;
			}
			else 
				return false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 将登录信息发送给好友(服务器转发)
	 * @param username
	 */
	public void sendLoginMessageToFriend(String  username) {
		System.out.println("执行sendLoginMessageToFriend"+" "+"参数@username="+username);
		try {
			Socket socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			Message requestMessage = new Message();
			User user = new User();
			user.setUsername(username);
			requestMessage.setMessageType(MessageType.LOGIN_TO_FRIEND);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//向好友发送登录信息
			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}

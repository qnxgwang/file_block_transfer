package serivce;

import java.net.Socket;

import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class RegisterService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	public boolean register(User user) {		    
		try {
			Socket socket = new Socket(IpUtil.getIpUtil().getIp(),IpUtil.getIpUtil().getPort());
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.REGISTER);
			requestMessage.setUser(user);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);//向服务端发送注册请求
			Message responsemessage = SocketUtil.getSocketUtil().readMessage(socket);//从服务端接收反馈	
			socket.close();			
			if(responsemessage.getMessageType() == MessageType.REGISTER_SUCCESS) {
				return true;
			}
			else 
				return false;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}

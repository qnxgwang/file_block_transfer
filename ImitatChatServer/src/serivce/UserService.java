package serivce;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.UserDao;
import entity.Message;
import entity.User;
import util.MessageType;
import util.SocketUtil;

public class UserService extends JFrame{
	public static void main(String [] args) {
		UserService serverView = new UserService();
		serverView.createFrame();
	}
	
	UserDao userDao = new UserDao(); 
	JLabel loginLabel = new JLabel("登录服务已经开启1.0");
	JLabel registerLabel = new JLabel("注册服务已经开启2.0");
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	public void createFrame() {
		panel.add(loginLabel);
		panel.add(registerLabel);
		this.add(panel);
		this.setTitle("服务端");
		this.setBounds(500,500,500,500);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			this.server();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void server() throws IOException, ClassNotFoundException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(9999);
		while(true) {
			Socket socket = serverSocket.accept();
			Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
			switch(requestMessage.getMessageType()) {
				case MessageType.LOGIN:{
					Message responseMessage = new Message();
					if(userDao.login(requestMessage.getUser().getUsername(),requestMessage.getUser().getPwd())!=null) {
						responseMessage.setMessageType(MessageType.LOGIN_SUCCESS);
					}
					else {
						responseMessage.setMessageType(MessageType.LOGIN_FAILURE);
					}
					SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
					break;
				}
				case MessageType.REGISTER:{
					Message responseMessage = new Message();
					if(userDao.queryUserByUsername(requestMessage.getUser().getUsername())==null) {
						userDao.register(requestMessage.getUser());
						responseMessage.setMessageType(MessageType.REGISTER_SUCCESS);
					}
					else {
						responseMessage.setMessageType(MessageType.REGISTER_FAILURE);
					}
					SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
					break;
				}
				case MessageType.GET_FRIEND_LIST:{
					Message responseMessage = new Message();
					User user = requestMessage.getUser();
					List<User> userList =  userDao.getFriendListByUsername(user.getUsername());
					if(userList != null) {
						responseMessage.setUserList(userList);
						responseMessage.setMessageType(MessageType.GET_FRIEND_LIST_SUCCESS);
					}
					else {
						responseMessage.setMessageType(MessageType.GET_FRIEND_LIST_FAILURE);
					}
					SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
					break;
				}
			}
			socket.close();
		}
	}
}

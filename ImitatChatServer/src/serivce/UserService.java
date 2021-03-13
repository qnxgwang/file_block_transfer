package serivce;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.DataDao;
import dao.UserDao;
import entity.ChatContent;
import entity.Message;
import entity.User;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class UserService extends JFrame{
	public static void main(String [] args) {
		UserService serverView = new UserService();
		serverView.createFrame();
	}
	
	UserDao userDao = new UserDao(); 
	DataDao dataDao = new DataDao();  
	JLabel Label = new JLabel("服务已经开启3.0");
	JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	HashMap<String,Socket> loginSocketMap = new HashMap<String,Socket>();
	
	public void createFrame(){
		panel.add(Label);
		this.add(panel);
		this.setTitle("服务端");
		this.setBounds(500,500,500,500);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new Thread() {
        	public void run() {
        		try {
					serverForChat();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }.start();
        new Thread() {
        	public void run() {
        		try {
					serverForOrder();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }.start();
	}
	/**
	 * 登录并返回好友列表
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public void serverForChat() throws IOException, ClassNotFoundException {
		ServerSocket serverSocketForChat = new ServerSocket(IpUtil.getLoginPort());
		while(true) {
			Socket socket = serverSocketForChat.accept();
			Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
			Message responseMessage = new Message();
			if(userDao.login(requestMessage.getUser().getUsername(),requestMessage.getUser().getPwd())!=null) {
				List<User> userList =  userDao.getFriendListByUsername(requestMessage.getUser().getUsername());
				List<User> responseUserList = new ArrayList<User>();
                for(int i=0;i<userList.size();i++) {
                	User user = userList.get(i);
                	List<ChatContent> contentList = dataDao.queryData(requestMessage.getUser().getUsername(), user.getUsername());
                	user.setContentList(contentList);
                	user.setOnlineFlag(loginSocketMap.containsKey(user.getUsername()));
                	responseUserList.add(user);
                }
                responseMessage.setUserList(responseUserList);
				responseMessage.setMessageType(MessageType.LOGIN_SUCCESS);			
				loginSocketMap.put(requestMessage.getUser().getUsername(), socket);	
			}
			else {
				responseMessage.setMessageType(MessageType.LOGIN_FAILURE);
			}
			SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);	
		}
	}
	/**
	 * 接收指令
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public void serverForOrder() throws IOException, ClassNotFoundException, InterruptedException {	
		ServerSocket serverSocketForOrder = new ServerSocket(IpUtil.getOrderPort());
		while(true) {
			Socket socket = serverSocketForOrder.accept();
			Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
			switch(requestMessage.getMessageType()) {
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
				case MessageType.MESSAGE_TO_FRIEND:{
					String username = requestMessage.getUser().getChatContent().getTo();
					
					if(loginSocketMap.containsKey(username)) {
						requestMessage.setMessageType(MessageType.MESSAGE_FROM_FRIEND);
						Socket loginSocket = loginSocketMap.get(username);
						SocketUtil.getSocketUtil().writeMessage(loginSocket, requestMessage);
					}
					else {
						ChatContent chatContent= requestMessage.getUser().getChatContent();
						dataDao.insertData(chatContent);
					}							
				}
				case MessageType.LOGIN_EXIT:{
					loginSocketMap.remove(requestMessage.getUser().getUsername());
					/**
					 * 将用户退出登陆的信息发送给好友
					 */
				}
				case MessageType.MESSAGE_DELETE:{
					/**
					 * 将用户获得的信息删除
					 */
				}
			}
			socket.close();
		}
	}
}

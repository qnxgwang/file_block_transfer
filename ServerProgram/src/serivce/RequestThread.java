package serivce;

import static java.lang.System.out;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import entity.Message;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class RequestThread extends Thread{
	
	
	private Map<String,Socket> loginSocketMap = null;
	
	private ExecutorService executor = null;
	
	public RequestThread(Map<String,Socket> loginSocketMap, ExecutorService executor) {
		this.loginSocketMap = loginSocketMap;
		this.executor = executor;
	}
	
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(IpUtil.getOrderPort());
			while(true) {
				//socket不能在finally里关闭,因为有些方法会持续使用socket,socket使用完毕后在接口里关闭
				Socket socket = serverSocket.accept();
				Message requestMessage = SocketUtil.getSocketUtil().readMessage(socket);
				handleMessage(socket,requestMessage);									
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void handleMessage(Socket socket, Message requestMessage) {
		switch(requestMessage.getMessageType()) {
		    /*
		     * 处理注册信息
		     */
			case MessageType.REGISTER:{
				out.println("case MessageType.REGISTER");
				executor.execute(()->{
					HandleMessageUtil.handleRegister(requestMessage, socket);																
				});
				break;				
			}
			/*
			 * 处理用户发给好友的信息
			 */
			case MessageType.MESSAGE_TO_FRIEND:{
				out.println("case MessageType.MESSAGE_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.sendMessageToFriend(requestMessage, loginSocketMap, socket);												
				});					
				break;
			}
			/*
			 * 用户退出登录，将用户socket从map中移除，并告知好友
			 */
			case MessageType.EXIT:{
				out.println("case MessageType.EXIT");
				executor.execute(()->{
					HandleMessageUtil.exitLogin(requestMessage, loginSocketMap, socket);
				});					
				break;
			}
			/*
			 * 用户登录,将用户登录信息发送给好友
			 */
			case MessageType.LOGIN_TO_FRIEND:{		
				out.println("case MessageType.LOGIN_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.updateOnlineList(requestMessage, loginSocketMap, socket);					
				});				
				break;
			}			
			/*
			 * 客户端发送文件请求
			 */
			case MessageType.FILE_TO_FRIEND:{
				out.println("case MessageType.FILE_TO_FRIEND");
				executor.execute(()->{
					HandleMessageUtil.sendFileToFriend(requestMessage, loginSocketMap, socket);					
				});
				break;
			}
			/*
			 * 客户端读取文件请求
			 */
			case MessageType.FILE_REQUEST:{
				out.println("case MessageType.FILE_REQUEST");
				executor.execute(()->{
					HandleMessageUtil.requestFile(requestMessage, loginSocketMap, socket);						
				});
				break;
			}
			default:break;
		}				
	}
}

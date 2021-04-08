package serivce;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.DataDao;
import dao.FileDao;
import dao.UserDao;
import entity.ChatContent;
import entity.FileData;
import entity.FileInformation;
import entity.Message;
import entity.User;
import util.MessageType;
import util.SocketUtil;

public class HandleMessageUtil {

	/**
	 * 处理用户登录请求
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 * @throws IOException
	 */
	static void handleLogin(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) throws IOException {
		Message responseMessage = new Message();
		String loginname = requestMessage.getUser().getUsername();
		if(UserDao.login(loginname,requestMessage.getUser().getPwd()) != null) {
			List<User> userList =  UserDao.getFriendListByUsername(loginname);//拿到好友列表
			userList.forEach(u->{
				List<ChatContent> contentList = DataDao.queryData(u.getUsername(), loginname);//查询好友发来的离线信息
            	u.setContentList(contentList);
            	u.setOnlineFlag(loginSocketMap.containsKey(u.getUsername()));
			});
            responseMessage.setUserList(userList);
			responseMessage.setMessageType(MessageType.LOGIN_SUCCESS);	
			loginSocketMap.put(loginname, socket);				
			DataDao.deleteData(loginname);//将好友发送的的离线信息从数据库删除
		}
		else {
			responseMessage.setMessageType(MessageType.LOGIN_FAILURE);
		}
		SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
	}
	
	/**
	 * 处理注册信息
	 * @param requestMessage
	 * @param socket
	 */
	static void handleRegister(Message requestMessage, Socket socket) {
		Message responseMessage = new Message();
		if(UserDao.queryUserByUsername(requestMessage.getUser().getUsername()) == null) {
			UserDao.register(requestMessage.getUser());
			responseMessage.setMessageType(MessageType.REGISTER_SUCCESS);
		}
		else {
			responseMessage.setMessageType(MessageType.REGISTER_FAILURE);
		}
		try {
			SocketUtil.getSocketUtil().writeMessage(socket, responseMessage);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 处理用户发给好友的信息
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 */
	static void sendMessageToFriend(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) {
		String messageTo = requestMessage.getUser().getChatContent().getMessageTo();					
		if(loginSocketMap.containsKey(messageTo)) {
			requestMessage.setMessageType(MessageType.MESSAGE_FROM_FRIEND);
			Socket loginSocket = loginSocketMap.get(messageTo);
			try {
				SocketUtil.getSocketUtil().writeMessage(loginSocket, requestMessage);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			ChatContent chatContent= requestMessage.getUser().getChatContent();
			DataDao.insertData(chatContent);
		}
	}
	
	/**
	 * 用户退出登录，将用户socket从map中移除，并告知好友
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 */
	static void exitLogin(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) {
		loginSocketMap.remove(requestMessage.getUser().getUsername());
		for(Socket loginSocket : loginSocketMap.values()) {
			requestMessage.setMessageType(MessageType.UPDATE_FRIEND_LIST_EXIT);
			try {
				SocketUtil.getSocketUtil().writeMessage(loginSocket, requestMessage);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 用户登录,将用户登录信息发送给好友
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 */
	static void updateOnlineList(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) {
		for(String key : loginSocketMap.keySet()) {
			if(! key.equals(requestMessage.getUser().getUsername())) {
				requestMessage.setMessageType(MessageType.UPDATE_FRIEND_LIST_LOGIN);
				try {
					SocketUtil.getSocketUtil().writeMessage(loginSocketMap.get(key), requestMessage);
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}						
		}
	}
	
	/**
	 * 客户端发送文件请求
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 */
	static void sendFileToFriend(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) {
		try {
			FileInformation info= requestMessage.getFileInformation();
			String totalIndex = info.getTotalIndex();
			String filename = info.getFileName();
			String sendname = info.getSendname();
			String receivename = info.getReceivename();
			boolean flag = FileDao.hasFile(sendname, receivename, filename, totalIndex);
			for(int i = 1; i<=Integer.valueOf(totalIndex); i++) {
				FileData filedata = SocketUtil.getSocketUtil().readFileData(socket);
				if(! flag)FileDao.insertFile(filedata,filename,sendname,receivename,totalIndex);							
			}
			if(loginSocketMap.containsKey(receivename)) {
				Socket loginSocket = loginSocketMap.get(receivename);
				requestMessage.setMessageType(MessageType.FILE_FROM_FRIEND);
				SocketUtil.getSocketUtil().writeMessage(loginSocket,requestMessage);
			}							
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 客户端读取文件请求
	 * @param requestMessage
	 * @param loginSocketMap
	 * @param socket
	 */
	static void requestFile(Message requestMessage, Map<String,Socket> loginSocketMap, Socket socket) {
		FileInformation info = requestMessage.getFileInformation();
		Socket loginSocket = loginSocketMap.get(info.getReceivename());
		List<FileData> list = FileDao.getFile(info.getSendname(), info.getReceivename(), info.getFileName());
		try {
			info.setTotalIndex(Integer.toString(list.size()));
			requestMessage.setFileInformation(info);
			SocketUtil.getSocketUtil().writeMessage(loginSocket,requestMessage);
            for(FileData f : list) {
            	SocketUtil.getSocketUtil().writeFileData(loginSocket,f);			
            }	
            socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
}

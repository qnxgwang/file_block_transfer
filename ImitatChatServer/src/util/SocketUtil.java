package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import entity.Message;

public class SocketUtil {

	private static SocketUtil socketUtil = null;
	
	public static SocketUtil getSocketUtil() {
		if(socketUtil == null) {
			socketUtil = new SocketUtil();
		}
		
		return socketUtil;
	}
	public void writeMessage(Socket socket,Message responseMessage) throws IOException {
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(responseMessage);
	}
	
	public Message readMessage(Socket socket) throws IOException, ClassNotFoundException {
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);		
		Message requestMessage = (Message)objectInputStream.readObject();//字节流反序列化
		return requestMessage;
	}
}

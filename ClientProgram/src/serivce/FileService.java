package serivce;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import entity.FileData;
import entity.FileInformation;
import entity.Message;
import util.IpUtil;
import util.MessageType;
import util.SocketUtil;

public class FileService {

	/**
	 * 向服务器请求文件
	 */
	public static void requestFile(String receivename, String sendname, String filename) {
		Socket socket = null;
		try {
			socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			FileInformation info = new FileInformation();
			{
				info.setReceivename(receivename);
				info.setSendname(sendname);
				info.setFileName(filename);
			}
			Message requestMessage = new Message();
			requestMessage.setMessageType(MessageType.FILE_REQUEST);		
			requestMessage.setFileInformation(info);
			SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);	
			System.out.println("向服务器请求文件");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 向服务器发送文件的接口
	 * @param sendname
	 * @param receivename
	 * @param filename
	 * @param location
	 */
	public static void sendMessage(String sendname,String receivename,String filename,String location) {
		File file = null;
		FileInputStream fi = null;
		BufferedInputStream bi = null;
		Socket socket = null;
		try {
			socket = new Socket(IpUtil.getIp(),IpUtil.getOrderPort());
			file = new File(location);
			fi = new FileInputStream(file);
			bi = new BufferedInputStream(fi);				
			int totalIndex = (int) (file.length()/1024) + 1;	
			if(file.length()%1024 == 0) totalIndex -= 1;
			sendRequestMessage(socket,sendname,receivename,filename,totalIndex);
			sendFileByBlock(socket,file,bi,totalIndex);					
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fi.close();
				bi.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 告诉服务器,客户端需要发送文件
	 * @param socket
	 * @param sendname
	 * @param receivename
	 * @param filename
	 * @param totalIndex
	 * @throws IOException
	 */
	public static void sendRequestMessage(Socket socket,String sendname,String receivename,String filename,int totalIndex) throws IOException {
		Message requestMessage = new Message();			
		requestMessage.setMessageType(MessageType.FILE_TO_FRIEND);
		FileInformation info = new FileInformation();{
			info.setSendname(sendname);
			info.setReceivename(receivename);
			info.setFileName(filename);
			info.setTotalIndex(Integer.toString(totalIndex));
		}
		requestMessage.setFileInformation(info);							
		SocketUtil.getSocketUtil().writeMessage(socket, requestMessage);
	}
	
	/**
	 * 向服务器发送文件数据
	 * @param socket
	 * @param file
	 * @param bi
	 * @param totalIndex
	 * @throws IOException
	 */
	public static void sendFileByBlock(Socket socket,File file,BufferedInputStream bi,int totalIndex) throws IOException {
		
		for(int i=1; i<totalIndex; i++) {
			byte[] byteArray = new byte[1024];
			bi.read(byteArray);
			int index = i;
			FileData fd = new FileData(); {			
				fd.setMd5("md5");
				fd.setIndex(Integer.toString(index));
				fd.setArrayLength(Integer.toString(1024));
				fd.setByteArray(byteArray);
			}
			SocketUtil.getSocketUtil().writeFileData(socket, fd);	
		}	
		int surplus = (int) (file.length() - (totalIndex-1) * 1024);
		byte[] byteArray= new byte[surplus];
		bi.read(byteArray);
		FileData fd = new FileData();{
			fd.setMd5("md5");
			fd.setIndex(Integer.toString(totalIndex));
			fd.setArrayLength(Integer.toString(surplus));
			fd.setByteArray(byteArray);
		}
		SocketUtil.getSocketUtil().writeFileData(socket, fd);
	}
}

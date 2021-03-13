package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class FileUtil {
	/**
	 * 读取文件
	 * @param username
	 * @param friendname
	 * @return
	 */
	public static String readFile(String username,String friendname) {
        String fileContent = "";
        try {
            File f = new File(username+"+"+friendname+".txt");
            if (f.isFile() && f.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                    fileContent += '\n';
                }
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }
	/**
	 * 读取聊天记录
	 * @param recordPanel
	 * @param username
	 * @param friendusername
	 */
	public static void readMessageFromFile(JTextPane recordPanel,String username,String friendusername) {
		String leavingMessage = FileUtil.readFile(username, friendusername);
		Document docs = recordPanel.getDocument();
		try {
	    	AttributeSet attributeSet=new javax.swing.text.SimpleAttributeSet();
	        docs.insertString(docs.getLength(),leavingMessage+'\n',attributeSet);//对文本进行追加			    
	    } catch (BadLocationException e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * 将好友发送的信息写入聊天记录
	 * @param username
	 * @param time
	 * @param friendname
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeFriendMessageToFile(String username,String time,String friendname,String content) throws UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(username+"+"+friendname+".txt",true);
		fos.write(new String(time+" "+friendname+" "+content+"\n").getBytes("UTF-8"));
		fos.close();   
	}
	/**
	 * 将用户发送的信息写入聊天记录
	 * @param username
	 * @param time
	 * @param friendname
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static void writeMyMessageToFile(String username,String time,String friendname,String content) throws UnsupportedEncodingException, IOException {
		FileOutputStream fos = new FileOutputStream(username+"+"+friendname+".txt",true);
		fos.write(new String(time+" "+username+" "+content+"\n").getBytes("UTF-8"));
	}


}

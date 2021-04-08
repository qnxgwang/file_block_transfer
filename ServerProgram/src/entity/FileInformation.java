package entity;

import java.io.Serializable;

public class FileInformation implements Serializable{
	
	/**
	 *    sendname代表发送文件的用户
	 *    receivename代表接收文件的用户
	 */
	private static final long serialVersionUID = 4L;

	private String fileName;
	
	private String totalIndex;
	
	private String md5;
	
	private String sendname;
	
	private String receivename;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTotalIndex() {
		return totalIndex;
	}

	public void setTotalIndex(String totalIndex) {
		this.totalIndex = totalIndex;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getSendname() {
		return sendname;
	}

	public void setSendname(String sendname) {
		this.sendname = sendname;
	}

	public String getReceivename() {
		return receivename;
	}

	public void setReceivename(String receivename) {
		this.receivename = receivename;
	}

	
}

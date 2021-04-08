package entity;

import java.io.Serializable;

public class FileData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	private String index;
	
	private String arrayLength;
	
	private byte[] byteArray;
	
	private String md5;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public byte[] getByteArray() {
		return byteArray;
	}

	public void setByteArray(byte[] byteArray) {
		this.byteArray = byteArray;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getArrayLength() {
		return arrayLength;
	}

	public void setArrayLength(String arrayLength) {
		this.arrayLength = arrayLength;
	}	  
	
	
	
}

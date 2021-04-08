package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.FileData;
import util.JdbcUtil;

public class FileDao {

	/**
	 * 向数据库插入分片文件
	 * @param filedata
	 * @param filename
	 * @param sendname
	 * @param receivename
	 * @param totalindex
	 */
	public static void insertFile(FileData filedata, String filename, String sendname, String receivename, String totalindex) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"insert into file_data(sendname,receivename,filename,totalindex,fileindex,md5,filedata,filelength)"
					+ " values(?,?,?,?,?,?,?,?)"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1,sendname);
			preparedStatement.setString(2,receivename);
			preparedStatement.setString(3,filename);
			preparedStatement.setString(4,totalindex);
			preparedStatement.setString(5,filedata.getIndex());
			preparedStatement.setString(6,filedata.getMd5());		
			preparedStatement.setBytes(7, filedata.getByteArray());
			preparedStatement.setString(8, filedata.getArrayLength());
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}
	}

	/**
	 * 查询数据库是否存在分片文件
	 * @param sendname
	 * @param receivename
	 * @param filename
	 * @param index
	 * @return
	 */
	public static boolean hasFile(String sendname, String receivename,String filename, String index) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					          "select md5"
							+ " from file_data"
							+ " where sendname = ? and receivename = ? and filename = ? and fileindex = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1,sendname);
			preparedStatement.setString(2,receivename);
			preparedStatement.setString(3,filename);
			preparedStatement.setString(4,index);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}
		return true; 
	}
	
	/**
	 * 从服务器读取分片文件
	 * @param sendname
	 * @param receivename
	 * @param filename
	 * @return
	 */
	public static List<FileData> getFile(String sendname,String receivename,String filename) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<FileData> list = new ArrayList<FileData>();
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					  "select sendname,receivename,filename,totalindex,fileindex,md5,filedata,filelength"
					+ " from file_data"
					+ " where sendname = ? and receivename = ? and filename = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1,sendname);
			preparedStatement.setString(2,receivename);
			preparedStatement.setString(3,filename);
			resultSet = preparedStatement.executeQuery();	
			while(resultSet.next()) {
				FileData f = new FileData();
				f.setMd5(resultSet.getString("md5"));
				f.setIndex(resultSet.getString("fileindex"));
				f.setArrayLength(resultSet.getString("filelength"));
				f.setByteArray(resultSet.getBytes("filedata"));
				list.add(f);
			}	
			return list;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}
		return null;		
	}
	
}

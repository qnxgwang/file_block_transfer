package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.ChatContent;
import util.JdbcUtil;

public class DataDao {
	
	/**
	 * 如果好友不在线，将信息插入数据库
	 * @param chatContent
	 */
	public static void insertData(ChatContent chatContent) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"insert into offline_message(message_send,message_to,time,content)"
					+ " values(?,?,?,?)"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1,chatContent.getMessageFrom());
			preparedStatement.setString(2,chatContent.getMessageTo());
			preparedStatement.setString(3,chatContent.getTimeStamp());
			preparedStatement.setString(4,chatContent.getContent());
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}
	}
	
	/**
	 * 用户上线后，首先查询是否存在好友发来的信息
	 * @param username
	 * @param friendname
	 * @return
	 */
	public static List<ChatContent> queryData(String messageFrom,String messageTo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<ChatContent> contentList = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"select message_send,message_to,time,content"
					+ " from offline_message"
					+ " where message_send = ? and message_to = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, messageFrom);
			preparedStatement.setString(2, messageTo);
			resultSet = preparedStatement.executeQuery();	
			contentList = new ArrayList<ChatContent>();
			boolean flag = false; 
			while(resultSet.next()) {
				flag = true;
				ChatContent content = new ChatContent();
				content.setTimeStamp(resultSet.getString("time"));
				content.setMessageFrom(resultSet.getString("message_send"));
				content.setMessageTo(resultSet.getString("message_to"));
				content.setContent(resultSet.getString("content"));
				contentList.add(content);
			}	
			if(flag) {
				return contentList;
			}else {
				return null;
			}			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}	
		return null;
	}
	
	/**
	 * 删除离线信息
	 * @param username
	 */
	public static void deleteData(String messageTo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"delete from offline_message"
					+ " where message_to = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, messageTo);
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}

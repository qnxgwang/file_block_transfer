package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.ChatContent;
import util.JdbcUtil;

public class DataDao {
	
	/**
	 * 如果好友不在线，将信息插入数据库
	 * @param chatContent
	 */
	public void insertData(ChatContent chatContent) {
		System.out.println("执行insertData"+" "+"参数@content="+chatContent.getContent());
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer("insert into offline_message(username,friendname,time,content) values(?,?,?,?)"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1,chatContent.getFrom());
			preparedStatement.setString(2,chatContent.getTo());
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
	public List<ChatContent> queryData(String username,String friendname) {
		System.out.println("执行queryData"+" "+"参数@username="+username);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<ChatContent> contentList = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer("select username,friendname,time,content from offline_message where username = ? & friendname = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, friendname);
			resultSet = preparedStatement.executeQuery();
			contentList = new ArrayList<ChatContent>();
			while(resultSet.next()) {
				ChatContent content = new ChatContent();
				content.setTimeStamp(resultSet.getString("time"));
				content.setFrom(resultSet.getString("friendname"));
				content.setTo(resultSet.getString("username"));
				content.setContent(resultSet.getString("content"));
				contentList.add(content);
			}
			return contentList;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}	
		return null;
	}

}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import entity.User;
import util.JdbcUtil;
import util.PropertiesUtil;

public class UserDao {

	public User login(String username,String pwd ) {
		System.out.println("Ö´ÐÐlogin");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer("select id,username,pwd,realname from user where username = ? and pwd = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, pwd);		
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				user =new User();
				user.setId(resultSet.getInt("id"));
				user.setUsername(resultSet.getString("username"));
				user.setRealname(resultSet.getString("realname"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(resultSet, preparedStatement, connection);
		}
		return user;	
	}
	public User queryUserByUsername(String username) {
		System.out.println("Ö´ÐÐqueryUserByUsername");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer("select id,username,pwd,realname from user where username = ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, username);	
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				user =new User();
				user.setId(resultSet.getInt("id"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(resultSet, preparedStatement, connection);
		}
		return user;
	}
	public void register(User user) {
		System.out.println("Ö´ÐÐregister");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer("insert into user(username,pwd,realname,photo) values(?,?,?,?)"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPwd());	
			preparedStatement.setString(3, user.getRealname());
			preparedStatement.setString(4, "Image/"+user.getPhoto());
			preparedStatement.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}
			
	}
}

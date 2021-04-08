package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import util.JdbcUtil;

public class UserDao {

	/**
	 * 用户登录
	 * @param username
	 * @param pwd
	 * @return
	 */
	public static User login(String username,String pwd ) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"select id,username,pwd,realname "
					+ "from user "
					+ "where username = ? and pwd = ?"); 
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
	
	/**
	 * 查询用户名是否已经注册
	 * @param username
	 * @return
	 */
	public static User queryUserByUsername(String username) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		User user = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"select id,username,pwd,realname"
					+ " from user"
					+ " where username = ?"); 
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
	
	/**
	 * 用户注册
	 * @param user
	 */
	public static void register(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"insert into user(username,pwd,realname,photo)"
					+ " values(?,?,?,?)"); 
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
	
	/**
	 * 返回用户列表
	 * @param username
	 * @return
	 */
	public static List<User> getFriendListByUsername(String username){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet  resultSet = null;
		List<User> userList= null;
		try {
			connection = JdbcUtil.getJdbcUtil().getConnection();
			StringBuffer stringBuffer = new StringBuffer(
					"select username,photo,realname"
					+ " from user where username != ?"); 
			preparedStatement = connection.prepareStatement(stringBuffer.toString());
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			userList = new ArrayList<User>();
			while(resultSet.next()) {
				User user =new User();
				user.setUsername(resultSet.getString("username"));
				user.setPhoto(resultSet.getString("photo"));
				userList.add(user);
			}
			return userList;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JdbcUtil.getJdbcUtil().closeConnection(null, preparedStatement, connection);
		}	
		return null;	
	}
}

package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {
	
	/**
	 * Jdbc连接数据库封装
	 */
	private static JdbcUtil jdbcUtil =null;
	
	private JdbcUtil() {
		
	}
	
	/**
	 * 静态方法，直接通过类名调用
	 * @return
	 */
	 public static synchronized JdbcUtil getJdbcUtil() {
		if(null==jdbcUtil) {			
			jdbcUtil = new JdbcUtil();
		}
		return jdbcUtil;
	}
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取数据库连接
	 */
	public Connection getConnection() throws SQLException{
		return DriverManager.getConnection(
				PropertiesUtil.getPropertiesUtil().getValue("url_qq"),
				PropertiesUtil.getPropertiesUtil().getValue("username"),
				PropertiesUtil.getPropertiesUtil().getValue("pwd")
				);
	}
	
	/**
	 * 关闭数据库连接
	 */	
	public void closeConnection(ResultSet resultSet,Statement statement,Connection connection) {
		try {
			if(null!=resultSet) {
				resultSet.close();
			}
		}catch(SQLException e) {
			e.printStackTrace();
		    }
		try {
			if(null!=statement) {
				statement.close();
			}
		}catch(SQLException e){
				e.printStackTrace();
		    }
		try {
			if(null!=connection) {
				connection.close();
			}
		}catch(SQLException e) {
				e.printStackTrace();
			}	
	}
}

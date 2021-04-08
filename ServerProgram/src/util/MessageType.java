package util;

public interface MessageType {

	/**
	 * LOGIN_SUCCESS 用户登录成功
	 * LOGIN_FAILURE 用户登录失败
	 * LOGIN 用户登录
	 * EXIT 用户退出登录
	 * REGISTER 用户注册
	 * REGISTER_SUCCESS 用户注册成功
	 * REGISTER_FAILURE 用户注册失败
	 * GET_FRIEND_LIST 获取好友列表
	 * GET_FRIEND_LIST_SUCCESS 获取好友列表成功
	 * GET_FRIEND_LIST_FAILURE 获取好友列表失败
	 * MESSAGE_TO_FRIEND 给好友发送信息
	 * MESSAGE_FROM_FRIEND 来自好友的信息
	 * UPDATE_FRIEND_LIST_LOGIN 用户登录，更新客户端在线列表
	 * UPDATE_FRIEND_LIST_EXIT 用户退出登录，更新客户端在线列表
	 * LOGIN_TO_FRIEND 用户登录，告知服务器
	 */
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_FAILURE = 2;
	public static final int LOGIN = 3;
	public static final int EXIT = 4;
	public static final int REGISTER = 5;
	public static final int REGISTER_SUCCESS = 6;
	public static final int REGISTER_FAILURE = 7;
	public static final int GET_FRIEND_LIST = 8;
	public static final int GET_FRIEND_LIST_SUCCESS = 9;
	public static final int GET_FRIEND_LIST_FAILURE = 10;
	public static final int MESSAGE_TO_FRIEND =11;
	public static final int MESSAGE_FROM_FRIEND = 12;
	public static final int UPDATE_FRIEND_LIST_LOGIN = 13;
	public static final int UPDATE_FRIEND_LIST_EXIT = 14;
	public static final int LOGIN_TO_FRIEND = 15;
	/*
	 * 
	 */
	public static final int FILE_TO_FRIEND = 16;
	public static final int FILE_FROM_FRIEND = 17;
	public static final int FILE_REQUEST= 18;
	
}

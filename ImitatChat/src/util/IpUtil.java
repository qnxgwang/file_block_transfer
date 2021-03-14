package util;

import java.net.InetAddress;

public class IpUtil {

	private static final int loginPort = 9898;
	private static final int orderPort = 9999;
	
	/**
	 * 动态获取服务器IP，测试时使用本机IP
	 * @return
	 */
	public static String getIp(){
        InetAddress inetAddress=null;
        String hostAddress = null;
        try {
        	inetAddress=InetAddress.getLocalHost();
        	hostAddress=inetAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hostAddress;
	}
	
	/**
	 * 返回登录端口
	 * @return
	 */
	public static int getLoginPort() {
		return loginPort;		
	}
	
    /**
     * 返回命令端口
     * @return
     */
	public static int getOrderPort() {
		return orderPort;
	}
	
}

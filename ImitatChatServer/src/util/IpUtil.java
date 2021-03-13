package util;

import java.net.InetAddress;

public class IpUtil {

	private static final int loginPort = 9898;
	private static final int orderPort = 9999;
	
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
	
	public static int getLoginPort() {
		return loginPort;		
	}

	public static int getOrderPort() {
		return orderPort;
	}
	
}

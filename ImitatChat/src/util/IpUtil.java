package util;

import java.net.InetAddress;

public class IpUtil {

	private static IpUtil ipUtil;
	private static final int port = 9999;
	
	public static IpUtil getIpUtil() {
		if(ipUtil==null) {
			ipUtil = new IpUtil();
		}
		return ipUtil;
	}
	
	public String getIp(){
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
	
	public int getPort() {
		return port;		
	}
}

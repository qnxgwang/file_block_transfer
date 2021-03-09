package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static PropertiesUtil propertiesUtil = null;
	
	private Properties properties;
	private PropertiesUtil() {
		properties = new Properties();
		InputStream inputstream = PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			properties.load(inputstream);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static PropertiesUtil getPropertiesUtil() {
		if(propertiesUtil == null) {
			propertiesUtil = new PropertiesUtil();
		}
		
		return propertiesUtil;
	}
	public String getValue(String key) {
		return properties.getProperty(key);
	}
}

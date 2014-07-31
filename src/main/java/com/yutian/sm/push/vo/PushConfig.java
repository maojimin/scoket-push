package com.yutian.sm.push.vo;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:maojimin@yutian.com.cn">毛积敏</a>
 * 2014年5月30日 下午9:40:52
 */
public class PushConfig {

	private static Logger log = Logger.getLogger(PushConfig.class);
	private int receivePort;
	private int pushPort;
	private static PushConfig config = new PushConfig();
	
	public static PushConfig getConfig(){
		return config;
	}
	
	private PushConfig() {
		Properties props = null;
		InputStream is = this.getClass().getResourceAsStream("/config.properties");
		try {
			props = getPropByInputStream(is);
			if(is!=null){
				is.close();
			}
		} catch (Exception e) {
			log.error("load config.properties error.",e);
		}
		
		receivePort = Integer.parseInt(props
				.getProperty("receive_port", "11511"));
		pushPort = Integer.parseInt(props.getProperty("push_port", "7071"));
		
	}

	private Properties getPropByInputStream(InputStream is) throws Exception {
		Properties prop = new Properties();
		try {
			prop.load(is);
			return prop;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(PushConfig.getConfig().getPushPort());
		System.out.println(PushConfig.getConfig().getReceivePort());
	}

	/**
	 * @return the receivePort
	 */
	public int getReceivePort() {
		return receivePort;
	}

	/**
	 * @return the pushPort
	 */
	public int getPushPort() {
		return pushPort;
	}

}

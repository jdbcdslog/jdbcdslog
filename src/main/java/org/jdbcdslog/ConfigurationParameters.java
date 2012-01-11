package org.jdbcdslog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationParameters {
	
	static Logger logger = LoggerFactory.getLogger(ConfigurationParameters.class);
	
	static long slowQueryThreshold = Long.MAX_VALUE;
	
	static boolean printStackTrace = true;
	
	static boolean logText = false;
	
	static {
		ClassLoader loader = ConfigurationParameters.class.getClassLoader();
		InputStream in = null;
		try {
			in = loader.getResourceAsStream("jdbcdslog.properties");
			Properties props = new Properties(System.getProperties());
			if(in != null)
				props.load(in);
			String sSlowQueryThreshold = props.getProperty("jdbcdslog.slowQueryThreshold");
			if(sSlowQueryThreshold != null && isLong(sSlowQueryThreshold))
				slowQueryThreshold = Long.parseLong(sSlowQueryThreshold);
			if(slowQueryThreshold == -1)
				slowQueryThreshold = Long.MAX_VALUE;
			String sLogText = props.getProperty("jdbcdslog.logText");
			if("true".equalsIgnoreCase(sLogText))
				logText = true;
			String sprintStackTrace = props.getProperty("jdbcdslog.printStackTrace");
			if("true".equalsIgnoreCase(sprintStackTrace))
				printStackTrace = true;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
		}
	}
	
	public static void setLogText(boolean alogText) {
		logText = alogText;
	}

	private static boolean isLong(String sSlowQueryThreshold) {
		try {
			Long.parseLong(sSlowQueryThreshold);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
}

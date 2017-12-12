package com.institucion.fm.conf;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ApplicationInfo {
	static private final String UNKNOWN_VERSION_INFO = "unknown version info";
	static Log log = LogFactory.getLog(ApplicationInfo.class);

	public String getVersionInfo() { 
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/institucion/fm/conf/fm.properties");
			if (in == null) {
				return UNKNOWN_VERSION_INFO;
			}
			Properties props = new Properties();
			props.load(in);
			String value = props.getProperty("fm.application.version");
			if ("".equals(value) || value == null) {
				return UNKNOWN_VERSION_INFO;
			}
			return value;
		} catch (Exception e) {
			log.error(e);
			return UNKNOWN_VERSION_INFO;
		}
	}
	
	public String toString() {
		return this.getVersionInfo();
	}
}

package com.institucion.fm.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportConf {
	public static final String DB_PROVIDER_URL = "db.provider.url";
	public static final String DB_USERS = "db.user";
	public static final String DB_password = "db.password";
	public static final String LOG_dir="log.dir";
	public static final String LOG_level="log.level";
	static final File INSTANCE_CONFIGURATION_PROPERTIES_DIR = org.jboss.system.server.ServerConfigLocator.locate().getServerHomeDir();

	static final String INSTANCE_CONFIGURATION_PROPERTIES_FILENAME = "report-configuration.properties";

	static final String DEPLOY_DIR = "conf";

	static Log log = LogFactory.getLog(ReportConf.class);

	private static Properties configuration = null;

	public static String getInstanceConfigurationPropertiesDir() {
		return INSTANCE_CONFIGURATION_PROPERTIES_DIR.getAbsolutePath() + File.separatorChar + DEPLOY_DIR + File.separatorChar;
	}

	public static String getInstanceConfigurationProperty(String key) {
		String value = getConfiguration().getProperty(key);
		//LogFactory.getLog("ws." + InstanceConf.class.getName()).info(INSTANCE_CONFIGURATION_PROPERTIES_FILENAME + " (key,value): (" + key + ", " + value +")");
		log.info(INSTANCE_CONFIGURATION_PROPERTIES_FILENAME + " (key,value): (" + key + ", " + value +")");
		return value;
	}

	private static Properties getConfiguration() {
		if (configuration == null) {
			configuration = new Properties();
			try {
				File file = new File(getInstanceConfigurationPropertiesDir(),INSTANCE_CONFIGURATION_PROPERTIES_FILENAME);
				configuration.load(new FileInputStream(file));
				LogFactory.getLog("ws." + InstanceConf.class.getName()).info("Loading ... " + file.getPath());
			} catch (FileNotFoundException e) {
				LogFactory.getLog("ws." + InstanceConf.class.getName()).info("File not Found: " + INSTANCE_CONFIGURATION_PROPERTIES_FILENAME);
				throw new RuntimeException();
			} catch (IOException e) {
				throw new RuntimeException();
			}
		}
		return configuration;
	}

	
	
	
	
	
}

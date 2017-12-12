/**
 * 
 */
package com.institucion.fm.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author mdenipotti
 *
 */
public class InstanceConf implements FactoryBean {

	public static final String INSTANCE_PROVIDER_URL = "instance.provider.url";
	public static final String FMCAL_HTTP_URL = "fmcal.http.url";
	public static final String ENABLE_LOGGER_PARAMETERS_WS = "enable.logger.parameters.ws";
	public static final String SIG_IMPORT_EMAIL_TO = "sig.error.email.to";
	public static final String SIG_IMPORT_EMAIL_FROM = "sig.error.email.from";
	
	public static final String DISTRIBUTION_PLAN_CLONE_EMAIL_TO = "distribution.plan.error.email.to";
	public static final String DISTRIBUTION_PLAN_CLONE_EMAIL_FROM = "distribution.plan.error.email.from";
	
	public static final String REMINDER_EMAIL_TO = "reminder.email.to";
	public static final String REMINDER_EMAIL_FROM = "reminder.email.from";

	public static final String SPECIALITYDOMAINTABLEID="sig.specialityDomainTableId";
	public static final String MESSAGE_USER = "message.user";
	
	public static final String QAD_ERROR_EMAIL_TO ="qad.error.email.to";
	public static final String QAD_ERROR_EMAIL_FROM ="qad.error.email.to";
	
	public static final String DISTRIBUTION_PLAN_FILENAME="ftp.distributionplan.filename";
	public static final String DISTRIBUTION_PLAN_FTP_IP="ftp.distributionplan.ip";
	public static final String DISTRIBUTION_PLAN_FTP_USR="ftp.distributionplan.usr";
	public static final String DISTRIBUTION_PLAN_FTP_PWS="ftp.distributionplan.psw";

	
	public static final String LENGTH_USERS_CODE="length.users.code";
	
	public static final String JNDI_NAME="jndi.import.closeUp";

	
	public static final String DOMAIN_TABLE_DETAIL_DOCTOR_ID = "domain.table.detail.doctor.id";
	
	public static final String QAD_USE_PROD_COD_LOCAL="qad.use.prod.cod.local";
	public static final String DEFAULT_LOCALE="default.locale";
	
	public static final String  JNDI_MAINTENANCE_TABLES_USER_POSTGRES= "jndi.maintenance.table.postgres";
	
	static final File INSTANCE_CONFIGURATION_PROPERTIES_DIR = org.jboss.system.server.ServerConfigLocator.locate().getServerHomeDir();

	static final String INSTANCE_CONFIGURATION_PROPERTIES_FILENAME = "instance-configuration.properties";

	static final String DEPLOY_DIR = "conf";
	
	public static final String BIRTHDAYDOMAINTABLEID="birthdayDomainTableId";

	static Log log = LogFactory.getLog(InstanceConf.class);

	private static Properties configuration = null;

	public static String getInstanceConfigurationPropertiesDir() {
		return INSTANCE_CONFIGURATION_PROPERTIES_DIR.getAbsolutePath() + File.separatorChar + DEPLOY_DIR+ File.separatorChar;
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

	@Override
	public Object getObject() throws Exception {
		return "file:" + getInstanceConfigurationPropertiesDir() + INSTANCE_CONFIGURATION_PROPERTIES_FILENAME;
	}
	
	@Override
	public Class getObjectType() {
		return String.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
}
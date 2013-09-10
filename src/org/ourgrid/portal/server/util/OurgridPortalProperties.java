package org.ourgrid.portal.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

public class OurgridPortalProperties {
	
	public static final String STORAGE_DIRECTORY = "storageDir";
	public static final String EMAIL_HOST_NAME = "hostName";
	public static final String EMAIL_FROM = "from";
	public static final String EMAIL_FROM_NAME = "from.name";
	public static final String EMAIL_USERNAME = "username";
	public static final String EMAIL_PASSWORD = "password";
	
	public static final String ADMIN_LOGIN = "admin.login";
	public static final String ADMIN_PASSWORD = "admin.password";
	public static final String ADMIN_EMAIL = "admin.email";
	
	public static final String DEFAULT_STORAGE_SIZE = "default.storage.size"; //in MB
	
	public static final String GENECODIS_PATH = "genecodis.path";
	
	public static final String RHO_PATH = "rho.path";
	
	public static final String FIBONACCI_PATH = "fibonacci.path";
	
	public static final String LVC_PATH = "lvc.path";
	
	public static final String RESERVOIR_PATH = "reservoir.path";
	
	public static final String CISTERNS_PATH = "cisterns.path";
	
	public static final String MARBS_PATH = "marbs.path";
	
	public static final String EPANET_PATH = "epanet.path";
	
	public static final String LOG_FILE_NAME = "log.file.name";
	
	public static final String BUILD_TEST = "build.test";

	private static OurgridPortalProperties instance  = null;

	private Properties props;
	private String propertiesPath;
	public static final String PROPERTIES_PATH_DEF = "/ourgridportal.cfg.xml";
	
	private OurgridPortalProperties(){
		loadProperties(PROPERTIES_PATH_DEF);
	}
	
	private void loadProperties(String path) {
		props = new Properties();
		URL cfgFilePath = OurgridPortalProperties.class.getResource(path);
		try {
			props.loadFromXML(new FileInputStream(new File(cfgFilePath.toURI())));
		} catch (Exception e) {
			
		}
	}

	public static OurgridPortalProperties getInstance(){
		if(instance == null){
			instance = new OurgridPortalProperties();
		}
		return instance;
	}
	
	public String getProperty(String property){
		return props.getProperty(property);
	}

	public void setPropertiesPath(String propertiesPath) {
		this.propertiesPath = propertiesPath;
		loadProperties(propertiesPath);
	}

	public String getPropertiesPath() {
		return propertiesPath;
	}
}
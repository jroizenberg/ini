package com.institucion.fm.conf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DataBaseParameters {

	private static Log log = LogFactory.getLog(DataBaseParameters.class);
	
	static final File INSTANCE_CONFIGURATION_PROPERTIES_DIR = org.jboss.system.server.ServerConfigLocator.locate().getServerHomeDir();
	static final String DATASOURCE = "postgres-ds.xml";
	
	static final String DEPLOY_DIR = "deploy";
	

	public static String getInstanceConfigurationPropertiesDir() {
		return INSTANCE_CONFIGURATION_PROPERTIES_DIR.getAbsolutePath() + File.separatorChar + "deploy" + File.separatorChar;
	}
	
	
	private static Map<String,String>connectionParamenters=null;
	
	private static void parseDataSourceXML(){
		connectionParamenters=new HashMap<String, String>();
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db=dbf.newDocumentBuilder();
			File file = new File(getInstanceConfigurationPropertiesDir(),DATASOURCE);
			
			Document doc=db.parse(file);
			NodeList element=doc.getElementsByTagName("local-tx-datasource");
			for (int i=0;i<element.getLength();i++){
				Node fstNode = element.item(i);
				 if (fstNode.getNodeType() == Node.ELEMENT_NODE){
				       Element fstElmnt = (Element) fstNode;
				       NodeList jndiName = fstElmnt.getElementsByTagName("jndi-name");
				       Element jndiNameElement = (Element) jndiName.item(i);
				      if(jndiNameElement!= null){
				    	  NodeList jndiNameNm = jndiNameElement.getChildNodes();
				    	  if (((Node) jndiNameNm.item(i)).getNodeValue().
				    		   equalsIgnoreCase(InstanceConf.getInstanceConfigurationProperty("jndi.import.closeUp"))){
				    	   
					    	   NodeList userNameLst = fstElmnt.getElementsByTagName("user-name");
						       Element userNameElmnt = (Element) userNameLst.item(i);
						       NodeList userNameNm = userNameElmnt.getChildNodes();
						       connectionParamenters.put("hibernate.connection.username", ((Node) userNameNm.item(i)).getNodeValue());
//						       System.out.println("user-name : "  + ((Node) userNameNm.item(i)).getNodeValue());
						       NodeList passwordLst = fstElmnt.getElementsByTagName("password");
						       Element passwordElmnt = (Element) passwordLst.item(i);
						       NodeList passwordNm = passwordElmnt.getChildNodes();
//						       System.out.println("password : "  + ((Node) passwordNm.item(i)).getNodeValue());
						       connectionParamenters.put("hibernate.connection.password", ((Node) passwordNm.item(i)).getNodeValue());
						       NodeList connectiondLst = fstElmnt.getElementsByTagName("connection-url");
						       Element connectionElmnt = (Element) connectiondLst.item(i);
						       NodeList connectionNm = connectionElmnt.getChildNodes();
//						       System.out.println("Connection : "  + ((Node) connectionNm.item(i)).getNodeValue());
						       connectionParamenters.put("hibernate.connection.url", ((Node) connectionNm.item(i)).getNodeValue());
						       NodeList driverLst = fstElmnt.getElementsByTagName("driver-class");
						       Element driverElmnt = (Element) driverLst .item(i);
						       NodeList driverNm = driverElmnt.getChildNodes();
//						       System.out.println("Driver : "  + ((Node) driverNm.item(i)).getNodeValue());
						       connectionParamenters.put("hibernate.connection.driver_class", ((Node) driverNm.item(i)).getNodeValue());
						       
						       NodeList elem1 = fstElmnt.getElementsByTagName("hibernate-c3p0-min_size");
						       if(elem1  != null){
						    	   Element elem1t = (Element) elem1 .item(i);
						           NodeList elem1tn = elem1t.getChildNodes();
//							       System.out.println("Driver : "  + ((Node) elem1tn.item(i)).getNodeValue());
							       connectionParamenters.put("hibernate.c3p0.min_size", ((Node) elem1tn.item(i)).getNodeValue());
						       }
						       
						       NodeList elem2 = fstElmnt.getElementsByTagName("hibernate-c3p0-max_size");
						       if(elem2  != null){
						    	   Element elem2t = (Element) elem2 .item(i);
							       NodeList elem2tn = elem2t.getChildNodes();
							       System.out.println("Driver : "  + ((Node) elem2tn.item(i)).getNodeValue());
//							       connectionParamenters.put("hibernate.c3p0.max_size", ((Node) elem2tn.item(i)).getNodeValue());
						       }
						       
						       
						       
						       NodeList elem3 = fstElmnt.getElementsByTagName("hibernate-c3p0-timeout");
						       if(elem3  != null){
						    	   Element elem3t = (Element) elem3 .item(i);
							       NodeList elem3tn = elem3t.getChildNodes();
//							       System.out.println("Driver : "  + ((Node) elem3tn.item(i)).getNodeValue());
							       connectionParamenters.put("hibernate.c3p0.timeout", ((Node) elem3tn.item(i)).getNodeValue());
						       }
						       
						       NodeList elem4 = fstElmnt.getElementsByTagName("hibernate-c3p0-max_statements");
						       if(elem4  != null){
						    	   Element elem4t = (Element) elem4 .item(i);
							       NodeList elem4tn = elem4t.getChildNodes();
//							       System.out.println("Driver : "  + ((Node) elem4tn.item(i)).getNodeValue());
							       connectionParamenters.put("hibernate.c3p0.max_statements", ((Node) elem4tn.item(i)).getNodeValue());
						       }
					       }
				      }		
				 }
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		} catch(IOException ioe){
			ioe.printStackTrace();
			log.error("Mensaje: " + ioe.getMessage() + "StackTrace: " + ioe.getStackTrace());
		} catch (SAXException sae){
			sae.printStackTrace();
			log.error("Mensaje: " + sae.getMessage() + "StackTrace: " + sae.getStackTrace());
		}
	}
	
//	public static void main(String[]args){
//		parseXML();
//	}
	
	
	public static Map<String,String>getDataBaseConnectionParamenters(){
		if(connectionParamenters==null)
			parseDataSourceXML();
		return connectionParamenters;
	}
}

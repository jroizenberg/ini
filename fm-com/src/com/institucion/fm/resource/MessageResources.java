package com.institucion.fm.resource;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The <code>MessageResources</code> class represents ... put your explanation here, please
 * 
 * @author msanchezb
 * @author
 * @version 1.0, May 4, 2005
 */
public class MessageResources {
   	private HashMap<String, String> messages = new HashMap<String, String>();
   	private HashMap<String, MessageFormat> formats = new HashMap<String, MessageFormat>();
   	private String resourceFile = null;
	private static Log log = LogFactory.getLog(MessageResources.class);

   	/**
   	 * 
   	 * @param locale
   	 * @param key
   	 * @return
   	 */
	private String messageKey(String locale, String key) {
		return (this.resourceFile + "." + locale + "." + key);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getNotlocatedMessage(String key) {
        String localeKey = "";
        String originalKey = messageKey(localeKey, key);
        String message = null;

        loadLocale(localeKey);
        synchronized (this.messages) {
            message = (String) this.messages.get(originalKey);
            if (message != null) {
                this.messages.put(originalKey, message);
                return (message);
            }
        }

        return key;
    }

	/**
	 * 
	 * @param locale
	 * @param key
	 * @return
	 */
	public String getMessage(Locale locale, String key) {
	   // Initialize variables we will require
	   String localeKey = locale.getLanguage();
	   String originalKey = messageKey(localeKey, key);
	   String messageKey = null;
	   String message = null;
	   int underscore = 0;
	   boolean addIt = false;  // Add if not found under the original key

	   // Loop from specific to general Locales looking for this message
	   while (true) {

		   // Load this Locale's messages if we have not done so yet
		   loadLocale(localeKey);

		   // Check if we have this key for the current locale key
		   messageKey = messageKey(localeKey, key);
		   synchronized (this.messages) {
			   message = (String) this.messages.get(messageKey);
			   if (message != null) {
				   if (addIt) {
					   this.messages.put(originalKey, message);
				   }
				   return (message);
			   }
		   }

		   // Strip trailing modifiers to try a more general locale key
		   addIt = true;
		   underscore = localeKey.lastIndexOf("_");
		   if (underscore < 0) {
			   break;
		   }
		   localeKey = localeKey.substring(0, underscore);

	   }
	   // As a last resort, try the default Locale
	   localeKey = "";
	   messageKey = messageKey(localeKey, key);
	   loadLocale(localeKey);
	   synchronized (this.messages) {
		   message = (String) this.messages.get(messageKey);
		   if (message != null) {
			   this.messages.put(originalKey, message);
			   return (message);
		   }
	   }
	   return key;
	}

	/**
	 * 
	 * @param localeKey
	 */
	private synchronized void loadLocale(String localeKey) {
		// Set up to load the property resource for this locale key, if we can
		String name = this.getResourceFile();
		if (name == null) {
			return;		 
		}
		if (localeKey.length() > 0) {
		   name += "_" + localeKey;
		}
		name += ".properties";
		InputStream is = null;
		Properties props = new Properties();

		is = inputFromPackage(this.getClass(), name);
        try {
			props.load(is);
		} catch (IOException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		} finally {
			try {
				is.close();
			} catch (IOException e1) {
				log.error("Mensaje: " + e1.getMessage() + "StackTrace: " + e1.getStackTrace());
			}
		}

        // Copy the corresponding values into our cache
		if (props.size() < 1) {
			return;
		}
    
		synchronized (this.messages) {
			Iterator<?> names = props.keySet().iterator();
			while (names.hasNext()) {
				String key = (String) names.next();
				this.messages.put(messageKey(localeKey, key), props.getProperty(key));
			}
		}
	}

	/**
	 * Devuelve un recurso dentro de una librería y en la ubicación de la clase.
	 * 
	 * @param pathClass  un objeto <code>Class</code> que determina la ubicación del recurso
	 * @param resourceName  nombre del recurso (ejemplo: resource.extension)
	 * @return el flujo de entrada para leer el recurso
	 */
	
	private InputStream inputFromPackage(Class<?> pathClass, String path)
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		log.debug("cargo a [" + path +"] con [" + cl.toString() +"]");
		java.io.InputStream in = cl.getResourceAsStream(path);
		if (in == null)
			throw new java.lang.IllegalArgumentException("El archivo '"+path+"' no fue encontrado en la librería");
		return in;
	}
/*
	public InputStream readFile(String file) throws RuntimeException { 
		InputStream is = null;

		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		try {
			if (is != null) {
				is = new BufferedInputStream(is);
			} else {
				is = new BufferedInputStream(new FileInputStream(file));
			}
		} catch (FileNotFoundException e) {
		    throw new RuntimeException(e);
		}
		return is;
	}
*/
	
	/**
	 * 
	 * @param locale
	 * @param key
	 * @param args
	 * @return
	 */
	public String getMessage(Locale locale, String key, Object args[]) {
		MessageFormat format = null;
		String formatKey = messageKey(locale.toString(), key);
		synchronized (this.formats) {
			format = (MessageFormat) this.formats.get(formatKey);
			if (format == null) {
				String formatString = this.getMessage(locale, key);
				if (formatString == null) {
					return null;
				}
				format = new MessageFormat(formatString);
				this.formats.put(formatKey, format);
			}
		}
		return (format.format(args));
	}
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	protected String escape(String string) {
		if ((string == null) || (string.indexOf('\'') < 0)) {
			return (string);
		}
		int n = string.length();
		StringBuffer sb = new StringBuffer(n);
		for (int i = 0; i < n; i++) {
			char ch = string.charAt(i);
			if (ch == '\'') {
				sb.append('\'');
			}
			sb.append(ch);
		}
		return (sb.toString());
	}
	
	/**
	 * @return
	 */
	public String getResourceFile() {
		return this.resourceFile;
	}

	/**
	 * @param string
	 */
	public void setResourceFile(String string) {
		this.resourceFile = string;
	}

}
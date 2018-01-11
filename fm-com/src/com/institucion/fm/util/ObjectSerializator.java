package com.institucion.fm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectSerializator implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ObjectSerializator.class);
	
	public static String serialization(Object obj) {
		if (obj == null) return null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			out.flush();
			out.close();
			return String.copyValueOf(Base64Coder.encode(bos.toByteArray()));
		} catch (IOException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new IllegalArgumentException(e);
		}
	}

	public static Object deserialization(String str) {
		try {
			byte[] decoded = Base64Coder.decode(str);
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(decoded));
			Object obj = in.readObject();
			return obj;
		} catch (IOException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new IllegalArgumentException(e);
		} catch (ClassNotFoundException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new IllegalArgumentException(e);
		}
	}

}

package com.institucion.fm.fe.validator.dao;

//import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.CallMethodRule;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.validator.Validation;

/**
 * Lee los operadores que se pueden utilizar en las validaciones desde un archivo XML.
 */
public class ValidationDAO
{
	private static ValidationDAO dao;
	static Log log = LogFactory.getLog(ValidationDAO.class);

	private Map<String,Validation> validations;
	private List<Validation> validationList;

	private ValidationDAO()
	{
		try
		{
			String packageName = ValidationDAO.class.getPackage().getName();
			packageName = "/"+packageName.replace('.', '/')+"/"; 
			InputStream in = ValidationDAO.class.getResourceAsStream(packageName+"validation.xml");
			validations = parse(in);

			// JBoss tiene problemas con operators.values(), por eso creamos una lista aparte
			validationList = new ArrayList<Validation>();
			Iterator<Validation> itop = validations.values().iterator();
			while (itop.hasNext())
				validationList.add(itop.next());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public static ValidationDAO instance()
	{
		if (dao == null)
			dao = new ValidationDAO();
		return dao;
	}

	public List<Validation> findAll() throws DAOException
	{
		return validationList;
	}

	public Validation findByName(String name)
	{
		return validations.get(name);
	}

	private  Map<String,Validation> parse(InputStream inputStream) throws IOException, SAXException
	{
		return parse(new InputStreamReader(inputStream));
	}

	@SuppressWarnings("unchecked")
	private Map<String,Validation> parse(Reader reader) throws IOException, SAXException
	{
		Digester digester = new Digester();
		digester.addObjectCreate("validations", HashMap.class);
		digester.addObjectCreate("validationList", List.class);

		digester.addObjectCreate("validations/validation", "className", Validation.class);
		digester.addCallMethod("validations/validation/name","setName",0);

		Rule operRule = new CallMethodRule(1, "put", 2);
		digester.addRule("validations/validation", operRule);
		digester.addCallParam("validations/validation/name", 0);
		digester.addCallParam("validations/validation", 1, true);

		return (Map<String,Validation>) digester.parse(reader);
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			Validation cuit = ValidationDAO.instance().findByName("cuit");
//			//log.debug(cuit);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}
package com.institucion.fm.filteradv.dao;

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
import com.institucion.fm.filteradv.model.Operator;
import com.institucion.fm.filteradv.model.Predicate.Type;

/**
 * Lee los operadores que se pueden utilizar en la consulta avanzada desde un
 * archivo XML.
 */
public class OperatorDAO{
	private static OperatorDAO dao;
	static Log log = LogFactory.getLog(OperatorDAO.class);

	private Map<String,Operator> operators;
	private List<Operator> operatorList;

	private OperatorDAO(){
		try	{
			String packageName = OperatorDAO.class.getPackage().getName();
			packageName = "/"+packageName.replace('.', '/')+"/"; 
			InputStream in = OperatorDAO.class.getResourceAsStream(packageName+"operator.xml");
			operators = parse(in);
			// JBoss tiene problemas con operators.values(), por eso creamos una lista aparte
			operatorList = new ArrayList<Operator>();
			Iterator<Operator> itop = operators.values().iterator();
			while (itop.hasNext())
				operatorList.add(itop.next());
		}catch (Exception e){
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public static OperatorDAO instance(){
		if (dao == null)
			dao = new OperatorDAO();
		return dao;
	}

	public List<Operator> findAll() throws DAOException{
		return operatorList;
	}

	public List<Operator> findAll(Type type) throws DAOException{
		List<Operator> operatorsType = new ArrayList<Operator>();
		for (int i = 0; i < operatorList.size(); i++){
			Operator operator = operatorList.get(i);
			if (operator.getTypes().contains(type))
				operatorsType.add(operator);
		}
		return operatorsType;
	}

	public Operator findByName(String name){
		return operators.get(name);
	}

	private  Map<String,Operator> parse(InputStream inputStream) throws IOException, SAXException{
		return parse(new InputStreamReader(inputStream));
	}

	@SuppressWarnings("unchecked")
	private Map<String,Operator> parse(Reader reader) throws IOException, SAXException{
		Digester digester = new Digester();
		digester.addObjectCreate("operators", HashMap.class);
		digester.addObjectCreate("operatorList", List.class);

		digester.addObjectCreate("operators/operator", Operator.class);
		digester.addCallMethod("operators/operator/name","setName",0);
		digester.addCallMethod("operators/operator/text","setText", 0);
		digester.addCallMethod("operators/operator/pattern","setPattern", 0);
		digester.addCallMethod("operators/operator/types/type","addType", 0);

		Rule operRule = new CallMethodRule(1, "put", 2);
		digester.addRule("operators/operator", operRule);
		digester.addCallParam("operators/operator/name", 0);
		digester.addCallParam("operators/operator", 1, true);

		return (Map<String,Operator>) digester.parse(reader);
	}

	public static void main(String[] args){
		try{
			//Operator opequal = OperatorDAO.instance().findByName("equal");
			//Operator opnotequal = OperatorDAO.instance().findByName("notEqual");
			//log.debug(opequal);
			//log.debug(opnotequal);
		}catch (Exception e){
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}
}
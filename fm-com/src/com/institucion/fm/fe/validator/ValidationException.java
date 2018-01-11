package com.institucion.fm.fe.validator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.exception.ExceptionBase;

public class ValidationException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// <campo[String|I18N.String|FEField],lista de errores>
	private Map<Object,List<String>> validatorMessage = new LinkedHashMap<Object,List<String>>();
	private List<String> tmpErrorList;
	private Object [] params;
	public ValidationException() { super(); }

	public ValidationException(String message)
	{
		this();
		this.addErrorMessage(message);
	}
	public ValidationException(String message,Object[]params){
		this();
		this.addErrorMessage(message);
		this.setParams(params);
	}
	public void addErrorMessage(String errorMessage)
	{
		if (tmpErrorList == null)
			tmpErrorList = new ArrayList<String>();

		tmpErrorList.add(errorMessage);
	}
	public void setParams (Object [] params){
		this.params= params;
	}
	public Object[] getParams(){
		return this.params;
	}
	/**
	 * Establecer el campo del mensaje de error. Si es un String se toma como viene,
	 * si es un mensaje I18N(Ej.: &company) se transforma en el cliente y si es un
	 * objeto FEField en el cliente se pide el FEFieldView en el idioma del usuario. 
	 * 
	 * @param field String|I18N.String|FEField
	 */
	public void setField(Object field)
	{
		validatorMessage.put(field, tmpErrorList);
		tmpErrorList = null;
	}

	public Map<Object,List<String>> getValidatorMessages()
	{
		return validatorMessage;
	}

	public List<String> getTemporalMessages()
	{
		return tmpErrorList;
	}

	public String[] getValidationMessageAsArray() {
		List<String> list = new ArrayList<String>();
		String value;
		for (Object key : this.validatorMessage.keySet()) {
			Object aux = this.validatorMessage.get(key);
			if (aux == null) {
				value = "null";
			} else {
				value = aux.toString();
			}
			list.add(key.toString()+"="+value);
		}
		return list.toArray(new String[0]);
	}
	
	public void addValidationException(ValidationException ex)
	{
		if (ex.getTemporalMessages() == null)
			validatorMessage.putAll(ex.getValidatorMessages());
		else
		{
			if (tmpErrorList == null)
				tmpErrorList = new ArrayList<String>();
			tmpErrorList.addAll(ex.getTemporalMessages());
		}
	}

	public String getMessage() {
		StringBuffer buf = new StringBuffer();
		StringBuffer parameters = new StringBuffer();

		buf.append("validator.message [").append(this.validatorMessage).append("]");
		buf.append(", tmp.error.list [").append(this.tmpErrorList).append("]");
		for (int idx = 0; this.params != null && idx < this.params.length; idx++) {
			parameters.append("param_").append(idx).append("{");
			parameters.append("value(").append(this.params[idx].toString()).append("), ");
			parameters.append("type(").append(this.params[idx].getClass().getName()).append(")");
			parameters.append("}");
			if (idx+1 < this.params.length) {
				parameters.append(", ");
			}
		}
		buf.append(", params [").append(parameters.toString()).append("]");
		
		return buf.toString();
	}
}
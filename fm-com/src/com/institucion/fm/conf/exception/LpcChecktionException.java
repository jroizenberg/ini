package com.institucion.fm.conf.exception;

import java.io.Serializable;



public class LpcChecktionException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private LocalizeValidationException [] params;
	public LpcChecktionException() { super(); }

	public LpcChecktionException(String message,LocalizeValidationException[]params){
		this();
		this.setParams(params);
	}

	public void setParams (LocalizeValidationException [] params){
		this.params= params;
	}
	public LocalizeValidationException[] getParams(){
		return this.params;
	}
	
	public String getMessage() {
		StringBuffer buf = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		for (int idx = 0; this.params != null && idx < this.params.length; idx++) {
			parameters.append("PARAM_").append(idx).append(" {");
			parameters.append("").append(this.params[idx].toString()).append(")");
			parameters.append("}");
			if (idx+1 < this.params.length) {
				parameters.append(", ");
			}
		}
		buf.append("LVEs [").append(parameters.toString()).append("]");
		return buf.toString();
		
	}
}

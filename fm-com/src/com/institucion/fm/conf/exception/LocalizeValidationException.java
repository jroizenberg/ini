package com.institucion.fm.conf.exception;




public class LocalizeValidationException extends ExceptionBase{
	private static final long serialVersionUID = 1L;

	
	private String errorMessage;
	private Object [] params;
	public LocalizeValidationException() { super(); }

	public LocalizeValidationException(String message)
	{
		this();
		this.errorMessage=message;
	}
	public LocalizeValidationException(String message,Object[]params){
		this();
		this.errorMessage=message;
		this.setParams(params);
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setParams (Object [] params){
		this.params= params;
	}
	public Object[] getParams(){
		return this.params;
	}

	public String getMessage() {
		StringBuffer buf = new StringBuffer();
		StringBuffer parameters = new StringBuffer();
		buf.append("message.code [").append(this.errorMessage).append("]");
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

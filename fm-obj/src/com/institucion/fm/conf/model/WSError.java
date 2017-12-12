package com.institucion.fm.conf.model;

public class WSError {
	
	private String errorKey;
	
	private String[] errorValues;

	public WSError(String errorKey, String[] errorValues) {
		this.errorKey = errorKey;
		this.errorValues = errorValues;
	}
	
	public WSError(){
		
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String[] getErrorValues() {
		return errorValues;
	}

	public void setErrorValues(String[] errorValues) {
		this.errorValues = errorValues;
	}

}

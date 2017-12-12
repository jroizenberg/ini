package com.institucion.fm.conf.exception.dao;

import com.institucion.fm.conf.exception.ExceptionBase;

public class NotNullConstraintException extends ExceptionBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NotNullConstraintException(){
		super();
	}
	public NotNullConstraintException(Throwable e) {
		super(e);
	}

}

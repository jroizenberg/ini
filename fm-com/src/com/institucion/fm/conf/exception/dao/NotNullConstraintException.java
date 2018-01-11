package com.institucion.fm.conf.exception.dao;

import java.io.Serializable;

import com.institucion.fm.conf.exception.ExceptionBase;

public class NotNullConstraintException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public NotNullConstraintException(){
		super();
	}
	public NotNullConstraintException(Throwable e) {
		super(e);
	}

}

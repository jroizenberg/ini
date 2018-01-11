package com.institucion.fm.conf.exception.dao;

import java.io.Serializable;

import com.institucion.fm.conf.exception.ExceptionBase;

public class ForeignKeyException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public ForeignKeyException() {
		super();
	}

	public ForeignKeyException(Throwable e) {
		super(e);
	}
}

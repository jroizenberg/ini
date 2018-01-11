package com.institucion.fm.conf.exception.dao;

import java.io.Serializable;

import com.institucion.fm.conf.exception.ExceptionBase;

public class CheckConstraintException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public CheckConstraintException() {
		super();
	}

	public CheckConstraintException(Throwable e) {
		super(e);
	}
}

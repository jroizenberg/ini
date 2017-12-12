package com.institucion.fm.conf.exception.dao;

import com.institucion.fm.conf.exception.ExceptionBase;

public class UniqueConstraintException extends ExceptionBase{
	private static final long serialVersionUID = 1L;

	public UniqueConstraintException() {
		super();
	}

	public UniqueConstraintException(Throwable e) {
		super(e);
	}
}

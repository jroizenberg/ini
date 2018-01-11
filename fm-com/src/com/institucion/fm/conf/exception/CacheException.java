package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class CacheException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	
	public CacheException(Throwable e){
		super(e);
	}

}

package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class CacheExceptionHandler extends ExceptionHandlerSpringAware implements Serializable {
	private static final long serialVersionUID = 1L;

	public void handle(Thread thread, Throwable throwable) throws Throwable {
        log.error("-------------Cache Exception Handler bean context aspect!!-------------");
		log.error("Exception:", throwable);
        throw throwable;
	}
	
}

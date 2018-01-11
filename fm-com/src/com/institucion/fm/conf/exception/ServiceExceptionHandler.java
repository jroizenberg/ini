package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class ServiceExceptionHandler extends ExceptionHandlerSpringAware implements Serializable {
	private static final long serialVersionUID = 1L;

	public void handle(Thread thread, Throwable throwable) throws Throwable {
        log.error("-------------Service Exception Handler bean context aspect!!-------------");
        throw throwable;
	}	
}

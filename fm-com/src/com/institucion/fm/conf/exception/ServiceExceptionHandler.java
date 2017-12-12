package com.institucion.fm.conf.exception;

public class ServiceExceptionHandler extends ExceptionHandlerSpringAware {

	public void handle(Thread thread, Throwable throwable) throws Throwable {
        log.error("-------------Service Exception Handler bean context aspect!!-------------");
        throw throwable;
	}	
}

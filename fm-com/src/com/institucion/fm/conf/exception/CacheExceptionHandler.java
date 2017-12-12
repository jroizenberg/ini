package com.institucion.fm.conf.exception;

public class CacheExceptionHandler extends ExceptionHandlerSpringAware {

	public void handle(Thread thread, Throwable throwable) throws Throwable {
        log.error("-------------Cache Exception Handler bean context aspect!!-------------");
		log.error("Exception:", throwable);
        throw throwable;
	}
	
}

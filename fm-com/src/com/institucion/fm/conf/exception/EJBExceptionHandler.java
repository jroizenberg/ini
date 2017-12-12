package com.institucion.fm.conf.exception;



public class EJBExceptionHandler extends ExceptionHandlerEJBAware {

	public void handle(Thread thread, Throwable throwable) throws Throwable {
		
		log.error("-------------EJB Exception Handler bean context aspect!!-------------");
		log.debug("Exception:", throwable);
        throw throwable;
	}

}

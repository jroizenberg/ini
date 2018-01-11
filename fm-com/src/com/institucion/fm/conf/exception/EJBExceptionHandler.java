package com.institucion.fm.conf.exception;

import java.io.Serializable;



public class EJBExceptionHandler extends ExceptionHandlerEJBAware implements Serializable {
	private static final long serialVersionUID = 1L;

	public void handle(Thread thread, Throwable throwable) throws Throwable {
		
		log.error("-------------EJB Exception Handler bean context aspect!!-------------");
		log.debug("Exception:", throwable);
        throw throwable;
	}

}

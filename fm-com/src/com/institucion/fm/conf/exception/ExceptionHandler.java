package com.institucion.fm.conf.exception;

public interface ExceptionHandler {
	public void handle(Thread thread, Throwable throwable) throws Throwable;
}

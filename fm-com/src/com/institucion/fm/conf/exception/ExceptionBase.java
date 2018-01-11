package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class ExceptionBase extends RuntimeException implements Serializable {
	
	private static final long serialVersionUID = 1334037753597789170L;
    protected Throwable cause;

    public ExceptionBase() {
        super("Error occurred in application.");
    }

    public ExceptionBase(String message)  {
        super(message);
    }

    public ExceptionBase(String message, Throwable cause)  {
        super(message, cause);
    }

    public ExceptionBase(Throwable cause)  {
        super(cause);
    }

}

package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class WorkFlowException extends ExceptionBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public WorkFlowException(String message)
	{
		super(message);
	}
}
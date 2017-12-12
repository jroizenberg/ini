package com.institucion.fm.conf.exception;

public class ParseException extends ExceptionBase
{
	private static final long serialVersionUID = 1L;

	public ParseException()
	{
		super("Error occurred in parse object.");
	}

	public ParseException(String message)
	{
		super(message);
	}

	public ParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ParseException(Throwable cause)
	{
		super(cause);
	}
}
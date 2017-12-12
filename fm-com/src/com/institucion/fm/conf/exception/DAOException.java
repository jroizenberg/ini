package com.institucion.fm.conf.exception;

public class DAOException extends ExceptionBase
{

	private static final long serialVersionUID = 8033574479348707844L;

   public DAOException(String message)  {
        super(message);
   }

	public DAOException() {
		super("DAO Exception");
	}

	public DAOException(Exception e) {
		super("DAO Exception", e);
	}
}
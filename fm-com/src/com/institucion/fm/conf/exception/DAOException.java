package com.institucion.fm.conf.exception;

import java.io.Serializable;

public class DAOException extends ExceptionBase implements Serializable {

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
package com.institucion.fm.conf.exception;

import java.sql.SQLException;

import com.institucion.fm.conf.exception.dao.CheckConstraintException;
import com.institucion.fm.conf.exception.dao.ForeignKeyException;
import com.institucion.fm.conf.exception.dao.NotNullConstraintException;
import com.institucion.fm.conf.exception.dao.UniqueConstraintException;

/* Referencia http://www.postgresql.org/docs/8.1/interactive/errcodes-appendix.html
Class 23  Integrity Constraint Violation
23000	INTEGRITY CONSTRAINT VIOLATION	integrity_constraint_violation
23001	RESTRICT VIOLATION	restrict_violation
23502	NOT NULL VIOLATION	not_null_violation
23503	FOREIGN KEY VIOLATION	foreign_key_violation
23505	UNIQUE VIOLATION	unique_violation
23514	CHECK VIOLATION	check_violation
*/
public class DAOExceptionHandler extends ExceptionHandlerSpringAware {
	// Unique no diferente entre una PK y un constraint...
    private static final String UNIQUE_CONSTRAINT_VIOLATION_CODE = "23505";
    private static final String FOREIGN_KEY_VIOLATION_CODE = "23503";
    private static final String CHECK_CONSTRAINT_VIOLATION_CODE = "23514";
    private static final String NOT_NULL_VIOLATION_CODE = "23502";

    public Throwable decode(Throwable ex) {
        Throwable t = ex;
        
        while (t != null) {
        	if (t instanceof SQLException) {
                SQLException e = (SQLException)t;
                if ("".equals(e.getSQLState()) || e.getSQLState() == null) {
                	t = e.getNextException();
                	continue;
                }
				// Procesamos el SQLState
				if (CHECK_CONSTRAINT_VIOLATION_CODE.equals(e.getSQLState())) {
					return new CheckConstraintException(ex); 
				} else if (FOREIGN_KEY_VIOLATION_CODE.equals(e.getSQLState())) {
					return new ForeignKeyException(ex); 
				} else if (UNIQUE_CONSTRAINT_VIOLATION_CODE.equals(e.getSQLState())) {
					return new UniqueConstraintException(ex); 
				} else if (NOT_NULL_VIOLATION_CODE.equals(e.getSQLState()))
					return new NotNullConstraintException(ex);
        	}
            t = t.getCause();
        }
        return ex;
    }

	
	public void handle(Thread thread, Throwable throwable) throws Throwable {
        log.error("-------------DAO Exception Handler bean context aspect!!-------------");
		log.error("Exception:", throwable);
		throw this.decode(throwable);
	}
	

}

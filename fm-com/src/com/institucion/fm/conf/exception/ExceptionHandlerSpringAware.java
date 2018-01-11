package com.institucion.fm.conf.exception;

import java.io.Serializable;

import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

public abstract class ExceptionHandlerSpringAware extends ExceptionHandlerBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
		  log = LogFactory.getLog(joinPoint.getSignature().getDeclaringTypeName());
	      try {
	         return joinPoint.proceed();
	      } catch (RuntimeException e) {
	         this.handle(Thread.currentThread(), e);
	      }catch (Exception e) {
	         this.handle(Thread.currentThread(), e);
	      } catch (Error e) {
	    	  this.handle(Thread.currentThread(), e);
	      }
	      return null;
   }

}

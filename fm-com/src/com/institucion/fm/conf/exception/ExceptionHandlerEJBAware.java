package com.institucion.fm.conf.exception;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.commons.logging.LogFactory;

public abstract class ExceptionHandlerEJBAware extends ExceptionHandlerBase implements Serializable {
	private static final long serialVersionUID = 1L;
	@AroundInvoke
	public Object invoke(InvocationContext ctx) throws Exception {
		  log = LogFactory.getLog(ctx.getMethod().getName());
	      try {
	         return ctx.proceed();
	      } catch (RuntimeException e) {
	         this.handle2(Thread.currentThread(), e);
	      }catch (Exception e) {
	         this.handle2(Thread.currentThread(), e);
	      } catch (Error e) {
	    	 this.handle2(Thread.currentThread(), e);
	      }
	      return null;
	}

	/**
	 * Workaround para resolver la limitación en la firma del @AroundInvoke que sólo
	 * permite throws de la clase Exception o hijos de la misma y no de Throwable.
	 * Esto deja afuera a Error y sus derivados. El método envuelve la excepción de tipo
	 * Error en una de tipo Exception.
	 * @param thread
	 * @param throwable
	 * @throws Exception
	 */
	public void handle2(Thread thread, Throwable throwable) throws Exception {
		try {
			this.handle(thread, throwable);
		} catch (Throwable e) {
			if (e instanceof Error) {
				throw new Exception(e);
			} else if (e instanceof Exception) {
				throw (Exception)e;
			}
			// Esto no debería suceder
			throw new Exception("unknown type of Exception", e);
		}
	}

}

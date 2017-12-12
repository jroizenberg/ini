package com.institucion.fm.runnable;

public interface Consumer {
	
	/**
	 * Este metodo sirve para notificar a los consumidores cuando todos los productores hayan terminado de trabajar.
	 */
	public void acknowldege();

}

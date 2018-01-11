package com.institucion.model;

import java.io.Serializable;


public class AnularSubsDevolucion implements Serializable {
	private static final long serialVersionUID = 1L;

	private AnularSubscripcionTipoDevolucionEnum tipo;
	private float dinero;
	
	public AnularSubscripcionTipoDevolucionEnum getTipo() {
		return tipo;
	}
	public void setTipo(AnularSubscripcionTipoDevolucionEnum tipo) {
		this.tipo = tipo;
	}
	public float getDinero() {
		return dinero;
	}
	public void setDinero(float dinero) {
		this.dinero = dinero;
	}
	
}
package com.institucion.model;


public class AnularSubsDevolucion {

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
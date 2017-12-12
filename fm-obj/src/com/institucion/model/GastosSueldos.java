package com.institucion.model;

import java.io.Serializable;

public class GastosSueldos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Actividad actividad;
	private Integer dinero;
	private String comentario;
	private Gastos gasto;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Actividad getActividad() {
		return actividad;
	}
	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}
	public Integer getDinero() {
		return dinero;
	}
	public void setDinero(Integer dinero) {
		this.dinero = dinero;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public Gastos getGasto() {
		return gasto;
	}
	public void setGasto(Gastos gasto) {
		this.gasto = gasto;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
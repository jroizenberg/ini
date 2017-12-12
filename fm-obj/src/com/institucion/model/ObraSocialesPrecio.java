package com.institucion.model;

import java.io.Serializable;

public class ObraSocialesPrecio implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Actividad actividad; 
	private ObraSocial obraSocial;
	private Boolean disponible;
	private Boolean sePagaUnaUnicaVez;

	private int precioQuePagaElCliente;  
	private Double precioQuePagaLaObraSocial;  
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPrecioQuePagaElCliente() {
		return precioQuePagaElCliente;
	}

	public void setPrecioQuePagaElCliente(int precioQuePagaElCliente) {
		this.precioQuePagaElCliente = precioQuePagaElCliente;
	}

	public Double getPrecioQuePagaLaObraSocial() {
		if(precioQuePagaLaObraSocial != null){
		       return Math.rint(precioQuePagaLaObraSocial*100)/100;
		}
		return precioQuePagaLaObraSocial;
	}

	public void setPrecioQuePagaLaObraSocial(Double precioQuePagaLaObraSocial) {
		this.precioQuePagaLaObraSocial = precioQuePagaLaObraSocial;
	}

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
	}

	public ObraSocial getObraSocial() {
		return obraSocial;
	}

	public Boolean getDisponible() {
		return disponible;
	}

	public Boolean getSePagaUnaUnicaVez() {
		return sePagaUnaUnicaVez;
	}

	public void setSePagaUnaUnicaVez(Boolean sePagaUnaUnicaVez) {
		this.sePagaUnaUnicaVez = sePagaUnaUnicaVez;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}
	
}
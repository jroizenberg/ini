package com.institucion.model;

import java.io.Serializable;

public class Matricula   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private int precio;
	
	public Matricula() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}
}
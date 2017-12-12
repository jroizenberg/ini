package com.institucion.model;

import java.io.Serializable;

public class ClaseCantidad   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;  // 3 clases unitarias- 3 clases por semana
							// - todo el mes.- todos los sabados o todos los miercoles, etc	 
	private String descripcion;
	private int cantidadDeDias;  // 1, 2, 3, 4, 5, 6, 7
	private ClasesTiposEnum tipoClaseId; // Enum ClasesTiposEnum  
			
	public ClaseCantidad() {}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public int getCantidadDeDias() {
		return cantidadDeDias;
	}


	public void setCantidadDeDias(int cantidadDeDias) {
		this.cantidadDeDias = cantidadDeDias;
	}


	public ClasesTiposEnum getTipoClaseId() {
		return tipoClaseId;
	}


	public void setTipoClaseId(ClasesTiposEnum tipoClaseId) {
		this.tipoClaseId = tipoClaseId;
	}
}
package com.institucion.model;

import java.io.Serializable;
import java.util.Set;

public class Empleado implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private Set<EmpleadoActividades> actividades;
	
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

	public Set<EmpleadoActividades> getActividades() {
		return actividades;
	}

	public void setActividades(Set<EmpleadoActividades> actividades) {
		this.actividades = actividades;
	}

}
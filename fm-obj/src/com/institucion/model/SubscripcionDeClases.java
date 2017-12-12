package com.institucion.model;

import java.io.Serializable;
import java.util.Set;

public class SubscripcionDeClases   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Subscripcion subscripcion;
	private Curso curso;
	private Set<SubscripcionDeClasesPorActividad> claseSeleccionadasList;
		
	public SubscripcionDeClases() {}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Subscripcion getSubscripcion() {
		return subscripcion;
	}


	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}


	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	public Set<SubscripcionDeClasesPorActividad> getClaseSeleccionadasList() {
		return claseSeleccionadasList;
	}


	public void setClaseSeleccionadasList(Set<SubscripcionDeClasesPorActividad> claseSeleccionadasList) {
		this.claseSeleccionadasList = claseSeleccionadasList;
	}

}
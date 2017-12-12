package com.institucion.model;

import java.io.Serializable;

public class ActividadYClase   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Curso curso;
	private Actividad activ;

	private int cantClases;
	
	public ActividadYClase() {}

	
	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public int getCantClases() {
		return cantClases;
	}


	public void setCantClases(int cantClases) {
		this.cantClases = cantClases;
	}

	public Actividad getActiv() {
		return activ;
	}


	public void setActiv(Actividad activ) {
		this.activ = activ;
	}

}
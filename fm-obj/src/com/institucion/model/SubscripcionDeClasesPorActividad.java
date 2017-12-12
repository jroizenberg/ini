package com.institucion.model;

import java.io.Serializable;

public class SubscripcionDeClasesPorActividad   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
//	private Long  idsubscripcion;
	private Subscripcion  subscripcion2;
	private Curso curso;
	
	private Actividad actividad;
	
	private boolean lunes;
	private boolean martes;
	private boolean miercoles;
	private boolean jueves;
	private boolean viernes;
	private boolean sabado;
	private boolean domingo;
	
	private Clase claseLunes;
	private Clase claseMartes;
	private Clase claseMiercoles;
	private Clase claseJueves;
	private Clase claseViernes;
	private Clase claseSabado;
	private Clase claseDomingo;
	
	public SubscripcionDeClasesPorActividad() {}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

//	public Subscripcion getSubscripcion() {
//		return subscripcion;
//	}
//
//
//	public void setSubscripcion(Subscripcion subscripcion) {
//		this.subscripcion = subscripcion;
//	}


	public Actividad getActividad() {
		return actividad;
	}


	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}


	public boolean isLunes() {
		return lunes;
	}


	public void setLunes(boolean lunes) {
		this.lunes = lunes;
	}


	public boolean isMartes() {
		return martes;
	}


	public void setMartes(boolean martes) {
		this.martes = martes;
	}


	public boolean isMiercoles() {
		return miercoles;
	}


	public void setMiercoles(boolean miercoles) {
		this.miercoles = miercoles;
	}


	public boolean isJueves() {
		return jueves;
	}


	public void setJueves(boolean jueves) {
		this.jueves = jueves;
	}


	public boolean isViernes() {
		return viernes;
	}


	public void setViernes(boolean viernes) {
		this.viernes = viernes;
	}


	public boolean isSabado() {
		return sabado;
	}


	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}


	public boolean isDomingo() {
		return domingo;
	}


	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}


	public Clase getClaseLunes() {
		return claseLunes;
	}


	public void setClaseLunes(Clase claseLunes) {
		this.claseLunes = claseLunes;
	}


	public Clase getClaseMartes() {
		return claseMartes;
	}


	public void setClaseMartes(Clase claseMartes) {
		this.claseMartes = claseMartes;
	}


	public Clase getClaseMiercoles() {
		return claseMiercoles;
	}


	public void setClaseMiercoles(Clase claseMiercoles) {
		this.claseMiercoles = claseMiercoles;
	}


	public Clase getClaseJueves() {
		return claseJueves;
	}


	public void setClaseJueves(Clase claseJueves) {
		this.claseJueves = claseJueves;
	}


	public Clase getClaseViernes() {
		return claseViernes;
	}


	public Subscripcion getSubscripcion2() {
		return subscripcion2;
	}


	public void setSubscripcion2(Subscripcion subscripcion2) {
		this.subscripcion2 = subscripcion2;
	}


	public void setClaseViernes(Clase claseViernes) {
		this.claseViernes = claseViernes;
	}


	public Clase getClaseSabado() {
		return claseSabado;
	}


	public void setClaseSabado(Clase claseSabado) {
		this.claseSabado = claseSabado;
	}

//
//	public Subscripcion getSubscripcion() {
//		return subscripcion;
//	}
//
//
//	public void setSubscripcion(Subscripcion subscripcion) {
//		this.subscripcion = subscripcion;
//	}


	public Curso getCurso() {
		return curso;
	}


	public void setCurso(Curso curso) {
		this.curso = curso;
	}


	public Clase getClaseDomingo() {
		return claseDomingo;
	}


	public void setClaseDomingo(Clase claseDomingo) {
		this.claseDomingo = claseDomingo;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


//	public Long getIdsubscripcion() {
//		return idsubscripcion;
//	}
//
//
//	public void setIdsubscripcion(Long idsubscripcion) {
//		this.idsubscripcion = idsubscripcion;
//	}

}
package com.institucion.model;

import java.io.Serializable;

public class EmpleadoActividades implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Actividad actividad;
	private boolean disponible;
	private Empleado empleado;
		
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

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

}
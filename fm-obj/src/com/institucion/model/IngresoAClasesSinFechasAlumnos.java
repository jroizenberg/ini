package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class IngresoAClasesSinFechasAlumnos  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Cliente cliente;
	private Clase clase;
	private Date fecha;
	private Curso curso;
	private Subscripcion subscripcion;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Clase getClase() {
		return clase;
	}
	public void setClase(Clase clase) {
		this.clase = clase;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Curso getCurso() {
		return curso;
	}
	public void setCurso(Curso curso) {
		this.curso = curso;
	}
	public Subscripcion getSubscripcion() {
		return subscripcion;
	}
	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}
}
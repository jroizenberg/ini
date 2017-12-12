package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class ClienteListaEncontradoEnPileta implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Cliente cliente;
	private ClaseConListaAlumnos clase;
	private Date fecha;
	private Boolean asistencia;
	private String comentario;
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
	public ClaseConListaAlumnos getClase() {
		return clase;
	}
	public void setClase(ClaseConListaAlumnos clase) {
		this.clase = clase;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Boolean getAsistencia() {
		return asistencia;
	}
	public void setAsistencia(Boolean asistencia) {
		this.asistencia = asistencia;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
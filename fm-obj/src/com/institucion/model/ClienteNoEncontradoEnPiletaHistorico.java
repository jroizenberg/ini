package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class ClienteNoEncontradoEnPiletaHistorico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String telefono;
	private String celular;
	private Date fecha;
	private ClaseConListaAlumnosHistorico clase;
	private String comentario;

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

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public ClaseConListaAlumnosHistorico getClase() {
		return clase;
	}
	public void setClase(ClaseConListaAlumnosHistorico clase) {
		this.clase = clase;
	}
	
}
package com.institucion.model;

import java.io.Serializable;

public class Actividad   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String descripcion;
	private boolean usaCarnet;
	private boolean usaEnObraSocial;
	private boolean imprimeComprobante;
	private boolean tomaLista;
	private boolean esparasueldos;
	private TipoCursoEnum idTipoCurso;
	
	public Actividad() {}


	public TipoCursoEnum getIdTipoCurso() {
		return idTipoCurso;
	}
	public void setIdTipoCurso(TipoCursoEnum idTipoCurso) {
		this.idTipoCurso = idTipoCurso;
	}
	
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


	public boolean isUsaCarnet() {
		return usaCarnet;
	}


	public boolean isTomaLista() {
		return tomaLista;
	}


	public void setTomaLista(boolean tomaLista) {
		this.tomaLista = tomaLista;
	}


	public boolean isUsaEnObraSocial() {
		return usaEnObraSocial;
	}


	public void setUsaEnObraSocial(boolean usaEnObraSocial) {
		this.usaEnObraSocial = usaEnObraSocial;
	}


	public void setUsaCarnet(boolean usaCarnet) {
		this.usaCarnet = usaCarnet;
	}


	public boolean isEsparasueldos() {
		return esparasueldos;
	}


	public void setEsparasueldos(boolean esparasueldos) {
		this.esparasueldos = esparasueldos;
	}


	public boolean isImprimeComprobante() {
		return imprimeComprobante;
	}


	public void setImprimeComprobante(boolean imprimeComprobante) {
		this.imprimeComprobante = imprimeComprobante;
	}
}
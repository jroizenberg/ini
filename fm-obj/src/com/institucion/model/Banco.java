package com.institucion.model;

import java.io.Serializable;
import java.util.Set;

public class Banco implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	
	private Set<BancoPromocion> listaPromocionesBanco;

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

	public Set<BancoPromocion> getListaPromocionesBanco() {
		return listaPromocionesBanco;
	}

	public void setListaPromocionesBanco(Set<BancoPromocion> listaPromocionesBanco) {
		this.listaPromocionesBanco = listaPromocionesBanco;
	}

}
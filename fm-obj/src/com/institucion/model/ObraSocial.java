package com.institucion.model;

import java.io.Serializable;
import java.util.Set;

public class ObraSocial implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
//	private Long idObraSocial;
	private String nombre;
	
	private Set<ObraSocialesPrecio> preciosActividadesObraSocial;

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

	public Set<ObraSocialesPrecio> getPreciosActividadesObraSocial() {
		return preciosActividadesObraSocial;
	}

	public void setPreciosActividadesObraSocial(
			Set<ObraSocialesPrecio> preciosActividadesObraSocial) {
		this.preciosActividadesObraSocial = preciosActividadesObraSocial;
	}


//	public Long getIdObraSocial() {
//		return idObraSocial;
//	}
//
//	public void setIdObraSocial(Long idObraSocial) {
//		this.idObraSocial = idObraSocial;
//	}
}
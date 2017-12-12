package com.institucion.model;

import java.io.Serializable;

public class BancoPromocion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Banco banco; 
	private Integer porcentajepromocion;
	private Integer cantcuotas;  
	private String nombrePromo;  
	private Boolean disponible;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getDisponible() {
		return disponible;
	}

	public void setDisponible(Boolean disponible) {
		this.disponible = disponible;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Integer getPorcentajepromocion() {
		return porcentajepromocion;
	}

	public void setPorcentajepromocion(Integer porcentajepromocion) {
		this.porcentajepromocion = porcentajepromocion;
	}

	public Integer getCantcuotas() {
		return cantcuotas;
	}

	public void setCantcuotas(Integer cantcuotas) {
		this.cantcuotas = cantcuotas;
	}

	public String getNombrePromo() {
		return nombrePromo;
	}

	public void setNombrePromo(String nombrePromo) {
		this.nombrePromo = nombrePromo;
	}
	
}
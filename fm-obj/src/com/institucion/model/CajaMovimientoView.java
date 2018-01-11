package com.institucion.model;

import java.io.Serializable;


public class CajaMovimientoView implements Serializable {
	private static final long serialVersionUID = 1L;

	private String tipo;
	private String fecha;
	private String responsable;
	private String cliente;
	private String concepto;
	
	private String valor;
	private String cantCuposTenia;
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getCantCuposTenia() {
		return cantCuposTenia;
	}
	public void setCantCuposTenia(String cantCuposTenia) {
		this.cantCuposTenia = cantCuposTenia;
	}
	
}
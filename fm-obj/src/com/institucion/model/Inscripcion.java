package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class Inscripcion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Cliente cliente;
	private Date fecha;
	private Date fechaVencimiento;
	private float saldoAbonado;
	private Boolean matriculaGratis;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public float getSaldoAbonado() {
		return saldoAbonado;
	}
	public void setSaldoAbonado(float saldoAbonado) {
		this.saldoAbonado = saldoAbonado;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Boolean getMatriculaGratis() {
		return matriculaGratis;
	}
	public void setMatriculaGratis(Boolean matriculaGratis) {
		this.matriculaGratis = matriculaGratis;
	}	
}
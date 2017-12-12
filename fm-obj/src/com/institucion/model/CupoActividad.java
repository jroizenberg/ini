package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class CupoActividad  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Curso curso;
	private Subscripcion subscripcion;
	private Actividad actividad;
	private int cupos;
	
	private Date fechaComienzo;  // cuando se setea la fecha de comienzo , se hace el calculo para la fecha de fin
	private Date fechaFin;            // SE DEBE actualizar la fecha de fin 
	private CupoActividadEstadoEnum estado;
		
	private Integer posponeMeses;
	private Boolean posponePagaAdicional;
	private Integer posponeAdicional;
	private String posponeComentario;
	private String posponeAprobadoPor;
	private Date posponefechaYHora;
	private Integer idUsuarioPosponeSubscripcion;
	
	private Integer posponeMeses2;
	private Boolean posponePagaAdicional2;
	private Integer posponeAdicional2;
	private String posponeComentario2;
	private String posponeAprobadoPor2;
	private Date posponefechaYHora2;
	private Integer idUsuarioPosponeSubscripcion2;
	private Boolean pagoElCopago;
	public CupoActividad() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Actividad getActividad() {
		return actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public int getCupos() {
		return cupos;
	}

	public void setCupos(int cupos) {
		this.cupos = cupos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getFechaComienzo() {
		return fechaComienzo;
	}

	public void setFechaComienzo(Date fechaComienzo) {
		this.fechaComienzo = fechaComienzo;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public Integer getPosponeMeses() {
		return posponeMeses;
	}

	public void setPosponeMeses(Integer posponeMeses) {
		this.posponeMeses = posponeMeses;
	}

	public Boolean getPosponePagaAdicional() {
		return posponePagaAdicional;
	}

	public void setPosponePagaAdicional(Boolean posponePagaAdicional) {
		this.posponePagaAdicional = posponePagaAdicional;
	}

	public Integer getPosponeAdicional() {
		return posponeAdicional;
	}

	public void setPosponeAdicional(Integer posponeAdicional) {
		this.posponeAdicional = posponeAdicional;
	}

	public String getPosponeComentario() {
		return posponeComentario;
	}

	public void setPosponeComentario(String posponeComentario) {
		this.posponeComentario = posponeComentario;
	}

	public String getPosponeAprobadoPor() {
		return posponeAprobadoPor;
	}

	public void setPosponeAprobadoPor(String posponeAprobadoPor) {
		this.posponeAprobadoPor = posponeAprobadoPor;
	}

	public Date getPosponefechaYHora() {
		return posponefechaYHora;
	}

	public void setPosponefechaYHora(Date posponefechaYHora) {
		this.posponefechaYHora = posponefechaYHora;
	}

	public Integer getIdUsuarioPosponeSubscripcion() {
		return idUsuarioPosponeSubscripcion;
	}

	public void setIdUsuarioPosponeSubscripcion(Integer idUsuarioPosponeSubscripcion) {
		this.idUsuarioPosponeSubscripcion = idUsuarioPosponeSubscripcion;
	}

	public Integer getPosponeMeses2() {
		return posponeMeses2;
	}

	public void setPosponeMeses2(Integer posponeMeses2) {
		this.posponeMeses2 = posponeMeses2;
	}

	public Boolean getPosponePagaAdicional2() {
		return posponePagaAdicional2;
	}

	public void setPosponePagaAdicional2(Boolean posponePagaAdicional2) {
		this.posponePagaAdicional2 = posponePagaAdicional2;
	}

	public Integer getPosponeAdicional2() {
		return posponeAdicional2;
	}

	public void setPosponeAdicional2(Integer posponeAdicional2) {
		this.posponeAdicional2 = posponeAdicional2;
	}

	public String getPosponeComentario2() {
		return posponeComentario2;
	}

	public void setPosponeComentario2(String posponeComentario2) {
		this.posponeComentario2 = posponeComentario2;
	}

	public String getPosponeAprobadoPor2() {
		return posponeAprobadoPor2;
	}

	public void setPosponeAprobadoPor2(String posponeAprobadoPor2) {
		this.posponeAprobadoPor2 = posponeAprobadoPor2;
	}

	public Date getPosponefechaYHora2() {
		return posponefechaYHora2;
	}

	public void setPosponefechaYHora2(Date posponefechaYHora2) {
		this.posponefechaYHora2 = posponefechaYHora2;
	}

	public Boolean getPagoElCopago() {
		return pagoElCopago;
	}

	public void setPagoElCopago(Boolean pagoElCopago) {
		this.pagoElCopago = pagoElCopago;
	}

	public Integer getIdUsuarioPosponeSubscripcion2() {
		return idUsuarioPosponeSubscripcion2;
	}

	public void setIdUsuarioPosponeSubscripcion2(
			Integer idUsuarioPosponeSubscripcion2) {
		this.idUsuarioPosponeSubscripcion2 = idUsuarioPosponeSubscripcion2;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public CupoActividadEstadoEnum getEstado() {
		return estado;
	}

	public void setEstado(CupoActividadEstadoEnum estado) {
		this.estado = estado;
	}

}
package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class Concepto  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String concepto;
	private Curso curso;
	private int importeOriginal;
	private int importeConDescuento;
	private TipoDescuentosEnum tipoDescuento;
	
//	private Integer cantClase;
	
	private SubscripcionDescuentoGeneralEnum subscripcionDescuentoGeneral;
	
	private String aprobadoPor;
	private String comentarioDescuento;
	
	private ObraSocial obraSocial;
	private Subscripcion subscripcion;
	private Integer precioPorClaseOSesionPagaCliente; 
	private Double precioPorClaseOSesionPagaObraSocial;
	private Actividad actividadDelConcepto;
	private Quincena quincena;
	private Date fechaCumple;

	private int cantSesiones; 
		
	public Concepto() {}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public int getImporteOriginal() {
		return importeOriginal;
	}

	public void setImporteOriginal(int importeOriginal) {
		this.importeOriginal = importeOriginal;
	}

	public Integer getImporteConDescuento() {
		return importeConDescuento;
	}

	public void setImporteConDescuento(Integer importeConDescuento) {
		this.importeConDescuento = importeConDescuento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoDescuentosEnum getTipoDescuento() {
		return tipoDescuento;
	}

	public void setTipoDescuento(TipoDescuentosEnum tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

	public SubscripcionDescuentoGeneralEnum getSubscripcionDescuentoGeneral() {
		return subscripcionDescuentoGeneral;
	}

	public void setSubscripcionDescuentoGeneral(
			SubscripcionDescuentoGeneralEnum subscripcionDescuentoGeneral) {
		this.subscripcionDescuentoGeneral = subscripcionDescuentoGeneral;
	}

	public Integer getPrecioPorClaseOSesionPagaCliente() {
		return precioPorClaseOSesionPagaCliente;
	}

	public void setPrecioPorClaseOSesionPagaCliente(
			Integer precioPorClaseOSesionPagaCliente) {
		this.precioPorClaseOSesionPagaCliente = precioPorClaseOSesionPagaCliente;
	}

	public Double getPrecioPorClaseOSesionPagaObraSocial() {
		if(precioPorClaseOSesionPagaObraSocial != null){
		       return Math.rint(precioPorClaseOSesionPagaObraSocial*100)/100;
		}
		return precioPorClaseOSesionPagaObraSocial;
	}

	public void setPrecioPorClaseOSesionPagaObraSocial(Double precioPorClaseOSesionPagaObraSocial) {
		this.precioPorClaseOSesionPagaObraSocial = precioPorClaseOSesionPagaObraSocial;
	}

	public String getAprobadoPor() {
		return aprobadoPor;
	}

	public void setAprobadoPor(String aprobadoPor) {
		this.aprobadoPor = aprobadoPor;
	}

	public String getComentarioDescuento() {
		return comentarioDescuento;
	}

	public void setComentarioDescuento(String comentarioDescuento) {
		this.comentarioDescuento = comentarioDescuento;
	}

	public ObraSocial getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
	}

	public Date getFechaCumple() {
		return fechaCumple;
	}
	public void setFechaCumple(Date fechaCumple) {
		this.fechaCumple = fechaCumple;
	}
	public int getCantSesiones() {
		return cantSesiones;
	}

	public void setCantSesiones(int cantSesiones) {
		this.cantSesiones = cantSesiones;
	}

	public Subscripcion getSubscripcion() {
		return subscripcion;
	}

	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}

//	public Integer getCantClase() {
//		return cantClase;
//	}

//	public void setCantClase(Integer cantClase) {
//		this.cantClase = cantClase;
//	}

	public Actividad getActividadDelConcepto() {
		return actividadDelConcepto;
	}

	public void setActividadDelConcepto(Actividad actividadDelConcepto) {
		this.actividadDelConcepto = actividadDelConcepto;
	}
	public Quincena getQuincena() {
		return quincena;
	}
	public void setQuincena(Quincena quincena) {
		this.quincena = quincena;
	}

}
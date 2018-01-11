package com.institucion.model;

import java.io.Serializable;

public class PulsoClinicaDetalleParte2  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idActividad;

	/* Segunda columna*/
	// el atributo mes viene vacio
	private Double cantidadIngresoEfectivo;
	private Double cantidadIngresoObraSocial;
	
	private Integer cantidadEgresoRRHH;
	
	private Double cantidadTotal;

	public PulsoClinicaDetalleParte2() {}


	public Double getCantidadIngresoEfectivo() {
		if(cantidadIngresoEfectivo == null)
			return new Double(0);

		return cantidadIngresoEfectivo;
	}

	public void setCantidadIngresoEfectivo(Double cantidadIngresoEfectivo) {
		this.cantidadIngresoEfectivo = cantidadIngresoEfectivo;
	}

	public Double getCantidadIngresoObraSocial() {
		if(cantidadIngresoObraSocial == null)
			return new Double(0);
		return cantidadIngresoObraSocial;
	}

	public void setCantidadIngresoObraSocial(Double cantidadIngresoObraSocial) {
		this.cantidadIngresoObraSocial = cantidadIngresoObraSocial;
	}

	public Integer getCantidadEgresoRRHH() {
		if(cantidadEgresoRRHH == null)
			return 0;
		return cantidadEgresoRRHH;
	}

	public void setCantidadEgresoRRHH(Integer cantidadEgresoRRHH) {
		this.cantidadEgresoRRHH = cantidadEgresoRRHH;
	}

	public Double getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(Double cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}


	public int getIdActividad() {
		return idActividad;
	}


	public void setIdActividad(int idActividad) {
		this.idActividad = idActividad;
	}	
}
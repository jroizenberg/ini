package com.institucion.model;

public class PulsoClinica  {
	
	private String mes;
	private Integer mesId;
	private Double cantidadIngreso;
	private Integer cantidadEgresoRRHH;
	private Integer cantidadEgresoGastosMes;
	private Double cantidadTotal;

	public PulsoClinica() {}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public Double getCantidadIngreso() {
		return cantidadIngreso;
	}

	public void setCantidadIngreso(Double cantidadIngreso) {
		this.cantidadIngreso = cantidadIngreso;
	}

	public Integer getCantidadEgresoRRHH() {
		if(cantidadEgresoRRHH == null)
			cantidadEgresoRRHH=0;

		return cantidadEgresoRRHH;
	}

	public void setCantidadEgresoRRHH(Integer cantidadEgresoRRHH) {
		this.cantidadEgresoRRHH = cantidadEgresoRRHH;
	}

	public Integer getCantidadEgresoGastosMes() {
		if(cantidadEgresoGastosMes == null)
			cantidadEgresoGastosMes=0;
		return cantidadEgresoGastosMes;
	}

	public void setCantidadEgresoGastosMes(Integer cantidadEgresoGastosMes) {
		this.cantidadEgresoGastosMes = cantidadEgresoGastosMes;
	}

	public Double getCantidadTotal() {
		if(cantidadTotal == null)
			cantidadTotal=(double) 0;

		return cantidadTotal;
	}

	public void setCantidadTotal(Double cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

	public Integer getMesId() {
		return mesId;
	}

	public void setMesId(Integer mesId) {
		this.mesId = mesId;
	}
	
}
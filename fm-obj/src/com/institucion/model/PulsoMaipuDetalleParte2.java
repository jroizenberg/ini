package com.institucion.model;

import java.io.Serializable;

public class PulsoMaipuDetalleParte2  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int idCurso;
	private GastosMaipuEnum tipoGasto;

	private TipoMovimientoCajaEnum tipo;
	/* Segunda columna*/
	// el atributo mes viene vacio
	private Double cantidadIngresoEfectivo;
	private Integer cantidadEgresoRRHH;

	private boolean esCursoQueLLevaSueldoYnoEgresos;
	
	private TipoMovimientoCajaEnum tipodeIngreso;

	public PulsoMaipuDetalleParte2() {}


	public Double getCantidadIngresoEfectivo() {
		if(cantidadIngresoEfectivo == null)
			return new Double(0);

		return cantidadIngresoEfectivo;
	}

	public void setCantidadIngresoEfectivo(Double cantidadIngresoEfectivo) {
		this.cantidadIngresoEfectivo = cantidadIngresoEfectivo;
	}


	public int getIdCurso() {
		return idCurso;
	}


	public TipoMovimientoCajaEnum getTipo() {
		return tipo;
	}


	public void setTipo(TipoMovimientoCajaEnum tipo) {
		this.tipo = tipo;
	}


	public void setIdCurso(int idCurso) {
		this.idCurso = idCurso;
	}


	public boolean isEsCursoQueLLevaSueldoYnoEgresos() {
		return esCursoQueLLevaSueldoYnoEgresos;
	}


	public void setEsCursoQueLLevaSueldoYnoEgresos(
			boolean esCursoQueLLevaSueldoYnoEgresos) {
		this.esCursoQueLLevaSueldoYnoEgresos = esCursoQueLLevaSueldoYnoEgresos;
	}


	public Integer getCantidadEgresoRRHH() {
		if(cantidadEgresoRRHH == null)
			cantidadEgresoRRHH=0;
		return cantidadEgresoRRHH;
	}


	public void setCantidadEgresoRRHH(Integer cantidadEgresoRRHH) {
		this.cantidadEgresoRRHH = cantidadEgresoRRHH;
	}


	public GastosMaipuEnum getTipoGasto() {
		return tipoGasto;
	}


	public void setTipoGasto(GastosMaipuEnum tipoGasto) {
		this.tipoGasto = tipoGasto;
	}


	public TipoMovimientoCajaEnum getTipodeIngreso() {
		return tipodeIngreso;
	}


	public void setTipodeIngreso(TipoMovimientoCajaEnum tipodeIngreso) {
		this.tipodeIngreso = tipodeIngreso;
	}
	
}
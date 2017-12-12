package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Gastos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long mes;
	private Long anio;
	private GastosEnum tipoGasto;
	private Integer dinero;
	private String comentario;
	private Boolean pagaSueldosPorMes;
	private Date fechaDesde; 
	private Date fechaHasta;
	private Integer idUsuarioGeneroMovimiento;
	private String nombreUsuarioGeneroMovimiento;
	private Set<GastosSueldos> gastosSueldoList;
	private Empleado empleado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMes() {
		return mes;
	}

	public void setMes(Long mes) {
		this.mes = mes;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public GastosEnum getTipoGasto() {
		return tipoGasto;
	}

	public void setTipoGasto(GastosEnum tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

	public Integer getDinero() {
		if(dinero == null)
			return 0;
		return dinero;
	}

	public void setDinero(Integer dinero) {
		this.dinero = dinero;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Set<GastosSueldos> getGastosSueldoList() {
		return gastosSueldoList;
	}

	public void setGastosSueldoList(Set<GastosSueldos> gastosSueldoList) {
		this.gastosSueldoList = gastosSueldoList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getPagaSueldosPorMes() {
		return pagaSueldosPorMes;
	}

	public void setPagaSueldosPorMes(Boolean pagaSueldosPorMes) {
		this.pagaSueldosPorMes = pagaSueldosPorMes;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public Integer getIdUsuarioGeneroMovimiento() {
		return idUsuarioGeneroMovimiento;
	}

	public void setIdUsuarioGeneroMovimiento(Integer idUsuarioGeneroMovimiento) {
		this.idUsuarioGeneroMovimiento = idUsuarioGeneroMovimiento;
	}

	public String getNombreUsuarioGeneroMovimiento() {
		return nombreUsuarioGeneroMovimiento;
	}

	public void setNombreUsuarioGeneroMovimiento(
			String nombreUsuarioGeneroMovimiento) {
		this.nombreUsuarioGeneroMovimiento = nombreUsuarioGeneroMovimiento;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
}
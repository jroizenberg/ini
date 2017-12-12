package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class GastosMaipu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Quincena quincena;
	private Long anio;
	private GastosMaipuEnum tipoGasto;
	private Integer dinero;
	private String comentario;
	private Date fecha;
	private Integer idUsuarioGeneroMovimiento;
	private String nombreUsuarioGeneroMovimiento;

	private Empleado empleado;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Quincena getQuincena() {
		return quincena;
	}
	public void setQuincena(Quincena quincena) {
		this.quincena = quincena;
	}
	public GastosMaipuEnum getTipoGasto() {
		return tipoGasto;
	}
	public void setTipoGasto(GastosMaipuEnum tipoGasto) {
		this.tipoGasto = tipoGasto;
	}
	public Integer getDinero() {
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
	public Long getAnio() {
		return anio;
	}
	public void setAnio(Long anio) {
		this.anio = anio;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
}
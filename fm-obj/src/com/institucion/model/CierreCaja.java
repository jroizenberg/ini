package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class CierreCaja  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Date fecha;
	private String nombreUsuarioGeneroMovimiento;
	private Integer idUsuarioGeneroMovimiento;
	private Integer valor;
	private String comentario;
	private SucursalEnum sucursal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public Integer getIdUsuarioGeneroMovimiento() {
		return idUsuarioGeneroMovimiento;
	}
	public void setIdUsuarioGeneroMovimiento(Integer idUsuarioGeneroMovimiento) {
		this.idUsuarioGeneroMovimiento = idUsuarioGeneroMovimiento;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getNombreUsuarioGeneroMovimiento() {
		return nombreUsuarioGeneroMovimiento;
	}
	public void setNombreUsuarioGeneroMovimiento(
			String nombreUsuarioGeneroMovimiento) {
		this.nombreUsuarioGeneroMovimiento = nombreUsuarioGeneroMovimiento;
	}
	public Integer getValor() {
		return valor;
	}
	public void setValor(Integer valor) {
		this.valor = valor;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	public SucursalEnum getSucursal() {
		return sucursal;
	}
	public void setSucursal(SucursalEnum sucursal) {
		this.sucursal = sucursal;
	}
	
	
}
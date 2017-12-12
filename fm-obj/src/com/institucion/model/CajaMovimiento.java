package com.institucion.model;

import java.io.Serializable;
import java.util.Date;

public class CajaMovimiento  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Date fecha;
	private TipoMovimientoCajaEnum tipoMovimiento;
	private SucursalEnum sucursal;
	private Integer idUsuarioGeneroMovimiento;
	private Cliente cliente;
	private String concepto;
	private Double valor;
	private Integer idSubscripcion;
	private String nombreUsuarioGeneroMovimiento;
	private Integer idVentaProducto;

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
	public TipoMovimientoCajaEnum getTipoMovimiento() {
		return tipoMovimiento;
	}
	public void setTipoMovimiento(TipoMovimientoCajaEnum tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}
	public Integer getIdUsuarioGeneroMovimiento() {
		return idUsuarioGeneroMovimiento;
	}
	public void setIdUsuarioGeneroMovimiento(Integer idUsuarioGeneroMovimiento) {
		this.idUsuarioGeneroMovimiento = idUsuarioGeneroMovimiento;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public Double getValor() {
		if(valor != null){
			Double doo=Math.rint(valor*100)/100;
			return doo;
		}
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getIdSubscripcion() {
		return idSubscripcion;
	}
	public void setIdSubscripcion(Integer idSubscripcion) {
		this.idSubscripcion = idSubscripcion;
	}
	public String getNombreUsuarioGeneroMovimiento() {
		return nombreUsuarioGeneroMovimiento;
	}
	public void setNombreUsuarioGeneroMovimiento(
			String nombreUsuarioGeneroMovimiento) {
		this.nombreUsuarioGeneroMovimiento = nombreUsuarioGeneroMovimiento;
	}
	public Integer getIdVentaProducto() {
		return idVentaProducto;
	}
	public void setIdVentaProducto(Integer idVentaProducto) {
		this.idVentaProducto = idVentaProducto;
	}
	public SucursalEnum getSucursal() {
		return sucursal;
	}
	public void setSucursal(SucursalEnum sucursal) {
		this.sucursal = sucursal;
	}
}
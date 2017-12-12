package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class VentaProducto  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Set<PagosPorSubscripcion> pagosPorSubscripcionList;
	private Date fechayhoracompra;
	private Integer idUsuarioCreoVenta;
	private Set<VentaProductoPorProducto> productosList;
	private String ventaComentario;
	private Boolean anulaVenta;
	private String anulaComentario;
	private Date fechaYHoraAnulacion;
	private Integer idUsuarioAnuloVenta;

	private String nombreUsuarioCreoVenta;
	private String nombreUsuarioAnuloVenta;
	private Integer anulaValorDevuelto;

	public VentaProducto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<PagosPorSubscripcion> getPagosPorSubscripcionList() {
		return pagosPorSubscripcionList;
	}

	public void setPagosPorSubscripcionList(
			Set<PagosPorSubscripcion> pagosPorSubscripcionList) {
		this.pagosPorSubscripcionList = pagosPorSubscripcionList;
	}

	public Date getFechayhoracompra() {
		return fechayhoracompra;
	}

	public void setFechayhoracompra(Date fechayhoracompra) {
		this.fechayhoracompra = fechayhoracompra;
	}

	public Integer getIdUsuarioCreoVenta() {
		return idUsuarioCreoVenta;
	}

	public void setIdUsuarioCreoVenta(Integer idUsuarioCreoVenta) {
		this.idUsuarioCreoVenta = idUsuarioCreoVenta;
	}

	public Boolean getAnulaVenta() {
		return anulaVenta;
	}

	public void setAnulaVenta(Boolean anulaVenta) {
		this.anulaVenta = anulaVenta;
	}

	public String getAnulaComentario() {
		return anulaComentario;
	}

	public void setAnulaComentario(String anulaComentario) {
		this.anulaComentario = anulaComentario;
	}

	public Date getFechaYHoraAnulacion() {
		return fechaYHoraAnulacion;
	}

	public void setFechaYHoraAnulacion(Date fechaYHoraAnulacion) {
		this.fechaYHoraAnulacion = fechaYHoraAnulacion;
	}

	public Integer getIdUsuarioAnuloVenta() {
		return idUsuarioAnuloVenta;
	}

	public void setIdUsuarioAnuloVenta(Integer idUsuarioAnuloVenta) {
		this.idUsuarioAnuloVenta = idUsuarioAnuloVenta;
	}

	public String getVentaComentario() {
		return ventaComentario;
	}

	public void setVentaComentario(String ventaComentario) {
		this.ventaComentario = ventaComentario;
	}

	public String getNombreUsuarioCreoVenta() {
		return nombreUsuarioCreoVenta;
	}

	public void setNombreUsuarioCreoVenta(String nombreUsuarioCreoVenta) {
		this.nombreUsuarioCreoVenta = nombreUsuarioCreoVenta;
	}

	public String getNombreUsuarioAnuloVenta() {
		return nombreUsuarioAnuloVenta;
	}

	public void setNombreUsuarioAnuloVenta(String nombreUsuarioAnuloVenta) {
		this.nombreUsuarioAnuloVenta = nombreUsuarioAnuloVenta;
	}

	public Integer getAnulaValorDevuelto() {
		return anulaValorDevuelto;
	}

	public void setAnulaValorDevuelto(Integer anulaValorDevuelto) {
		this.anulaValorDevuelto = anulaValorDevuelto;
	}

	public Set<VentaProductoPorProducto> getProductosList() {
		return productosList;
	}

	public void setProductosList(Set<VentaProductoPorProducto> productosList) {
		this.productosList = productosList;
	}

}
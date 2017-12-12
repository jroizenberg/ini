package com.institucion.model;

import java.io.Serializable;

public class VentaProductoPorProducto  implements Serializable  {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Producto producto;
	private VentaProducto ventaProducto;
	private Integer cantidadProd;
	private Integer preciocostoProd;
	
	public VentaProductoPorProducto() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public VentaProducto getVentaProducto() {
		return ventaProducto;
	}

	public void setVentaProducto(VentaProducto ventaProducto) {
		this.ventaProducto = ventaProducto;
	}

	public Integer getCantidadProd() {
		return cantidadProd;
	}

	public void setCantidadProd(Integer cantidadProd) {
		this.cantidadProd = cantidadProd;
	}

	public Integer getPreciocostoProd() {
		return preciocostoProd;
	}

	public void setPreciocostoProd(Integer preciocostoProd) {
		this.preciocostoProd = preciocostoProd;
	}

}
package com.institucion.model;

import java.io.Serializable;

import javax.persistence.Lob;
import javax.persistence.Transient;

public class Producto   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String descripcion;
	private String codigo;
	private int precio;
	private int precioCosto;
	private int stock; 
	private byte[] imagen;
	@Transient
	private boolean checked;

	@Transient
	private int cantidad; 

	 public Producto() {}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Transient
	public boolean isChecked() {
		return checked;
	}

	@Transient
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Lob
	public byte[] getImagen() {
		return imagen;
	}

	@Lob
	public void setImagen(byte[] imagen) {
		this.imagen = imagen;
	}

	@Transient
	public int getCantidad() {
		return cantidad;
	}

	@Transient
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getPrecioCosto() {
		return precioCosto;
	}

	public void setPrecioCosto(int precioCosto) {
		this.precioCosto = precioCosto;
	}
}
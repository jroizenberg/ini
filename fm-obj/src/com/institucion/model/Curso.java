package com.institucion.model;

import java.io.Serializable;
import java.util.Set;

public class Curso   implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String descripcion;
	private int precio;
	private boolean pagaSubscripcion;
	private boolean esConCodigosDeBarras;
	private TipoCursoEnum idTipoCurso; 
	private VencimientoCursoEnum vencimiento;  
	private Set<ActividadYClase> actividadYClaseList;
	private boolean disponible;
	private Integer cantClasesPorSemana;

	
	public Curso() {}

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public boolean isPagaSubscripcion() {
		return pagaSubscripcion;
	}
	public void setPagaSubscripcion(boolean pagaSubscripcion) {
		this.pagaSubscripcion = pagaSubscripcion;
	}

	public Set<ActividadYClase> getActividadYClaseList() {
		return actividadYClaseList;
	}


	public void setActividadYClaseList(Set<ActividadYClase> actividadYClaseList) {
		this.actividadYClaseList = actividadYClaseList;
	}
	
	public TipoCursoEnum getIdTipoCurso() {
		return idTipoCurso;
	}


	public void setIdTipoCurso(TipoCursoEnum idTipoCurso) {
		this.idTipoCurso = idTipoCurso;
	}


	public VencimientoCursoEnum getVencimiento() {
		return vencimiento;
	}


	public void setVencimiento(VencimientoCursoEnum vencimiento) {
		this.vencimiento = vencimiento;
	}


	public boolean isDisponible() {
		return disponible;
	}


	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}


	public Integer getCantClasesPorSemana() {
		return cantClasesPorSemana;
	}


	public void setCantClasesPorSemana(Integer cantClasesPorSemana) {
		this.cantClasesPorSemana = cantClasesPorSemana;
	}


	public boolean isEsConCodigosDeBarras() {
		return esConCodigosDeBarras;
	}


	public void setEsConCodigosDeBarras(boolean esConCodigosDeBarras) {
		this.esConCodigosDeBarras = esConCodigosDeBarras;
	}
	

}
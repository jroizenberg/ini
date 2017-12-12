package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String apellido;
	private String dni;
	private Date fechaNacimiento;
	private String domicilio;
	private String telefonoFijo;
	private String celular;
	private String facebook;
	private String mail;
	private Set<Subscripcion> subscripcionesList;
	private Set<Inscripcion> inscripcionesList; // esto es realidad es la lista de matriculas abonadas
	private boolean entregoCertificado;
//	private ClienteEstadoEnum estado;
	private Long isEnClase;
//	private boolean estadoAdeudaAlgo;
//	private boolean estadoConClasesDisponibles;
	
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getTelefonoFijo() {
		return telefonoFijo;
	}
	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getFacebook() {
		return facebook;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public boolean isEntregoCertificado() {
		return entregoCertificado;
	}
	public void setEntregoCertificado(boolean entregoCertificado) {
		this.entregoCertificado = entregoCertificado;
	}	
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
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Set<Subscripcion> getSubscripcionesList() {
		return subscripcionesList;
	}
	public void setSubscripcionesList(Set<Subscripcion> subscripcionesList) {
		this.subscripcionesList = subscripcionesList;
	}
	public Set<Inscripcion> getInscripcionesList() {
		return inscripcionesList;
	}
	public void setInscripcionesList(Set<Inscripcion> inscripcionesList) {
		this.inscripcionesList = inscripcionesList;
	}
//	public ClienteEstadoEnum getEstado() {
//		return estado;
//	}
//	public void setEstado(ClienteEstadoEnum estado) {
//		this.estado = estado;
//	}
//	public boolean isEstadoAdeudaAlgo() {
//		return estadoAdeudaAlgo;
//	}
//	public void setEstadoAdeudaAlgo(boolean estadoAdeudaAlgo) {
//		this.estadoAdeudaAlgo = estadoAdeudaAlgo;
//	}
//	public boolean isEstadoConClasesDisponibles() {
//		return estadoConClasesDisponibles;
//	}
//	public void setEstadoConClasesDisponibles(boolean estadoConClasesDisponibles) {
//		this.estadoConClasesDisponibles = estadoConClasesDisponibles;
//	}
	//	public Set<Clase> getClasesList() {
//		return clasesList;
//	}
//	public void setClasesList(Set<Clase> clasesList) {
//		this.clasesList = clasesList;
//	}
	public Long getIsEnClase() {
		return isEnClase;
	}
	public void setIsEnClase(Long isEnClase) {
		this.isEnClase = isEnClase;
	}
	
}
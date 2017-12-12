package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class Clase  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String nombre;
	private String nombreAdic;

	private String descripcion;
	private Actividad actividad;

	private Date horaDesde;
	private Date horaHasta;	
	
	private Boolean lunes;
	private Boolean martes;
	private Boolean miercoles;
	private Boolean jueves;
	private Boolean viernes;
	private Boolean sabado;
	private Boolean domingo;
	private Long cantAlumnos;	
	private Long cantAlumnosPorFecha;
	private boolean disponible;

	private Boolean esClaseSinHora;

//	private Long cantAlumnosHistorico;	
//	private Long cantAlumnosPorFechaHistorico;
	private Set<ClaseConListaAlumnos> claseConListaAlumnosList;

	
	private Set<Cliente> clienesEnClase;
	private Set<IngresoAClasesSinFechasAlumnos> ingresoAClaseSinFechas;

	
	private Set<ClaseConListaAlumnosHistorico> claseConListaAlumnosHistoryList;
//	private Set<Cliente> clienesEnClaseHistory;

	public Clase(){}
	public Clase(Long id, String nombre,Date horaDesde,Date horaHasta){
		this.id= id;
		this.nombre=nombre;
		this.horaDesde=horaDesde;
		this.horaHasta= horaHasta;
	}
	
	public Clase(Long id, String nombre,Date horaDesde,Date horaHasta, Boolean lunes, Boolean martes, Boolean miercoles, Boolean jueves, 
			Boolean viernes, Boolean sabado, Boolean domingo){
		this.id= id;
		this.nombre=nombre;
		this.horaDesde=horaDesde;
		this.horaHasta= horaHasta;
		this.lunes= lunes;
		this.martes= martes;
		this.miercoles= miercoles;
		this.jueves= jueves;
		this.viernes= viernes;
		this.sabado= sabado;
		this.domingo= domingo;
		
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

	public Long getCantAlumnos() {
		return cantAlumnos;
	}
	public void setCantAlumnos(Long cantAlumnos) {
		this.cantAlumnos = cantAlumnos;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombreAdic() {
		return nombreAdic;
	}
	public void setNombreAdic(String nombreAdic) {
		this.nombreAdic = nombreAdic;
	}
	public Set<Cliente> getClienesEnClase() {
		return clienesEnClase;
	}
	public void setClienesEnClase(Set<Cliente> clienesEnClase) {
		this.clienesEnClase = clienesEnClase;
	}

	public Boolean getLunes() {
		return lunes;
	}
	public void setLunes(Boolean lunes) {
		this.lunes = lunes;
	}
	public Boolean getMartes() {
		return martes;
	}
	public void setMartes(Boolean martes) {
		this.martes = martes;
	}
	public Boolean getMiercoles() {
		return miercoles;
	}
	public void setMiercoles(Boolean miercoles) {
		this.miercoles = miercoles;
	}
	public Boolean getJueves() {
		return jueves;
	}
	public void setJueves(Boolean jueves) {
		this.jueves = jueves;
	}
	public Boolean getViernes() {
		return viernes;
	}
	public void setViernes(Boolean viernes) {
		this.viernes = viernes;
	}
	public Boolean getSabado() {
		return sabado;
	}
	public void setSabado(Boolean sabado) {
		this.sabado = sabado;
	}
	public Boolean getDomingo() {
		return domingo;
	}
	public void setDomingo(Boolean domingo) {
		this.domingo = domingo;
	}
//	public Long getCantAlumnosHistorico() {
//		return cantAlumnosHistorico;
//	}
//	public void setCantAlumnosHistorico(Long cantAlumnosHistorico) {
//		this.cantAlumnosHistorico = cantAlumnosHistorico;
//	}
//	public Long getCantAlumnosPorFechaHistorico() {
//		return cantAlumnosPorFechaHistorico;
//	}
//	public void setCantAlumnosPorFechaHistorico(Long cantAlumnosPorFechaHistorico) {
//		this.cantAlumnosPorFechaHistorico = cantAlumnosPorFechaHistorico;
//	}

	public Date getHoraDesde() {
		return horaDesde;
	}
	public void setHoraDesde(Date horaDesde) {
		this.horaDesde = horaDesde;
	}
	public Date getHoraHasta() {
		return horaHasta;
	}
	public void setHoraHasta(Date horaHasta) {
		this.horaHasta = horaHasta;
	}
	public Actividad getActividad() {
		return actividad;
	}
	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}
	public Boolean getEsClaseSinHora() {
		return esClaseSinHora;
	}
	public Set<IngresoAClasesSinFechasAlumnos> getIngresoAClaseSinFechas() {
		return ingresoAClaseSinFechas;
	}
	public void setIngresoAClaseSinFechas(
			Set<IngresoAClasesSinFechasAlumnos> ingresoAClaseSinFechas) {
		this.ingresoAClaseSinFechas = ingresoAClaseSinFechas;
	}
	public void setEsClaseSinHora(Boolean esClaseSinHora) {
		this.esClaseSinHora = esClaseSinHora;
	}
	public Set<ClaseConListaAlumnos> getClaseConListaAlumnosList() {
		return claseConListaAlumnosList;
	}
	public void setClaseConListaAlumnosList(
			Set<ClaseConListaAlumnos> claseConListaAlumnosList) {
		this.claseConListaAlumnosList = claseConListaAlumnosList;
	}
	public Long getCantAlumnosPorFecha() {
		return cantAlumnosPorFecha;
	}
	public void setCantAlumnosPorFecha(Long cantAlumnosPorFecha) {
		this.cantAlumnosPorFecha = cantAlumnosPorFecha;
	}
	public Set<ClaseConListaAlumnosHistorico> getClaseConListaAlumnosHistoryList() {
		return claseConListaAlumnosHistoryList;
	}
	public void setClaseConListaAlumnosHistoryList(
			Set<ClaseConListaAlumnosHistorico> claseConListaAlumnosHistoryList) {
		this.claseConListaAlumnosHistoryList = claseConListaAlumnosHistoryList;
	}

	public boolean isDisponible() {
		return disponible;
	}


	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

//	public Set<Cliente> getClienesEnClaseHistory() {
//		return clienesEnClaseHistory;
//	}
//	public void setClienesEnClaseHistory(Set<Cliente> clienesEnClaseHistory) {
//		this.clienesEnClaseHistory = clienesEnClaseHistory;
//	}
	
}
package com.institucion.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class ClaseConListaAlumnosHistorico  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Clase clase;
	private String profeNombre;
	private Date fecha;
	private String comentarioGeneralDelProfe;
	private Integer cantPresentes;
	private Integer cantPresentesNoEncontrados;

	private Set<ClienteNoEncontradoEnPiletaHistorico> clienesNoEncontradosEnPiletaList;
	private Set<ClienteListaEncontradoEnPiletaHistorico> clienesListaEncontradosEnPiletaList;

	public ClaseConListaAlumnosHistorico(){
		
	}
	
	public ClaseConListaAlumnosHistorico(String profeNombre, String comentarioGeneralDelProfe, Integer cantPresentes, Integer cantPresentesNoEncontrados){
		this.profeNombre= profeNombre;
		this.comentarioGeneralDelProfe= comentarioGeneralDelProfe;
		this.cantPresentes= cantPresentes;
		this.cantPresentesNoEncontrados= cantPresentesNoEncontrados;
		
	}
	
	public ClaseConListaAlumnosHistorico(Long id, String profeNombre, String comentarioGeneralDelProfe, Integer cantPresentes, Integer cantPresentesNoEncontrados){
		this.id= id;
		this.profeNombre= profeNombre;
		this.comentarioGeneralDelProfe= comentarioGeneralDelProfe;
		this.cantPresentes= cantPresentes;
		this.cantPresentesNoEncontrados= cantPresentesNoEncontrados;
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Clase getClase() {
		return clase;
	}
	public void setClase(Clase clase) {
		this.clase = clase;
	}
	public String getProfeNombre() {
		return profeNombre;
	}
	public void setProfeNombre(String profeNombre) {
		this.profeNombre = profeNombre;
	}
	public String getComentarioGeneralDelProfe() {
		return comentarioGeneralDelProfe;
	}
	public void setComentarioGeneralDelProfe(String comentarioGeneralDelProfe) {
		this.comentarioGeneralDelProfe = comentarioGeneralDelProfe;
	}
//	public Integer getCantAlumnos() {
//		return cantAlumnos;
//	}
//	public void setCantAlumnos(Integer cantAlumnos) {
//		this.cantAlumnos = cantAlumnos;
//	}
	public Set<ClienteNoEncontradoEnPiletaHistorico> getClienesNoEncontradosEnPiletaList() {
		return clienesNoEncontradosEnPiletaList;
	}
	public void setClienesNoEncontradosEnPiletaList(
			Set<ClienteNoEncontradoEnPiletaHistorico> clienesNoEncontradosEnPiletaList) {
		this.clienesNoEncontradosEnPiletaList = clienesNoEncontradosEnPiletaList;
	}
	public Set<ClienteListaEncontradoEnPiletaHistorico> getClienesListaEncontradosEnPiletaList() {
		return clienesListaEncontradosEnPiletaList;
	}
	public void setClienesListaEncontradosEnPiletaList(
			Set<ClienteListaEncontradoEnPiletaHistorico> clienesListaEncontradosEnPiletaList) {
		this.clienesListaEncontradosEnPiletaList = clienesListaEncontradosEnPiletaList;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Integer getCantPresentes() {
		return cantPresentes;
	}

	public void setCantPresentes(Integer cantPresentes) {
		this.cantPresentes = cantPresentes;
	}

	public Integer getCantPresentesNoEncontrados() {
		return cantPresentesNoEncontrados;
	}

	public void setCantPresentesNoEncontrados(Integer cantPresentesNoEncontrados) {
		this.cantPresentesNoEncontrados = cantPresentesNoEncontrados;
	}

}
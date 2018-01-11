package com.institucion.model;

import java.io.Serializable;


public class SubsYCurso  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Long idCurso;
	private Long idSubscripcion;
	
	public SubsYCurso() {}

	public Long getIdCurso() {
		return idCurso;
	}

	public void setIdCurso(Long idCurso) {
		this.idCurso = idCurso;
	}

	public Long getIdSubscripcion() {
		return idSubscripcion;
	}

	public void setIdSubscripcion(Long idSubscripcion) {
		this.idSubscripcion = idSubscripcion;
	}

}
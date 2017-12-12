package com.institucion.desktop.delegated;

import com.institucion.model.Concepto;
import com.institucion.model.ObraSocial;



public interface CursosSeleccionadosDelegate {
	
	public void  actualizarPreciosPorModificacionEnCumples(int precio, Integer cursoID);
	public void  quitarConceptoDeLaLista(int cursoID);
	public boolean actualizarCursoSegunDescuento(ObraSocial oc, String type, Concepto concepto);

}

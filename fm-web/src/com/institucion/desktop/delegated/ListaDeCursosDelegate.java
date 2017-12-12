package com.institucion.desktop.delegated;

import org.zkoss.zk.ui.event.Event;


public interface ListaDeCursosDelegate {
	
//	public void buscar(Event evt, boolean isFromCodigoBarras);
	public boolean agregarNuevoCursoAConceptoEvento(Event evt, int idCurso);
	public boolean quitarNuevoCursoAConceptoEvento(Event evt, int idCurso);
}

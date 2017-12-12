package com.institucion.desktop.delegated;

import com.institucion.model.VencimientoCursoEnum;


public interface CursosDelegate {
	
	public void actualizarTipoCurso();
	public VencimientoCursoEnum getTipoCursoEnum();
	public void muestraLasSubscripciones();
	public void setearClasesCantidades(boolean esLibre, boolean clear);
	public void setearClasesCantidadesSemanales(boolean clear);
	public void onCheckFiltersClasesDoing(boolean check, boolean esCheckTodosLosDias);
	public void setearPrimeros10DiasDeClasesCantidades(VencimientoCursoEnum vencimientoEnum, boolean clear);
	public void buscarSubscripcionesDesdeFiltro();
}

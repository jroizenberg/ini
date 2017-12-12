package com.institucion.desktop.delegated;

import java.util.List;

import com.institucion.model.Actividad;
import com.institucion.model.Quincena;
import com.institucion.model.TipoDescuentosEnum;


public interface SubscripcionDelegate {
	public void asignarClase();

	public void guardarCambios();
	public void eliminarCurso();
	public void pagarCurso();
	public int getValorCurso();
	public boolean seSeleccionoCurso();
	
	public void actualizarAdicionalyImporteTotal();
	public boolean esModificacion();

	public Actividad getActividadDelCursoSeleccionado();
	public boolean agregarCurso(boolean isFromContinuar, Integer valorFinalCurso);

	public void volverAlTabPrincipalYCancelar();
	
	public void continuarAgregandoCumpleanios();

	public int getValorOriginalCurso();
	public void continuarAgregandoCursoPorClase();

	public List<Quincena> obtenerQuincenalesList();


}

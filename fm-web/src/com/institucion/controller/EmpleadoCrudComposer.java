package com.institucion.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CursoEJB;
import com.institucion.bz.EmpleadoEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Actividad;
import com.institucion.model.ActividadesDeEmpleadosList;
import com.institucion.model.Empleado;
import com.institucion.model.EmpleadoActividades;
import com.institucion.model.EmpleadoCrud;

public class EmpleadoCrudComposer extends CrudComposer {

	public final String idClase = "idEmpleado";
	private Empleado empleado;
	private ActividadesDeEmpleadosList preciosListGrid;
	private CursoEJB cursoEJB;
	private EmpleadoEJB obraSocialEJB;

	private PanelCrud crud;	
	
	public EmpleadoCrudComposer() {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		obraSocialEJB = BeanFactory.<EmpleadoEJB>getObject("fmEjbEmpleado");

	}

	private EmpleadoCrud getClaseCrud() {
		return (EmpleadoCrud) (crud.getGridCrud());
	}


	public void onCreate() {
		Empleado clase= (Empleado) Session.getAttribute(idClase);
		setEmpleado(clase);
		this.fromModelToView();
	}
	
	private ArrayList<EmpleadoActividades> cargarActividadesEnCrud(List<Actividad> act, Empleado lista){
		ArrayList<EmpleadoActividades> obrasSoprecios= new ArrayList();
		
		if(lista == null){
			// creo para todas las actividades
			for (Actividad actividad : act) {
				EmpleadoActividades osP = new EmpleadoActividades();
				osP.setActividad(actividad);
//				osP.setPrecioQuePagaElCliente(0);
//				osP.setSePagaUnaUnicaVez(false);
//				osP.setPrecioQuePagaLaObraSocial(new Double(0));
				obrasSoprecios.add(osP);
			}
			
		}else{
			// Creo solo si hace falta
			for (Actividad actividad : act) {
				
				EmpleadoActividades precio=existeActividad(actividad, lista);
				if(precio != null){
					obrasSoprecios.add(precio);
					
				}else{
					EmpleadoActividades osP = new EmpleadoActividades();
					osP.setActividad(actividad);
//					osP.setPrecioQuePagaElCliente(0);
//					osP.setPrecioQuePagaLaObraSocial(new Double(0));
//					osP.setSePagaUnaUnicaVez(false);

					obrasSoprecios.add(osP);	
				}
			}
		}
		return obrasSoprecios;
	}
	
	private EmpleadoActividades existeActividad(Actividad act,Empleado lista){
		if(lista != null && lista.getActividades() != null){
			
			for (EmpleadoActividades iterable_element : lista.getActividades()) {
				if(iterable_element.getActividad() != null 
						&& iterable_element.getActividad().getId() != null &&
						iterable_element.getActividad().getId().intValue() == act.getId().intValue())
					return iterable_element;
			}
		}
		return null;
	}
	
	private void fromModelToView() {
		List<Actividad> listaActQueDebenEstarEnLista=cursoEJB.findAllActividadParaPagoSueldos();
		
		if(empleado != null){
			
			if(empleado.getNombre() != null){
				getClaseCrud().getNombre().setValue(empleado.getNombre().toUpperCase());
			}
			
			ArrayList<EmpleadoActividades> precios =cargarActividadesEnCrud(listaActQueDebenEstarEnLista, empleado);
			empleado.setActividades(new HashSet(precios));
			preciosListGrid.setField(precios);

		}else{
			// dejo el nombre vacio pero creo las actividades igual
			ArrayList<EmpleadoActividades> precios =cargarActividadesEnCrud(listaActQueDebenEstarEnLista, empleado);
			Empleado ob= new Empleado();
			ob.setActividades(new HashSet(precios));
			empleado = ob;
			preciosListGrid.setField(precios);

		}		
	}
	

	
	public void onSave(Event event) throws Exception {
		try{
			if(validateCrud()){
				this.fromViewToModel(empleado);
				
				obraSocialEJB.save(empleado);

				super.gotoPage("/institucion/empleado-selector.zul");
				
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
			super.gotoPage("/institucion/empleado-selector.zul");
	}

	private Empleado fromViewToModel(Empleado cliente) {
		
		if(getClaseCrud().getNombre().getValue() != null){
			cliente.setNombre(getClaseCrud().getNombre().getValue().toUpperCase());	
		}
	
		List<EmpleadoActividades> listasprecios=preciosListGrid.getListActividadesSueldosActualizada();
		cliente.setActividades(new HashSet(listasprecios));
		return cliente;
	}

	private boolean validateCrud() {

		if(getClaseCrud().getNombre().getValue()  == null || 
				(getClaseCrud().getNombre().getValue() != null && getClaseCrud().getNombre().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClaseCrud().getNombre(), I18N.getLabel("error.empty.field"));
		
		return true;
	}
	

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado obraSocial) {
		this.empleado = obraSocial;
	}

}

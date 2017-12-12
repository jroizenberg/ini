package com.institucion.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CursoEJB;
import com.institucion.bz.ObraSocialEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Actividad;
import com.institucion.model.ObraSocial;
import com.institucion.model.ObraSocialCrud;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.PreciosObraSocialList;

public class ObraSocialCrudComposer extends CrudComposer {

	public final String idClase = "idObraSocial";
	private ObraSocial obraSocial;
	private PreciosObraSocialList preciosListGrid;
	private CursoEJB cursoEJB;
	private ObraSocialEJB obraSocialEJB;

	private PanelCrud crud;	
	
	public ObraSocialCrudComposer() {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		obraSocialEJB = BeanFactory.<ObraSocialEJB>getObject("fmEjbObraSocial");

	}

	private ObraSocialCrud getClaseCrud() {
		return (ObraSocialCrud) (crud.getGridCrud());
	}


	public void onCreate() {
		ObraSocial clase= (ObraSocial) Session.getAttribute(idClase);
		setObraSocial(clase);
		this.fromModelToView();
	}
	
	private ArrayList<ObraSocialesPrecio> cargarActividadesEnCrud(List<Actividad> act, ObraSocial lista){
		ArrayList<ObraSocialesPrecio> obrasSoprecios= new ArrayList();
		
		if(lista == null){
			// creo para todas las actividades
			for (Actividad actividad : act) {
				ObraSocialesPrecio osP = new ObraSocialesPrecio();
				osP.setActividad(actividad);
				osP.setPrecioQuePagaElCliente(0);
				osP.setSePagaUnaUnicaVez(false);
				osP.setPrecioQuePagaLaObraSocial(new Double(0));
				obrasSoprecios.add(osP);
			}
			
		}else{
			// Creo solo si hace falta
			for (Actividad actividad : act) {
				
				ObraSocialesPrecio precio=existeActividad(actividad, lista);
				if(precio != null){
					obrasSoprecios.add(precio);
					
				}else{
					ObraSocialesPrecio osP = new ObraSocialesPrecio();
					osP.setActividad(actividad);
					osP.setPrecioQuePagaElCliente(0);
					osP.setPrecioQuePagaLaObraSocial(new Double(0));
					osP.setSePagaUnaUnicaVez(false);

					obrasSoprecios.add(osP);	
				}
			}
		}
		return obrasSoprecios;
	}
	
	private ObraSocialesPrecio existeActividad(Actividad act,ObraSocial lista){
		if(lista != null && lista.getPreciosActividadesObraSocial() != null){
			
			for (ObraSocialesPrecio iterable_element : lista.getPreciosActividadesObraSocial()) {
				if(iterable_element.getActividad() != null 
						&& iterable_element.getActividad().getId() != null &&
						iterable_element.getActividad().getId().intValue() == act.getId().intValue())
					return iterable_element;
			}
		}
		return null;
	}
	
	private void fromModelToView() {
		List<Actividad> listaActQueDebenEstarEnLista=cursoEJB.findAllActividadQueConObraSocial();
		
		if(obraSocial != null){
			
			if(obraSocial.getNombre() != null){
				getClaseCrud().getNombre().setValue(obraSocial.getNombre().toUpperCase());
			}
			
			ArrayList<ObraSocialesPrecio> precios =cargarActividadesEnCrud(listaActQueDebenEstarEnLista, obraSocial);
			obraSocial.setPreciosActividadesObraSocial(new HashSet(precios));
			preciosListGrid.setField(precios);

		}else{
			// dejo el nombre vacio pero creo las actividades igual
			ArrayList<ObraSocialesPrecio> precios =cargarActividadesEnCrud(listaActQueDebenEstarEnLista, obraSocial);
			ObraSocial ob= new ObraSocial();
			ob.setPreciosActividadesObraSocial(new HashSet(precios));
			obraSocial = ob;
			preciosListGrid.setField(precios);

		}		
	}
	

	
	public void onSave(Event event) throws Exception {
		try{
			if(validateCrud()){
				this.fromViewToModel(obraSocial);
				
				obraSocialEJB.save(obraSocial);

				super.gotoPage("/institucion/obra-social-selector.zul");
				
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
			super.gotoPage("/institucion/obra-social-selector.zul");
	}

	private ObraSocial fromViewToModel(ObraSocial cliente) {
		
		if(getClaseCrud().getNombre().getValue() != null){
			cliente.setNombre(getClaseCrud().getNombre().getValue().toUpperCase());	
		}
	
		List<ObraSocialesPrecio> listasprecios=preciosListGrid.getListObraSocialesPreciosSelectedConCantidadActualizada();
		cliente.setPreciosActividadesObraSocial(new HashSet(listasprecios));
		return cliente;
	}

	private boolean validateCrud() {

		if(getClaseCrud().getNombre().getValue()  == null || 
				(getClaseCrud().getNombre().getValue() != null && getClaseCrud().getNombre().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getClaseCrud().getNombre(), I18N.getLabel("error.empty.field"));
	
		List<ObraSocialesPrecio> listasprecios=preciosListGrid.getListObraSocialesPreciosSelectedConCantidadActualizada();
		if(listasprecios != null && listasprecios.size() >0){
			
			for (ObraSocialesPrecio obraSocialesPrecio : listasprecios) {
				
				if(obraSocialesPrecio.getPrecioQuePagaElCliente()< 0 || obraSocialesPrecio.getPrecioQuePagaLaObraSocial()< 0){
					MessageBox.info("El valor de los precios que paga el cliente y obra social deben ser mayores a 0", "No se puede continuar");
					return false ;	
				}
						
			}
			
		}		
		return true;
	}
	

	public ObraSocial getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
	}

}

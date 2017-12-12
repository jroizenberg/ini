package com.institucion.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import com.institucion.bz.ObraSocialEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.ObraSocial;
import com.institucion.model.ObraSocialFilter;
import com.institucion.model.ObraSocialesList;

public class ObraSocialSelectorComposer extends SelectorFEComposer implements CursosCrudDelegate{
	
	private ObraSocialesList clasepanelListGrid;
	private List<ObraSocial> pharmacyList;
	private PanelFilter filter;
	private ObraSocialEJB claseEJB;

	public ObraSocialSelectorComposer(){
		super();
		claseEJB = BeanFactory.<ObraSocialEJB>getObject("fmEjbObraSocial");
	}

	private ObraSocialFilter getFilter() {
		return (ObraSocialFilter)(filter.getGridFilter());
	}
	
	public void clear(){
		
		getFilter().clear();
	}
	
	public void onCreate() {
		getFilter().setDelegate(this);
		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
			setCallFromMenu(false);			
		}
		onFind();	
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();

	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
		this.onUpdate(null);
	}
	
	private void refreshEvents(){
		
		getFilter().getNombre().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	
	}

	public void onUpdate(Event event) throws Exception{
		
		if (hasSelectedOneItem(clasepanelListGrid)){
			Sessions.getCurrent().setAttribute("idObraSocial", clasepanelListGrid.getSelectedItem().getValue());		
			super.saveHistory("filter");
			super.gotoPage("/institucion/obra-social-crud.zul");		
		}
	}

	public void onDelete(Event event) {  	}

	public void onInsert(Event event) throws Exception {
	
		com.institucion.fm.conf.Session.setAttribute("idObraSocial", null);
		super.saveHistory("filter");
		super.gotoPage("/institucion/obra-social-crud.zul");
	

	}

	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<ObraSocial>();
		
		clasepanelListGrid.setDisabled(false);

		if(getFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
			pharmacyList =claseEJB.findAllConJdbc(getFilter().getFilters());	
			
		}else{
			pharmacyList =claseEJB.findAll();	
		}
		
		clasepanelListGrid.setList(pharmacyList);
			
		log.info("Fin Creando listado de farmacia en la version modificada");
	
	}
	
	public void onClearFilter(Event evt){
		
		getFilter().clear();
		if (isCallFromMenu()){
			getFilter().clear();
		}else{
			this.onFind();
		}		
	}

	@Override
	public void buscar() {
		onFind();
	}
}
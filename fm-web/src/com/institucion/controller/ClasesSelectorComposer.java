package com.institucion.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import com.institucion.bz.ClaseEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.Clase;
import com.institucion.model.ClaseList;
import com.institucion.model.ClasesFilter;

public class ClasesSelectorComposer extends SelectorFEComposer implements CursosCrudDelegate{
	
	private ClaseList clasepanelListGrid;
	private List<Clase> pharmacyList;
	private PanelFilter filter;

	private ClaseEJB claseEJB;

	public ClasesSelectorComposer(){
		super();
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
	}

	private ClasesFilter getFilter() {
		return (ClasesFilter)(filter.getGridFilter());
	}
	public void onCreate() {
		getFilter().setDelegate(this);
//		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
//		setCallFromMenu(false);
//		clear();
		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
			setCallFromMenu(false);			
		}
		onFind();	
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();
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
			Sessions.getCurrent().setAttribute("idClase", clasepanelListGrid.getSelectedItem().getValue());		
			Sessions.getCurrent().setAttribute("position", clasepanelListGrid);
			Sessions.getCurrent().setAttribute("key", pharmacyList);
			Sessions.getCurrent().setAttribute("max", pharmacyList.size());
			super.saveHistory("filter");
			super.gotoPage("/institucion/clase-crud.zul");		
		}
		
	}

	public void onDelete(Event event) {
//		if (hasSelectedItem(clasepanelListGrid)){
//			if (MessageBox.question(I18N.getLabel("selector.delete.question"), I18N.getLabel("selector.delete.title","Clases"))){
//				for (Object iterable_element : clasepanelListGrid.getSelectedItems()) {
//					Clase curso=(Clase)((Listitem)iterable_element).getValue();
//					this.claseEJB.delete( curso);	
//				}
//				onFind();		
//
//			}	
//		}
	}

	public void onInsert(Event event) throws Exception {
	
		com.institucion.fm.conf.Session.setAttribute("idClase", null);
		Sessions.getCurrent().setAttribute("max", 0);
		super.saveHistory("filter");
		super.gotoPage("/institucion/clase-crud.zul");
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
//		if (this.getManager().havePermission(PermissionTxManager.TX_PHARMACY_MODIFY)) {
			this.onUpdate(null);
//		}
	}


	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Clase>();
		
//		if(getFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		pharmacyList =claseEJB.findAllConJdbc(getFilter().getFilters());	
			
//		}else{
//			pharmacyList =claseEJB.findAll();	
//		}
		
		clasepanelListGrid.setList(pharmacyList, null);
			
		log.info("Fin Creando listado de farmacia en la version modificada");
	
	}
	
	
	
	public void clear(){
		getFilter().clear();
//		getFilter().clear();

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
package com.institucion.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import com.institucion.bz.ProductoEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.model.Producto;
import com.institucion.model.ProductoListPrueba;
import com.institucion.model.ProductosFilter;

public class ProductosSelectorComposer extends SelectorFEComposer implements CursosCrudDelegate{
	
	private List<Producto> pharmacyList;

	private ProductoEJB productoEJB;
	private PanelFilter filter;
	private PanelList curso2;

	public ProductosSelectorComposer(){
		super();
		productoEJB = BeanFactory.<ProductoEJB>getObject("fmEjbProducto");
	}

	private ProductoListPrueba  getList(){
		return (ProductoListPrueba)curso2.getGridList();
	}

	private ProductosFilter getFilter() {
		return (ProductosFilter)(filter.getGridFilter());
	}
	
	public void onCreate() {

		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		getFilter().setDelegate(this);
		clear();
		onFind();		
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();
	}
	
	private void refreshEvents(){
		
		getFilter().getCodigo().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	

		getFilter().getDescripcion().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	
	}
	
	public void onUpdate(Event event) throws Exception{
		if(getList().getSelectedItem() == null){
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
		}else{
			Producto prod = (Producto) getList().getSelectedItem().getValue();
			if (prod != null){
				Sessions.getCurrent().setAttribute("idProducto", prod);		
//				Sessions.getCurrent().setAttribute("position", getList());
				super.saveHistory();
				super.gotoPage("/institucion/productos-crud.zul");		
			}

		}
	}

	
	public void onInsertClass(Event event) throws Exception{
		super.gotoPage("/institucion/productos-selector.zul");				
	}
	
	
	public void onDelete(Event event) {

	}

	public void onInsert(Event event) throws Exception {
	
		com.institucion.fm.conf.Session.setAttribute("idProducto", null);
		Sessions.getCurrent().setAttribute("max", 0);
		super.saveHistory();
		super.gotoPage("/institucion/productos-crud.zul");
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
		this.onUpdate(null);
	}


	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	

	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Producto>();
		
		if(getFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
			pharmacyList =productoEJB.findAllConJdbc(getFilter().getFilters());	
			
		}else{
			pharmacyList =productoEJB.findAllConJdbc(getFilter().getFilters2());	
		}
		getList().setList(pharmacyList);

	}
	
	
	public void clear(){
		getFilter().clear();
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
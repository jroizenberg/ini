package com.institucion.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.GastosEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.GastosMaipu;
import com.institucion.model.GastosMaipuFilter;
import com.institucion.model.GastosMaipuList;

public class GastosMaipuSelectorComposer extends SelectorFEComposer{
	
	private GastosMaipuList clientepanelListGrid;
	private List<GastosMaipu> pharmacyList;
	private PanelFilter filter;
	private GastosEJB gastosEJB;

	public GastosMaipuSelectorComposer(){
		super();
		gastosEJB = BeanFactory.<GastosEJB>getObject("fmEjbGastos");
	}

	private GastosMaipuFilter getClientFilter() {
		return (GastosMaipuFilter)(filter.getGridFilter());
	}
	
	public void clear(){
		getClientFilter().clear();
	}
	
	
	public void onCreate() {

		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		clear();
		getClientFilter().getAnio().setSelectedIndex(5);
//		getClientFilter().getQuincena().setSelectedIndex(new Date().getMonth());
		getClientFilter().getTipoGastoCB().setSelectedIndex(0);

		onFind();
	}

	public void onUpdate(Event event) throws Exception{
		if (hasSelectedOneItem(clientepanelListGrid)){
			Sessions.getCurrent().setAttribute("idGasto", clientepanelListGrid.getSelectedItem().getValue());		
			super.saveHistory();
			super.gotoPage("/institucion/gastosMaipu-crud.zul");		
		}
	}

	public void onDelete(Event event) {
		// No se eliminan clientes

	}

	public void onInsert(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		super.saveHistory();
		super.gotoPage("/institucion/gastosMaipu-crud.zul");
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
		pharmacyList= new ArrayList<GastosMaipu>();
		
		if(getClientFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
			pharmacyList =gastosEJB.findAllConJdbcMaipu(getClientFilter().getFilters());	
			
		}else{
			pharmacyList =gastosEJB.findAllMaipu();	
		}
		clientepanelListGrid.setList(pharmacyList);
	}
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();
		if (isCallFromMenu()){
			getClientFilter().clear();
		}else{
			this.onFind();
		}		
	}

}
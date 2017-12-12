package com.institucion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.GastosEJB;
import com.institucion.desktop.delegated.SucursalDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.Gastos;
import com.institucion.model.GastosFilter;
import com.institucion.model.GastosList;
import com.institucion.model.GastosMaipu;
import com.institucion.model.SucursalEnum;

public class GastosSelectorComposer extends SelectorFEComposer implements SucursalDelegate{
	
	private GastosList clientepanelListGrid;
	private List<Gastos> pharmacyList;
	private List<GastosMaipu> pharmacyListMaipu;
	private PanelFilter filter;
	private GastosEJB gastosEJB;

	public GastosSelectorComposer(){
		super();
		gastosEJB = BeanFactory.<GastosEJB>getObject("fmEjbGastos");
	}

	private GastosFilter getClientFilter() {
		return (GastosFilter)(filter.getGridFilter());
	}
	
	public void clear(){
		getClientFilter().clear();
	}
	
	@Override
	public void actualizarLitado() {
		onFind();
	}
	
	public void onCreate() {
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("¡Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		getClientFilter().setDelegate(this);
		clear();
				
		getClientFilter().actualizarTodoTemaSucursales();
		
		getClientFilter().getAnio().setSelectedIndex(5);
		
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if( (suc.equals(SucursalEnum.CENTRO))){
					if(getClientFilter().getMes() != null && getClientFilter().getMes().getItems().size() >0 )
						getClientFilter().getMes().setSelectedIndex(new Date().getMonth());
				}else{
					if(getClientFilter().getQuincena() != null && getClientFilter().getQuincena().getItems().size() >0 ){
						// enero =0
						int mes=new Date().getMonth();
						int dia	=new Date().getDay();
						Integer selectedItem= null;
						if(mes == 11){  // dic
							if(dia < 15)
								selectedItem=1;
							else
								selectedItem=2;
						}
							 
						if(mes == 0){  // enero
							if(dia < 15)
								selectedItem=3;
							else
								selectedItem=4;							
						}else if(mes == 1){ // feb
							if(dia < 15)
								selectedItem=5;
							else
								selectedItem=6;
						}
						if(selectedItem == null)
							selectedItem= -1;
						getClientFilter().getQuincena().setSelectedIndex(selectedItem);
					}
				}
				
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){

				if(getClientFilter().getMes() != null && getClientFilter().getMes().getItems().size() >0 )
					getClientFilter().getMes().setSelectedIndex(new Date().getMonth());
				
				if(getClientFilter().getQuincena() != null && getClientFilter().getQuincena().getItems().size() >0 ){
					// enero =0
					int mes=new Date().getMonth();
					int dia	=new Date().getDay();
					Integer selectedItem= null;
					if(mes == 11){  // dic
						if(dia < 15)
							selectedItem=1;
						else
							selectedItem=2;
					}
						 
					if(mes == 0){  // enero
						if(dia < 15)
							selectedItem=3;
						else
							selectedItem=4;							
					}else if(mes == 1){ // feb
						if(dia < 15)
							selectedItem=5;
						else
							selectedItem=6;
					}
					if(selectedItem == null)
						selectedItem= -1;
					getClientFilter().getQuincena().setSelectedIndex(selectedItem);
				}
			}
		}
		
		if(getClientFilter().getTipoGastoCB() != null && getClientFilter().getTipoGastoCB().getItems().size() >0 )
			getClientFilter().getTipoGastoCB().setSelectedIndex(0);

		onFind();
	}

	public void onUpdate(Event event) throws Exception{
		if (hasSelectedOneItem(clientepanelListGrid)){
			Sessions.getCurrent().setAttribute("idGasto", clientepanelListGrid.getSelectedItem().getValue());		
			super.saveHistory();
			super.gotoPage("/institucion/gastos-crud.zul");		
		}
	}

	public void onDelete(Event event) {
		// No se eliminan clientes

	}

	public void onInsert(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		super.saveHistory();
		super.gotoPage("/institucion/gastos-crud.zul");
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
		pharmacyList= new ArrayList<Gastos>();
		pharmacyListMaipu= new ArrayList<GastosMaipu>();
		
		if (getClientFilter().getSucursal().getSelectedItem() != null 
				&& getClientFilter().getSucursal().getSelectedItem().getValue() instanceof SucursalEnum) {
		
			if ( ((SucursalEnum)getClientFilter().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt()  ){
				pharmacyList =gastosEJB.findAllConJdbc(getClientFilter().getFilters());	
			}else{
				// es maipu
				pharmacyListMaipu =gastosEJB.findAllConJdbcMaipu(getClientFilter().getFilters());	
			}
			
			
		}else if ((getClientFilter().getSucursal().getSelectedItem() != null 
				&& getClientFilter().getSucursal().getSelectedItem().getValue() instanceof String) 
				|| (getClientFilter().getSucursal().getSelectedItem() == null)) {
				pharmacyList =gastosEJB.findAllConJdbc(getClientFilter().getFiltersTODASCentro());
				
				boolean mostrarQuincena=true;
				if(getClientFilter().getQuincena() != null && getClientFilter().getQuincena().getItems().size() >0 ){
					// enero =0
					int mes=new Date().getMonth();
					int dia	=new Date().getDay();
					Integer selectedItem= null;
					if(mes == 11){  // dic
						if(dia < 15)
							selectedItem=1;
						else
							selectedItem=2;
					}
						 
					if(mes == 0){  // enero
						if(dia < 15)
							selectedItem=3;
						else
							selectedItem=4;							
					}else if(mes == 1){ // feb
						if(dia < 15)
							selectedItem=5;
						else
							selectedItem=6;
					}
					if(selectedItem == null)
						mostrarQuincena= false;
				}

				
				pharmacyListMaipu =gastosEJB.findAllConJdbcMaipu(getClientFilter().getFiltersTODASMaipu(mostrarQuincena));	
		}
		clientepanelListGrid.setList(pharmacyList, pharmacyListMaipu);
	}
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();
		if (isCallFromMenu()){
			getClientFilter().clear();
		}else{
			this.onFind();
		}		
	}

	@Override
	public void actualizarPorSucursal(SucursalEnum suc) {
		// TODO Auto-generated method stub
		
	}

}
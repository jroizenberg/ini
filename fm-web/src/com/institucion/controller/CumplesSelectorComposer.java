package com.institucion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.Cliente;
import com.institucion.model.CumplesFilter;
import com.institucion.model.CumplesList;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Subscripcion;

public class CumplesSelectorComposer extends SelectorFEComposer  implements ClienteDelegate{
	
	private CumplesList clasepanelListGrid;
	private List<Subscripcion> pharmacyList;
	private PanelFilter filter;

	private InscripcionEJB inscripcionEJB;
//	private ClienteEJB clienteEJB;

	public CumplesSelectorComposer(){
		super();
		inscripcionEJB= BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
//		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");

	}

	private CumplesFilter getFilter() {
		return (CumplesFilter)(filter.getGridFilter());
	}
	public void onCreate() {
		clasepanelListGrid.setActionComposerDelegate(this);
		Sessions.getCurrent().setAttribute("isFromCumples", null);

		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
			setCallFromMenu(false);			
		}
		
		// preselecciono el anio
		if(getFilter().getAnioContratado().getItems() != null){
			getFilter().setSelectedAnioContratado();
		}
		onFind();	
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();
		
		getFilter().setActionComposerDelegate(this);
	}

	
	
	
	private void refreshEvents(){
		
		getFilter().getAnioContratado().addEventListener(Events.ON_SELECT, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	
		
		getFilter().getMesContratado().addEventListener(Events.ON_SELECT, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	

	}
	
	public void onUpdate(Event event) throws Exception{
//		if (hasSelectedOneItem(clasepanelListGrid)){
//			Sessions.getCurrent().setAttribute("idClase", clasepanelListGrid.getSelectedItem().getValue());		
//			Sessions.getCurrent().setAttribute("position", clasepanelListGrid);
//			Sessions.getCurrent().setAttribute("key", pharmacyList);
//			Sessions.getCurrent().setAttribute("max", pharmacyList.size());
//			super.saveHistory("filter");
//			super.gotoPage("/institucion/clase-crud.zul");		
//		}
		
	}

	public void onDelete(Event event) {
	}

	public void onInsert(Event event) throws Exception {
	
//		com.institucion.fm.conf.Session.setAttribute("idClase", null);
//		Sessions.getCurrent().setAttribute("max", 0);
//		super.saveHistory("filter");
//		super.gotoPage("/institucion/clase-crud.zul");
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
			this.onVerSubs(null);
	}


	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Subscripcion>();
		
		// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		pharmacyList =inscripcionEJB.findAllConJdbc(getFilter().getFilters());	
		clasepanelListGrid.setList(pharmacyList, null);
	}
	
	

	public void onVerSubs(Event event) throws Exception {
		this.onUpdateSubscripciones(null);
	}
	

	public void onBack(Event event) {
		Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", null);
		super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	}

	public void onSaldarDeudaSubs(Event event) throws Exception {

		if (hasSelectedOneItem(clasepanelListGrid)){
			Subscripcion cli=(Subscripcion)clasepanelListGrid.getSelectedItem().getValue();
			if(cli != null ){
				
				float deuda=getDeuda(cli);
				if(deuda > 0){
					com.institucion.fm.conf.Session.setAttribute("idCliente", cli.getCliente());
					com.institucion.fm.conf.Session.setAttribute("isFromCumples", true);
					super.saveHistory("filter");
					super.gotoPage("/institucion/subscripcion-saldar-deuda-crud.zul");
					
				}else{
					MessageBox.validation("El cumpleaños seleccionado no tiene deudas", 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;
				}
			}
		}
	}
	
	
	private float getDeuda(Subscripcion subs){
		float cantDeuda= 0;
			
			if(subs.getCupoActividadList() != null){
				for (CupoActividad cupoAct: subs.getCupoActividadList()) {
					if(cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("CUMPLEA")){
						float pagosRealizados=0;
						for (PagosPorSubscripcion pagoPorSubscripcion: subs.getPagosPorSubscripcionList()) {
							if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
								pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
								pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
							}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
								pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
								pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
							}else{
								pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
							}		
						}
						
						if(pagosRealizados < subs.getPrecioTOTALadicionalTarjeta() ){
							cantDeuda =cantDeuda + (subs.getPrecioTOTALadicionalTarjeta() - pagosRealizados);
							
						}
						break;
					}						
				}
			}
		return cantDeuda;		
	}
	
	
	public void onUpdateSubscripciones(Event event) throws Exception{
		if (hasSelectedOneItem(clasepanelListGrid)){
			
			if(clasepanelListGrid.getSelectedItem().getValue() == null){
				MessageBox.validation("Debe seleccionar un cumpleaños.",I18N.getLabel("selector.actionwithoutitem.title"));
				return ;
				
			}
			if(clasepanelListGrid.getSelectedItem().getValue() instanceof Subscripcion){
				Sessions.getCurrent().setAttribute("idSubs", clasepanelListGrid.getSelectedItem().getValue());

			}
			Sessions.getCurrent().setAttribute("idCliente", ((Subscripcion)clasepanelListGrid.getSelectedItem().getValue()).getCliente());
			com.institucion.fm.conf.Session.setAttribute("isFromCumples", true);

//			guardarTemasPaginacion();

			super.saveHistory("filter");
			super.gotoPage("/institucion/subscripcion-crud.zul");		
		}
	}
	
	public void onAnularSubs(Event event) throws Exception {
		
		if (hasSelectedOneItem(clasepanelListGrid)){

				Subscripcion subsSeleccionada= null;
				subsSeleccionada=(Subscripcion)clasepanelListGrid.getSelectedItem().getValue();
				
				if(subsSeleccionada != null && subsSeleccionada.getFechaYHoraAnulacion() != null){
					MessageBox.validation("La subscripcion ya se encuentra anulada", 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;

				}else{
					Cliente cli=((Subscripcion)clasepanelListGrid.getSelectedItem().getValue()).getCliente();
					com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
					com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
						
					if(subsSeleccionada != null){
					
						if(subsSeleccionada != null){
							if(subsSeleccionada.getCupoActividadList() != null ){
								for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
									if(iterable_element.getEstado() != null 
											&&(iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt() 
												|| iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt())){
										MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.errors.solo.nuevasoconcupos"), 
												I18N.getLabel("selector.actionwithoutitem.title"));
										return ;
										
									}
								}
							}
						}
						
						Calendar ahoraCal= Calendar.getInstance();
						ahoraCal.setTime(subsSeleccionada.getFechaIngresoSubscripcion() );
						int mes=ahoraCal.get(Calendar.MONTH)+ 1;
						String fehchaIngreso=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
						
						Object[] params5 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase(), fehchaIngreso };
						
						if (MessageBox.question(I18N.getLabel("subscripcion.list.title.anular", params5), I18N.getLabel("subscripcion.list.title.anular.title"))){
							
							com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
							com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
							com.institucion.fm.conf.Session.setAttribute("isFromCumples", true);
							
							super.saveHistory("filter");

							super.gotoPage("/institucion/subscripcion-anular-crud.zul");
							
						}
						
						
					}else{
						return ;
					}
			
				}

		}else{
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.debe.seleccionar.una.inscricion"), 
					I18N.getLabel("selector.actionwithoutitem.title"));
			return ;
			
		}
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
	public void sortEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		onFind(evt);
	}

	@Override
	public void venderNuevaSubs(Event evt, int idCliente) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actualizarPaneldePrecioProducto() {
		// TODO Auto-generated method stub
		
	}
}
package com.institucion.controller;

import java.util.Date;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.desktop.delegated.CerrarCajaDelegate;
import com.institucion.desktop.delegated.IngresosEgresosDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.CerrarCajaCrud;
import com.institucion.model.CerrarCajaFilter;
import com.institucion.model.CierreCaja;
import com.institucion.model.CierreCajaList;
import com.institucion.model.SucursalEnum;

public class CerrarCajaCrudComposer extends CrudComposer implements AnularSubscripcionDelegate,  IngresosEgresosDelegate, CerrarCajaDelegate{

	private CajaEJB cajaEJB;
	private PanelCrud crud;
	private CierreCajaList clientepanelListGrid;
	private PanelFilter filter;
	
	private CerrarCajaFilter getFilter() {
		return (CerrarCajaFilter)(filter.getGridFilter());
	}
	
	private CerrarCajaCrud getCerrarCajaCrud() {
		return (CerrarCajaCrud)(crud.getGridCrud());
	}
	
	public CerrarCajaCrudComposer() {
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}
 	
	public void onCreate() {
		getCerrarCajaCrud().setDelegate(this);
		SucursalEnum suc= null;
		
		getFilter().setDelegate(this);
		
		if(getFilter().getSucursal().getSelectedItem() != null)
			suc= (SucursalEnum)getFilter().getSucursal().getSelectedItem().getValue();

		clientepanelListGrid.setList(cajaEJB.findCierresCaja(suc));
		
		filter.setOpen(true);
		filter.getInnerPanel().setOpen(true);
	}
	
	
	public void onfindFromChangeSucursal(){
		SucursalEnum suc= null;

		if(getFilter().getSucursal().getSelectedItem() != null)
			suc= (SucursalEnum)getFilter().getSucursal().getSelectedItem().getValue();

		clientepanelListGrid.setList(cajaEJB.findCierresCaja(suc));

		
	}
	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
			this.onUpdate(null);
	}
	
	
	public void onUpdate(Event event) throws Exception{
		if (hasSelectedOneItem(clientepanelListGrid)){
			
			CierreCaja ca=((CierreCaja)clientepanelListGrid.getSelectedItem().getValue());
			// selecciono todas las cosas
			getCerrarCajaCrud().getComentario().setValue(null);
			getCerrarCajaCrud().getComentario().setText(null);

//			getCerrarCajaCrud().getSucursal().setSelectedItem(null);
//			getCerrarCajaCrud().getValor().setValue(null);

			if(ca != null){
				
				if(ca.getSucursal() != null){
					
					if(ca.getSucursal().equals(SucursalEnum.CENTRO)){
						getCerrarCajaCrud().getSucursal().setSelectedIndex(0);
					}else{
						getCerrarCajaCrud().getSucursal().setSelectedIndex(1);
					}

					if(ca.getComentario() != null){
						getCerrarCajaCrud().getComentario().setValue(ca.getComentario() );
					}
					
					if(ca.getValor() != null){
						getCerrarCajaCrud().getValor().setValue(ca.getValor() );
					}
					
					getCerrarCajaCrud().getValor().setAttribute("esModif", ca);
					
				}
				
				
			}
		}
	}
	
	public void onSave(Event event) throws Exception {
		try{
		
			CierreCaja aaa=(CierreCaja)getCerrarCajaCrud().getValor().getAttribute("esModif");
			if(aaa != null){
				// es una modificacion
				if(getCerrarCajaCrud().getSucursal().getSelectedItem() != null){
					aaa.setSucursal((SucursalEnum)getCerrarCajaCrud().getSucursal().getSelectedItem().getValue());
				}else{
					throw new WrongValueException(getCerrarCajaCrud().getSucursal(), I18N.getLabel("error.empty.field"));

				}	

				if(getCerrarCajaCrud().getComentario().getValue() != null){
					aaa.setComentario(getCerrarCajaCrud().getComentario().getValue());
				}	
				
				if(getCerrarCajaCrud().getValor().getValue() != null){
					aaa.setValor(getCerrarCajaCrud().getValor().getValue());
				}else{
					aaa.setValor(0);
				}
				aaa.setFecha(new Date());
				aaa.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());

				cajaEJB.save(aaa);
				
			}else{
				

				// Guardo el movimiento en la caja ahora
				CierreCaja cierraCaja=  new CierreCaja();
				
				if(getCerrarCajaCrud().getSucursal().getSelectedItem() != null){
					cierraCaja.setSucursal((SucursalEnum)getCerrarCajaCrud().getSucursal().getSelectedItem().getValue());
				}else{
					throw new WrongValueException(getCerrarCajaCrud().getSucursal(), I18N.getLabel("error.empty.field"));

				}			

				if(getCerrarCajaCrud().getComentario().getValue() != null){
					cierraCaja.setComentario(getCerrarCajaCrud().getComentario().getValue());
				}	
				
				if(getCerrarCajaCrud().getValor().getValue() != null){
					cierraCaja.setValor(getCerrarCajaCrud().getValor().getValue());
				}else{
					cierraCaja.setValor(0);
				}
				cierraCaja.setFecha(new Date());
				cierraCaja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());

				cajaEJB.save(cierraCaja);				
				
			}
			
			super.gotoPage("/institucion/caja-selector.zul");
			
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
	
	public void onBack(Event event) {
		super.gotoPage("/institucion/caja-selector.zul");
	}

	@Override
	public int getValorCurso() {
			return 0;
	}

	@Override
	public boolean seSeleccionoElCurso() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actualizarLista() {
//
//		crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(getValorCurso()));
//		crudFormasDePago.getValorSumadoPesos().setValue("0");
//		
//		crudFormasDePago.cleanCrud();
//		crudFormasDePago.getGridList().removeAll();
	}


	@Override
	public void pagarCurso() {
		// TODO Auto-generated method stub
		
	}
	
}

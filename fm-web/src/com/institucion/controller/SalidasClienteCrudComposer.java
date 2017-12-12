package com.institucion.controller;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.InscripcionEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Cliente;
import com.institucion.model.SalidasClienteCrud;

public class SalidasClienteCrudComposer extends CrudComposer{

	public final String idCurso = "idCliente";
	private Cliente cliente;
	private InscripcionEJB subscripcionEJB;

	private PanelCrud crud;
	
	public SalidasClienteCrudComposer() {
		subscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
	}

	private SalidasClienteCrud getSalidasClienteCrud() {
		return (SalidasClienteCrud)(crud.getGridCrud());
	}
 
	
	public void onCreate() {
		Cliente prod= (Cliente) Session.getAttribute(idCurso);
		setCliente(prod);
//		fromModelToView();
	}
	
//	private void fromModelToView() {
//		// si lo que obtuve de bd es != null
//		if(cliente != null){
//
//			if(producto.getNombre() != null){
//				getProductoCrud().getNombreCb().setValue(producto.getNombre());
//			}
//			
//			if(producto.getDescripcion() != null){
//				getProductoCrud().getDescripcionCb().setValue(producto.getDescripcion());
//			}
//			getProductoCrud().getPrecioCb().setValue(producto.getPrecio());
//
//			getProductoCrud().getStockCb().setValue(producto.getStock());
//			
////			if(producto.getDescripcion() != null){
////				getProductoCrud().getDescripcionCb().setValue(producto.getDescripcion());
////			}
//		}			
//	}
//	

	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idCurso) != null) {
				// es una modificacion
					cliente = (Cliente) Sessions.getCurrent().getAttribute(idCurso);
					this.fromViewToModel(cliente);
					if(!validateCrud()){
						MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
						return;
					}
//					subscripcionEJB.save(cliente);
					
					if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
						super.gotoPage("/institucion/producto-selector.zul");
					}else	
						super.gotoPage("/institucion/producto-selector.zul");

			} else {
				// es nuevo 
				cliente= new Cliente();
				this.fromViewToModel(cliente);
				if(!validateCrud()){
					MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
//				subscripcionEJB.save(cliente);
				Cliente cli= (Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente");

				if(cli != null){
					super.gotoPage("/institucion/subscripcion-crud.zul");
					return;
				}
				
				if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
					Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
					super.gotoPage("/institucion/producto-selector.zul");
				}else	
					super.gotoPage("/institucion/producto-selector.zul");
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
		super.gotoPage("/institucion/cursos-selector.zul");
	}


	private Cliente fromViewToModel(Cliente cliente) {

//		
//		if(getSalidasClienteCrud().getNombreCb().getValue() != null)
//			producto.setNombre(getSalidasClienteCrud().getNombreCb().getValue());
//		
//		if(getSalidasClienteCrud().getDescripcionCb().getValue() != null)
//			producto.setDescripcion(getSalidasClienteCrud().getDescripcionCb().getValue());
//	
//		if(getSalidasClienteCrud().getPrecioCb().getValue() != null)
//			producto.setPrecio(getSalidasClienteCrud().getPrecioCb().getValue());	
//
//		if(getSalidasClienteCrud().getStockCb().getValue() != null)
//			producto.setPrecio(getSalidasClienteCrud().getStockCb().getValue());
		
		return cliente;
	}
	
	private boolean validateCrud() {
			
		if(getSalidasClienteCrud().getNombreClienteCb().getValue()  == null || 
				(getSalidasClienteCrud().getNombreClienteCb().getValue() != null && getSalidasClienteCrud().getNombreClienteCb().getValue().trim().equalsIgnoreCase("")))
			throw new WrongValueException(getSalidasClienteCrud().getNombreClienteCb(), I18N.getLabel("error.empty.field"));
	
		if(getSalidasClienteCrud().getPrecioCb().getValue() == null)
			throw new WrongValueException(getSalidasClienteCrud().getPrecioCb(), I18N.getLabel("error.empty.field"));

		if(getSalidasClienteCrud().getStockCb().getValue() == null)
			throw new WrongValueException(getSalidasClienteCrud().getStockCb(), I18N.getLabel("error.empty.field"));
		
		return true;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}

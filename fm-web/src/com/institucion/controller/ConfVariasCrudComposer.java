package com.institucion.controller;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.ConfVariasCrud;
import com.institucion.model.Matricula;

public class ConfVariasCrudComposer extends CrudComposer {

	private CursoEJB cursoEJB;
	private PanelCrud crud;	

	public ConfVariasCrudComposer() {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
	}

	private ConfVariasCrud getConfVariasCrud() {
		return (ConfVariasCrud) (crud.getGridCrud());
	}

	public void onCreate() {
		this.fromModelToView();
	}
	
	private void fromModelToView() {
		Matricula mat=cursoEJB.findMatricula();
		if(mat != null){
			getConfVariasCrud().setPrecioInt(mat.getPrecio());
		}else{
			getConfVariasCrud().setPrecioInt(0);
		}	
		
		Boolean bool=cursoEJB.findImprimible();
		getConfVariasCrud().setSelectedDisponible(bool);
	}
	
	public void onSave(Event event) throws Exception {
		try{
			if(validateCrud()){
				Matricula mat=cursoEJB.findMatricula();
				mat.setPrecio(getConfVariasCrud().getPrecio().getValue());
				cursoEJB.saveMatricula(mat);
				
				if(getConfVariasCrud().getImprimible().getSelectedItem() != null){
					cursoEJB.saveImprimible((Boolean)getConfVariasCrud().getImprimible().getSelectedItem().getValue());	
				}
				MessageBox.info("Se guardaron los cambios correctamente", "Confirmacion");
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}

	private boolean validateCrud() {
		if(getConfVariasCrud().getPrecio() == null || (getConfVariasCrud().getPrecio() != null && getConfVariasCrud().getPrecio().getValue() == null)){
			throw new WrongValueException(getConfVariasCrud().getPrecio(), I18N.getLabel("error.empty.field"));
		} 		
		return true;
	}

}

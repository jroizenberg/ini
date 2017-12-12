package com.institucion.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ProductoVentaCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Textbox deudaaSaldar;
	
	public ProductoVentaCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
	
		deudaaSaldar= new Textbox();
		deudaaSaldar.setMaxlength(50);
		deudaaSaldar.setConstraint(new TextConstraint());
		deudaaSaldar.setReadonly(true);
		deudaaSaldar.setStyle("color: red ; font-weight: bold ; ");
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("client.nombre.deuda.cliee.a.saldar.tot")), deudaaSaldar);
	}

	public Textbox getDeudaaSaldar() {
		return deudaaSaldar;
	}

	public void setDeudaaSaldar(Textbox deudaaSaldar) {
		this.deudaaSaldar = deudaaSaldar;
	}
}

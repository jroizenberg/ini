package com.institucion.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class VentaProductoCantidadCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Textbox cantidadDineroASaldar;
	
	public VentaProductoCantidadCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		cantidadDineroASaldar= new Textbox();
		cantidadDineroASaldar.setMaxlength(50);
		cantidadDineroASaldar.setConstraint(new TextConstraint());
		cantidadDineroASaldar.setReadonly(true);
		cantidadDineroASaldar.setStyle("color: red ; font-weight: bold ; ");
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.deuda.cliee.a.saldar")), cantidadDineroASaldar);
	}

	public Textbox getCantidadDineroASaldar() {
		return cantidadDineroASaldar;
	}

	public void setCantidadDineroASaldar(Textbox cantidadDineroASaldar) {
		this.cantidadDineroASaldar = cantidadDineroASaldar;
	}
}

package com.institucion.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.view.GridCrud;

public class SubscripcionResponsableVentaCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label responsableLabel;
	private Label nombreLabel;
	
	public SubscripcionResponsableVentaCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		responsableLabel =new Label("Responsable Venta");
		nombreLabel =new Label(" ");
		this.addField(responsableLabel, nombreLabel);
		this.addField(new Label(" "), new Label(" "));
	}

	public Label getResponsableLabel() {
		return responsableLabel;
	}

	public void setResponsableLabel(Label responsableLabel) {
		this.responsableLabel = responsableLabel;
	}

	public Label getNombreLabel() {
		return nombreLabel;
	}

	public void setNombreLabel(Label nombreLabel) {
		this.nombreLabel = nombreLabel;
	}

}

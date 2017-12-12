package com.institucion.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class PosponerSubscripcionCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label cliente;
//	private Textbox estado;
//	private Textbox fechaVencimiento;
	
	
	public PosponerSubscripcionCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		cliente= new Label();
		cliente.setMaxlength(50);
		this.addField1ComponentePorRow(new RequiredLabel(I18N.getLabel("client.nombre.1")), this.cliente);

//		estado= new Textbox();
//		estado.setMaxlength(50);
//		estado.setConstraint(new TextConstraint());
//		estado.setDisabled(true);
//		this.addField1ComponentePorRow(new RequiredLabel(I18N.getLabel("client.nombre.estadoSubs")), this.estado);
		
//		fechaVencimiento= new Textbox();
//		fechaVencimiento.setMaxlength(50);
//		fechaVencimiento.setConstraint(new TextConstraint());
//		fechaVencimiento.setDisabled(true);
//		this.addField1ComponentePorRow(new RequiredLabel(I18N.getLabel("client.nombre.totalAbonado.vencimiento")), fechaVencimiento);
		
	
	}

	public Label getCliente() {
		return cliente;
	}

	public void setCliente(Label cliente) {
		this.cliente = cliente;
	}

//	public Textbox getEstado() {
//		return estado;
//	}

//	public Textbox getFechaVencimiento() {
//		return fechaVencimiento;
//	}
//
//	public void setFechaVencimiento(Textbox fechaVencimiento) {
//		this.fechaVencimiento = fechaVencimiento;
//	}

//	public void setEstado(Textbox estado) {
//		this.estado = estado;
//	}

}

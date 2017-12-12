package com.institucion.model;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class AnularSubscripcionCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label cliente;
//	private Textbox estado;
	private Textbox totalAbonado;
	private Textbox deuda;
	
	public AnularSubscripcionCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		cliente= new Label();
		cliente.setMaxlength(50);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.1")), cliente);
		
//		estado= new Textbox();
//		estado.setMaxlength(50);
//		estado.setConstraint(new TextConstraint());
//		estado.setDisabled(true);
//		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.estadoSubs")), estado);
		
		totalAbonado= new Textbox();
		totalAbonado.setMaxlength(50);
		totalAbonado.setConstraint(new TextConstraint());
		totalAbonado.setDisabled(true);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.totalAbonado")), totalAbonado);
		
		deuda= new Textbox();
		deuda.setMaxlength(50);
		deuda.setConstraint(new TextConstraint());
		deuda.setDisabled(true);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.deuda")), deuda);
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
//
//	public void setEstado(Textbox estado) {
//		this.estado = estado;
//	}

	public Textbox getTotalAbonado() {
		return totalAbonado;
	}

	public void setTotalAbonado(Textbox totalAbonado) {
		this.totalAbonado = totalAbonado;
	}

	public Textbox getDeuda() {
		return deuda;
	}

	public void setDeuda(Textbox deuda) {
		this.deuda = deuda;
	}
}

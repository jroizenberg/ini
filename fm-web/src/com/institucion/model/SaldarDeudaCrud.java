package com.institucion.model;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SaldarDeudaCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label cliente;
	private Textbox estado;
	private Textbox deuda;
	private Textbox deudaaSaldar;
	
	public SaldarDeudaCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		cliente= new Label();
		cliente.setMaxlength(50);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.1")), cliente);
		
		estado= new Textbox();
		estado.setMaxlength(50);
		estado.setConstraint(new TextConstraint());
		estado.setDisabled(true);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.estadoSubs")), estado);
		
		deuda= new Textbox();
		deuda.setMaxlength(50);
		deuda.setConstraint(new TextConstraint());
		deuda.setReadonly(true);
		deuda.setStyle("color: red ;");
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.deuda.cliee")), deuda);
		
		deudaaSaldar= new Textbox();
		deudaaSaldar.setMaxlength(50);
		deudaaSaldar.setConstraint(new TextConstraint());
		deudaaSaldar.setReadonly(true);
		deudaaSaldar.setStyle("color: red ; font-weight: bold ; ");
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.deuda.cliee.a.saldar")), deudaaSaldar);
	}

	public Label getCliente() {
		return cliente;
	}

	public void setCliente(Label cliente) {
		this.cliente = cliente;
	}

	public Textbox getEstado() {
		return estado;
	}

	public void setEstado(Textbox estado) {
		this.estado = estado;
	}

	public Textbox getDeuda() {
		return deuda;
	}

	public void setDeuda(Textbox deuda) {
		this.deuda = deuda;
	}

	public Textbox getDeudaaSaldar() {
		return deudaaSaldar;
	}

	public void setDeudaaSaldar(Textbox deudaaSaldar) {
		this.deudaaSaldar = deudaaSaldar;
	}
}

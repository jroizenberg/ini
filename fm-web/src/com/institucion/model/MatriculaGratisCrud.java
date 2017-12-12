package com.institucion.model;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class MatriculaGratisCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Label nombreCliente;
	private Datebox fechaDesde;

	public MatriculaGratisCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){

		nombreCliente= new Label();
		nombreCliente.setMaxlength(150);
		nombreCliente.setWidth("auto");
		
		this.addField(new RequiredLabel("Cliente"), nombreCliente); 
		
		fechaDesde  = new Datebox();
		fechaDesde .setMaxlength(20);
		fechaDesde.setId("sasadss");
		fechaDesde .setFormat(I18N.getDateFormat());
		
		this.addField(new RequiredLabel("Fecha de Pago de Matricula del cliente"), fechaDesde); 
		
	}

	public Label getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(Label nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public Datebox getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Datebox fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
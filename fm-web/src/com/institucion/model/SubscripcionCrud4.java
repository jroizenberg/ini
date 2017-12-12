package com.institucion.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SubscripcionCrud4 extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label precio;
	private Label adicionalTarjeta;
	private Label precioTOTALadicionalTarjeta;
	
	public SubscripcionCrud4 (){
		super();
		this.setStyle("  width:50%;  align:center;  margin-left: 20%;");
		super.setStyle("  width:50%;   align:center;  margin-left: 20%;");
		this.makeFields();
	}
	    
	private void makeFields(){

		precio = new Label();
		this.addField1ComponentePorRow(new Label(I18N.getLabel("importe.precio")), precio);
		
		adicionalTarjeta = new Label();
		this.addField1ComponentePorRow(new Label(I18N.getLabel("adicionalTarjeta")), adicionalTarjeta);

		precioTOTALadicionalTarjeta = new Label();
		precioTOTALadicionalTarjeta.setStyle("font-size:11pt;font-weight: bold;");
		RequiredLabel aaa= new RequiredLabel(I18N.getLabel("precioTOTALadicionalTarjeta"));
		aaa.setStyle("font-size:11pt;font-weight: bold;");
		this.addField1ComponentePorRow(aaa, precioTOTALadicionalTarjeta);
				
	}

	public Label getAdicionalTarjeta() {
		return adicionalTarjeta;
	}

	public void setAdicionalTarjeta(Label adicionalTarjeta) {
		this.adicionalTarjeta = adicionalTarjeta;
	}

	public Label getPrecioTOTALadicionalTarjeta() {
		return precioTOTALadicionalTarjeta;
	}

	public void setPrecioTOTALadicionalTarjeta(Label precioTOTALadicionalTarjeta) {
		this.precioTOTALadicionalTarjeta = precioTOTALadicionalTarjeta;
	}

	public Label getPrecio() {
		return precio;
	}

	public void setPrecio(Label precio) {
		this.precio = precio;
	}

}

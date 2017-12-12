package com.institucion.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SalidasClienteCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Textbox nombreClienteCb;
	private Textbox descripcionCb;
	private Intbox precioCb;
	private Intbox stockCb;
	
    private Textbox archivo;
    private Image marketImage;


	public SalidasClienteCrud (){
		super();
		this.makeFields();
	}

	
	private void makeFields(){

		nombreClienteCb= new Textbox();
		nombreClienteCb.setMaxlength(50);
		nombreClienteCb.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("producto.nombre")), nombreClienteCb);

		precioCb = new Intbox();
		this.addField(new RequiredLabel(I18N.getLabel("producto.precio")), precioCb);
		
		descripcionCb= new Textbox();
		descripcionCb.setMaxlength(50);
		descripcionCb.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("producto.descripcion")), descripcionCb);
		
		
	}
  
	
	public Textbox getNombreClienteCb() {
		return nombreClienteCb;
	}

	public void setNombreClienteCb(Textbox nombreCb) {
		this.nombreClienteCb = nombreCb;
	}

	public Textbox getDescripcionCb() {
		return descripcionCb;
	}

	public void setDescripcionCb(Textbox descripcionCb) {
		this.descripcionCb = descripcionCb;
	}

	public Intbox getPrecioCb() {
		return precioCb;
	}

	public void setPrecioCb(Intbox precioCb) {
		this.precioCb = precioCb;
	}

	public Intbox getStockCb() {
		return stockCb;
	}

	public void setStockCb(Intbox stockCb) {
		this.stockCb = stockCb;
	}

	public Textbox getArchivo() {
		return archivo;
	}

	public void setArchivo(Textbox archivo) {
		this.archivo = archivo;
	}

	public Image getMarketImage() {
		return marketImage;
	}

	public void setMarketImage(Image marketImage) {
		this.marketImage = marketImage;
	}

}

package com.institucion.model;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import com.institucion.desktop.delegated.SubscripcionDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;

public class CumpleaniosCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
//	private Datebox fechaCumpleanios;
	private Intbox importeTotalCumple;

	private SubscripcionDelegate delegate;

	public SubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SubscripcionDelegate delegate) {
		this.delegate = delegate;
	}
	
	public CumpleaniosCrud (){
		super();
		this.makeFields();
	}
	
	private void makeFields(){	
//		fechaCumpleanios  = new Datebox();
//		fechaCumpleanios .setMaxlength(20);
//		fechaCumpleanios.setId("sasas222");
//		fechaCumpleanios.setFormat(I18N.getDateFormat());
//
//		this.addFieldClases(new Label("Fecha cumpleaños"),fechaCumpleanios, 1);

		importeTotalCumple= new Intbox();
		importeTotalCumple.setConstraint("no negative");
		this.addFieldClases(new Label("Importe Final cumple"), importeTotalCumple, 2);	

		
	}

	public void clear (){
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

//	public Datebox getFechaCumpleanios() {
//		return fechaCumpleanios;
//	}
//
//	public void setFechaCumpleanios(Datebox fechaCumpleanios) {
//		this.fechaCumpleanios = fechaCumpleanios;
//	}

	public Intbox getImporteTotalCumple() {
		return importeTotalCumple;
	}

	public void setImporteTotalCumple(Intbox importeTotalCumple) {
		this.importeTotalCumple = importeTotalCumple;
	}


}

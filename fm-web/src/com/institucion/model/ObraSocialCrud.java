package com.institucion.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ObraSocialCrud extends GridCrud{

	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	
	public ObraSocialCrud (){
		super();
		this.makeFields();
	}

	private void makeFields(){

		nombre= new Textbox();
		nombre.setMaxlength(50);
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("client.nombre")), nombre);
	
	}

	@SuppressWarnings("deprecation")
	public void clear(){		
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}
	
	public void setNombreS(String nombre) {
		
		this.nombre.setValue( nombre);
	}
	
}

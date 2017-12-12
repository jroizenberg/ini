package com.institucion.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class EmpleadoCrud extends GridCrud{

	private static final long serialVersionUID = 1L;

	private Textbox nombre;
//	private Combobox disponible;

	public EmpleadoCrud (){
		super();
		this.makeFields();
	}

	private void makeFields(){

		nombre= new Textbox();
		nombre.setMaxlength(50);
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre")), nombre);
		
//		disponible = BooleanViewHelper.getComboBox();
//		disponible.setConstraint("strict");
//		this.addField(new RequiredLabel("Disponible"), disponible);

	
	}

//	public void setSelectedDisponible (boolean tieneCertficadobool){
//		boolean found = false;
//		int i = 0;
//		while(!found && i<disponible.getItemCount()){
//			if(disponible.getItemAtIndex(i) != null 
//					&&(Boolean)disponible.getItemAtIndex(i).getValue() == tieneCertficadobool){
//				found = true;
//				disponible.setSelectedIndex(i);
//			}
//			i++;
//		}
//	}	
	
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

//	public Combobox getDisponible() {
//		return disponible;
//	}
//
//	public void setDisponible(Combobox disponible) {
//		this.disponible = disponible;
//	}
	
}

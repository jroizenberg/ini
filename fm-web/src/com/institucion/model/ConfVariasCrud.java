package com.institucion.model;


import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;

import com.institucion.fm.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ConfVariasCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Intbox precio;
	private Combobox imprimible;

	public ConfVariasCrud (){
		super();
		this.makeFields();
	}

	private void makeFields(){

		precio= new Intbox();
		precio.setMaxlength(4);
		this.addFieldUnique(new RequiredLabel("Precio Matricula"), precio);
		
		imprimible= new Combobox();
		imprimible= BooleanViewHelper.getComboBox();
		this.addFieldUnique(new RequiredLabel("Imprimir comprobantes?"), imprimible);
	
	
	}

	public void setSelectedDisponible (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<imprimible.getItemCount()){
			if(imprimible.getItemAtIndex(i) != null 
					&&(Boolean)imprimible.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				imprimible.setSelectedIndex(i);
			}
			i++;
		}
	}	

	public void clear(){		
		
	}

	public Intbox getPrecio() {
		return precio;
	}

	public void setPrecio(Intbox precio) {
		this.precio = precio;
	}
	
	public void setPrecioInt(int precio) {
		if(this.precio == null)
			this.precio= new Intbox();
		this.precio.setValue(precio);
	}

	public Combobox getImprimible() {
		return imprimible;
	}

	public void setImprimible(Combobox imprimible) {
		this.imprimible = imprimible;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}

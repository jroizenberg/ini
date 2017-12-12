package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class ClasesCantidadViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		
		for(int i=0 ; i< 21 ; i++){
			Comboitem item;
			item = new Comboitem(String.valueOf(i));
			item.setValue(i);			
			cb.appendChild(item);		
		}
		return cb;
	}

	public static Combobox getMesesPosponeVencimientoComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		
		for(int i=1 ; i< 4 ; i++){
			Comboitem item;
			item = new Comboitem(String.valueOf(i) + " semana");
			int codi= i + 20;
			item.setValue(codi);			
			cb.appendChild(item);		
		}
		
		
		for(int i=1 ; i< 13 ; i++){
			Comboitem item;
			item = new Comboitem(String.valueOf(i) + " meses");
			item.setValue(i);			
			cb.appendChild(item);		
		}
		return cb;
	}
	
	public static Combobox getComboBoxLibre() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		
		Comboitem item;
		item = new Comboitem("LIBRE");
		item.setValue(99);
		cb.appendChild(item);		

		return cb;
	}
}
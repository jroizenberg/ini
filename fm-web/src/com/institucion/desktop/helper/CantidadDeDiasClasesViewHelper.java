package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;


public class CantidadDeDiasClasesViewHelper {
	
	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		for (int i=1 ; i <= 8 ; i++){
			Comboitem item2 = new Comboitem(String.valueOf(i));
			item2.setValue(String.valueOf(i));
			cb.appendChild(item2);
			
		}
		
		return cb;
	}
	
	public static Combobox getMedicinaCantidadDeClasesComboBox() {
		Combobox cb = new Combobox();
		for (int i=1 ; i <= 31 ; i++){
			Comboitem item2 = new Comboitem(String.valueOf(i));
			item2.setValue(String.valueOf(i));
			cb.appendChild(item2);
			
		}
		
		return cb;
	}

	public static Combobox gethorariosClasesComboBox() {
		Combobox cb = new Combobox();
		for (int i=1 ; i <= 24 ; i++){
			Comboitem item2 = new Comboitem(String.valueOf(i));
			item2.setValue(String.valueOf(i));
			cb.appendChild(item2);
			
		}
		
		return cb;
	}
	
}

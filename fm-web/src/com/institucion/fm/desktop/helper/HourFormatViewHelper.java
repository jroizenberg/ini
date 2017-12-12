package com.institucion.fm.desktop.helper;

import java.util.Iterator;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;


public class HourFormatViewHelper {
	
	public static String getHourFormatString(String hourFormat) {
		if (hourFormat.equals(I18N.getLabel("hourformat.0"))) {
			return I18N.getLabel("hourformat.0"); 
		} 
		return I18N.getLabel("hourformat.0"); 
	}
	
	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		Comboitem item1 = new Comboitem(I18N.getLabel("hourformat.0"));
		item1.setValue(I18N.getLabel("hourformat.0"));
		cb.appendChild(item1);
		
		return cb;
	}
	
	public static String getHourFormat(String hourFormat) {
		if (hourFormat == null || "".equals(hourFormat)) {
			return I18N.getLabel("hourformat.0");
		}
		if (hourFormat.equals(I18N.getLabel("hourformat.0"))) {
			return I18N.getLabel("hourformat.0");
		}
		return I18N.getLabel("hourformat.0");
	}
	
	public static Comboitem getItem(Combobox combo, String hourFormat) {
		for (Iterator<?> it = combo.getChildren().iterator(); it.hasNext();) {
			Comboitem item = (Comboitem) it.next();
			if (item.getValue().equals(hourFormat)) {
				return item;
			}
		}
		return null;
	}
}

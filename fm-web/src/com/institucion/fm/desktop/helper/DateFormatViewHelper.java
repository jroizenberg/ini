package com.institucion.fm.desktop.helper;

import java.util.Iterator;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;


public class DateFormatViewHelper {
	
	public static String getDateFormatString(String dateFormat) {
		if (dateFormat.equals(I18N.getLabel("dateformat.0"))) {
			return I18N.getLabel("dateformat.0"); 
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.1"))) {
			return I18N.getLabel("dateformat.1"); 
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.2"))) {
			return I18N.getLabel("dateformat.2"); 
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.3"))) {
			return I18N.getLabel("dateformat.3"); 
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.4"))) {
			return I18N.getLabel("dateformat.4"); 
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.5"))) {
			return I18N.getLabel("dateformat.5"); 
		}
		return I18N.getLabel("dateformat.0"); 
	}
	
	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		
		Comboitem item1 = new Comboitem(I18N.getLabel("dateformat.0"));
		item1.setValue(I18N.getLabel("dateformat.0"));
		cb.appendChild(item1);
		
		Comboitem item2 = new Comboitem(I18N.getLabel("dateformat.1"));
		item2.setValue(I18N.getLabel("dateformat.1"));
		cb.appendChild(item2);
		
		Comboitem item3 = new Comboitem(I18N.getLabel("dateformat.2"));
		item3.setValue(I18N.getLabel("dateformat.2"));
		cb.appendChild(item3);
		
		Comboitem item4 = new Comboitem(I18N.getLabel("dateformat.3"));
		item4.setValue(I18N.getLabel("dateformat.3"));
		cb.appendChild(item4);
		
		Comboitem item5 = new Comboitem(I18N.getLabel("dateformat.4"));
		item5.setValue(I18N.getLabel("dateformat.4"));
		cb.appendChild(item5);
		
		Comboitem item6 = new Comboitem(I18N.getLabel("dateformat.5"));
		item6.setValue(I18N.getLabel("dateformat.5"));
		cb.appendChild(item6);
		
		return cb;
	}
	
	public static String getDateFormat(String dateFormat) {
		if (dateFormat == null || "".equals(dateFormat)) {
			return I18N.getLabel("dateformat.0");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.0"))) {
			return I18N.getLabel("dateformat.0");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.1"))) {
			return I18N.getLabel("dateformat.1");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.2"))) {
			return I18N.getLabel("dateformat.2");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.3"))) {
			return I18N.getLabel("dateformat.3");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.4"))) {
			return I18N.getLabel("dateformat.4");
		}else
		if (dateFormat.equals(I18N.getLabel("dateformat.5"))) {
			return I18N.getLabel("dateformat.5");
		}else
		return I18N.getLabel("dateformat.0");
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

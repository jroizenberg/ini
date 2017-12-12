package com.institucion.fm.desktop.helper;

import java.util.Iterator;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.confsys.model.ConfigRegional;
import com.institucion.fm.desktop.service.I18N;


public class LanguageViewHelper {
	
	public static String getLanguageString(Integer language) {
		if (language.equals(ConfigRegional.CASTELLANO_LANG)) {
			return I18N.getLabel("lang.0"); 
		}else
		if (language.equals(ConfigRegional.PORTUGUES_LANG)) {
			return I18N.getLabel("lang.1"); 
		}else
		if (language.equals(ConfigRegional.ENGLISH_LANG)) {
			return I18N.getLabel("lang.2"); 
		}else
		return I18N.getLabel("lang.0"); 
	}
	
	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		
		Comboitem item1 = new Comboitem(I18N.getLabel("lang.0"));
		item1.setValue(ConfigRegional.CASTELLANO_LANG);
		cb.appendChild(item1);
		
		Comboitem item2 = new Comboitem(I18N.getLabel("lang.1"));
		item2.setValue(ConfigRegional.PORTUGUES_LANG);
		cb.appendChild(item2);
		
		Comboitem item3 = new Comboitem(I18N.getLabel("lang.2"));
		item3.setValue(ConfigRegional.ENGLISH_LANG);
		cb.appendChild(item3);
		
		return cb;
	}
	
	public static String getLanguage(Integer language) {
		if (language == null || "".equals(language)) {
			return I18N.getLabel("lang.0");
		}else
		if (language.equals(ConfigRegional.CASTELLANO_LANG)) {
			return I18N.getLabel("lang.0"); 
		}else
		if (language.equals(ConfigRegional.PORTUGUES_LANG)) {
			return I18N.getLabel("lang.1"); 
		}else
		if (language.equals(ConfigRegional.ENGLISH_LANG)) {
			return I18N.getLabel("lang.2"); 
		}else
		return I18N.getLabel("lang.0");
	}
	
	public static Comboitem getItem(Combobox combo, Integer language) {
		for (Iterator<?> it = combo.getChildren().iterator(); it.hasNext();) {
			Comboitem item = (Comboitem) it.next();
			if (item.getValue().equals(language)) {
				return item;
			}
		}
		return null;
	}
}

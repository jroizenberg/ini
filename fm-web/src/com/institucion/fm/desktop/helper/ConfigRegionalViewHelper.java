package com.institucion.fm.desktop.helper;

import java.util.Iterator;
import java.util.Set;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.confsys.model.ConfigRegional;
import com.institucion.fm.security.model.UserState;

public class ConfigRegionalViewHelper {


	public static Combobox getComboBox(Set<ConfigRegional> regs) {
		
		
		
		return null;
//		Combobox cb = new Combobox();
//		Comboitem item;
//		item = new Comboitem(getStateString(UserState.ACTIVE));
//		item.setValue(UserState.ACTIVE);
//		cb.appendChild(item);
//		
//		item = new Comboitem(getStateString(UserState.INACTIVE));
//		item.setValue(UserState.INACTIVE);
//		cb.appendChild(item);
//		
//		item = new Comboitem(getStateString(UserState.BLOCKED));
//		item.setValue(UserState.BLOCKED);
//		cb.appendChild(item);
//		return cb;
	}

	public static Comboitem getItem(Combobox combo, UserState state) {
		for (Iterator<?> it = combo.getChildren().iterator(); it.hasNext();) {
			Comboitem item = (Comboitem) it.next();
			if (item.getValue() == state) {
				return item;
			}
		}
		return null;
	}
}

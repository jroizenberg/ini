package com.institucion.fm.desktop.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.wf.model.RoleType;

public class RoleTypeViewHelper {
	private static Map<RoleType, String>strings = null;

	static { // no lazy init. StartUp init.
		strings = new HashMap<RoleType, String>();
		
		strings.put(RoleType.HPA, I18N.getLabel("role.type.hpa")); 
		strings.put(RoleType.SUPERVISOR, I18N.getLabel("role.type.supervisor")); 
		strings.put(RoleType.PLMANAGER, I18N.getLabel("role.type.plmanager")); 
		strings.put(RoleType.NORMAL, I18N.getLabel("role.type.normal")); 
	}

	public static String getRoleString(RoleType role) {
		return strings.get(role);
	}

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		List<Comboitem>	itemsSort= new ArrayList<Comboitem>	();
		cb.setConstraint("strict");
		Comboitem item;
		item = new Comboitem(getRoleString(RoleType.NORMAL));
		item.setValue(RoleType.NORMAL);
		itemsSort.add(item);
		item = new Comboitem(getRoleString(RoleType.HPA));
		item.setValue(RoleType.HPA);
		itemsSort.add(item);
		item = new Comboitem(getRoleString(RoleType.SUPERVISOR));
		item.setValue(RoleType.SUPERVISOR);
		itemsSort.add(item);
		item = new Comboitem(getRoleString(RoleType.PLMANAGER));
		item.setValue(RoleType.PLMANAGER);
		itemsSort.add(item);
		OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}

	public static Comboitem getItem(Combobox combo, RoleType role) {
		for (Iterator<?> it = combo.getChildren().iterator(); it.hasNext();) {
			Comboitem item = (Comboitem) it.next();
			if (item.getValue() == role) {
				return item;
			}
		}
		return null;
	}
}
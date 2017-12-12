package com.institucion.fm.desktop.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.security.model.RoleType;

public class GroupRoleViewHelper {
	static private Combobox cb =null;
	public static String getRoleString(RoleType role) {
		if (role == RoleType.FMADMIN) {
			return I18N.getLabel("role.type.fmadmin"); 
		} else if (role == RoleType.WFMANAGER) {
			return I18N.getLabel("role.type.wfmanager"); 
		} else if (role == RoleType.SYSADMIN) {
			return I18N.getLabel("role.type.sysadmin"); 
		}
		return I18N.getLabel("role.type.user"); 
	}

	public synchronized static Combobox getComboBox() {
		cb = new Combobox();
		List<Comboitem>	itemsSort= new ArrayList<Comboitem>	();
		cb.setConstraint("strict");
		Comboitem item;
		item = new Comboitem(getRoleString(RoleType.FMADMIN));
		item.setValue(RoleType.FMADMIN);
		//cb.appendChild(item);
		itemsSort.add(item);

		item = new Comboitem(getRoleString(RoleType.USER));
		item.setValue(RoleType.USER);
		//cb.appendChild(item);
		itemsSort.add(item);

		item = new Comboitem(getRoleString(RoleType.WFMANAGER));
		item.setValue(RoleType.WFMANAGER);
		//cb.appendChild(item);
		itemsSort.add(item);

		item = new Comboitem(getRoleString(RoleType.SYSADMIN));
		item.setValue(RoleType.SYSADMIN);
		//cb.appendChild(item);
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
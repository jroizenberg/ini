package com.institucion.fm.desktop.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.security.model.UserState;

public class UserStateViewHelper {

	private static Map<UserState, String>strings = null;

	static { // no lazy init. StartUp init.
		strings = new HashMap<UserState, String>();
		
		strings.put( UserState.ACTIVE, I18N.getLabel("state.type.active")); 
		strings.put(UserState.INACTIVE,I18N.getLabel("state.type.inactive")); 
		strings.put(UserState.BLOCKED, I18N.getLabel("state.type.blocked"));
	}
	
	public static String getStateString(UserState state) {
		return strings.get(state);
	}

	public static UserState getUserState(String state)
	{
		if (state == null || state.equals(""))
			return UserState.ACTIVE;

		if (state.equals(I18N.getLabel("state.type.inactive")))
			return UserState.INACTIVE;
		else if (state.equals(I18N.getLabel("state.type.blocked")))
			return UserState.BLOCKED;
		return UserState.ACTIVE;
	}

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		List<Comboitem>	itemsSort= new ArrayList<Comboitem>	();
		cb.setConstraint("strict");
		Comboitem item;
		item = new Comboitem(getStateString(UserState.ACTIVE));
		item.setValue(UserState.ACTIVE);
		itemsSort.add(item);
		
		item = new Comboitem(getStateString(UserState.INACTIVE));
		item.setValue(UserState.INACTIVE);
		itemsSort.add(item);
		
		item = new Comboitem(getStateString(UserState.BLOCKED));
		item.setValue(UserState.BLOCKED);
		itemsSort.add(item);
		OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
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

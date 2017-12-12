package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class CantClasesCursosViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(String.valueOf(1));
		item.setValue(1);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(String.valueOf(2));
		item.setValue(2);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(String.valueOf(4));
		item.setValue(4);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(String.valueOf(8));
		item.setValue(8);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(String.valueOf(12));
		item.setValue(12);
		// cb.appendChild(item);
		cb.appendChild(item);
		
	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
}
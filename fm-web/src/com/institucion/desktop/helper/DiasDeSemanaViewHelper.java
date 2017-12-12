package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.fm.desktop.service.I18N;

public class DiasDeSemanaViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		Comboitem item;
		
		item = new Comboitem(I18N.getLabel("dayofweek.monday"));
		item.setValue(1);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(I18N.getLabel("dayofweek.tuesday"));
		item.setValue(2);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(I18N.getLabel("dayofweek.wednesday"));
		item.setValue(3);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dayofweek.thursday"));
		item.setValue(4);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dayofweek.friday"));
		item.setValue(5);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dayofweek.saturday"));
		item.setValue(6);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(I18N.getLabel("dayofweek.sunday"));
		item.setValue(0);
		// cb.appendChild(item);
		cb.appendChild(item);


	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
}
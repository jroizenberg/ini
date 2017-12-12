package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.ClienteEstadoEnum;

public class EstadoClienteViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;

		item = new Comboitem(ClienteEstadoEnum.toString(ClienteEstadoEnum.ADEUDA.toInt()));
		item.setValue(ClienteEstadoEnum.ADEUDA);
		// cb.appendChild(item);
		cb.appendChild(item);


		item = new Comboitem(ClienteEstadoEnum.toString(ClienteEstadoEnum.C_CLASES_DISP.toInt()));
		item.setValue(ClienteEstadoEnum.C_CLASES_DISP);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(ClienteEstadoEnum.toString(ClienteEstadoEnum.S_CLASES_DISP.toInt()));
		item.setValue(ClienteEstadoEnum.S_CLASES_DISP);
		// cb.appendChild(item);
		cb.appendChild(item);

	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
}
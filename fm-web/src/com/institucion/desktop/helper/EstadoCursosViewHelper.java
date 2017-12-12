package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.EstadoCursoEnum;

public class EstadoCursosViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(EstadoCursoEnum.toString(EstadoCursoEnum.ADEUDA.toInt()));
		item.setValue(EstadoCursoEnum.ADEUDA);
		// cb.appendChild(item);
		cb.appendChild(item);


		item = new Comboitem(EstadoCursoEnum.toString(EstadoCursoEnum.C_CLASES_DISPONIBLES.toInt()));
		item.setValue(EstadoCursoEnum.C_CLASES_DISPONIBLES);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(EstadoCursoEnum.toString(EstadoCursoEnum.S_CLASES_DISPONIBLES.toInt()));
		item.setValue(EstadoCursoEnum.S_CLASES_DISPONIBLES);
		// cb.appendChild(item);
		cb.appendChild(item);

	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
}
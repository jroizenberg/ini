package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.TipoCursoEnum;

public class TipoCursosViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(TipoCursoEnum.NATACION.toString());
		item.setValue(TipoCursoEnum.NATACION);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toString());
		item.setValue(TipoCursoEnum.TRATAMIENTOS_KINESICOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(TipoCursoEnum.MASAJES.toString());
		item.setValue(TipoCursoEnum.MASAJES);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.ACTIVIDADES_FISICAS.toString());
		item.setValue(TipoCursoEnum.ACTIVIDADES_FISICAS);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.OTROS.toString());
		item.setValue(TipoCursoEnum.OTROS);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.COMBO.toString());
		item.setValue(TipoCursoEnum.COMBO);
		// cb.appendChild(item);
		cb.appendChild(item);
		
	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
	public static Combobox getComboBoxTipoActividad() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(TipoCursoEnum.NATACION.toString());
		item.setValue(TipoCursoEnum.NATACION);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.TRATAMIENTOS_KINESICOS.toString());
		item.setValue(TipoCursoEnum.TRATAMIENTOS_KINESICOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(TipoCursoEnum.MASAJES.toString());
		item.setValue(TipoCursoEnum.MASAJES);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.ACTIVIDADES_FISICAS.toString());
		item.setValue(TipoCursoEnum.ACTIVIDADES_FISICAS);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(TipoCursoEnum.OTROS.toString());
		item.setValue(TipoCursoEnum.OTROS);
		// cb.appendChild(item);
		cb.appendChild(item);

	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
}
package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.CupoActividadEstadoEnum;

public class EstadoCupoActividadViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		
		item = new Comboitem(CupoActividadEstadoEnum.TODAS.toString());
		item.setValue(CupoActividadEstadoEnum.TODAS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
//		item = new Comboitem(CupoActividadEstadoEnum.NUEVA.toString());
//		item.setValue(CupoActividadEstadoEnum.NUEVA);
//		// cb.appendChild(item);
//		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.C_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.C_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.S_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.S_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.VENCIDA.toString());
		item.setValue(CupoActividadEstadoEnum.VENCIDA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.ANULADA.toString());
		item.setValue(CupoActividadEstadoEnum.ANULADA);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toString());
		item.setValue(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
		cb.appendChild(item);
	
		return cb;
	}
	
	
	public static Combobox getComboBoxFromIngresoInsc() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item2;
		
		item2 = new Comboitem(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toString());
		item2.setValue(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);
		cb.appendChild(item2);
		cb.setSelectedItem(item2);
		
		Comboitem item;

		item = new Comboitem(CupoActividadEstadoEnum.TODAS.toString());
		item.setValue(CupoActividadEstadoEnum.TODAS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
//		item = new Comboitem(CupoActividadEstadoEnum.NUEVA.toString());
//		item.setValue(CupoActividadEstadoEnum.NUEVA);
//		// cb.appendChild(item);
//		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.C_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.C_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.S_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.S_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(CupoActividadEstadoEnum.VENCIDA.toString());
		item.setValue(CupoActividadEstadoEnum.VENCIDA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toString());
		item.setValue(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.ANULADA.toString());
		item.setValue(CupoActividadEstadoEnum.ANULADA);
		cb.appendChild(item);

		item = new Comboitem(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toString());
		item.setValue(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
		cb.appendChild(item);
	
		return cb;
	}
}
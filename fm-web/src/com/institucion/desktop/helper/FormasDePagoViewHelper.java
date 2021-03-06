package com.institucion.desktop.helper;

import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.BancoPromocion;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagoRapidoSubscripcionEnum;

public class FormasDePagoViewHelper {

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(FormasDePagoSubscripcionEnum.EFECTIVO.toString());
		item.setValue(FormasDePagoSubscripcionEnum.EFECTIVO);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(FormasDePagoSubscripcionEnum.DEBITO.toString());
		item.setValue(FormasDePagoSubscripcionEnum.DEBITO);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem("TARJETA CON %15");
		item.setValue(FormasDePagoSubscripcionEnum.TARJETA_15);
		
		item = new Comboitem("TARJETA CON %20");
		item.setValue(FormasDePagoSubscripcionEnum.TARJETA_20);

		// cb.appendChild(item);
		cb.appendChild(item);
		return cb;
	}

	
	public static Combobox getCantCuotasComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		for(int i=1; i<= 3; i++){
			Comboitem item;
			item = new Comboitem(String.valueOf(i));
			item.setValue(i);
			cb.appendChild(item);
		}
		return cb;
	}

	public static Combobox getPagoRapidoComboBox(List<BancoPromocion> listBancoPromocion) {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.TODO_EN_EFECTIVO.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.TODO_EN_EFECTIVO);
		cb.appendChild(item);

		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.TODO_CON_DEBITO.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.TODO_CON_DEBITO);
		cb.appendChild(item);
		
		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_1_CON_TARJETA.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_1_CON_TARJETA);
		cb.appendChild(item);
		
//		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_3_CON_TARJETA.toInt()));
//		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_3_CON_TARJETA);
//		cb.appendChild(item);
		
		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_6_CON_TARJETA.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_6_CON_TARJETA);
		cb.appendChild(item);

		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.PAGO_COMBINADO.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.PAGO_COMBINADO);
		cb.appendChild(item);
		
		if(listBancoPromocion != null){
			for (BancoPromocion bancoPromocion : listBancoPromocion) {
				Comboitem item2;
				item2 = new Comboitem(bancoPromocion.getNombrePromo());
				item2.setValue(bancoPromocion);
				cb.appendChild(item2);
			}
		}
		return cb;
	}
	
	public static Combobox getPagoRapido2ComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.TODO_EN_EFECTIVO.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.TODO_EN_EFECTIVO);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.TODO_CON_DEBITO.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.TODO_CON_DEBITO);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_1_CON_TARJETA.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_1_CON_TARJETA);
		// cb.appendChild(item);
		cb.appendChild(item);
		
//		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_3_CON_TARJETA.toInt()));
//		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_3_CON_TARJETA);
//		// cb.appendChild(item);
//		cb.appendChild(item);

		item = new Comboitem(PagoRapidoSubscripcionEnum.toString(PagoRapidoSubscripcionEnum.CUOTAS_6_CON_TARJETA.toInt()));
		item.setValue(PagoRapidoSubscripcionEnum.CUOTAS_6_CON_TARJETA);
		// cb.appendChild(item);
		cb.appendChild(item);

		return cb;
	}
}
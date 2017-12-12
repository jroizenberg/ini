package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.VencimientoCursoEnum;

public class CursosVencimientoViewHelper {

	public static Combobox getComboBox() {

		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()));
		item.setValue(VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt()));
		item.setValue(VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()));
		item.setValue(VencimientoCursoEnum.LIBRE_VENCE_AL_MES);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()));
		item.setValue(VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES);
		// cb.appendChild(item);
		cb.appendChild(item);	
		
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()));
		item.setValue(VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()));
		item.setValue(VencimientoCursoEnum.SEMANAL_VENCE_AL_MES);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()));
		item.setValue(VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()));
		item.setValue(VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()));
		item.setValue(VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA);
		// cb.appendChild(item);
		cb.appendChild(item);

		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.MENSUAL_VENCE_AL_MES.toInt()));
		item.setValue(VencimientoCursoEnum.MENSUAL_VENCE_AL_MES);
		// cb.appendChild(item);
		cb.appendChild(item);
			
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.MENSUAL_VENCE_A_LOS_3_MES.toInt()));
		item.setValue(VencimientoCursoEnum.MENSUAL_VENCE_A_LOS_3_MES);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		item = new Comboitem(VencimientoCursoEnum.toString(VencimientoCursoEnum.MENSUAL_VENCE_AL_ANo.toInt()));
		item.setValue(VencimientoCursoEnum.MENSUAL_VENCE_AL_ANo);
		// cb.appendChild(item);
		cb.appendChild(item);
		
		
		return cb;
	}
}

package com.institucion.desktop.helper;

import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.model.Actividad;
import com.institucion.model.Curso;
import com.institucion.model.ObraSocial;
import com.institucion.model.TipoCursoEnum;
import com.institucion.model.VencimientoCursoEnum;

public class ObraSocialViewHelper {
	
	public static Combobox getComboBox(List <ObraSocial> obraSocialList, Combobox cb, Actividad act, Curso curso) {
		cb.getItems().clear();
		if(obraSocialList != null){
			Comboitem item3 = new Comboitem("SIN descuento");
			item3.setValue("SIN descuento");
			cb.appendChild(item3);
			
			Comboitem item4 = new Comboitem("Descuento General/Manual");
			item4.setValue("Descuento General/Manual");
			cb.appendChild(item4);

			if(curso.getIdTipoCurso().toInt() == TipoCursoEnum.TRATAMIENTOS_KINESICOS.toInt()){
				
				if(curso.getVencimiento().toInt() != VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt()
						&& curso.getVencimiento().toInt() != VencimientoCursoEnum.QUINCENAL_VENCE_A_LA_QUINCENA.toInt()){
					
					for (ObraSocial obraSocial : obraSocialList) {
							Comboitem item2 = new Comboitem("Por obra social:"+obraSocial.getNombre().toUpperCase());
							item2.setValue(obraSocial);
							cb.appendChild(item2);
					}		
				}
			}
		}else{
			
			Comboitem item3 = new Comboitem("SIN descuento");
			item3.setValue("SIN descuento");
			cb.appendChild(item3);
			
			Comboitem item4 = new Comboitem("Descuento General/Manual");
			item4.setValue("Descuento General/Manual");
			cb.appendChild(item4);
		}
		
		cb.setSelectedIndex(0);
		return cb;
	}
	
}

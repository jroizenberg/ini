package com.institucion.desktop.helper;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.model.SucursalEnum;

public class SucursalViewHelper {
	private static CursoEJB cursoEJB;

	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem("Centro");
		item.setValue(SucursalEnum.CENTRO);
		cb.appendChild(item);

		item = new Comboitem("Maipu 5500-Cursos verano-");
		item.setValue(SucursalEnum.MAIPU);
		cb.appendChild(item);
				
	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
	
	public static Combobox getComboBoxConOpcionTodos() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem("Centro");
		item.setValue(SucursalEnum.CENTRO);
		cb.appendChild(item);

		item = new Comboitem("Maipu 5500-Cursos verano-");
		item.setValue(SucursalEnum.MAIPU);
		cb.appendChild(item);

		item = new Comboitem("Todas");
		item.setValue("Todas");
		cb.appendChild(item);

	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
	
	public static Combobox getComboBoxConOpcionTodos222() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		item = new Comboitem("Centro");
		item.setValue(SucursalEnum.CENTRO);
		cb.appendChild(item);

		item = new Comboitem("Maipu 5500-Cursos verano-");
		item.setValue(SucursalEnum.MAIPU);
		cb.appendChild(item);

		item = new Comboitem("Todas");
		item.setValue(SucursalEnum.TODAS);
		cb.appendChild(item);

	//	OrderCombobox.orderCombobox(itemsSort, cb);
		return cb;
	}
	
	public static Combobox getComboBoxImprimir(Combobox cb) {
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		Boolean bool=cursoEJB.findImprimible();

		cb.setConstraint("strict");
		cb.setWidth("60%");
		Comboitem item;
		item = new Comboitem("SI");
		item.setValue("SI");
		item.setWidth("60%");
		cb.appendChild(item);
		if(bool)
			cb.setSelectedItem(item);
		
		item = new Comboitem("NO");
		item.setValue("NO");
		item.setWidth("60%");
		cb.appendChild(item);
		if(!bool)
			cb.setSelectedItem(item);

		return cb;
	}

	
	public static Combobox getComboBoxConOpcionMenuTree(Combobox cb, SucursalEnum sucDeUsuario ) {
		cb.setConstraint("strict");
		Comboitem item;
		
			
		if(sucDeUsuario != null && sucDeUsuario.toInt() == SucursalEnum.CENTRO.toInt()){
			item = new Comboitem("Centro");
			item.setValue(SucursalEnum.CENTRO);
			cb.appendChild(item);
			if(Session.getAttribute("sucursalPrincipalSeleccionada") == null)
				cb.setSelectedItem(item);
			else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof SucursalEnum 
					&&  ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals( SucursalEnum.CENTRO)){
				cb.setSelectedItem(item);
			} 
		}else if(sucDeUsuario != null && sucDeUsuario.toInt() == SucursalEnum.MAIPU.toInt()){
			item = new Comboitem("Maipu 5500-Cursos verano-");
			item.setValue(SucursalEnum.MAIPU);
			cb.appendChild(item);
			if(Session.getAttribute("sucursalPrincipalSeleccionada") == null)
				cb.setSelectedItem(item);
			else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof SucursalEnum 
					&&  ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals( SucursalEnum.MAIPU)){
				cb.setSelectedItem(item);
			} 

		}else if(sucDeUsuario != null && sucDeUsuario.toInt() == SucursalEnum.TODAS.toInt()){
			Comboitem item1 = new Comboitem("Centro");
			item1.setValue(SucursalEnum.CENTRO);
			cb.appendChild(item1);
			cb.setSelectedItem(item1);
			
			Comboitem item3 = new Comboitem("Maipu 5500-Cursos verano-");
			item3.setValue(SucursalEnum.MAIPU);
			cb.appendChild(item3);

			Comboitem item2 = new Comboitem("TODAS- Con cierre caja de: CENTRO");
			item2.setValue("TodasCentro");
			cb.appendChild(item2);
		
			Comboitem item4 = new Comboitem("TODAS- Con cierre caja de: MAIPU 5500-CURSOS VERANO-");
			item4.setValue("TodasMaipu");
			cb.appendChild(item4);
			
			
			if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
				cb.setSelectedItem(item2);
			
			}else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof SucursalEnum 
					&&  ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals( SucursalEnum.MAIPU)){
				cb.setSelectedItem(item3);

			}else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof SucursalEnum 
						&&  ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals( SucursalEnum.CENTRO)){
				cb.setSelectedItem(item1);

			}else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof String 
					&&  ((String)Session.getAttribute("sucursalPrincipalSeleccionada")).equalsIgnoreCase("TodasCentro")){
				cb.setSelectedItem(item2);
			
			}else if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof String 
					&&  ((String)Session.getAttribute("sucursalPrincipalSeleccionada")).equalsIgnoreCase("TodasMaipu")){
				cb.setSelectedItem(item4);
			} 
		}
		return cb;
	}
}
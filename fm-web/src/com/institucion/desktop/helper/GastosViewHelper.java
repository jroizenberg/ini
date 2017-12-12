package com.institucion.desktop.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.bz.EmpleadoEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.model.Empleado;
import com.institucion.model.GastosEnum;
import com.institucion.model.GastosMaipuEnum;

public class GastosViewHelper {

	private static EmpleadoEJB empleadoEJB;
	
	public static EmpleadoEJB getEmpleado(){
		if(empleadoEJB== null) {
			empleadoEJB=  BeanFactory.<EmpleadoEJB>getObject("fmEjbEmpleado");
		}
		return empleadoEJB;
	}

	
	
	public static Combobox getComboBoxMes() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;

		item = new Comboitem("Enero");
		item.setValue(new Long(0));
		cb.appendChild(item);

		item = new Comboitem("Febrero");
		item.setValue(new Long(1));
		cb.appendChild(item);

		item = new Comboitem("Marzo");
		item.setValue(new Long(2));
		cb.appendChild(item);

		item = new Comboitem("Abril");
		item.setValue(new Long(3));
		cb.appendChild(item);

		item = new Comboitem("Mayo");
		item.setValue(new Long(4));
		cb.appendChild(item);

		item = new Comboitem("Junio");
		item.setValue(new Long(5));
		cb.appendChild(item);

		item = new Comboitem("Julio");
		item.setValue(new Long(6));
		cb.appendChild(item);

		item = new Comboitem("Agosto");
		item.setValue(new Long(7));
		cb.appendChild(item);

		item = new Comboitem("Septiembre");
		item.setValue(new Long(8));
		cb.appendChild(item);

		item = new Comboitem("Octubre");
		item.setValue(new Long(9));
		cb.appendChild(item);

		item = new Comboitem("Noviembre");
		item.setValue(new Long(10));
		cb.appendChild(item);

		item = new Comboitem("Diciembre");
		item.setValue(new Long(11));
		cb.appendChild(item);

		return cb;
	}
	
	public static Combobox getComboBoxAnio() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		Comboitem item;

		Calendar calendarDate = Calendar.getInstance();  
		calendarDate.setTime(new Date());  
		int anio=calendarDate.get(Calendar.YEAR); 
		
		int hasta= anio+ 5;
		int desde= anio -5;
		for(int i=desde ; i<= hasta; i++ ){
			item = new Comboitem(String.valueOf(i));
			item.setValue(new Long(i));
			cb.appendChild(item);
		}
		return cb;
	}
	
	
	public static Combobox getComboBoxAnioMaipu() {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");
		Comboitem item;

		Calendar calendarDate = Calendar.getInstance();  
		calendarDate.setTime(new Date());  
		int anio=calendarDate.get(Calendar.YEAR); 
		
		int hasta= anio+ 5;
		int desde= anio -5;
		for(int i=desde ; i<= hasta; i++ ){
			int nuevo= i +1;
			item = new Comboitem(String.valueOf(i) +"-"+ nuevo);
			item.setValue(new Long(i));
			cb.appendChild(item);
		}
		return cb;
	}
	
	
	
	public static Combobox getComboBoxEmpleados(Combobox cb) {
//		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		List<Empleado> list=getEmpleado().findAll();
		Comboitem item;

		if(list!= null ){
			for (Empleado empleado : list) {
				
				item = new Comboitem(empleado.getNombre());
				item.setValue(empleado);
				cb.appendChild(item);
			}
		}
		return cb;
	}
	
	public static Combobox getComboBoxTipoGasto(Combobox cb, boolean mostrarTodos, boolean mostrarTipoGastoSueldos) {
//		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		
		if(mostrarTodos){
			item = new Comboitem("Todos");
			item.setValue("Todos");
			cb.appendChild(item);
		}
		
		if(mostrarTipoGastoSueldos){
			item = new Comboitem(GastosEnum.toString(GastosEnum.SUELDOS.toInt()));
			item.setValue(GastosEnum.SUELDOS);
			cb.appendChild(item);	
		}
		
		item = new Comboitem(GastosEnum.toString(GastosEnum.SERVICIO_LUZ.toInt()));
		item.setValue(GastosEnum.SERVICIO_LUZ);
		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.SERVICIO_AGUA.toInt()));
		item.setValue(GastosEnum.SERVICIO_AGUA);
		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.SERVICIO_GAS_A_GRANEL.toInt()));
		item.setValue(GastosEnum.SERVICIO_GAS_A_GRANEL);
		cb.appendChild(item);

//		item = new Comboitem(GastosEnum.toString(GastosEnum.SERVICIO_GAS_ENVASADO.toInt()));
//		item.setValue(GastosEnum.SERVICIO_GAS_ENVASADO);
//		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.MANTENIMIENTO.toInt()));
		item.setValue(GastosEnum.MANTENIMIENTO);
		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.MATERIAL_DE_CONSUMO.toInt()));
		item.setValue(GastosEnum.MATERIAL_DE_CONSUMO);
		cb.appendChild(item);

		
		item = new Comboitem(GastosEnum.toString(GastosEnum.MARKETING.toInt()));
		item.setValue(GastosEnum.MARKETING);
		cb.appendChild(item);

		
//		item = new Comboitem(GastosEnum.toString(GastosEnum.TERCEROS.toInt()));
//		item.setValue(GastosEnum.TERCEROS);
//		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.IMPUESTOS.toInt()));
		item.setValue(GastosEnum.IMPUESTOS);
		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.ALQUILER.toInt()));
		item.setValue(GastosEnum.ALQUILER);
		cb.appendChild(item);

//		item = new Comboitem(GastosEnum.toString(GastosEnum.REFORMAS.toInt()));
//		item.setValue(GastosEnum.REFORMAS);
//		cb.appendChild(item);

		item = new Comboitem(GastosEnum.toString(GastosEnum.OTROS.toInt()));
		item.setValue(GastosEnum.OTROS);
		cb.appendChild(item);
		
		return cb;
	}
	
	
	public static Combobox getComboBoxTipoGastoMaipu(Combobox cb, boolean mostrarTodos, boolean mostrarTipoGastoSueldos) {
//		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		
		if(mostrarTodos){
			item = new Comboitem("Todos");
			item.setValue("Todos");
			cb.appendChild(item);
		}
		
		if(mostrarTipoGastoSueldos){
			item = new Comboitem(GastosMaipuEnum.toString(GastosMaipuEnum.SUELDOS.toInt()));
			item.setValue(GastosMaipuEnum.SUELDOS);
			cb.appendChild(item);
		}

		item = new Comboitem(GastosMaipuEnum.toString(GastosMaipuEnum.CANTINA.toInt()));
		item.setValue(GastosMaipuEnum.CANTINA);
		cb.appendChild(item);

		item = new Comboitem(GastosMaipuEnum.toString(GastosMaipuEnum.TRANSPORTE.toInt()));
		item.setValue(GastosMaipuEnum.TRANSPORTE);
		cb.appendChild(item);

//		item = new Comboitem(GastosMaipuEnum.toString(GastosMaipuEnum.OTROS.toInt()));
//		item.setValue(GastosMaipuEnum.OTROS);
//		cb.appendChild(item);


		
		return cb;
	}
}
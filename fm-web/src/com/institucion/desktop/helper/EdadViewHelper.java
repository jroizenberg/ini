package com.institucion.desktop.helper;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;


public class EdadViewHelper {
	
	public static Combobox getComboBox() {
		Combobox cb = new Combobox();
		for (int i=0 ; i <= 100 ; i++){
			Comboitem item2 = new Comboitem(String.valueOf(i));
			item2.setValue(String.valueOf(i));
			cb.appendChild(item2);
			
		}
		
		return cb;
	}
	
	public static Combobox getComboAnioBox() {
		Combobox cb = new Combobox();
		Calendar c= Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int yearDesde=year -5;
		int yearHasta=year +2;
		for (int i=yearDesde ; i <= yearHasta ; i++){
			Comboitem item2 = new Comboitem(String.valueOf(i));
			item2.setValue(String.valueOf(i));
			cb.appendChild(item2);		
			if(i == year){
				cb.setSelectedItem(item2);
			}
			
		}
		
		return cb;
	}
	
	
	public static Combobox getComboBoxCumplesMes() {
		Combobox cb = new Combobox();
		Comboitem item2= null;
		
//		Calendar c= Calendar.getInstance();
//		int mes=c.get(Calendar.MONTH);

		
		item2 = new Comboitem("Enero");
		item2.setValue("01");
		cb.appendChild(item2);

//		if(mes== 0){
//			cb.setSelectedItem(item2);
//		}
		
		item2 = new Comboitem("Febrero");
		item2.setValue("02");
		cb.appendChild(item2);
//		if(mes== 1){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Marzo");
		item2.setValue("03");
		cb.appendChild(item2);
//		if(mes== 2){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Abril");
		item2.setValue("04");
		cb.appendChild(item2);
//		if(mes== 3){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Mayo");
		item2.setValue("05");
		cb.appendChild(item2);
//		if(mes== 4){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Junio");
		item2.setValue("06");
		cb.appendChild(item2);
//		if(mes== 5){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Julio");
		item2.setValue("07");
		cb.appendChild(item2);
//		if(mes== 6){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Agosto");
		item2.setValue("08");
		cb.appendChild(item2);
//		if(mes== 7){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Septiembre");
		item2.setValue("09");
		cb.appendChild(item2);
//		if(mes== 8){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Octubre");
		item2.setValue("10");
		cb.appendChild(item2);
//		if(mes== 9){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Noviembre");
		item2.setValue("11");
		cb.appendChild(item2);
//		if(mes== 10){
//			cb.setSelectedItem(item2);
//		}
		item2 = new Comboitem("Diciembre");
		item2.setValue("12");
		cb.appendChild(item2);
//		if(mes== 11){
//			cb.setSelectedItem(item2);
//		}
		return cb;
	}
	
	public static Combobox getComboBoxMes() {
		Combobox cb = new Combobox();
		Comboitem item2= null;
		
		item2 = new Comboitem("Enero");
		item2.setValue("01");
		cb.appendChild(item2);

		item2 = new Comboitem("Febrero");
		item2.setValue("02");
		cb.appendChild(item2);

		item2 = new Comboitem("Marzo");
		item2.setValue("03");
		cb.appendChild(item2);

		item2 = new Comboitem("Abril");
		item2.setValue("04");
		cb.appendChild(item2);

		item2 = new Comboitem("Mayo");
		item2.setValue("05");
		cb.appendChild(item2);

		item2 = new Comboitem("Junio");
		item2.setValue("06");
		cb.appendChild(item2);

		item2 = new Comboitem("Julio");
		item2.setValue("07");
		cb.appendChild(item2);

		item2 = new Comboitem("Agosto");
		item2.setValue("08");
		cb.appendChild(item2);

		item2 = new Comboitem("Septiembre");
		item2.setValue("09");
		cb.appendChild(item2);

		item2 = new Comboitem("Octubre");
		item2.setValue("10");
		cb.appendChild(item2);

		item2 = new Comboitem("Noviembre");
		item2.setValue("11");
		cb.appendChild(item2);

		item2 = new Comboitem("Diciembre");
		item2.setValue("12");
		cb.appendChild(item2);
		
		return cb;
	}
	
	public static long calcularEdadEnMilis(Date fechaNacimiento, Date fechaActual) {
	    long diferencia = ( fechaActual.getTime() - fechaNacimiento.getTime() );
	    return diferencia; 
	}
	
	public static String calcularEdad(Date fechaNacimiento, Date fechaActual){
	    Calendar fechaAct = Calendar.getInstance();
	    fechaAct.setTime(fechaActual);

	    Calendar fechaNac = Calendar.getInstance();
	    fechaNac.setTime(fechaNacimiento);

	    int dif_anios = fechaAct.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
	    int dif_meses = fechaAct.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
	    int dif_dias = fechaAct.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

	    //Si está en ese año pero todavía no los ha cumplido
	    if(dif_meses<0 || (dif_meses==0 && dif_dias<0)){
	        dif_anios--;
	    }
	    if(dif_anios == 0){
	    	if(dif_meses> 0)
	    		return String.valueOf(dif_meses+ " meses");
	    	else{
	    		String faltaParaElAño=String.valueOf(dif_meses);
	    		String anio=faltaParaElAño.substring(1);
	    		return String.valueOf(anio+ " meses");
	    	}
	    }
	    	
	    return String.valueOf(dif_anios + " años");
	}
}

package com.institucion.fm.desktop.helper;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.ibm.icu.util.Calendar;
import com.institucion.fm.desktop.service.I18N;

public class DateBoxRememberViewHelper {
	
	
	public synchronized static Combobox getDayComboBox(){
		Combobox cb = new Combobox();
		List<Comboitem>	itemsSort= new ArrayList<Comboitem>	();
		cb.setConstraint("strict");
		int j =0;
		Comboitem item;
		for ( j = 1; j < 32; ++j) {
			item = new Comboitem(String.valueOf(j));
			item.setValue(j);
			cb.appendChild(item);
			
		}

		return cb;
	}
	
	public synchronized static Combobox getMounthComboBox(){
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		
		item = new Comboitem(I18N.getLabel("dateboxremember.january"));
		item.setValue(Calendar.JANUARY);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.february"));
		item.setValue(Calendar.FEBRUARY);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.march"));
		item.setValue(Calendar.MARCH);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.april"));
		item.setValue(Calendar.APRIL);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.may"));
		item.setValue(Calendar.MAY);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.june"));
		item.setValue(Calendar.JUNE);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.july"));
		item.setValue(Calendar.JULY);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.august"));
		item.setValue(Calendar.AUGUST);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.september"));
		item.setValue(Calendar.SEPTEMBER);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.october"));
		item.setValue(Calendar.OCTOBER);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.november"));
		item.setValue(Calendar.NOVEMBER);
		cb.appendChild(item);
		
		item = new Comboitem(I18N.getLabel("dateboxremember.december"));
		item.setValue(Calendar.DECEMBER);
		cb.appendChild(item);
		
		


		return cb;
	}
	public synchronized static Combobox getYearComboBox(){
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem item;
		int year = Calendar.getInstance().get(Calendar.YEAR);
	
		for (int i =year -100; i < year; i++) {
			item = new Comboitem(String.valueOf(i));
			item.setValue(i);
			cb.appendChild(item);
		}
				
		
		return cb;
		
		
		
	}

}

package com.institucion.fm.desktop.helper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

public class OrderCombobox {
	
	public static void orderCombobox(List<Comboitem> list, Combobox cb) {
		Collections.sort(list, new ValueComparator());
		for (Iterator<Comboitem> iterator = list.iterator(); iterator.hasNext();) {
			Comboitem object =  iterator.next();
			cb.appendChild(object);
		}
	}
}

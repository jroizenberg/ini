package com.institucion.fm.desktop.helper;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class OrderListbox {

	public static void orderListbox(List<Listitem> list, Listbox cb) {
		Collections.sort(list, new ListboxComparator());
		for (Iterator<Listitem> iterator = list.iterator(); iterator.hasNext();) {
			Listitem object =  iterator.next();
			cb.appendChild(object);
		}
	}

}

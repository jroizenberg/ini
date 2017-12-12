package com.institucion.fm.desktop.helper;

import java.util.Comparator;

import org.zkoss.zul.Comboitem;

public class ValueComparator implements Comparator<Comboitem> {

	public boolean equals(Object o) {
		return this == o;
	}

	public int compare(Comboitem u1, Comboitem u2) {
		return u1.getLabel().compareTo(u2.getLabel());
	}
}
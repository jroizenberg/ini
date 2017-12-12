package com.institucion.fm.desktop.helper;

import java.util.Comparator;

import org.zkoss.zul.Listitem;

public class ListboxComparator implements Comparator<Listitem> {

	public boolean equals(Object o) {
		return this == o;
	}

	public int compare(Listitem u1, Listitem u2) {
		return u1.getLabel().compareTo(u2.getLabel());
	}

}

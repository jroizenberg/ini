package com.institucion.model;

import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.desktop.view.dad.DadPanel;

public class SubscripcionDaDPanel extends DadPanel<Subscripcion> {
	private static final long serialVersionUID = 1L;

	protected String getDadFilterClassName() {
		return SubscripcionDaDFilter.class.getCanonicalName();
	}

	protected DadList<Subscripcion> getDadList() {
		return new SubscripcionDaDList();
	}

}

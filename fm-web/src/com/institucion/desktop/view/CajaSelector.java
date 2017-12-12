package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class CajaSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addIngresosEgresosButton();
		addNuevoGastoCentroButton();
		
		addSeparator();
		addSeparator();
		addCerrarCajaButton();

		addExportExcelCajaButton();

	}
}
package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ProductoVentaSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {

		addVenderButton();
		
//		addExportExcelButton();
		addSeparator();
	}
}
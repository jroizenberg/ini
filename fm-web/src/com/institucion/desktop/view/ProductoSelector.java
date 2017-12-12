package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ProductoSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton();
		addUpdateButton();
//		addDeleteButton();
		addSeparator();
//		addVenderButton();
		
//		addExportExcelButton();
		addSeparator();
	}
}
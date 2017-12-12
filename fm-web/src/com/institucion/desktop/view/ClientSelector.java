package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ClientSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton();
		addUpdateButton();
//		addDeleteButton();
		addSeparator();
//		addExportExcelButton();
		addSeparator();
		
		addExportExcelClientButton();
		onExportClientTODOExcel();
	}
}
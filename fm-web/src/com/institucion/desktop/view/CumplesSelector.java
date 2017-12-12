package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class CumplesSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addVerSubsButton();
		addSaldarDeudaSubsButton();
		addAnularSubsButton();
		addSeparator();
		addClearFilterButton();
		addOnBackButton();
//		addExportExcelClientButton();

	}
}
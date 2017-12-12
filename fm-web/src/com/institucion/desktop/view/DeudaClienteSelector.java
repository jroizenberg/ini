	package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class DeudaClienteSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addSaldarDeudaSubsButton();

		addExportExcelClientButton("Exportar Deudas");

	}
	
}
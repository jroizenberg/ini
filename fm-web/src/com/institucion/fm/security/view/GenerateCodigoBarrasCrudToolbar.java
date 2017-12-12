package com.institucion.fm.security.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class GenerateCodigoBarrasCrudToolbar extends TextToolbar {

	private static final long serialVersionUID = 1L;

	public void addButtons() {
		this.addGenerarCodigoBarrasButton();
		this.addFindButton();
		this.addClearFilterButton();
	}
}

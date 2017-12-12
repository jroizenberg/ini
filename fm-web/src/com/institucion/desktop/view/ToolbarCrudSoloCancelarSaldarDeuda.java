package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;


public class ToolbarCrudSoloCancelarSaldarDeuda extends TextToolbar{
	private static final long serialVersionUID = 1L;

	public void addButtons() {
//		this.addSaveButton();
		this.addAbonarYSildarDeleteButton("Abonar y guardar");
		this.addOnBackButton();
	}
}
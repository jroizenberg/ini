package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;


public class ToolbarCrud extends TextToolbar{
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		this.addSaveButton();
		this.addOnBackButton();
	}
}
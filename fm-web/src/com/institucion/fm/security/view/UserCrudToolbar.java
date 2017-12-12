package com.institucion.fm.security.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class UserCrudToolbar extends TextToolbar {

	private static final long serialVersionUID = 1L;

	public void addButtons() {
		this.addSaveButton();
		this.addOnBackButton();
	}
}

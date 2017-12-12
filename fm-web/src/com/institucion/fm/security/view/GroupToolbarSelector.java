package com.institucion.fm.security.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class GroupToolbarSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton();
		addUpdateButton();
		addDeleteButton();
		//addSeparator();
		//addSeparator();
		//addFindButton();
		//addClearFilterButton();
	}
}

package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ToolbarSinFindFilters extends TextToolbar {
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
//		addFindButton();
		addClearFilterButton();
	}

}

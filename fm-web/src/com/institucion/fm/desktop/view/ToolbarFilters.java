package com.institucion.fm.desktop.view;

public class ToolbarFilters extends TextToolbar {
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
//		addFindAdvButton();
		addFindButton();
		addClearFilterButton();
//		addUnifySearchButton();
	}

}

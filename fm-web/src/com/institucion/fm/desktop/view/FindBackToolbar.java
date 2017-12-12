package com.institucion.fm.desktop.view;

public class FindBackToolbar extends TextToolbar{
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
		addFindButton();
		addClearFilterButton();
		addOnBackButton();
	}
}

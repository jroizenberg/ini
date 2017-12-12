package com.institucion.fm.desktop.view;


public class ToolbarSelector extends TextToolbar
{
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton();
		addUpdateButton();
		addDeleteButton();
		addSeparator();
		addFindButton();
		addClearFilterButton();
	}
}
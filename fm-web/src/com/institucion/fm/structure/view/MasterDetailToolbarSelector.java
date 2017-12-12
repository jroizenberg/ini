package com.institucion.fm.structure.view;

import com.institucion.fm.desktop.view.TextToolbar;


public class MasterDetailToolbarSelector extends TextToolbar
{
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton();
		addUpdateButton();
		addDeleteButton();
		addSaveButton();
		addSeparator();
//		addActivateButton();
//		addDesactivateButton();
		//addSeparator();
		//addFindButton();
		//addClearFilterButton();
		//addSeparator();
		//addOnBackButton();
	}
}
package com.institucion.fm.desktop.view;

public class ReduceToolbar extends TextToolbar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
		super.addSaveButton();
		super.addClearFilterButton();
		super.addOnBackButton();
		
	}

}

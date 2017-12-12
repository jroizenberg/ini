package com.institucion.fm.desktop.view;

import com.institucion.fm.desktop.service.I18N;

public class FindToolbarCloseupErrorsHP extends TextToolbar{
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
		addSaveButtonHP();
		addFindButton();
		addClearFilterButton();
	}
	
	protected void addSaveButtonHP() {
		TextToolbarButton saveButton = new TextToolbarButton(null);
		saveButton.setLabelAndTooltip(I18N.getLabel("toolbarselector.save.short"), I18N.getLabel("toolbarselector.save"));
		saveButton.setOnClickMethod("onSaveHP");
		addToolbarButton(saveButton);
	}

}

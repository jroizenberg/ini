package com.institucion.fm.desktop.view;

@Deprecated
public class ToolbarMasterDetail extends ToolbarCrud
{
	private static final long serialVersionUID = 1L;

	public ToolbarMasterDetail()
	{
		super();
		addInsertButton();
		addUpdateButton();
		addDeleteButton();
		addSaveButton();
		addSeparator();
//		addActivateButton();
//		addDesactivateButton();
//		addSeparator();
		addFindButton();
		addClearFilterButton();
		addSeparator();
		addOnBackButton();
	}
}
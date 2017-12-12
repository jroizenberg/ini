package com.institucion.fm.desktop.view;

import org.zkoss.zul.Label;

public class RequiredLabel extends Label{
	private static final long serialVersionUID = 1L;

	public RequiredLabel(String labeltext)
	{
		this.setValue(labeltext);
		this.setSclass("required");
	}
}

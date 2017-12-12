package com.institucion.fm.desktop.view;

import org.zkoss.zul.Window;

public class WindowSelector extends Window{
	public static final String ID = "selector";

	private static final long serialVersionUID = 1L;

	private String entity;

	public WindowSelector(){
		this.setId(ID);
	}

	public String getEntity() { return entity; }
	public void setEntity(String entity) { this.entity = entity; }
}
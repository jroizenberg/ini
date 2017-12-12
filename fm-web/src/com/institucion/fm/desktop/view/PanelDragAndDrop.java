package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PanelDragAndDrop extends Panel implements AfterCompose
{
	private static final long serialVersionUID = 1L;

	public static final String ID = "panelDragAndDrop";

	public PanelDragAndDrop()
	{
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);
	}

	public void afterCompose() { }

	public void add(Component cmp)
	{
		this.getPanelchildren().appendChild(cmp);
	}
}
package com.institucion.fm.desktop.view;

import org.zkoss.zul.Listitem;

public class GridViewList extends GridList{

	private static final long serialVersionUID = 1L;
	
	public void addRow(Listitem item){
		item.setClass("cells");
//		Component nullComponent = null;
//		item.addForward("onDoubleClick",nullComponent,"onDoubleClickEvt");
		this.appendChild(item);
	}
}
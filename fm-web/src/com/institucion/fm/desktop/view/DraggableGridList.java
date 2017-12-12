package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listitem;

public class DraggableGridList extends GridList {
	private static final long serialVersionUID = 1L;

	public DraggableGridList() {
		super();
		/* head
		 * droppable="true" onDrop="move(event.dragged)"
		 * 
		 * item
		 * draggable="true" droppable="true" onDrop="move(event.dragged)"
		 */
		//log.debug("adding drag&drop feature");
		this.getListhead().addForward("onDrop",(Component)null,"onDrop");
		this.getListhead().setDroppable("true");
	}

	public void addRow(Listitem item) {
		item.setDraggable("true");
		item.setDroppable("true");
		item.addForward("onDrop",(Component)null,"onDrop");
		super.addRow(item);		
	}
}

package com.institucion.fm.security.model;

import java.util.Collection;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.DraggableGridList;
import com.institucion.fm.security.model.Group;

public class DraggableGroupList extends DraggableGridList {
	private static final long serialVersionUID = 1L;

	public DraggableGroupList() {
		super();
		this.addHeader(new Listheader(I18N.getLabel("crud.user.group")));
	}
	
	public void setList(Collection<Group> groups) {
		//log.debug("draggID: " + this.getId());
		this.removeAll();

		if (groups == null) {
			return;
		}
		for (Group group : groups) {
			Listitem row = new Listitem();
			row.setValue(group.getId());
			Listcell nameCell = new Listcell(group.getName());
//			nameCell.setImage("/img/icon/pharmacy_16.png");
//			nameCell.setStyle("padding-left: 10px");
			row.appendChild(nameCell);
			this.addRow(row);
		}
		((Listheader)this.getListhead().getFirstChild()).sort(true);
	}
	
}

package com.institucion.fm.security.model;

import java.util.Collection;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.DraggableGridList;
import com.institucion.fm.security.model.Permission;

@Deprecated
public class DraggablePermissionList extends DraggableGridList {
	private static final long serialVersionUID = 1L;

	public DraggablePermissionList() {
		super();
		this.addHeader(new Listheader(I18N.getLabel("crud.group.permission")));
	}
	
	public void setList(Collection<Permission> perms) {
		//log.debug("draggID: " + this.getId());
		this.removeAll();

		if (perms == null) {
			return;
		}

		for (Permission permission : perms) {
			Listitem row = new Listitem();
			String text = I18N.getStringLabel(permission.getDescription());
			//log.debug("permission [" + permission.getDescription() + "][" + text +"]");
			row.setValue(permission.getId());
			Listcell nameCell = new Listcell(text);
			row.appendChild(nameCell);
			this.addRow(row);
		}
		((Listheader)this.getListhead().getFirstChild()).sort(true);
	}
	
}

package com.institucion.fm.security.model;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.security.model.Permission;

public class PermissionDaDList  extends DadList<Permission> {
	private static final long serialVersionUID = 1L;

	protected void addHeaders() {
		this.addHeader(new Listheader(I18N.getLabel("crud.alarm.users")));
	}

	protected void addRow(Permission item) {
		Listitem row = new Listitem();
		String text = I18N.getStringLabel(item.getDescription());
		//log.debug("permission [" + item.getDescription() + "][" + text +"]");
		row.setValue(item);
		Listcell nameCell = new Listcell(text);
		row.appendChild(nameCell);
		this.addRow(row);
	}

	protected boolean areEquals(Permission item1, Permission item2) {
		if (item1 == item2 && item1 == null) {
			return true;
		} else if (item1 == null) {
			return false;
		}
		if (item1.getId().equals(item2.getId())) {
			return true;
		}
		return false;
	}

}

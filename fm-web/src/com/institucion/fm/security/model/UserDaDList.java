package com.institucion.fm.security.model;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.security.model.User;

public class UserDaDList extends DadList<User> {
	private static final long serialVersionUID = 1L;

	protected void addHeaders() {
		this.addHeader(new Listheader(I18N.getLabel("header.user.fullname")));
	}

	protected void addRow(User item) {
		Listitem row = new Listitem();
		row.setValue(item);
		Listcell nameCell = new Listcell(item.getFullName());
		row.appendChild(nameCell);
		addRow(row);
	}

	protected boolean areEquals(User item1, User item2) {
		if (item1 == item2 && item1 == null) {
			return true;
		} else if (item1 == null) {
			return false;
		}
		if (item1.getFullName().equals(item2.getFullName())) {
			return true;
		}
		return false;
	}
	
}

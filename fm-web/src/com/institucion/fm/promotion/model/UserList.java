package com.institucion.fm.promotion.model;

import java.util.Collection;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.security.model.User;

public class UserList extends GridList {
	private static final long serialVersionUID = 1L;

	public UserList() {
		super();
		this.addHeader(new Listheader(I18N.getLabel("header.user.username")));
		this.addHeader(new Listheader(I18N.getLabel("header.user.fullname")));
	}
	
	public void setList(Collection<User> users) {
		//log.debug("draggID: " + this.getId());
		this.removeAll();

		if (users == null) {
			return;
		}
		for (User user : users) {
			Listitem row = new Listitem();
			row.setValue(user.getId());
			Listcell usernameCell = new Listcell(user.getName());
//			usernameCell.setImage("/img/icon/pharmacy_16.png");
//			usernameCell.setStyle("padding-left: 10px");
			row.appendChild(usernameCell);
			Listcell nameCell = new Listcell(user.getFullName());
			row.appendChild(nameCell);
			this.addRow(row);
		}
	}
	
}


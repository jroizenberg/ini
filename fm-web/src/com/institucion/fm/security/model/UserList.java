package com.institucion.fm.security.model;

import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.helper.UserStateViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.security.model.User;
public class UserList extends GridList {
	private static final long serialVersionUID = 1L;

	public UserList() {
		super();
		Listheader header = new Listheader(I18N.getLabel("header.user.name"));
		this.addHeader(header);
		this.addHeader(new Listheader(I18N.getLabel("header.user.fullname")));
		this.addHeader(new Listheader(I18N.getLabel("header.user.state")));
	}
	
	public void setList(List<User> users) {
		this.removeAll();
		for (User user : users) {
			Listitem row = new Listitem();
			row.setValue(user);
			Listcell nameCell = new Listcell(user.getName());
//			nameCell.setImage("/img/icon/pharmacy_16.png");
//			nameCell.setStyle("padding-left: 10px");
			row.appendChild(nameCell);
			row.appendChild(new Listcell(user.getFullName()));
			row.appendChild(new Listcell(UserStateViewHelper.getStateString(user.getState())));
			super.addRow(row);
		}
	}

}

package com.institucion.fm.security.model;

import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.helper.GroupRoleViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.security.model.Group;

public class GroupList extends GridList {
	private static final long serialVersionUID = 1L;
	
	public GroupList() {
		super();
		this.addHeader(new Listheader(I18N.getLabel("header.group.name")));
		this.addHeader(new Listheader(I18N.getLabel("header.group.description")));
		this.addHeader(new Listheader(I18N.getLabel("header.group.role")));
	}
	
	public void setList(List<Group> groups) {
		this.removeAll();
		for (Group group : groups) {
			Listitem row = new Listitem();
			row.setValue(group.getId());
			Listcell nameCell = new Listcell(group.getName());
//			nameCell.setImage("/img/icon/pharmacy_16.png");
//			nameCell.setStyle("padding-left: 10px");
			row.appendChild(nameCell);
			row.appendChild(new Listcell(group.getDescription()));
			row.appendChild(new Listcell(GroupRoleViewHelper.getRoleString(group.getRole())));
			super.addRow(row);
		}
	}

}

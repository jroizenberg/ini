package com.institucion.fm.security.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.helper.GroupRoleViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.security.model.RoleType;

public class GroupCrud extends GridCrud {
	private static final long serialVersionUID = 1L;
	private Textbox name;
	private Textbox description;
	private Combobox role;
	public static final String SESSION_GROUP_ID = "session_group_id";
	
	public GroupCrud()
	{
		super();
		this.makeFields();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public void setRole(RoleType role) {
		this.role.setSelectedItem(GroupRoleViewHelper.getItem(this.role, role));
	}

	public String getName() {
		return this.name.getValue();
	}
	
	public String getDescription() {
		return this.description.getValue();
	}
	
	public RoleType getRole() {
		this.role.getValue(); // WorkAround: ESTO es para que salte el constraint. El geteSelectedItem no lo ejecuta y el getValue si
		return (RoleType)this.role.getSelectedItem().getValue();
	}
	
	public void setFocus() {
		name.focus();
	}
	
	private void makeFields() {
		name = new Textbox();
		name.setMaxlength(20);
		name.setWidth("98%");
		name.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY));
		this.addField(new RequiredLabel(I18N.getLabel("crud.group.name")), name);
		
		description = new Textbox();
		description.setMaxlength(100);
		description.setWidth("98%");
		description.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("crud.group.description")), description);
		
		role = GroupRoleViewHelper.getComboBox();
		role.setConstraint("no empty, strict");
		role.setWidth("91%");
		this.addField(new RequiredLabel(I18N.getLabel("crud.group.role")), role);
	}
}

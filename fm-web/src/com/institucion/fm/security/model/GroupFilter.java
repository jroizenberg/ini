package com.institucion.fm.security.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.helper.GroupRoleViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.RoleType;

public class GroupFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Textbox name;
	private Textbox description;
	private Combobox role;

	public GroupFilter() {
		super();
		this.buildFilter();
	}

	private void buildFilter() {
		Row row = new Row();
		row.appendChild(new Label(I18N.getLabel("header.group.name")));
		this.name = new Textbox();
		name.setConstraint(new TextConstraint());
		name.setMaxlength(20);
		row.appendChild(name);

		row.appendChild(new Label(I18N.getLabel("header.group.description")));
		this.description = new Textbox();
		description.setConstraint(new TextConstraint());
		description.setMaxlength(100);
		row.appendChild(description);
		
		row.appendChild(new Label(I18N.getLabel("header.group.role")));
		role = GroupRoleViewHelper.getComboBox();
		row.appendChild(role);
		this.addRow(row);
	}
	
	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();

		this.setPredicate(criteria, name, "name");
		this.setPredicate(criteria, description, "description");
		if (role.getSelectedIndex() >= 0)
			this.setPredicate(criteria, (RoleType)role.getSelectedItem().getValue(), "role");

		return criteria;	
	}

	public void clear() {
		name.setText("");
		description.setText("");
		role.setText("");
	}

}

package com.institucion.fm.security.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.helper.UserStateViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.UserState;

public class UserFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Textbox name;
	private Textbox firstName;
	private Textbox lastName;
	private Combobox state;

	public UserFilter() {
		super();
		this.buildFilter();
	}

	private void buildFilter() {
		Row row1 = new Row();
		row1.appendChild(new Label(I18N.getLabel("header.user.name")));
		this.name = new Textbox();
		name.setConstraint(new TextConstraint());
		name.setMaxlength(20);
		row1.appendChild(name);

		row1.appendChild(new Label(I18N.getLabel("header.user.firstname")));
		this.firstName = new Textbox();
		firstName.setConstraint(new TextConstraint());
		firstName.setMaxlength(50);
		row1.appendChild(firstName);
		this.addRow(row1);

		Row row2 = new Row();
		row2.appendChild(new Label(I18N.getLabel("header.user.lastname")));
		this.lastName = new Textbox();
		lastName.setConstraint(new TextConstraint());
		lastName.setMaxlength(50);
		row2.appendChild(lastName);
		
		row2.appendChild(new Label(I18N.getLabel("header.user.state")));
		state = UserStateViewHelper.getComboBox();
		row2.appendChild(state);
		this.addRow(row2);
	}
	
	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();

		this.setPredicate(criteria, name, "name");
		this.setPredicate(criteria, firstName, "firstName");
		this.setPredicate(criteria, lastName, "lastName");
		this.setPredicate(criteria, firstName, "firstName");

		if (state.getSelectedIndex() >= 0){
			this.setPredicate(criteria, (UserState)state.getSelectedItem().getValue(), "state");
		}
		return criteria;	
	}

	public void clear() {
		name.setText("");
		firstName.setText("");
		lastName.setText("");
		state.setText("");
	}

}

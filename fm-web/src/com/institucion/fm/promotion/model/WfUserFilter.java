package com.institucion.fm.promotion.model;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class WfUserFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Textbox firstName;
	private Textbox lastName;

	public WfUserFilter() {
		super();
		this.buildFilter();
	}

	private void buildFilter() {
		Row row = new Row();
		row.appendChild(new Label(I18N.getLabel("productline.filter.FirstName")));
		this.firstName = new Textbox();
		this.firstName.setConstraint(new TextConstraint());
		this.firstName.setMaxlength(50);
		row.appendChild(this.firstName);

		row.appendChild(new Label(I18N.getLabel("productline.filter.LastName")));
		this.lastName = new Textbox();
		this.lastName.setConstraint(new TextConstraint());
		this.lastName.setMaxlength(50);
		row.appendChild(this.lastName);

		this.addRow(row);
	}
	
	public void clear() {
		firstName.setText("");
		lastName.setText("");
	}

	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();

		this.setPredicate(criteria, firstName, "firstName");
		this.setPredicate(criteria, lastName, "lastName");
		
		return criteria;
	}

}

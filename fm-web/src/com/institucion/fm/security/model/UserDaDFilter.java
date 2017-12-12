package com.institucion.fm.security.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.filteradv.model.Predicate;

public class UserDaDFilter extends DadFilter {
	private static final long serialVersionUID = 1L;

	protected Label getMasterFieldLabel() {
		return new Label(I18N.getLabel("filter.wfuser.firstname"));
	}

	protected String getOnFindEventName() {
		return "onFind";
	}

	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();
		Predicate firstnamePredicate = super.setPredicate(criteria, this.getMaster(), "firstname");
		if (firstnamePredicate != null)
			firstnamePredicate.setBitwise(Predicate.OR_BITWISE);
		super.setPredicate(criteria, this.getMaster(), "lastname");
		return criteria;
	}
	
}

package com.institucion.fm.security.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class PermissionDaDFilter extends DadFilter {
	private static final long serialVersionUID = 1L;

	protected Label getMasterFieldLabel() {
		return new Label(I18N.getLabel("crud.group.permission"));
	}

	protected String getOnFindEventName() {
		return "onFind";
	}

	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();
		super.setPredicate(criteria, this.getMaster(), "description");
		return criteria;
	}

}

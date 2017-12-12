package com.institucion.fm.contact.view;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.bb.BandBoxFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.filteradv.model.Predicate;

public class ApmBbFilter extends BandBoxFilter {

	
	private static final long serialVersionUID = 1L;

	public ApmBbFilter() {
		super();
	}
	@Override
	protected Label getMasterFieldLabel() {
		return new Label(I18N.getLabel("header.hpa.name"));
	}

	@Override
	protected String getOnFindEventName() {
		return "onFindHpa";
	}

	@Override
	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();
		setPredicate(criteria, this.getMaster(),"u.lastName", "u.firstName", "u.name", Predicate.OR_BITWISE, Predicate.OR_BITWISE, Predicate.OR_BITWISE);
		return criteria;
	}
	
	public String getText(){
		return this.getMaster().getValue();
	}
	
}

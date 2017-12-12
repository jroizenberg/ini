package com.institucion.fm.security.model;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.dad.DadFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class ClienteDaDFilter extends DadFilter {
	private static final long serialVersionUID = 1L;
	private Textbox apellido;
	private Checkbox conCupos;

	public CriteriaClause getCriteriaFilters() {
		CriteriaClause criteria = new CriteriaClause();
//		Predicate namePredicate = super.setPredicate(criteria, this.getMaster(), "name");
//		if (namePredicate != null)
//			namePredicate.setBitwise(Predicate.AND_BITWISE);
//		super.setPredicate(criteria, FEState.ACTIVE, "state");
//		super.setPredicate(criteria, HardDomainTable.SPECIALITY_ID.intValue(), "domainTable.id");
		return criteria;
	}
	

	@Override
	protected Label getMasterFieldLabel() {
		return new Label(I18N.getLabel("header.user.lastname"));
	
	}

	@Override
	protected String getOnFindEventName() {
		
		return "onFind";
	}
	
	public Textbox getApellido() {
		return apellido;
	}


	public void setApellido(Textbox apellido) {
		this.apellido = apellido;
	}


	protected void buildFilter() {
		super.buildFilter();
		Row row = new Row();
		row.appendChild(new Label(I18N.getLabel("header.user.firstname")));
		apellido= new Textbox();
		apellido.setConstraint(new TextConstraint());
		apellido.setMaxlength(20);
		apellido.addForward("onOK", (Component) null, this.getOnFindEventName());

		row.appendChild(apellido);
		
		row.appendChild(new Label("Mostrar solo con cupos Disponibles:"));
		conCupos= new Checkbox();
		conCupos.addForward("onCheck", (Component) null, this.getOnFindEventName());
		conCupos.setChecked(true);
		row.appendChild(conCupos);
		this.addRow(row);
		
	}


	public Checkbox getConCupos() {
		return conCupos;
	}


	public void setConCupos(Checkbox conCupos) {
		this.conCupos = conCupos;
	}
	


}

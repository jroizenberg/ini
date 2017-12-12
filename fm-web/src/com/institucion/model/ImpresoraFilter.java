package com.institucion.model;

import org.zkoss.zul.Constraint;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class ImpresoraFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;

	public ImpresoraFilter()	{
		super();

		buildFilter();
		
	}

	private void buildFilter() {
		
		Row row1 = new Row();
	
		String nameLabel = I18N.getLabel("client.nombre");
		row1.appendChild(new RequiredLabel(nameLabel));
		nombre = new Textbox();
		nombre.setConstraint(new TextConstraint());
		row1.appendChild(nombre);
		
		this.addRow(row1);
	}

	public boolean validateHaveFilters(){
		
		if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
			return true;
		}
	

		return false;
	}
	
	
	
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, nombre, "companyName");

		return criteria;
	}

	public void clear()
	{
		Constraint c;
		
		c =nombre.getConstraint();
		nombre.setConstraint("");
		nombre.setText(null);
		nombre.setConstraint(c);
	
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

}
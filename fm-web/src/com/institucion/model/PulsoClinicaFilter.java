package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.PulsoDelegate;
import com.institucion.desktop.helper.GastosViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class PulsoClinicaFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Combobox anio;   
	private PulsoDelegate delegate;	

	public PulsoClinicaFilter()	{
		super();
		buildFilter();
	}
	
	private void buildFilter() {
				
		Row row3 = new Row();
			
		String estadoLabel =  I18N.getLabel("gastos.anio")+ "/ Temporada :";
		row3.appendChild(new RequiredLabel(estadoLabel));
		anio = GastosViewHelper.getComboBoxAnio();
		anio.setConstraint("strict");
		anio.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar(evt);
			}
		});	

		row3.appendChild(anio);
			
		this.addRow(row3);

	}
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, anio, "nombre");
		
		return criteria;
	}

	public boolean validateHaveFilters(){
	
		if (anio.getSelectedIndex() >= 0) {
			return true;
		}

		return false;
	}
	
	public String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder("select gas.id  from gastos gas   ");
			actionConditions.append("where 1=1  ");
		
			if (anio.getSelectedIndex() >= 0) {
				Long stateType= ((Long)anio.getSelectedItem().getValue());
				actionConditions.append(" and gas.anio= "+stateType);
			}
						
		return actionConditions.toString();
	}
	
	
	public void clear(){
		Constraint c;
	
		c= anio.getConstraint();
		anio.setConstraint("");
		anio.setSelectedItem(null);
		anio.setConstraint(c);
		
	}

	public Combobox getAnio() {
		return anio;
	}

	public void setAnio(Combobox anio) {
		this.anio = anio;
	}

	public PulsoDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(PulsoDelegate delegate) {
		this.delegate = delegate;
	}



}
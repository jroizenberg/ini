package com.institucion.model;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class EmpleadoFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private CursosCrudDelegate delegate;

	public EmpleadoFilter()	{
		super();

		buildFilter();
	}

	private void buildFilter() {
		
		Row row1 = new Row();
	
		String nameLabel = I18N.getLabel("client.nombre");
		row1.appendChild(new RequiredLabel(nameLabel));
		nombre = new Textbox();
		nombre.addEventListener(Events.ON_CHANGING, new EventListener() {
			public void onEvent(Event evt){
				nombre.setValue(((InputEvent)evt).getValue());	

				delegate.buscar();
			}
		});	

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
	
	
	public String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder( "select id from empleado clase  ");
			actionConditions.append("where 1=1  ");
				
			if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
				actionConditions.append(" and  Upper(clase.nombre) like Upper('%"+nombre.getValue()+"%') ");
			}	
			actionConditions.append(" order by clase.nombre ");
		return actionConditions.toString();
	}
	
	public static Combobox getComboBoxActividad(List<Actividad> actividades, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		for (Actividad actividad : actividades) {
			Comboitem item;
			item = new Comboitem(actividad.getNombre());
			item.setValue(actividad);
			act.appendChild(item);	
		}

		return act;
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

	public CursosCrudDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosCrudDelegate delegate) {
		this.delegate = delegate;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

}
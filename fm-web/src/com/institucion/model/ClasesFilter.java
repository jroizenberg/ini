package com.institucion.model;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.desktop.helper.CantidadDeDiasClasesViewHelper;
import com.institucion.desktop.helper.DiasDeSemanaViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class ClasesFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Combobox diaSemana;
	private Combobox horaInicio;
	private Combobox curso;    // estos se obtienen directamente de un enum
	private CursoEJB cursoEJB;
	private Combobox disponible;
	private CursosCrudDelegate delegate;

	public void desabilitarDisponible(){
		disponible.setVisible(false);
		disponible.setSelectedItem(null);
	}

	public ClasesFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");

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
		
		String diaSemanaLabel = I18N.getLabel("clase.diaSemana");
		row1.appendChild(new Label(diaSemanaLabel));
		
		diaSemana = DiasDeSemanaViewHelper.getComboBox();
		diaSemana.setConstraint("strict");
		diaSemana.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row1.appendChild(diaSemana);
		
		String diaSemanaHoraLabel = I18N.getLabel("clase.diaSemanaHora");
		row1.appendChild(new Label(diaSemanaHoraLabel));
		
		horaInicio= CantidadDeDiasClasesViewHelper.gethorariosClasesComboBox();
		horaInicio.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	
		horaInicio.setConstraint("strict");
		row1.appendChild(horaInicio);
		
		this.addRow(row1);

		Row row2 = new Row();

		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row2.appendChild(new Label(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row2.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		String dispLabel =  "Disponible";
		row2.appendChild(new Label(dispLabel));
		disponible = BooleanViewHelper.getComboBox();
		disponible.setConstraint("strict");
		disponible.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row2.appendChild(disponible);

		
		this.addRow(row2);
	}

	public boolean validateHaveFilters(){
		
		if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
			return true;
		}
		
		if (horaInicio.getSelectedIndex() >= 0) {
			return true;
		}

		if (diaSemana.getSelectedIndex() >= 0) {
			return true;
		}

		if (curso.getSelectedIndex() >= 0) {
			return true;		
			}
		if (disponible.getSelectedIndex() >= 0) {
			return true;		
		}

		return false;
	}
	
	
	public String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder( "select id from clase clase  ");
			actionConditions.append("where 1=1  ");
					
				if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(clase.nombre) like Upper('%"+nombre.getValue()+"%') ");
				}
			
				if (curso.getSelectedIndex() >= 0) {
					Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
					actionConditions.append(" and clase.idactividad= '"+stateType.getId()+"' ");
				}
				
				if (horaInicio.getSelectedIndex() >= 0) {
					String stateType= ((String)horaInicio.getSelectedItem().getValue());
					actionConditions.append(" and EXTRACT(HOUR FROM clase.horadesde) = '"+stateType+"' ");
				}

				if (disponible.getSelectedIndex() >= 0) {
					Boolean stateType= ((Boolean)disponible.getSelectedItem().getValue());
					actionConditions.append(" and clase.disponible is "+stateType );
				}		

				if (diaSemana.getSelectedIndex() >= 0) {
					Integer stateType= ((Integer)diaSemana.getSelectedItem().getValue());
					String dia=null;
					if(stateType == 0){
						dia="domingo";
					}else if(stateType == 1){
						dia="lunes";
					}else if(stateType == 2){
						dia="martes";
					}else if(stateType == 3){
						dia="miercoles";
					}else if(stateType == 4){
						dia="jueves";
					}else if(stateType == 5){
						dia="viernes";
					}else{
						dia="sabado";
					}
					actionConditions.append(" and clase."+dia+ " is true ");

				}
				
				actionConditions.append(" order by lunes is true desc,martes is true desc,miercoles is true desc,jueves is true desc,viernes is true desc,sabado is true desc,domingo is true desc, " );
				actionConditions.append(" date_part('hour', clase.horadesde) ");
				
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
		

		c= horaInicio.getConstraint();
		horaInicio.setConstraint("");
		horaInicio.setSelectedItem(null);
		horaInicio.setConstraint(c);
		
		c= diaSemana.getConstraint();
		diaSemana.setConstraint("");
		diaSemana.setSelectedItem(null);
		diaSemana.setConstraint(c);
		
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		c = disponible.getConstraint();
		disponible.setConstraint("");
		disponible.setSelectedItem(null);
		disponible.setConstraint(c);

	}

	public Combobox getCurso() {
		return curso;
	}

	public void setCurso(Combobox curso) {
		this.curso = curso;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

	public Combobox getDisponible() {
		return disponible;
	}

	public void setDisponible(Combobox disponible) {
		this.disponible = disponible;
	}

	public CursosCrudDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosCrudDelegate delegate) {
		this.delegate = delegate;
	}

}
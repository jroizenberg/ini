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
import com.institucion.desktop.helper.CursosVencimientoViewHelper;
import com.institucion.desktop.helper.TipoCursosViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CursosFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Combobox pagaSubscripcion;
	private Combobox tipoCurso; // estos se obtienen directamente de un enum
	private Combobox curso; // estos se obtienen directamente de un enum
	private Combobox vencimiento; // estos se obtienen directamente de un enum
//	private Combobox cursoSinClasesConfiguradas; // estos se obtienen directamente de un enum
	private Combobox disponible;
	private CursosCrudDelegate delegate;	

	private CursoEJB cursoEJB;

	public CursosFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");

		buildFilter();
	}
	
	
	public void desabilitarDisponible(){
		disponible.setVisible(false);
		disponible.setSelectedItem(null);
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

		
		String tipoCursoLabel =  I18N.getLabel("client.tipo.curso");
		row1.appendChild(new Label(tipoCursoLabel));
		tipoCurso = TipoCursosViewHelper.getComboBox();
		tipoCurso.setConstraint("strict");
		tipoCurso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row1.appendChild(tipoCurso);
		
		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row1.appendChild(new Label(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row1.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		this.addRow(row1);

		Row row7 = new Row();
			
		String vencLabel =  I18N.getLabel("curso.vencimiento");
		row7.appendChild(new Label(vencLabel));
		
		vencimiento = CursosVencimientoViewHelper.getComboBox();
		vencimiento.setConstraint("strict");
		vencimiento.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row7.appendChild(vencimiento);

		
		String estadoLabel =  I18N.getLabel("curso.pagaSubscripcion");
		row7.appendChild(new Label(estadoLabel));
		pagaSubscripcion = BooleanViewHelper.getComboBox();
		pagaSubscripcion.setConstraint("strict");
		pagaSubscripcion.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	


//		pagaSubscripcion.setWidth("50%");
		row7.appendChild(pagaSubscripcion);
		
		String dispLabel =  "Disponible";
		row7.appendChild(new Label(dispLabel));
		disponible = BooleanViewHelper.getComboBox();
		disponible.setConstraint("strict");
		disponible.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row7.appendChild(disponible);

		this.addRow(row7);

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
	
	public boolean validateHaveFilters(){
		
		if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
			return true;
		}
		
		if (pagaSubscripcion.getSelectedIndex() >= 0) {
			return true;
		}

		if (tipoCurso.getSelectedIndex() >= 0) {
			return true;
		}

		if (curso.getSelectedIndex() >= 0) {
			return true;		
		}
		
		if (vencimiento.getSelectedIndex() >= 0) {
			return true;		
		}
		
		if (disponible.getSelectedIndex() >= 0) {
			return true;		
		}
		return false;
	}

	public String getFilters(boolean isFromSubscripcion){
					
		StringBuilder actionConditions= new StringBuilder( "select * from curso curso   ");
				actionConditions.append(" inner join ActividadYClase ac on (ac.idcurso=curso.id)  "); 

				actionConditions.append(" where 1=1  ");
				
				if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(curso.nombre) like Upper('%"+nombre.getValue()+"%') ");
				}
			
				if (pagaSubscripcion.getSelectedIndex() >= 0) {
					Boolean stateType= ((Boolean)pagaSubscripcion.getSelectedItem().getValue());
					actionConditions.append(" and curso.pagasubscripcion is "+stateType );
				}

				if(isFromSubscripcion){
					actionConditions.append(" and curso.disponible is true");
				}else{
					if (disponible.getSelectedIndex() >= 0) {
						Boolean stateType= ((Boolean)disponible.getSelectedItem().getValue());
						actionConditions.append(" and curso.disponible is "+stateType );
					}		
				}
				if (tipoCurso.getSelectedIndex() >= 0) {
					TipoCursoEnum stateType= ((TipoCursoEnum)tipoCurso.getSelectedItem().getValue());
					actionConditions.append(" and curso.idtipocurso='"+stateType.toInt() +"' ");
				}
				
				if (vencimiento.getSelectedIndex() >= 0) {
					VencimientoCursoEnum stateType= ((VencimientoCursoEnum)vencimiento.getSelectedItem().getValue());
					actionConditions.append(" and curso.vencimiento='"+stateType.toInt() +"' ");
				}		
				
				if (curso.getSelectedIndex() >= 0) {
					
					Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
					actionConditions.append("   and  curso.id in   " +
							" (select ActividadYClase.idcurso from ActividadYClase where ActividadYClase.idActividad='"+stateType.getId()+"') ");
				}
			
					
				actionConditions.append(" order by curso.descripcion, ac.cantclases  desc ");

		
		
				
		return actionConditions.toString();
	}

	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, nombre, "companyName");

		return criteria;
	}

	
	public void clear()	{
		Constraint c;
		
		c =nombre.getConstraint();
		nombre.setConstraint("");
		nombre.setText(null);
		nombre.setConstraint(c);
		
		c = pagaSubscripcion.getConstraint();
		pagaSubscripcion.setConstraint("");
		pagaSubscripcion.setSelectedItem(null);
		pagaSubscripcion.setConstraint(c);
				
		c = disponible.getConstraint();
		disponible.setConstraint("");
		disponible.setSelectedItem(null);
		disponible.setConstraint(c);
		
		c= tipoCurso.getConstraint();
		tipoCurso.setConstraint("");
		tipoCurso.setSelectedItem(null);
		tipoCurso.setConstraint(c);
		
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		c= vencimiento.getConstraint();
		vencimiento.setConstraint("");
		vencimiento.setSelectedItem(null);
		vencimiento.setConstraint(c);
	
//		c= cursoSinClasesConfiguradas.getConstraint();
//		cursoSinClasesConfiguradas.setConstraint("");
//		cursoSinClasesConfiguradas.setSelectedItem(null);
//		cursoSinClasesConfiguradas.setConstraint(c);
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
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
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CursosFilter2 extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Textbox nombre;
	private Combobox pagaSubscripcion;
//	private Combobox tipoCurso; // estos se obtienen directamente de un enum
	private Combobox curso; // estos se obtienen directamente de un enum
//	private Combobox vencimiento; // estos se obtienen directamente de un enum
//	private Combobox cursoSinClasesConfiguradas; // estos se obtienen directamente de un enum
//	private Combobox disponible;

	private CursoEJB cursoEJB;
	private ClienteDelegate actionComposerDelegate;	

	public CursosFilter2()	{
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
				actionComposerDelegate.buscar(evt, false);
			}
		});	

	
		nombre.setConstraint(new TextConstraint());
		row1.appendChild(nombre);
		
		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row1.appendChild(new Label(cursoLabel));
		
		curso= new Combobox();
		curso.setConstraint("strict");
		curso.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				actionComposerDelegate.buscar(evt, false);
			}
		});
		row1.appendChild(curso);
		
		List<Actividad> actividades=cursoEJB.findAllActividad();
		if(actividades != null){
			setCurso(getComboBoxActividad(actividades, curso));						
		}
		
		boolean esMaipu=false;
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
					esMaipu= true;
				}
			}
		}

		if(!esMaipu){
			String estadoLabel =  I18N.getLabel("curso.pagaSubscripcion");
			row1.appendChild(new Label(estadoLabel));
			pagaSubscripcion = BooleanViewHelper.getComboBox();
			pagaSubscripcion.setConstraint("strict");
			pagaSubscripcion.addEventListener(Events.ON_CHANGE, new EventListener() {
				public void onEvent(Event evt){
					actionComposerDelegate.buscar(evt, false);
				}
			});
			row1.appendChild(pagaSubscripcion);	
		}
		this.addRow(row1);
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

		if (curso.getSelectedIndex() >= 0) {
			return true;		
		}
		return false;
	}

	public String getFilters(boolean isFromSubscripcion, boolean esMaipu, boolean esCentro, String nombrechanging){
					
		StringBuilder actionConditions= new StringBuilder( "select * from curso curso   ");
				actionConditions.append(" inner join ActividadYClase ac on (ac.idcurso=curso.id)  "); 

				actionConditions.append(" where 1=1  ");
				
				if(nombrechanging != null){
					actionConditions.append(" and  Upper(curso.nombre) like Upper('%"+nombrechanging+"%') ");
				
				}else if (nombre.getValue()!= null && !nombre.getValue().trim().equals("") && !nombre.getValue().trim().equals(" ")) {
					actionConditions.append(" and  Upper(curso.nombre) like Upper('%"+nombre.getValue()+"%') ");
				}
			
				if (pagaSubscripcion.getSelectedIndex() >= 0) {
					Boolean stateType= ((Boolean)pagaSubscripcion.getSelectedItem().getValue());
					actionConditions.append(" and curso.pagasubscripcion is "+stateType );
				}

				actionConditions.append(" and curso.disponible is true");

				if(esMaipu){
					actionConditions.append(" and curso.nombre ilike '%ini%'   ");
				}else if (esCentro){
					actionConditions.append(" and curso.nombre not ilike '%ini%'   ");
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
				
		c= curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
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

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}


}
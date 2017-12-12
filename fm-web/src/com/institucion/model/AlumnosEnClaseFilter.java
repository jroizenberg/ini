package com.institucion.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.model.User;

public class AlumnosEnClaseFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Datebox fecha1;
	private Label dia;
	private Combobox curso; 
	private Checkbox consultarPorFechaExacta;
	private Label labelFecha;
	private SecurityAAEJB securityService;

	public AlumnosEnClaseFilter()	{
		super();
		securityService= BeanFactory.<SecurityAAEJB>getObject("fm.ejb.securityAAService");
		buildFilter();
		

	}

	public SecurityAAEJB getSecurityService(){
		return securityService;
	}

	public Combobox getComboBoxCurso(List<Actividad> actividades, Combobox act) {
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
	
	private void buildFilter() {
	
		Row row5 = new Row();
		
		String cursoLabel2 =  I18N.getLabel("client.curso.clases.del.dia.de.la.semana");
		row5.appendChild(new RequiredLabel(cursoLabel2));
	

		String dia33=" ";

		Date fechas2=new Date();
		if(fechas2 != null){
		
			Integer stateType= fechas2.getDay();
			if(stateType == 0){
				dia33="domingo";
			}else if(stateType == 1){
				dia33="lunes";
			}else if(stateType == 2){
				dia33="martes";
			}else if(stateType == 3){
				dia33="miercoles";
			}else if(stateType == 4){
				dia33="jueves";
			}else if(stateType == 5){
				dia33="viernes";
			}else{
				dia33="sabado";
			}
		
			dia = new RequiredLabel(dia33);
			dia.setAttribute("numeroDia", fechas2.getDay());
		}
		
		row5.appendChild(dia);

		String cursoLabel =  I18N.getLabel("client.curso.actividad");
		row5.appendChild(new Label(cursoLabel));
		curso = new Combobox();
		curso.setConstraint("strict");
		
		row5.appendChild(curso);
		

		this.addRow(row5);
		
		Row row6 = new Row();

		row6.appendChild(new Label("Obtener Clases Finalizadas por Fecha"));
		this.consultarPorFechaExacta= new Checkbox();
		this.consultarPorFechaExacta.setChecked(false);
		this.consultarPorFechaExacta.addEventListener(Events.ON_CHECK, new EventListener()		{
			public void onEvent(Event evt){
				if(consultarPorFechaExacta.isChecked()){
					fecha1.setVisible(true);
					fecha1.setValue(new Date());
					labelFecha.setVisible(true);
				}else{
					fecha1.setVisible(false);
					labelFecha.setVisible(false);
				}
			}
		});
		
		
		//
		if(validaPermisos()){
			consultarPorFechaExacta.setDisabled(true);
		}
		row6.appendChild(consultarPorFechaExacta);
		
		fecha1 = new Datebox();
		fecha1.setMaxlength(20);
		fecha1.setFormat(I18N.getDateFormat());
		fecha1.setVisible(false);
		
		
		String tipoCursoLabel =  I18N.getLabel("client.tipo.curso.fechass");
		labelFecha=new Label(tipoCursoLabel);
		row6.appendChild(labelFecha);
		row6.appendChild(fecha1);

		
		this.addRow(row6);

	}


	private boolean validaPermisos(){
		User usuario=getSecurityService().getUserWs(Session.getUsername());
		if(usuario != null && usuario.getGroups() != null){
			for (com.institucion.fm.security.model.Group iterable_element : usuario.getGroups()) {
				if(iterable_element.getName().equalsIgnoreCase("Todos los Permisos") 
						|| iterable_element.getName().equalsIgnoreCase("Gerente"))
					return false;
			}
		}
		return true;
	}
	
	public boolean validateHaveFilters(){
		
		if (fecha1.getValue()!= null) {
			return true;
		}
	
		if (curso.getSelectedIndex() >= 0) {
			return true;		
		}

		if(consultarPorFechaExacta.isChecked())
			return true;
		
		return false;
	}
	
	
	public String getFilters(){
		
		StringBuilder actionConditions= null;
		
		if(consultarPorFechaExacta.isChecked() && fecha1.getValue() != null){
			Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTime(fecha1.getValue());

			String fecha2= calendarDate.get(calendarDate.YEAR) +"-";
			Integer aa4=calendarDate.get(calendarDate.MONTH) +1;
			
			if(aa4 < 10)
				fecha2= fecha2+"0"+aa4+"-";
			else
				fecha2= fecha2+aa4+"-";
			
			Integer aa3=calendarDate.get(calendarDate.DAY_OF_MONTH);
			if(aa3 < 10)
				fecha2= fecha2+"0"+aa3;
			else
				fecha2= fecha2+aa3;
			
			actionConditions= new StringBuilder( "select distinct(clase.id) from clase clase   ");
			actionConditions.append(" inner join actividad act on (clase.idactividad = act.id) ");
			actionConditions.append("   left join ClaseConListaAlumnos claseConLista on (clase.id = claseConLista.idClase)  ");
			actionConditions.append("   left join ClaseConListaAlumnosHistorico claseConListaHistorico on (clase.id = claseConListaHistorico.idClase)  ");
			actionConditions.append(" where 1=1  ");
			actionConditions.append("  and act.tomalista is true  ");
			actionConditions.append(" and ( to_char(claseConLista.fecha,'YYYY-MM-DD')='"+fecha2+"'  OR  to_char(claseConListaHistorico.fecha,'YYYY-MM-DD')='"+fecha2+"'           )");

			if (curso.getSelectedIndex() >= 0) {
				Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
				actionConditions.append(" and clase.idactividad= '"+stateType.getId()+"' ");
			}
		}else{
			
			actionConditions= new StringBuilder( "select clase.id from clase clase  ");
			actionConditions.append(" inner join actividad act on (clase.idactividad = act.id) ");
			actionConditions.append(" where 1=1  ");
			actionConditions.append("  and act.tomalista is true  ");
			actionConditions.append("  and clase.disponible is true  ");

			if (curso.getSelectedIndex() >= 0) {
				Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
				actionConditions.append(" and clase.idactividad= '"+stateType.getId()+"' ");
			}
			Integer di22a=(Integer)dia.getAttribute("numeroDia");

				String dia=null;
				if(di22a == 0){
					dia="domingo";
				}else if(di22a == 1){
					dia="lunes";
				}else if(di22a == 2){
					dia="martes";
				}else if(di22a == 3){
					dia="miercoles";
				}else if(di22a == 4){
					dia="jueves";
				}else if(di22a == 5){
					dia="viernes";
				}else{
					dia="sabado";
				}
				actionConditions.append(" and clase."+dia+ " is true  order by clase.horadesde");	
//			}
		}
								
		return actionConditions.toString();
	}
	
	
	public String getFiltersConFecha(Date fecha, boolean preguntaSiEsHistorico, boolean seAplicoBusquedaConFechas){
		
		StringBuilder actionConditions= null;
		
		Calendar calendarDate = Calendar.getInstance();
		calendarDate.setTime(fecha);

		String fecha2= calendarDate.get(calendarDate.YEAR) +"-";
		Integer aa4=calendarDate.get(calendarDate.MONTH) +1;
		
		if(aa4 < 10)
			fecha2= fecha2+"0"+aa4+"-";
		else
			fecha2= fecha2+aa4+"-";
		
		Integer aa3=calendarDate.get(calendarDate.DAY_OF_MONTH);
		if(aa3 < 10)
			fecha2= fecha2+"0"+aa3;
		else
			fecha2= fecha2+aa3;
		
		actionConditions= new StringBuilder( "select distinct(clase.id) from clase clase   ");
		actionConditions.append(" inner join actividad act on (clase.idactividad = act.id) ");
		
		if(preguntaSiEsHistorico)
			actionConditions.append("   inner join ClaseConListaAlumnosHistorico claseConListaHistorico on (clase.id = claseConListaHistorico.idClase)  ");
		else
			actionConditions.append("   inner join ClaseConListaAlumnos claseConLista on (clase.id = claseConLista.idClase)  ");
		
		actionConditions.append(" where 1=1  ");
		actionConditions.append("  and act.tomalista is true  ");
		
		if(preguntaSiEsHistorico)
			actionConditions.append(" and (  to_char(claseConListaHistorico.fecha,'YYYY-MM-DD')='"+fecha2+"'  )");
		else
			actionConditions.append(" and ( to_char(claseConLista.fecha,'YYYY-MM-DD')='"+fecha2+"' )");
		
		
		if (curso.getSelectedIndex() >= 0) {
			Actividad stateType= ((Actividad)curso.getSelectedItem().getValue());
			actionConditions.append(" and clase.idactividad= '"+stateType.getId()+"' ");
		}
	
		if(!seAplicoBusquedaConFechas)
			actionConditions.append(" and clase.disponible is true ");

			
								
		return actionConditions.toString();
	}

	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		return criteria;
	}

	public void clear(){
		Constraint c;
		
		c =fecha1.getConstraint();
		fecha1.setConstraint("");
		fecha1.setText(null);
		fecha1.setConstraint(c);
		fecha1.setValue(new Date(0,0,0,0,0));
		
		c = curso.getConstraint();
		curso.setConstraint("");
		curso.setSelectedItem(null);
		curso.setConstraint(c);
		
		consultarPorFechaExacta.setChecked(false);
		
//		dia.setValue("");	

	}

	public Datebox getFecha1() {
		return fecha1;
	}

	public void setFecha1(Datebox fecha) {
		this.fecha1 = fecha;
	}

	public Combobox getCurso() {
		return curso;
	}

	public void setCurso(Combobox curso) {
		this.curso = curso;
	}

	public Label getDia() {
		return dia;
	}

	public void setDia(Label dia) {
		this.dia = dia;
	}

	public Checkbox getConsultarPorFechaExacta() {
		return consultarPorFechaExacta;
	}

	public void setConsultarPorFechaExacta(Checkbox consultarPorFechaExacta) {
		this.consultarPorFechaExacta = consultarPorFechaExacta;
	}

	public Label getLabelFecha() {
		return labelFecha;
	}

	public void setLabelFecha(Label labelFecha) {
		this.labelFecha = labelFecha;
	}

}
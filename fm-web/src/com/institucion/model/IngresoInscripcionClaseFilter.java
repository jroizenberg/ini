package com.institucion.model;

import java.util.Calendar;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class IngresoInscripcionClaseFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	
	private Checkbox check; 
	private Checkbox checkTodosLosDias; 

	private CursosDelegate delegate;

	public IngresoInscripcionClaseFilter()	{
		super();
		buildFilter();
	}

	public void onCreate() {
	}
	
	
	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}	
	
	private void buildFilter() {
	
		Row row7 = new Row();
		String estadoLabel =  I18N.getLabel("clase.obtenerDeTodas");
		Label lab=new Label(estadoLabel);
		lab.setWidth("50%");
		row7.appendChild(lab);
		this.check= new Checkbox();
		this.check.setWidth("50%");
		this.check.setChecked(false);		
		this.check.addEventListener(Events.ON_CHECK, new EventListener(){
				public void onEvent(Event evt){
					if(check.isChecked()){
						checkTodosLosDias.setChecked(false);
					}
					getDelegate().onCheckFiltersClasesDoing(((Checkbox)evt.getTarget()).isChecked(), false);
				}
			});
		row7.appendChild(check);

		String estadoLabel2 =  I18N.getLabel("clase.obtenerDeTodas.toda.semana");
		Label lab2=new Label(estadoLabel2);
		lab2.setWidth("50%");
		row7.appendChild(lab2);
		this.checkTodosLosDias= new Checkbox();
		this.checkTodosLosDias.setWidth("50%");
		this.checkTodosLosDias.setChecked(false);		
		this.checkTodosLosDias.addEventListener(Events.ON_CHECK, new EventListener(){
				public void onEvent(Event evt){
					if(checkTodosLosDias.isChecked()){
						check.setChecked(false);
					}	
					getDelegate().onCheckFiltersClasesDoing(((Checkbox)evt.getTarget()).isChecked(), true);
				}
			});
		row7.appendChild(checkTodosLosDias);
		
		this.addRow(row7);
		
	}

	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();
		return criteria;
	}

	
	public boolean validateHaveFilters(){
		return true;
	}
	
	public String getFilters(Long idActividad, int day, String horaDesde, Long idSubscripcion, Long idCurso ){
		Calendar cal = Calendar.getInstance();

		if(day == -1){
			day=cal.getTime().getDay();
		}
		String aConcatenerDespues = null;
		if(day == 0) // dom
			aConcatenerDespues="domingo is true ";
		if(day == 1) // lunes
			aConcatenerDespues="lunes is true ";
		if(day == 2) // martes
			aConcatenerDespues="martes is true ";
		if(day == 3) // miercoles
			aConcatenerDespues="miercoles is true ";
		if(day == 4) // jueves
			aConcatenerDespues="jueves is true ";
		if(day == 5) // viernes
			aConcatenerDespues="viernes is true ";
		if(day == 6) // sabado
			aConcatenerDespues="sabado is true ";
		
		
		StringBuilder actionConditions= new StringBuilder("	select clase.id from subscripcionDeClasesPorActividad subsDeClasesPorAct,  " +
			"  Clase clase , SubscripcionDeClases subsDeClases   ");
			actionConditions.append(" where 1=1  ");
			actionConditions.append(" and (subsDeClasesPorAct.idsubscripcion2= subsDeClases.idsubscripcion ) ");			
					
				actionConditions.append(" and clase.disponible is true and clase.id in (SELECT  clase.id  FROM   clase clase where EXTRACT(HOUR FROM horadesde) ="+horaDesde +
						" and clase.idactividad='"+idActividad+"' and  "+ aConcatenerDespues + " )");
						
				actionConditions.append(" and subsDeClases.idcurso='"+idCurso+"'");
				actionConditions.append(" and subsDeClasesPorAct.idsubscripcion2='"+idSubscripcion+"'");
				actionConditions.append(" and subsDeClasesPorAct.idactividad='"+idActividad+"'");
				actionConditions.append(" and  subsDeClasesPorAct."+ aConcatenerDespues );
				actionConditions.append(" order by clase.horadesde asc ");
				
		return actionConditions.toString();
	}
	
	
	public String getFiltersClasesActualByClienteAndCurso( Long idCliente, Long idCurso, Long idSubscripcion){

		StringBuilder actionConditions= new StringBuilder("	select ingreso.idclase from ingresoaclasessinfechasalumnos ingreso  " );
			actionConditions.append(" where 1=1  ");
				actionConditions.append(" and  ingreso.idcliente="+ idCliente );
				actionConditions.append(" and  ingreso.idCurso="+ idCurso );
				actionConditions.append(" and  ingreso.idSubscripcion="+ idSubscripcion );
		return actionConditions.toString();
	}
	
	public String getFiltersSubscripcionesActualByCliente( Long idCliente){

		StringBuilder actionConditions= new StringBuilder("	select ingreso.idSubscripcion, ingreso.idCurso  from ingresoaclasessinfechasalumnos ingreso  " );
			actionConditions.append(" where 1=1  ");
				actionConditions.append(" and  ingreso.idcliente="+ idCliente );
		return actionConditions.toString();
	}
	
	public void clear()	{
		check.setChecked(false);
	}

	public Checkbox getCheck() {
		return check;
	}

	public void setCheck(Checkbox check) {
		this.check = check;
	}

	public Checkbox getCheckTodosLosDias() {
		return checkTodosLosDias;
	}

	public void setCheckTodosLosDias(Checkbox checkTodosLosDias) {
		this.checkTodosLosDias = checkTodosLosDias;
	}

}
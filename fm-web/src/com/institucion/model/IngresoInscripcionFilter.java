package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.helper.EstadoCupoActividadViewHelper;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class IngresoInscripcionFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	
	private Combobox estado;  // Adeuda, activa, vencida
	
//	private FlexibleEntityEJB service;
	private CursosDelegate delegate;
	
	public IngresoInscripcionFilter()	{
		super();

		buildFilter();
	}

	public void onCreate() {
	}
		
	private void buildFilter() {
	
		Row row7 = new Row();
		String estadoLabel =  "Estado cursos";
		row7.appendChild(new Label(estadoLabel));
		estado = EstadoCupoActividadViewHelper.getComboBox();
		estado.setConstraint("strict");
		estado.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
					//llamar al Onfind
					getDelegate().buscarSubscripcionesDesdeFiltro();
			}
		});	
		row7.appendChild(estado);
		this.addRow(row7);

	}

	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();
		super.setPredicate(criteria, estado, "state");

		return criteria;
	}

	
	public boolean validateHaveFilters(){
						
		if (estado.getSelectedIndex() >= 0) {
			return true;
		}
		return false;
	}
	
	public String getFilters(Long clientId){
		StringBuilder actionConditions= new StringBuilder(" select distinct aa.id from ( ");
		
			actionConditions.append("select subscripcion.id  from subscripcion subscripcion   ");
			actionConditions.append(" inner join cupoactividad ca on (subscripcion.id =ca.idsubscripcion) ");
					actionConditions.append(" where 1=1  ");
					actionConditions.append(" and subscripcion.idcliente=  '"+clientId+"'" );

					if (estado.getSelectedIndex() >= 0 &&  
							((CupoActividadEstadoEnum)estado.getSelectedItem().getValue()).toInt() != CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()) {
						CupoActividadEstadoEnum stateType= ((CupoActividadEstadoEnum)estado.getSelectedItem().getValue());
						if(stateType.toInt() ==  CupoActividadEstadoEnum.TODAS.toInt()){

						}else
							actionConditions.append(" and ca.estado= '"+stateType.toInt()+"' ");

					}else{
						actionConditions.append(" and (ca.estado in ("+CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt() 
																					+","+CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()
																					+","+CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
//																					+","+CupoActividadEstadoEnum.VENCIDA.toInt()
																					+","+CupoActividadEstadoEnum.C_CUPOS.toInt() +" )");
						
//							actionConditions.append(" or (ca.estado= 8  and  subscripcion.idcliente in (select  idcliente from cliente_clase )) )");
						
						actionConditions.append(" or (ca.estado in( 8, 0)  and  subscripcion.idcliente in " +
								" (select  ica.idcliente from ingresoaclasessinfechasalumnos ica  where ica.idcurso= ca.idcurso and  ica.idsubscripcion=ca.idsubscripcion  )) )");
		
					}

					actionConditions.append(" order by subscripcion.fechaingresosubscripcion, ca.estado  asc "); //,  subscripcion.fechafin  ");
								
					actionConditions.append(" )  aa  ");
					
		return actionConditions.toString();
	}
	
	public String getFiltersConSubsActivasOConCuposDisponibles(Long clientId){
		StringBuilder actionConditions= new StringBuilder(" select distinct aa.id from ( ");
		
			actionConditions.append("select subscripcion.id  from subscripcion subscripcion   ");
			actionConditions.append(" inner join cupoactividad ca on (subscripcion.id =ca.idsubscripcion) ");
					actionConditions.append(" where 1=1  ");
					actionConditions.append(" and subscripcion.idcliente=  '"+clientId+"'" );
					actionConditions.append(" and (ca.estado in ("+CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()
																	+","+CupoActividadEstadoEnum.C_CUPOS.toInt() 
																	+","+CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
																	+","+CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
																	+" )");
//					actionConditions.append(" or (ca.estado= 8  and  subscripcion.idcliente in (select  idcliente from cliente_clase )) )");
					
					actionConditions.append(" or (ca.estado in( 8, 0)  and  subscripcion.idcliente in " +
							" (select  ica.idcliente from ingresoaclasessinfechasalumnos ica  where ica.idcurso= ca.idcurso and  ica.idsubscripcion=ca.idsubscripcion  )) )");
					
					actionConditions.append(" order by subscripcion.fechaingresosubscripcion, ca.estado  asc "); //,  subscripcion.fechafin  ");
					actionConditions.append(" )  aa  ");
		return actionConditions.toString();
	}
	
	public void clear()	{
		Constraint c;
		
		c = estado.getConstraint();
		estado.setConstraint("");
		estado.setSelectedItem(null);
		estado.setConstraint(c);
	}

	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}

	public void blanquearEstado(){
		if(estado != null && estado.getSelectedItem() != null){
			estado.setSelectedItem(null);
			estado.setText(null);
		}
	}
}
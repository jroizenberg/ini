package com.institucion.model;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CumplesFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Combobox anioContratado;
	private Combobox mesContratado;

//	private CursoEJB cursoEJB;
//	private ClaseEJB claseEJB;
	private ClienteDelegate actionComposerDelegate;	

	
//	agregar 2 filtros que sean, cumpleanios contratados: ANIO:   MES
	
	public CumplesFilter()	{
		super();
//		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
//		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		buildFilter();
	}

	private void buildFilter() {

		Row row1 = new Row();
	
		String edadLabel =  "Año Contratado";
		row1.appendChild(new Label(edadLabel));
		anioContratado = EdadViewHelper.getComboAnioBox();
		anioContratado.setConstraint("strict");
		anioContratado.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				actionComposerDelegate.buscar(evt, false);
				}
		});	
		row1.appendChild(anioContratado);

		
		
		row1.appendChild(new Label("Mes contratado"));
		mesContratado = EdadViewHelper.getComboBoxCumplesMes();
		mesContratado.setConstraint("strict");
		mesContratado.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				actionComposerDelegate.buscar(evt, false);
			}
		});	
		row1.appendChild(mesContratado);		
		
		this.addRow(row1);
	}


	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

//		super.setPredicate(criteria, nombre, "nombre");
		
		return criteria;
	}
	
	public String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder("select distinct(clientexx.id)  from subscripcion clientexx   ");
					
					if (anioContratado.getSelectedIndex() >= 0 && mesContratado.getSelectedIndex() >= 0 ) {
						String aniostateType= ((String)anioContratado.getSelectedItem().getValue());
						String stateType= ((String)mesContratado.getSelectedItem().getValue());
						if(stateType != null && aniostateType != null){

							actionConditions.append("where 1=1  ");
							actionConditions.append(" and  clientexx.id in ( " +
									"	select  ca.idSubscripcion from concepto  ca  "	+
									"   inner join curso c  on (ca.idcurso=c.id and c.nombre ilike '%CUMPLEA%')  "	+
									"   where  ca.fechaCumple is not null  " +
									" and to_char(ca.fechaCumple, 'YYYY') =  '"+aniostateType.trim()+"' " +
									" and to_char(ca.fechaCumple, 'MM') =  '"+stateType.trim()+"'   order by ca.fechaCumple asc) "); 
						}
					
					}else 	if (anioContratado.getSelectedIndex() >= 0) {
							String stateType= ((String)anioContratado.getSelectedItem().getValue());
							if(stateType != null){
								
								actionConditions.append("where 1=1  ");
								actionConditions.append(" and  clientexx.id in ( " +
										"	select  ca.idSubscripcion from concepto  ca  "	+
										"   inner join curso c  on (ca.idcurso=c.id and c.nombre ilike '%CUMPLEA%')  "	+
										"   where  ca.fechaCumple is not null  " +
										" and to_char(ca.fechaCumple, 'YYYY') =  '"+stateType.trim()+"' "+" order by ca.fechaCumple asc )"); 
							}
							
					}else	if (mesContratado.getSelectedIndex() >= 0) {
							String stateType= ((String)mesContratado.getSelectedItem().getValue());
							if(stateType != null){
								actionConditions.append(" where 1=1  ");
								actionConditions.append(" and  clientexx.id in ( " +
										"	select  ca.idSubscripcion from concepto  ca  "	+
										"   inner join curso c  on (ca.idcurso=c.id and c.nombre ilike '%CUMPLEA%')  "	+
										"   where  ca.fechaCumple is not null  " +
										" and to_char(ca.fechaCumple, 'MM') =  '"+stateType.trim()+"' "+"  order by ca.fechaCumple asc)"); 
							}
					}else{
						actionConditions.append("	inner join concepto ca on (ca.idSubscripcion = clientexx.id) " +
								"   inner join curso c  on (ca.idcurso=c.id and c.nombre ilike '%CUMPLEA%') " +
								"   where  ca.fechaCumple is not null   order by ca.fechaCumple asc  ");

					}
		return actionConditions.toString();
	}
	
	public void clear(){
		Constraint c;
		
		c= anioContratado.getConstraint();
		anioContratado.setConstraint("");
		anioContratado.setSelectedItem(null);
		anioContratado.setConstraint(c);

		c= mesContratado.getConstraint();
		mesContratado.setConstraint("");
		mesContratado.setSelectedItem(null);
		mesContratado.setConstraint(c);
	}


	public void clear2(){
		Constraint c;
		
		c= anioContratado.getConstraint();
		anioContratado.setConstraint("");
		anioContratado.setSelectedItem(null);
		anioContratado.setConstraint(c);

		c= mesContratado.getConstraint();
		mesContratado.setConstraint("");
		mesContratado.setSelectedItem(null);
		mesContratado.setConstraint(c);		
		
	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}

	public Combobox getAnioContratado() {
		return anioContratado;
	}

	
	public void setSelectedAnioContratado() {
		Calendar ahoraCal= Calendar.getInstance();
		ahoraCal.setTime(new Date());
		int mes=ahoraCal.get(Calendar.YEAR);
		
		if(anioContratado != null && anioContratado.getItems() != null){
			for (Object iterable_element : anioContratado.getItems()) {
				
				if((((String)(((Comboitem)iterable_element).getValue()))).equalsIgnoreCase(String.valueOf(mes))){
					anioContratado.setSelectedItem((Comboitem)iterable_element);
				}
			}
		}
	}
	public void setAnioContratado(Combobox anioContratado) {
		this.anioContratado = anioContratado;
	}

	public Combobox getMesContratado() {
		return mesContratado;
	}

	public void setMesContratado(Combobox mesContratado) {
		this.mesContratado = mesContratado;
	}



}
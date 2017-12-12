package com.institucion.model;

import java.util.Date;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Row;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class AControlarFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Datebox fechaDesde;
	private Datebox fechaHasta;
//	private Textbox coment;
	private CursoEJB cursoEJB;
	private CursosCrudDelegate delegate;

	public AControlarFilter()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		buildFilter();

	}
	
	private void buildFilter() {
		
		Row row3 = new Row();

		String edadLabel =  I18N.getLabel("caja.fechaD");
		row3.appendChild(new RequiredLabel(edadLabel));
		fechaDesde  = new Datebox();
		fechaDesde .setMaxlength(20);
		fechaDesde.setId("sasas");
		fechaDesde .setFormat(I18N.getDateFormat());
		fechaDesde.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	
		
		row3.appendChild(fechaDesde);

		String edadLabel2 =  I18N.getLabel("caja.fechaH");
		row3.appendChild(new RequiredLabel(edadLabel2));
		fechaHasta  = new Datebox();
		fechaHasta .setMaxlength(20);
		fechaHasta .setFormat(I18N.getDateFormat());
		fechaHasta.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				delegate.buscar();
			}
		});	

		row3.appendChild(fechaHasta);
		
		
		this.addRow(row3);
			
	}
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

		super.setPredicate(criteria, fechaDesde, "curso");
		
		return criteria;
	}

	public boolean validateHaveFilters(){
		
		if (fechaDesde.getValue()!= null) {
			return true;
		}
		if (fechaHasta.getValue()!= null) {
			return true;
		}
		
		return false;
	}
	
	
	public void clear(){
		Constraint c;
						
		c =fechaDesde.getConstraint();
		fechaDesde.setConstraint("");
		fechaDesde.setText(null);
		fechaDesde.setConstraint(c);
		fechaDesde.setValue(new Date(0,0,0,0,0));
		
		c =fechaHasta.getConstraint();
		fechaHasta.setConstraint("");
		fechaHasta.setText(null);
		fechaHasta.setConstraint(c);
		fechaHasta.setValue(new Date(0,0,0,0,0));

	}

	public Datebox getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Datebox fechaDesde) {
		this.fechaDesde = fechaDesde;
	}


	public Datebox getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Datebox fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	
	public CursoEJB getCursoEJB() {
		return cursoEJB;
	}

	public void setCursoEJB(CursoEJB cursoEJB) {
		this.cursoEJB = cursoEJB;
	}

	public CursosCrudDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosCrudDelegate delegate) {
		this.delegate = delegate;
	}

}
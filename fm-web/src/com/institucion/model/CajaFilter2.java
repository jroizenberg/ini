package com.institucion.model;

import java.util.Calendar;
import java.util.Date;

import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Row;

import com.institucion.bz.CursoEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CajaFilter2 extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Datebox fechaHasta;
	
	private CursoEJB cursoEJB;

	public CajaFilter2()	{
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		buildFilter();

	}
	
	private void buildFilter() {
			
		Row row3 = new Row();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		String edadLabel2 =  I18N.getLabel("caja.fechaH");
		row3.appendChild(new RequiredLabel(edadLabel2));
		fechaHasta  = new Datebox();
		fechaHasta .setMaxlength(20);
		fechaHasta .setFormat(I18N.getDateFormat());
		fechaHasta .setConstraint("strict");
		fechaHasta.setValue(cal.getTime());

		row3.appendChild(fechaHasta);
		
		this.addRow(row3);
	}
	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();
		
		return criteria;
	}

	public boolean validateHaveFilters(){
		
		
		if (fechaHasta.getValue()!= null) {
			return true;
		}
		
		return false;
	}
	
	public void clear(){
		Constraint c;
		
		c =fechaHasta.getConstraint();
		fechaHasta.setConstraint("");
		fechaHasta.setText(null);
		fechaHasta.setConstraint(c);
		fechaHasta.setValue(new Date(0,0,0,0,0));
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

}
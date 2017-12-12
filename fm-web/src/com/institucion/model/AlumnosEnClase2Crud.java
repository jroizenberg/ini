package com.institucion.model;

import org.zkoss.zul.Constraint;
import org.zkoss.zul.Intbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class AlumnosEnClase2Crud extends GridCrud{

	private static final long serialVersionUID = 1L;

	private Intbox cantAlumnosEnClase;
	
	public AlumnosEnClase2Crud (){
		super();
		this.setStyle("  width:95%;  align:center;  margin-left: 2%;");
		super.setStyle("  width:95%;   align:center;  margin-left: 2%;");
		this.makeFields();
	}
	
	private void makeFields(){
		
		cantAlumnosEnClase = new Intbox();
		this.addField(new RequiredLabel(I18N.getLabel("curso.cant.alum.en.clase.listaclientes")), cantAlumnosEnClase);
	}

	public Intbox getCantAlumnosEnClase() {
		return cantAlumnosEnClase;
	}

	public void setCantAlumnosEnClase(Intbox cantAlumnosEnClase) {
		this.cantAlumnosEnClase = cantAlumnosEnClase;
	}

	public void clear(){
		Constraint c;
		
		c =cantAlumnosEnClase.getConstraint();
		cantAlumnosEnClase.setConstraint("");
		cantAlumnosEnClase.setText(null);
		cantAlumnosEnClase.setConstraint(c);
	}

}

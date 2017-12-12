package com.institucion.model;

import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class AlumnosEnClaseCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Textbox profe;
	private Textbox nombreClase;
	private Textbox comentariosProfeGenerales;

	public AlumnosEnClaseCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		profe= new Textbox();
//		profe.setMaxlength(50);
		profe.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.profesor")), profe);
		
		nombreClase= new Textbox();
//		nombreClase.setMaxlength(50);
		nombreClase.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre.estadoSubs.nombre.clase")), nombreClase);
		
		comentariosProfeGenerales= new Textbox();
		comentariosProfeGenerales.setMaxlength(150);
		comentariosProfeGenerales.setRows(4);
		comentariosProfeGenerales.setWidth("98%");
		comentariosProfeGenerales.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("combo.anula.subscripcion.comentario.del.profe")), comentariosProfeGenerales);

		this.addField(new Label(" "), new Label(" "));
		
	}

	public Textbox getProfe() {
		return profe;
	}

	public void setProfe(Textbox profe) {
		this.profe = profe;
	}

	public Textbox getNombreClase() {
		return nombreClase;
	}

	public void setNombreClase(Textbox nombreClase) {
		this.nombreClase = nombreClase;
	}

	public Textbox getComentariosProfeGenerales() {
		return comentariosProfeGenerales;
	}

	public void setComentariosProfeGenerales(Textbox comentariosProfeGenerales) {
		this.comentariosProfeGenerales = comentariosProfeGenerales;
	}

	
	public void clear(){
		Constraint c;
		
		c =profe.getConstraint();
		profe.setConstraint("");
		profe.setText(null);
		profe.setConstraint(c);
		
		c =nombreClase.getConstraint();
		nombreClase.setConstraint("");
		nombreClase.setText(null);
		nombreClase.setConstraint(c);
		
		c =comentariosProfeGenerales.getConstraint();
		comentariosProfeGenerales.setConstraint("");
		comentariosProfeGenerales.setText(null);
		comentariosProfeGenerales.setConstraint(c);

	}
}

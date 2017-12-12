package com.institucion.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.desktop.helper.TipoCursosViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class CursoCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Textbox nombreCb;
	private Textbox descripcionCb;

	private Combobox pagaSubscripcion;
	private Combobox disponible;

	private Combobox tipoCurso;
	
	public CursoCrud (){
		super();
		this.makeFields();
	}
	
	    
	private void makeFields(){

		tipoCurso =  TipoCursosViewHelper.getComboBox();
		tipoCurso.setConstraint("strict");
		tipoCurso.setDisabled(true);
		this.addField(new RequiredLabel(I18N.getLabel("curso.tipoCurso")), tipoCurso);

		nombreCb= new Textbox();
		nombreCb.setMaxlength(70);
		nombreCb.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("curso.nombre")), nombreCb);

		descripcionCb= new Textbox();
		descripcionCb.setMaxlength(50);
		descripcionCb.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("curso.descripcion")), descripcionCb);
		
		pagaSubscripcion = BooleanViewHelper.getComboBox();
		pagaSubscripcion.setConstraint("strict");
		this.addField(new RequiredLabel(I18N.getLabel("curso.pagaSubscripcion")), pagaSubscripcion);
		
		disponible = BooleanViewHelper.getComboBox();
		disponible.setConstraint("strict");
		this.addField(new RequiredLabel("Disponible"), disponible);

	}

	
	public void setSelectedDisponible (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<disponible.getItemCount()){
			if(disponible.getItemAtIndex(i) != null 
					&&(Boolean)disponible.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				disponible.setSelectedIndex(i);
			}
			i++;
		}
	}	
	
	public void setSelectedPagaSubscripcion (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<pagaSubscripcion.getItemCount()){
			if(pagaSubscripcion.getItemAtIndex(i) != null 
					&&(Boolean)pagaSubscripcion.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				pagaSubscripcion.setSelectedIndex(i);
			}
			i++;
		}
	}	
	
	public void setSelectedTipoDeCurso (TipoCursoEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<tipoCurso.getItemCount()){
			if(selectedHPType == null){
				tipoCurso.setSelectedItem(null);
			}else if(selectedHPType.toInt() == (((TipoCursoEnum) tipoCurso.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				tipoCurso.setSelectedIndex(i);
			}
			i++;
		}
		
	}
	
	public Textbox getNombreCb() {
		return nombreCb;
	}

	public void setNombreCb(Textbox nombreCb) {
		this.nombreCb = nombreCb;
	}

	public Textbox getDescripcionCb() {
		return descripcionCb;
	}

	public void setDescripcionCb(Textbox descripcionCb) {
		this.descripcionCb = descripcionCb;
	}

	public Combobox getPagaSubscripcion() {
		return pagaSubscripcion;
	}

	public void setPagaSubscripcion(Combobox pagaSubscripcion) {
		this.pagaSubscripcion = pagaSubscripcion;
	}

	public Combobox getTipoCurso() {
		return tipoCurso;
	}

	public void setTipoCurso(Combobox tipoCurso) {
		this.tipoCurso = tipoCurso;
	}


	public Combobox getDisponible() {
		return disponible;
	}


	public void setDisponible(Combobox disponible) {
		this.disponible = disponible;
	}

}

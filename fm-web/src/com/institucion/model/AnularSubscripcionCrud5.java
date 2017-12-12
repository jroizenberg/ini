package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Set;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Doublebox;

import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class AnularSubscripcionCrud5 extends GridCrud{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private static final long serialVersionUID = 1L;
//	private Combobox comboAnulaSubs;
	private Textbox comentarioCb;
	private Doublebox tipoDevoluciones;
	private Textbox comboDevuelveDinero;
	private AnularSubscripcionDelegate delegate;

	public AnularSubscripcionCrud5 (){
		super();
		this.makeFields();
	}

	private void makeFields(){
	
		comboDevuelveDinero= new Textbox();
		comboDevuelveDinero.setReadonly(true);
		comboDevuelveDinero.setValue("SI");
		this.addField(new RequiredLabel(I18N.getLabel("combo.anula.subscripcion.devuelve.dinero")), comboDevuelveDinero);	
		
		tipoDevoluciones = new org.zkoss.zul.Doublebox();
		tipoDevoluciones.setVisible(true);
		tipoDevoluciones.setReadonly(true);
		tipoDevoluciones.setFormat("#.##");

		tipoDevoluciones.setConstraint("no negative,no empty");
		this.addField(new RequiredLabel(I18N.getLabel("combo.anula.subscripcion.devuelve.que.devuelve")), tipoDevoluciones);	
		
		comentarioCb= new Textbox();
		comentarioCb.setMaxlength(150);
//		comentarioCb.setVisible(false);
		comentarioCb.setRows(3);
		comentarioCb.setWidth("80%");
		comentarioCb.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("combo.anula.subscripcion.comentario")), comentarioCb);
		
	}

	public AnularSubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(AnularSubscripcionDelegate delegate) {
		this.delegate = delegate;
	}
//
//	public Combobox getComboAnulaSubs() {
//		return comboAnulaSubs;
//	}
//
//	public void setComboAnulaSubs(Combobox comboAnulaSubs) {
//		this.comboAnulaSubs = comboAnulaSubs;
//	}

	public Textbox getComentarioCb() {
		return comentarioCb;
	}

	public void setComentarioCb(Textbox comentarioCb) {
		this.comentarioCb = comentarioCb;
	}

//	public Combobox getComboDevuelveDinero() {
//		return comboDevuelveDinero;
//	}

	public void setComboDevuelveDinero(Combobox comboDevuelveDinero) {
		this.comboDevuelveDinero = comboDevuelveDinero;
	}
	
	public Combobox getComboBoxTipoDevoluciones(Set<Concepto> conceptoList, Combobox act) {
		Constraint brandC = act.getConstraint();
		act.setConstraint("");
		act.setText("");
		act.setConstraint(brandC);
		act.getItems().clear();
		
		float matricula=obtenerPrecioMatricula(conceptoList);
		float cursos=obtenerPrecioCursos(conceptoList);
		
		if(matricula != -1){
			AnularSubsDevolucion anu= new AnularSubsDevolucion();
			anu.setDinero(matricula);
			anu.setTipo(AnularSubscripcionTipoDevolucionEnum.MATRICULA_SOLA);

			Comboitem item= new Comboitem("Matricula: $"+formateador.format(matricula));
			item.setValue(anu);
			act.appendChild(item);
		}
	
		AnularSubsDevolucion anu= new AnularSubsDevolucion();
		anu.setDinero(cursos);
		anu.setTipo(AnularSubscripcionTipoDevolucionEnum.CURSO_SOLO);
		
		Comboitem item2= new Comboitem("Cursos: $"+formateador.format(cursos));
		item2.setValue(anu);
		act.appendChild(item2);
		
		if(matricula != -1){
			float nuevoVal=cursos+ matricula;
			AnularSubsDevolucion anu2= new AnularSubsDevolucion();
			anu2.setDinero(nuevoVal);
			anu2.setTipo(AnularSubscripcionTipoDevolucionEnum.CURSO_SOLO);
			
			Comboitem item3= new Comboitem("Cursos y Matricula: $"+formateador.format(nuevoVal));
			item3.setValue(anu2);
			act.appendChild(item3);		
		}

		return act;
	}	
	
	private int obtenerPrecioMatricula(Set<Concepto> conceptoList){
		int matricula=-1;
		if(conceptoList != null){
			for (Concepto concepto : conceptoList) {
				if(concepto.getCurso() == null){
					return concepto.getImporteConDescuento();		
				}
			}
		}
		return matricula;
	}

	private int obtenerPrecioCursos(Set<Concepto> conceptoList){
		int cursos=-1;
		if(conceptoList != null){
			for (Concepto concepto : conceptoList) {
				if(concepto.getCurso() != null){
					if(cursos == -1)
						cursos=0;
					cursos=concepto.getImporteConDescuento()+cursos;
				}
			}
		}
		return cursos;
	}

	public Doublebox getTipoDevoluciones() {
		return tipoDevoluciones;
	}

	public void setTipoDevoluciones(Doublebox tipoDevoluciones) {
		this.tipoDevoluciones = tipoDevoluciones;
	}

}
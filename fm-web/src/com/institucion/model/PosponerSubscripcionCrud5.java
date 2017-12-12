package com.institucion.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Doublebox;

import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.desktop.helper.ClasesCantidadViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class PosponerSubscripcionCrud5 extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Combobox comboMesesPospone;
	private Combobox comboPagaAdicionalSubs;
	private Doublebox adicional;
	private Textbox comentarioCb;
//	private Textbox aprovadoPorCb;
	private AnularSubscripcionDelegate delegate;

	public PosponerSubscripcionCrud5 (){
		super();
		this.makeFields();
	}

	private void makeFields(){
		
		comboMesesPospone = ClasesCantidadViewHelper.getMesesPosponeVencimientoComboBox();
		comboMesesPospone.setConstraint("strict");
		this.addFieldClases(new RequiredLabel("Tiempo que se Pospone"), comboMesesPospone, 0);	
	
		comentarioCb= new Textbox();
		comentarioCb.setMaxlength(150);
		comentarioCb.setRows(5);
		comentarioCb.setWidth("98%");
		comentarioCb.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("combo.anula.subscripcion.comentario")), comentarioCb);
		
//		aprovadoPorCb= new Textbox();
//		aprovadoPorCb.setMaxlength(100);
//		aprovadoPorCb.setConstraint(new TextConstraint());
//		this.addField(new RequiredLabel(I18N.getLabel("combo.pospone.subscripcion.aprobadoPor")), aprovadoPorCb);
		
	}

	public AnularSubscripcionDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(AnularSubscripcionDelegate delegate) {
		this.delegate = delegate;
	}

	public Textbox getComentarioCb() {
		return comentarioCb;
	}

	public void setComentarioCb(Textbox comentarioCb) {
		this.comentarioCb = comentarioCb;
	}

	public Combobox getComboMesesPospone() {
		return comboMesesPospone;
	}

	public void setComboMesesPospone(Combobox comboMesesPospone) {
		this.comboMesesPospone = comboMesesPospone;
	}

	public Combobox getComboPagaAdicionalSubs() {
		return comboPagaAdicionalSubs;
	}

	public void setComboPagaAdicionalSubs(Combobox comboPagaAdicionalSubs) {
		this.comboPagaAdicionalSubs = comboPagaAdicionalSubs;
	}

	public Doublebox getAdicional() {
		return adicional;
	}

	public void setAdicional(Doublebox adicional) {
		this.adicional = adicional;
	}

//	public Textbox getAprovadoPorCb() {
//		return aprovadoPorCb;
//	}
//
//	public void setAprovadoPorCb(Textbox aprovadoPorCb) {
//		this.aprovadoPorCb = aprovadoPorCb;
//	}

}
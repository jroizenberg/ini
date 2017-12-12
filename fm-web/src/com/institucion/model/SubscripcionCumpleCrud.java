package com.institucion.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SubscripcionCumpleCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Textbox detalleCumpleanios;
	
	public SubscripcionCumpleCrud (){
		super();
		this.setStyle("  width:50%;  align:center;  margin-left: 20%;");
		super.setStyle("  width:50%;   align:center;  margin-left: 20%;");
		this.makeFields();
	}
	    
	private void makeFields(){

		this.detalleCumpleanios = new Textbox();
		this.detalleCumpleanios.setId("gfdsadsfdg");
		this.detalleCumpleanios.setConstraint(new TextConstraint());
		this.detalleCumpleanios.setMaxlength(300);
		this.detalleCumpleanios.setRows(3);
		this.detalleCumpleanios.setCols(40);
		
		this.addField1ComponentePorRow(new RequiredLabel("Detalle cumpleaños:"), this.detalleCumpleanios);				
	}
	
	
	public String getDetalleCumple(){
		return detalleCumpleanios.getValue();
	}
	
	public void setDetalleCumple(String val){
		detalleCumpleanios.setValue(val);
	}
	public void setVisibleDetalleCumple(boolean visible){
		detalleCumpleanios.setVisible(visible);
	}
	
}

package com.institucion.fm.structure.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;

public class HpaCrud extends GridCrud{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Label contactLabel= new Label("");
	

	public HpaCrud(){
		makeFields();
	}
	
	private void makeFields(){

		this.addField(new Label(I18N.getLabel("hpa.view.contact.name")),contactLabel);
		
	}

	public Label getContactLabel() {
		return contactLabel;
	}

	public void setContactLabel(String contactLabel) {
		this.contactLabel.setValue(contactLabel);
	}
	


}

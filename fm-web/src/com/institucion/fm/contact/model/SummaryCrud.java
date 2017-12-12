package com.institucion.fm.contact.model;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SummaryCrud extends GridCrud {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	Label clientLabel = new Label("");
	private	Label addressLabel = new Label("");
	private	Label PortFolioLabel = new Label("");
	private	Label contactTypeLabel = new Label("");
	private	Label contactDegreeLabel = new Label("");
	
	public SummaryCrud(){
		this.addField(new RequiredLabel(I18N.getLabel("summary.denomination")),clientLabel);
		this.addField(new RequiredLabel(I18N.getLabel("summary.address")),addressLabel);
		this.addField(new RequiredLabel(I18N.getLabel("summary.portfolio")),PortFolioLabel);
		this.addField(new RequiredLabel(I18N.getLabel("summary.contactType")),contactTypeLabel);
		this.addField(new RequiredLabel(I18N.getLabel("summary.contactDegree")),contactDegreeLabel);
	}

	public Label getClientLabel() {
		return clientLabel;
	}

	public void setClientLabel(String clientLabel) {
		this.clientLabel.setValue(clientLabel);
	}

	public Label getAddressLabel() {
		return addressLabel;
	}

	public void setAddressLabel(String addressLabel) {
		this.addressLabel.setValue(addressLabel);
	}

	public Label getPortFolioLabel() {
		return PortFolioLabel;
	}

	public void setPortFolioLabel(String portFolioLabel) {
		PortFolioLabel.setValue(portFolioLabel);
	}

	public Label getContactTypeLabel() {
		return contactTypeLabel;
	}

	public void setContactTypeLabel(String contactTypeLabel) {
		this.contactTypeLabel.setValue(contactTypeLabel);
	}
	
	public Label getContactDegreeLabel() {
		return contactDegreeLabel;
	}

	public void setContactDegreeLabel(String contactDegreeLabel) {
		this.contactDegreeLabel.setValue(contactDegreeLabel);
	}
	
}

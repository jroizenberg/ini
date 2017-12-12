package com.institucion.fm.security.model;

import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.util.RegularExpressions;

public class PasswordGridCrud extends GridCrud
{
	private static final long serialVersionUID = 1L;

	private Textbox currentPassword;
	private Textbox enterPassword;
	private Textbox reenterPassword;

	public PasswordGridCrud()
	{
		super();
		this.makeFields();
	}

	private void makeFields()
	{
		currentPassword = new Textbox();
		currentPassword.setWidth("98%");
		currentPassword.setMaxlength(20);
		currentPassword.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
		currentPassword.setId("currentPassword");
		currentPassword.setType("password");
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("crud.currentpassword")), currentPassword);

		enterPassword = new Textbox();
		enterPassword.setWidth("98%");
		enterPassword.setMaxlength(20);
		enterPassword.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
		enterPassword.setId("enterPassword");
		enterPassword.setType("password");
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("crud.enterpassword")), enterPassword);

		reenterPassword = new Textbox();
		reenterPassword.setWidth("98%");
		reenterPassword.setMaxlength(20);
		reenterPassword.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
		reenterPassword.setId("reenterPassword");
		reenterPassword.setType("password");
		this.addFieldUnique(new RequiredLabel(I18N.getLabel("crud.reenterpassword")), reenterPassword);
	}

	public Textbox getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(Textbox currentPassword) {
		this.currentPassword = currentPassword;
	}

	public Textbox getEnterPassword() {
		return enterPassword;
	}

	public void setEnterPassword(Textbox enterPassword) {
		this.enterPassword = enterPassword;
	}

	public Textbox getReenterPassword() {
		return reenterPassword;
	}

	public void setReenterPassword(Textbox reenterPassword) {
		this.reenterPassword = reenterPassword;
	}
	
}
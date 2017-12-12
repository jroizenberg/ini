package com.institucion.fm.security.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.helper.UserStateViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.EmailConstraint;
import com.institucion.fm.desktop.validator.SimpleTextConstraint;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;
import com.institucion.fm.util.RegularExpressions;
import com.institucion.model.SucursalEnum;

public class UserCrud extends GridCrud {
	private static final long serialVersionUID = 1L;
	private Textbox name;
	private Textbox firstName;
	private Textbox lastName;
	private Textbox password;
	private Textbox telephone;
	private Textbox cellphone;
	private Textbox address;
	private Label state;
	private Textbox email;
	private Combobox sucursal;
	private Radiogroup userWF;
//	private Textbox code;
	private Radiogroup validatePass;
//	private final int CODE_COUNT=6;
	
	public static final String SESSION_USER_ID = "session_user_id";
	
	public UserCrud()
	{
		super();
		this.makeFields();
	}

	public String getName() {
		return this.name.getValue();
	}	
	public void setName(String name) {
		this.name.setValue(name);
	}
	
	public String getFirstName() {
		return this.firstName.getValue();
	}
	
	public String setSucursal(SucursalEnum suc) {
		if(sucursal != null && sucursal.getItems() != null ){
			
			for (Object iterable_element : sucursal.getItems() ) {
				if(((SucursalEnum)(((Comboitem)iterable_element).getValue())).toInt() == suc.toInt()){
					sucursal.setSelectedItem((Comboitem)iterable_element);	
				}
			}
			
		}
		return this.firstName.getValue();
	}
	public void setFirstName(String firstName) {
		this.firstName.setValue(firstName);
	}

	public String getLastName() {
		return this.lastName.getValue();
	}
	public void setLastName(String lastName) {
		this.lastName.setValue(lastName);
	}
	
	public String getPassword() {
		return this.password.getValue();
	}
	public void setPassword(String password) {
		this.password.setValue(password);
	}
	
	public String getAddress() {
		return this.address.getValue();
	}

	public void setAddress(String address) {
		this.address.setValue(address);
	}

	public String getTelephone() {
		return this.telephone.getValue();
	}
	public void setTelephone(String telephone) {
		this.telephone.setValue(telephone);
	}
	
	public String getCellphone() {
		return this.cellphone.getValue();
	}
	public void setCellphone(String cellphone) {
		this.cellphone.setValue(cellphone);
	}
	public Radiogroup getUserWF() {
		return this.userWF;
	}

	public Combobox getSucursal() {
		return this.sucursal;
	}
	public void setUserWF(Boolean userWF) {
	
			this.userWF.getItemAtIndex(0).setSelected(true);
	
	}
	
	public Radiogroup getValidatePass() {
		return this.validatePass;
	}

	public void setValidatePass(Boolean validatePass) {
			this.validatePass.getItemAtIndex(0).setSelected(true);
	
	}

	public String getEmail() {
		return this.email.getValue();
	}
	public void setEmail(String email) {
		this.email.setValue(email);
	}
	
	public UserState getState()
	{
		return UserStateViewHelper.getUserState(state.getValue());
	}

	public void setState(UserState state)
	{
		this.state.setValue(UserStateViewHelper.getStateString(state));
	}

	public void setFocus() {
		name.focus();
	}
	
	private void makeFields() {

		name = new Textbox();
		name.setWidth("98%");
		name.setMaxlength(20);
		name.setConstraint(new SimpleTextConstraint(SimpleTextConstraint.NO_EMPTY));
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.name")), name);
	
		password = new Textbox();
		password.setWidth("98%");
		password.setMaxlength(20);
		password.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY, RegularExpressions.SIMPLE_VALID_TEXT_EXPRESSION));
		password.setType("password");
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.password")), password);

		firstName = new Textbox();
		firstName.setWidth("98%");
		firstName.setMaxlength(50);
		firstName.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY));
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.firstname")), firstName);
		
		lastName = new Textbox();
		lastName.setWidth("98%");
		lastName.setMaxlength(50);
		lastName.setConstraint(new TextConstraint(TextConstraint.NO_EMPTY));
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.lastname")), lastName);
		
		telephone = new Textbox();
		telephone.setWidth("98%");
		telephone.setMaxlength(50);
		this.addField(new Label(I18N.getLabel("crud.user.telephone")), telephone);
		telephone.setConstraint(new TextConstraint(RegularExpressions.TELEPHONE_VALID_EXPRESSION));
		
		cellphone = new Textbox();
		cellphone.setWidth("98%");
		cellphone.setMaxlength(50);
		this.addField(new Label(I18N.getLabel("crud.user.cellphone")), cellphone);
		cellphone.setConstraint(new TextConstraint(RegularExpressions.TELEPHONE_VALID_EXPRESSION));
		
		email = new Textbox();
		email.setWidth("98%");
		email.setConstraint(new EmailConstraint(EmailConstraint.NO_EMPTY));
		email.setMaxlength(50);
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.email")), email);
		
		address = new Textbox();
		address.setWidth("98%");
		address.setMaxlength(100);
		this.addField(new Label(I18N.getLabel("crud.user.address")), address);

		state = new Label(UserStateViewHelper.getStateString(UserState.ACTIVE));
		
		this.addField(new Label(I18N.getLabel("crud.user.state")), state);		
		
		userWF = BooleanViewHelper.getRadiogroup3(true);
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.userWF")), userWF);
		
		validatePass = BooleanViewHelper.getRadiogroup2(true);
		this.addField(new RequiredLabel(I18N.getLabel("crud.user.validatePass")), validatePass);	
		
		sucursal= SucursalViewHelper.getComboBoxConOpcionTodos222();
		this.addField(new RequiredLabel("Sucursal"), sucursal);

	}
	
}
package com.institucion.model;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.EmailConstraint;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ClienteCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Textbox nombre;
	private Textbox apellido;
	private Textbox domicilio;
	private Textbox dni;
	private Datebox fechaNacimiento;
	private Textbox telefono;
	private Textbox celular;
	private Textbox facebook;
	private Textbox mail;
	private Combobox tieneCertificado;
	
	
	public ClienteCrud (){
		super();
		this.makeFields();
	}
	
	    
	private void makeFields(){

		apellido= new Textbox();
		apellido.setMaxlength(20);
		apellido.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.apellido")), apellido);
		
		nombre= new Textbox();
		nombre.setMaxlength(20);
		nombre.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.nombre")), nombre);
		
		dni= new Textbox();
		dni.setMaxlength(20);
		dni.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("client.dni")), dni);
				
		this.fechaNacimiento = new Datebox();
		fechaNacimiento.setFormat(I18N.getDateFormat());
		this.addField(new RequiredLabel(I18N.getLabel("client.fechaNac")), fechaNacimiento);

		domicilio= new Textbox();
		domicilio.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.domicilio")), domicilio);
		
		celular= new Textbox();
		celular.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("client.celular")), celular);
		
		telefono= new Textbox();
		telefono.setMaxlength(20);
		telefono.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("client.telefono")), telefono);
	
		facebook= new Textbox();
		facebook.setMaxlength(40);
		facebook.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("client.facebook")), facebook); 
		
		mail= new Textbox();
		mail.setMaxlength(40);
		mail.setConstraint(new EmailConstraint());
		this.addField(new Label(I18N.getLabel("client.mail")), mail); 

		tieneCertificado =  BooleanViewHelper.getComboBox();
		tieneCertificado.setConstraint("strict");
		this.addField(new Label(I18N.getLabel("client.tieneCertificado")), tieneCertificado);
		
	}

	public Textbox getNombre() {
		return nombre;
	}


	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}


	public Textbox getApellido() {
		return apellido;
	}


	public void setApellido(Textbox apellido) {
		this.apellido = apellido;
	}


	public Textbox getDni() {
		return dni;
	}


	public void setDni(Textbox dni) {
		this.dni = dni;
	}


	public Datebox getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(Datebox fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public Textbox getDomicilio() {
		return domicilio;
	}


	public void setDomicilio(Textbox domicilio) {
		this.domicilio = domicilio;
	}


	public Textbox getTelefono() {
		return telefono;
	}


	public void setTelefono(Textbox telefono) {
		this.telefono = telefono;
	}


	public Textbox getCelular() {
		return celular;
	}


	public void setCelular(Textbox celular) {
		this.celular = celular;
	}


	public Textbox getFacebook() {
		return facebook;
	}


	public void setFacebook(Textbox facebook) {
		this.facebook = facebook;
	}


	public Textbox getMail() {
		return mail;
	}


	public void setMail(Textbox mail) {
		this.mail = mail;
	}


	public Combobox getTieneCertificado() {
		return tieneCertificado;
	}


	public void setTieneCertificado(Combobox tieneCertificado) {
		this.tieneCertificado = tieneCertificado;
	}

	public void setSelectedTieneCertificado (boolean tieneCertficadobool){
		boolean found = false;
		int i = 0;
		while(!found && i<tieneCertificado.getItemCount()){
			tieneCertificado.getItemAtIndex(i);
			if(tieneCertificado.getItemAtIndex(i) != null 
					&&(Boolean)tieneCertificado.getItemAtIndex(i).getValue() == tieneCertficadobool){
				found = true;
				tieneCertificado.setSelectedIndex(i);
			}
			i++;
		}
	}	
}
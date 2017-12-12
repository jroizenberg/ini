package com.institucion.model;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SubscripcionCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private RequiredLabel cliente;
//	private Textbox estado;
	private Textbox comentario;
	private Textbox comentarioPosponer;
	private Label comentarioLabel;
	private Label comentarioPosponerLabel;
	private Label comentarioAdeudaLabel;
	private Textbox comentarioAdeuda;
	
	public SubscripcionCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		cliente= new RequiredLabel(" ");
		cliente.setWidth("35px");
		cliente.setMaxlength(50);
		RequiredLabel rr=new RequiredLabel(I18N.getLabel("client.nombre.1"));
		rr.setWidth("20px");
//		this.addField(rr, cliente);
		
		comentario= new Textbox();
		comentario.setMaxlength(200);
		comentario.setRows(3);
		comentario.setWidth("98%");
		comentario.setConstraint(new TextConstraint());
		comentario.setDisabled(true);
		
		comentarioLabel =new Label(I18N.getLabel("client.comment.1"));
		comentarioLabel.setVisible(false);
		comentario.setVisible(false);
//		this.addField(comentarioLabel, comentario);

		comentarioPosponer= new Textbox();
		comentarioPosponer.setMaxlength(400);
		comentarioPosponer.setRows(3);
		comentarioPosponer.setWidth("98%");
		comentarioPosponer.setConstraint(new TextConstraint());
		comentarioPosponer.setDisabled(true);
		comentarioPosponer.setVisible(false);
		comentarioPosponerLabel =new Label(I18N.getLabel("client.comment.1.posponer"));
		comentarioPosponerLabel.setVisible(false);

//		this.addField(comentarioPosponerLabel, comentarioPosponer);
		
		comentarioAdeuda= new Textbox();
		comentarioAdeuda.setMaxlength(200);
		comentarioAdeuda.setRows(2);
		comentarioAdeuda.setWidth("98%");
		comentarioAdeuda.setConstraint(new TextConstraint());
		comentarioAdeuda.setDisabled(true);
		
		comentarioAdeudaLabel =new Label(I18N.getLabel("client.comment.deuda.saldada"));
		comentarioAdeudaLabel.setVisible(false);
		comentarioAdeuda.setVisible(false);
//		this.addField(comentarioAdeudaLabel, comentarioAdeuda);
	
		
		this.addFieldUnique(rr, cliente,comentarioLabel, comentario , comentarioPosponerLabel ,comentarioPosponer, comentarioAdeudaLabel, comentarioAdeuda );
	}

	public Label getCliente() {
		return cliente;
	}

	public void setCliente(RequiredLabel cliente) {
		this.cliente = cliente;
	}

	public Textbox getComentario() {
		return comentario;
	}

	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}

	public Textbox getComentarioPosponer() {
		return comentarioPosponer;
	}

	public void setComentarioPosponer(Textbox comentarioPosponer) {
		this.comentarioPosponer = comentarioPosponer;
	}

	public Label getComentarioLabel() {
		return comentarioLabel;
	}

	public void setComentarioLabel(Label comentarioLabel) {
		this.comentarioLabel = comentarioLabel;
	}

	public Label getComentarioPosponerLabel() {
		return comentarioPosponerLabel;
	}

	public void setComentarioPosponerLabel(Label comentarioPosponerLabel) {
		this.comentarioPosponerLabel = comentarioPosponerLabel;
	}

	public Label getComentarioAdeudaLabel() {
		return comentarioAdeudaLabel;
	}

	public void setComentarioAdeudaLabel(Label comentarioAdeudaLabel) {
		this.comentarioAdeudaLabel = comentarioAdeudaLabel;
	}

	public Textbox getComentarioAdeuda() {
		return comentarioAdeuda;
	}

	public void setComentarioAdeuda(Textbox comentarioAdeuda) {
		this.comentarioAdeuda = comentarioAdeuda;
	}
}

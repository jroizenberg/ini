package com.institucion.fm.desktop.view;


public class ToolbarGuardarMatriculaGratuitaCrud extends TextToolbar{
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		this.addSaveButton("Guardar Matricula Gratuita");
		this.addOnBackButton();
	}
}
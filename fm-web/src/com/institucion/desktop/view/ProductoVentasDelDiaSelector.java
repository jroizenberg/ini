package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ProductoVentasDelDiaSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addAnularVentaButton("Anular Venta");
		addCambiarProductoPorFallaButton("Cambiar Prod Fallado");
		
		addOnBackButton();

	}
}

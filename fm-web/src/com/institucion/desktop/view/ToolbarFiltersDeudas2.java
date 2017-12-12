package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ToolbarFiltersDeudas2 extends TextToolbar {
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
		addVerSubsButton();

		addBar();
//		addBar();

//		addPospoenerSubsButton();
//		addAnularSubsButton();
//		addVencerElCursoButton();
		addPasarASaldadoButton();
	}

}

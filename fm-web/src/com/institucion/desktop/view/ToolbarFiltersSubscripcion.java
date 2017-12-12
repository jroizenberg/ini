package com.institucion.desktop.view;

import com.institucion.fm.desktop.view.TextToolbar;

public class ToolbarFiltersSubscripcion extends TextToolbar {
	private static final long serialVersionUID = 1L;

	@Override
	public void addButtons() {
		
		addAbonarYSildarDeleteButton("Abonar y Guardar");
		
//		boolean esMaipu=false;
//		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
//			
//			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
//				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
//					esMaipu= true;
//				}
//			}
//		}
//
//		if(!esMaipu)
//			addMatriculaGratisButton();
		
		addOnBackButton();
	}
	
	
}

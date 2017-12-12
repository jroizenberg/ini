	package com.institucion.desktop.view;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.TextToolbar;

public class IngresoInscripcionSelector extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		addInsertButton(I18N.getLabel("toolbarselector.crear.cliente"));
		addUpdateButton(I18N.getLabel("toolbarselector.modificar.cliente"));
		addSeparator();
		addBar();
		addBar();
		addInsertSubsButton();
		addSaldarDeudaSubsButton();
		addinsertReimprimirComprobanteButton();

//		boolean esMaipu=false;
//		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
//			
//			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
//				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
//					esMaipu= true;
//				}
//			}
//		}
////		
//		if(!esMaipu){
//			addMatriculaGratisButton();
//		}
	}
	
}
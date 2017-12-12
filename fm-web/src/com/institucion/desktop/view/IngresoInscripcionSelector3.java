	package com.institucion.desktop.view;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.view.TextToolbar;
import com.institucion.model.SucursalEnum;

public class IngresoInscripcionSelector3 extends TextToolbar {
	private static final long serialVersionUID = 1L;

	public void addButtons() {
		
		addIngresosEgresosButton();
		addNuevoGastoCentroButton();
		
		boolean esMaipu=false;
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
					esMaipu= true;
				}
			}
		}
		if(!esMaipu)
			addVenderProductosButton();

		addExportExcelClientButton();
//		onExportClientTODOExcel();
	}
	
}
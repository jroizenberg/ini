package com.institucion.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.model.SucursalEnum;

public class MainSelectorComposer extends SelectorFEComposer  {
	
	public MainSelectorComposer(){
		super();
	}

	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
				
				if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
					SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));

					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
//					Session.getDesktopPanel().setSrc(null);
//					Session.getDesktopPanel().setSrc("/institucion/ingresoInscripcion-selector.zul");
//					Session.getDesktopPanel().setMessage("");

				}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
					String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				}
			}
	}
		
	public void clear(){
	}
	
	
	public void onCreate() {
		
		
	if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));

				super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
//				Session.getDesktopPanel().setSrc(null);
//				Session.getDesktopPanel().setSrc("/institucion/ingresoInscripcion-selector.zul");
//				Session.getDesktopPanel().setMessage("");

			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
			}
		}
	}



	@Override
	public void onFind(Event evt) {
		// TODO Auto-generated method stub
		
	}

}
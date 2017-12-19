package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;

import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;

public class ImprimirCombobox extends Combobox implements AfterCompose{
	private static final long serialVersionUID = 1L;
//	private Comboitem ultimoCombo;
//	public boolean esLaPrimeraVez=true;;
	public void onCreate() throws Exception {
		this.setWidth("40%");
//
//		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
//			
//			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
//				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
//				setSelected(this, suc, null);
//				
//			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
//				String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
//				setSelected(this, null, suc);
//			}
//		}
	}
	public void afterCompose(){
		this.setWidth("28%");

		getComboboxForLoginAdmin(this);
		this.setAutocomplete(false);		
		this.setConstraint("no empty, strict");
		this.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				Combobox combo = (Combobox) evt.getTarget();
				Session.setAttribute("imprimirPorCombo", combo.getSelectedItem().getValue());
			}
		});
		this.setStyle("font-weight: bold !important; display: ");
	}
		 

	public static Combobox getComboboxForLoginAdmin(Combobox combo) {
		combo.getItems().clear();
		SucursalViewHelper.getComboBoxImprimir(combo);
		
		if(combo != null && combo.getSelectedItem() != null){
			Session.setAttribute("imprimirPorCombo", combo.getSelectedItem().getValue());
		}
	
		return combo;
	}
}
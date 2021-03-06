package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.model.SucursalEnum;

public class SucursalesCombobox extends Combobox implements AfterCompose{
	private static final long serialVersionUID = 1L;
	private Comboitem ultimoCombo;
	public boolean esLaPrimeraVez=true;;
	public void onCreate() throws Exception {
		this.setWidth("40%");

		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				SucursalEnum suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				setSelected(this, suc, null);
				
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				setSelected(this, null, suc);
			}
		}
	}

	private void setSelected(Combobox combo, SucursalEnum val, String valString){
		
		if(combo != null && combo.getItems() != null){
			
			if(val != null){
				for (Object comboit : combo.getItems()) {
					if(((Comboitem)comboit).getValue() instanceof SucursalEnum){
						int aa=((SucursalEnum)((Comboitem)comboit).getValue()).toInt();
						if(aa == val.toInt())
							combo.setSelectedItem((Comboitem)comboit);
					}
				} 
			}
			if(valString != null){
				for (Object comboit : combo.getItems()) {
					
					if(((Comboitem)comboit).getValue() instanceof String){
						String aa=((String)((Comboitem)comboit).getValue());
						if(aa.equalsIgnoreCase(valString))
							combo.setSelectedItem((Comboitem)comboit);

					}
				} 
			}
		}
	}
	


	public void afterCompose(){
		this.setWidth("28%");

		getComboboxForLoginAdmin(this);
		this.setAutocomplete(false);		
		this.setConstraint("no empty, strict");
		this.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){

				Combobox combo = (Combobox) evt.getTarget();
				
				if(combo.getSelectedItem() != null){
					ultimoCombo=combo.getSelectedItem();
				}else if(ultimoCombo != null)
					combo.setSelectedItem(ultimoCombo);
					
				Session.setAttribute("sucursalPrincipalSeleccionada", combo.getSelectedItem().getValue());
				// Se redirecciona a la nueva session del nuevo pais
				Executions.getCurrent().sendRedirect(null);
			}
		});
		this.setStyle("font-weight: bold !important; display: ");

		if(this.getSelectedItem() != null){
			ultimoCombo=this.getSelectedItem();
		}else if(ultimoCombo != null){
			this.setSelectedItem(ultimoCombo);
		}

		if(!esLaPrimeraVez){
			Session.setAttribute("sucursalPrincipalSeleccionada", ultimoCombo.getValue());
			// Se redirecciona a la nueva session del nuevo pais
			Executions.getCurrent().sendRedirect(null);
		}

		esLaPrimeraVez=false;

	}
		 

	public static Combobox getComboboxForLoginAdmin(Combobox combo) {
		combo.getItems().clear();
		SucursalEnum sucDelUsuario=Session.getsucursalDelLoguin();

		SucursalViewHelper.getComboBoxConOpcionMenuTree(combo, sucDelUsuario);
		
		if(combo != null && combo.getSelectedItem() != null){
			Session.setAttribute("sucursalPrincipalSeleccionada", combo.getSelectedItem().getValue());
		}
	
		return combo;
	}
}
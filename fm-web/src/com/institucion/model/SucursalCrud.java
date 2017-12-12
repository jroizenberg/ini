package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;

import com.institucion.desktop.delegated.SucursalDelegate;
import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class SucursalCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Combobox sucursal;
	private Label responsable;
	private Label responsableL;

//	private ClaseEJB claseEJB;
	private SucursalDelegate delegate;
	public SucursalCrud (){
		super();
//		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

		this.makeFields();
		
	}
	    	
	private void makeFields(){
	
		sucursal= new Combobox();
		sucursal.setConstraint("strict");
		sucursal= SucursalViewHelper.getComboBox();			
		
		sucursal.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
				if(sucursal.getSelectedItem() != null && ((SucursalEnum)sucursal.getSelectedItem().getValue()).toInt() == 
						SucursalEnum.CENTRO.toInt()){
					delegate.actualizarPorSucursal((SucursalEnum)((Combobox)evt.getTarget()).getSelectedItem().getValue());
//					quincena.setVisible(false);
//					quincena.setSelectedItem(null);				
				}else{
					delegate.actualizarPorSucursal((SucursalEnum)((Combobox)evt.getTarget()).getSelectedItem().getValue());
//					quincena.setVisible(true);
				}
			}
		});	

		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			SucursalEnum suc= null;
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if( (suc.equals(SucursalEnum.CENTRO))){
					sucursal.setSelectedIndex(0);
				}else{
					sucursal.setSelectedIndex(1);
				}
				sucursal.setDisabled(true);
				
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					suc= SucursalEnum.CENTRO;
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					suc= SucursalEnum.MAIPU;
				}
				if( (suc.equals(SucursalEnum.CENTRO))){
					sucursal.setSelectedIndex(0);
				}else{
					sucursal.setSelectedIndex(1);
				}
				sucursal.setDisabled(true);
			}
		}

		this.addFieldUnique(new RequiredLabel("Sucursal"), sucursal);	
		
		responsable= new Label("");
		responsableL= new RequiredLabel("Responsable");

		this.addFieldUnique(responsableL, responsable);
	}

	
	public Combobox getSucursal() {
		return sucursal;
	}

	public void setSucursal(Combobox sucursal) {
		this.sucursal = sucursal;
	}

	public SucursalDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(SucursalDelegate delegate) {
		this.delegate = delegate;
	}

	public Label getResponsable() {
		return responsable;
	}

	public void setResponsable(Label responsable) {
		this.responsable = responsable;
	}

	public Label getResponsableL() {
		return responsableL;
	}

	public void setResponsableL(Label responsableL) {
		this.responsableL = responsableL;
	}
}

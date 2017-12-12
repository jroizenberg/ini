package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.delegated.IngresosEgresosDelegate;
import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class CerrarCajaCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Intbox valor;
	private Textbox comentario;
    private IngresosEgresosDelegate delegate;
	private Combobox sucursal;

	public CerrarCajaCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
			
		sucursal = SucursalViewHelper.getComboBox();
		sucursal.setConstraint("no empty, strict");
		sucursal.setWidth("91%");

		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			SucursalEnum suc= null;
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc.equals(SucursalEnum.CENTRO)){
					sucursal.setSelectedIndex(0);
				}else if(suc.equals(SucursalEnum.MAIPU)){
					sucursal.setSelectedIndex(1);
				}

			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					suc= SucursalEnum.CENTRO;
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					suc= SucursalEnum.MAIPU;
				}
				if(suc.equals(SucursalEnum.CENTRO)){
					sucursal.setSelectedIndex(0);
				}else if(suc.equals(SucursalEnum.MAIPU)){
					sucursal.setSelectedIndex(1);
				}
			}
			sucursal.setDisabled(true);
		}
		valor= new Intbox();
		valor.setStyle("color: red ; font-weight: bold ; ");
		valor.setConstraint("/(([0-9]+)?)+/"); //, /(([0-9]+)?)+/"); //"/([0-9])/"); //   ([0-9]+)?)+/");
		valor.addEventListener(Events.ON_CHANGE, new EventListener(){
			public void onEvent(Event evt){
				
				if(((InputEvent)evt).getValue() != null){
					if(((InputEvent)evt).getValue().contains(",")){
						String s=((InputEvent)evt).getValue();
						s=s.substring(0, s.indexOf(","));
						valor.setValue(Integer.parseInt(s));
						valor.setText(s);
						((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
					}
				}
			}
		});
		
		comentario= new Textbox();
		
		this.addFieldUnique(new RequiredLabel("Sucursal"), sucursal, 
				new RequiredLabel("Ingresar que monto en $ queda en caja: "), valor, 
				new Label(I18N.getLabel("ingresos.egresos.comentario")), comentario);
	}

	public Textbox getComentario() {
		return comentario;
	}

	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}

	public IngresosEgresosDelegate getDelegate() {
		return delegate;
	}

	public Intbox getValor() {
		return valor;
	}

	public void setValor(Intbox valor) {
		this.valor = valor;
	}

	public void setDelegate(IngresosEgresosDelegate delegate) {
		this.delegate = delegate;
	}

	public Combobox getSucursal() {
		return sucursal;
	}

	public void setSucursal(Combobox sucursal) {
		this.sucursal = sucursal;
	}
}

package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;

import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ProductoPrecioCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private RequiredLabel precioCb;
	private RequiredLabel precioCbLabel;

	private Checkbox efectivo;
	private Checkbox debito;
	private Checkbox tarjeta_15;
	private Checkbox tarjeta_20;
	private ClienteDelegate delegate;
	
	public ProductoPrecioCrud (){
		super();
		this.makeFields();
	}
	
	private void makeFields(){
		
		this.efectivo = new Checkbox();
		this.efectivo .setWidth("1px");
		this.efectivo .setChecked(true);
		this.efectivo.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(efectivo.isChecked()){
					debito.setChecked(false);
					tarjeta_15.setChecked(false);
					tarjeta_20.setChecked(false);
				}
				delegate.actualizarPaneldePrecioProducto();
			}
		});	
		
		this.debito = new Checkbox();
		this.debito .setWidth("1px");
		this.debito.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(debito.isChecked()){
					efectivo.setChecked(false);
					tarjeta_15.setChecked(false);
					tarjeta_20.setChecked(false);
				}
				delegate.actualizarPaneldePrecioProducto();
			}
		});	

		this.tarjeta_15 = new Checkbox();
		this.tarjeta_15 .setWidth("1px");
		this.tarjeta_15.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(tarjeta_15.isChecked()){
					efectivo.setChecked(false);
					debito.setChecked(false);
					tarjeta_20.setChecked(false);
				}
				delegate.actualizarPaneldePrecioProducto();
			}
		});	
	
		this.tarjeta_20 = new Checkbox();
		this.tarjeta_20 .setWidth("1px");
		this.tarjeta_20.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event evt) {
				if(tarjeta_20.isChecked()){
					efectivo.setChecked(false);
					debito.setChecked(false);
					tarjeta_15.setChecked(false);
				}
				delegate.actualizarPaneldePrecioProducto();
			}
		});	

		precioCb= new RequiredLabel("$0");		
		precioCbLabel= new RequiredLabel("Precio TOTAL ACUMULADO: ");
		this.precioCbLabel .setWidth("30%");

		Label l= new Label("CON EFECTIVO:");
		l .setWidth("6px");
		Label l2= new Label("CON DEBITO:");
		l2.setWidth("5px");
		Label l3= new Label("CON TARJETA(%15):");	
		l3.setWidth("5px");
		Label l4= new Label("CON TARJETA(%20):");	
		l4.setWidth("5px");

		this.addFieldUnique(l, this.efectivo, l2, this.debito,l3, this.tarjeta_15,l4, this.tarjeta_20 , precioCbLabel, precioCb);
	}

	public RequiredLabel getPrecioCb() {
		return precioCb;
	}

	public void setPrecioCb(RequiredLabel precioCb) {
		this.precioCb = precioCb;
	}

	public Checkbox getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Checkbox efectivo) {
		this.efectivo = efectivo;
	}

	public Checkbox getDebito() {
		return debito;
	}

	public void setDebito(Checkbox debito) {
		this.debito = debito;
	}

	public Checkbox getTarjeta_15() {
		return tarjeta_15;
	}

	public void setTarjeta_15(Checkbox tarjeta) {
		this.tarjeta_15 = tarjeta;
	}

	public Checkbox getTarjeta_20() {
		return tarjeta_20;
	}

	public void setTarjeta_20(Checkbox tarjeta) {
		this.tarjeta_20 = tarjeta;
	}

	public ClienteDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(ClienteDelegate delegate) {
		this.delegate = delegate;
	}

	public RequiredLabel getPrecioCbLabel() {
		return precioCbLabel;
	}

	public void setPrecioCbLabel(RequiredLabel precioCbLabel) {
		this.precioCbLabel = precioCbLabel;
	}
}

package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.delegated.GastosDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.desktop.helper.GastosViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class GastosCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Combobox anio;   
	private Combobox mes;
	private Combobox esPagoMensual;
	private Combobox tipoGastoCB;
//	private Combobox mesAlQueSeLeDescuentaElDineroDelPagoDelSueldo;
//	private Combobox anioAlQueSeLeDescuentaElDineroDelPagoDelSueldo;   

	private Combobox empleadoCB;

	private Textbox comentario;
	private Intbox dinero;
	private GastosDelegate delegate;
	private Datebox fechaDesde;
	private Datebox fechaHasta;
	
	private Label mesL;
	private Label anioL;
	
	private Label empleado;
	private Label fechaPagoDL;
	private Label fechaPagoHL;
	
	public GastosCrud (){
		super();
		this.makeFields();
	}
	    
	private void makeFields(){
		
		esPagoMensual= BooleanViewHelper.getComboBox(true);
		esPagoMensual.setConstraint("strict");
		esPagoMensual.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {

				if(esPagoMensual.getSelectedItem() != null && (Boolean)esPagoMensual.getSelectedItem().getValue()){
//					mes.setDisabled(false);
//					anio.setDisabled(false);
					fechaDesde.setVisible(false);
					fechaHasta.setVisible(false);
					fechaPagoDL.setVisible(false);
					fechaPagoHL.setVisible(false);

//					mesAlQueSeLeDescuentaElDineroDelPagoDelSueldo.setVisible(false);
				}else{
//					mes.setDisabled(true);
//					anio.setDisabled(true);
					fechaDesde.setVisible(true);
					fechaHasta.setVisible(true);
					fechaPagoDL.setVisible(true);
					fechaPagoHL.setVisible(true);

//					mesAlQueSeLeDescuentaElDineroDelPagoDelSueldo.setVisible(true);
				}
			}
		});	
		this.addField(new RequiredLabel("Es Pago Mensual"), esPagoMensual);
		
		this.addField(new Label(" "), new Label(" "));

		fechaDesde  = new Datebox();
		fechaDesde .setMaxlength(20);
		fechaDesde.setId("sasas");
		fechaDesde .setFormat(I18N.getDateFormat());
		
		fechaPagoDL=new RequiredLabel("Fecha Pago Desde");
		this.addField(fechaPagoDL, fechaDesde);
		
		fechaHasta  = new Datebox();
		fechaHasta .setMaxlength(20);
		fechaHasta .setFormat(I18N.getDateFormat());
		fechaPagoHL=new RequiredLabel("Fecha Pago Hasta");
		this.addField(fechaPagoHL, fechaHasta);		
				
		fechaDesde.setVisible(false);
		fechaHasta.setVisible(false);
		fechaPagoDL.setVisible(false);
		fechaPagoHL.setVisible(false);

//		mesAlQueSeLeDescuentaElDineroDelPagoDelSueldo.setVisible(false);
					
		anioL= new RequiredLabel(I18N.getLabel("gastos.anio"));
		anio = GastosViewHelper.getComboBoxAnio();
		this.addField(anioL, anio);

		mesL= new RequiredLabel(I18N.getLabel("gastos.mes"));
		mes = GastosViewHelper.getComboBoxMes();
		mes.setConstraint("strict");
		this.addField(mesL, mes);
	

		String tipoGasto =  I18N.getLabel("gastos.tipogasto");
		tipoGastoCB= new Combobox();
		tipoGastoCB = GastosViewHelper.getComboBoxTipoGasto(tipoGastoCB, false, false);
		tipoGastoCB.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {

				Empleado emp= null;
				if(empleadoCB.getSelectedItem() != null){
					emp=((Empleado)(empleadoCB).getSelectedItem().getValue());
				}

				if(tipoGastoCB.getSelectedItem() != null && 
						(((GastosEnum)tipoGastoCB.getSelectedItem().getValue())).toInt() == GastosEnum.SUELDOS.toInt()){
					
					
						delegate.actualizarPanelSueldos(false, emp);
						esPagoMensual.setDisabled(false);
						
						anioL.setValue("Año al que se le Descuenta el Dinero de los Sueldos");
						mesL.setValue("Mes al que se le Descuenta el Dinero de los Sueldos");

						empleadoCB.setVisible(true);
						empleadoCB.setSelectedItem(null);
						empleado.setVisible(true);
				}else{
					delegate.actualizarPanelSueldos(false, emp);
					esPagoMensual.setDisabled(true);
					fechaDesde.setVisible(false);
					fechaHasta.setVisible(false);
					fechaPagoDL.setVisible(false);
					fechaPagoHL.setVisible(false);
					empleadoCB.setSelectedItem(null);
					empleadoCB.setVisible(false);
					empleado.setVisible(false);

					anioL.setValue(I18N.getLabel("gastos.anio"));
					mesL.setValue(I18N.getLabel("gastos.mes"));
				}
			}
		});	
		
		this.addField(new RequiredLabel(tipoGasto), tipoGastoCB);
		
		
		empleadoCB= new Combobox();
		empleadoCB= GastosViewHelper.getComboBoxEmpleados(empleadoCB);
		
		empleadoCB.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt) {
				Empleado emp= null;
				if(((Combobox)evt.getTarget()).getSelectedItem() != null){
					emp=((Empleado)(((Combobox)evt.getTarget())).getSelectedItem().getValue());
				}
				if(tipoGastoCB.getSelectedItem() != null && 
						(((GastosEnum)tipoGastoCB.getSelectedItem().getValue())).toInt() == GastosEnum.SUELDOS.toInt()){
					
						delegate.actualizarPanelSueldos(true, emp);
				}else{
					delegate.actualizarPanelSueldos(false, emp);
				}
			}
		});	
		empleado= new RequiredLabel("Empleado");
		empleado.setVisible(false);
		empleadoCB.setVisible(false);
		this.addField(empleado, empleadoCB);
		
		
		
		dinero = new Intbox();
		dinero.setConstraint("/(([0-9]+)?)+/"); //, /(([0-9]+)?)+/"); //"/([0-9])/"); //   ([0-9]+)?)+/");
		dinero.addEventListener(Events.ON_CHANGE, new EventListener(){
			public void onEvent(Event evt){
				
				if(((InputEvent)evt).getValue() != null){
					if(((InputEvent)evt).getValue().contains(",")){
						String s=((InputEvent)evt).getValue();
						s=s.substring(0, s.indexOf(","));
						dinero.setValue(Integer.parseInt(s));
						dinero.setText(s);
						((Intbox)evt.getTarget()).setValue(Integer.parseInt(s));
					}
				}
			}
		});
		
		this.addField(new RequiredLabel(I18N.getLabel("gastos.list.dinero")), dinero);
		
		comentario= new Textbox();
		comentario.setRows(3);
		comentario.setMaxlength(150);
		comentario.setWidth("auto");
		comentario.setConstraint(new TextConstraint());
		
		this.addField(new Label(I18N.getLabel("gastos.list.comentario")), comentario); 
	}

	public void setSelectedTipoGasto (GastosEnum selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<tipoGastoCB.getItemCount()){
			if(selectedHPType.toInt() == (((GastosEnum) tipoGastoCB.getItemAtIndex(i).getValue()).toInt())){
				found = true;
				tipoGastoCB.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedEmpleado (Empleado selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<empleadoCB.getItemCount()){
			if(selectedHPType.getId().intValue() == (((Empleado) empleadoCB.getItemAtIndex(i).getValue()).getId().intValue())){
				found = true;
				empleadoCB.setSelectedIndex(i);
			}
			i++;
		}
	}
	public void setSelectedPagoMensual (Boolean selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<esPagoMensual.getItemCount()){
			if(selectedHPType  == (((Boolean) esPagoMensual.getItemAtIndex(i).getValue()))){
				found = true;
				esPagoMensual.setSelectedIndex(i);
			}
			i++;
		}
	}

	public void setSelectedMes (Long selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<mes.getItemCount()){
			if(selectedHPType.intValue()  == (((Long) mes.getItemAtIndex(i).getValue())).intValue()){
				found = true;
				mes.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public void setSelectedAnio (Long selectedHPType){
		boolean found = false;
		int i = 0;
		while (!found && i<anio.getItemCount()){
			if(selectedHPType.intValue()  == (((Long) anio.getItemAtIndex(i).getValue())).intValue()){
				found = true;
				anio.setSelectedIndex(i);
			}
			i++;
		}
	}
	
	public Combobox getAnio() {
		return anio;
	}


	public void setAnio(Combobox anio) {
		this.anio = anio;
	}


	public Combobox getMes() {
		return mes;
	}


	public void setMes(Combobox mes) {
		this.mes = mes;
	}


	public Combobox getTipoGastoCB() {
		return tipoGastoCB;
	}


	public void setTipoGastoCB(Combobox tipoGastoCB) {
		this.tipoGastoCB = tipoGastoCB;
	}


	public Textbox getComentario() {
		return comentario;
	}


	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}


	public Intbox getDinero() {
		return dinero;
	}


	public void setDinero(Intbox dinero) {
		this.dinero = dinero;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public GastosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(GastosDelegate delegate) {
		this.delegate = delegate;
	}

	public Combobox getEmpleadoCB() {
		return empleadoCB;
	}

	public void setEmpleadoCB(Combobox empleadoCB) {
		this.empleadoCB = empleadoCB;
	}

	public Combobox getEsPagoMensual() {
		return esPagoMensual;
	}

	public void setEsPagoMensual(Combobox esPagoMensual) {
		this.esPagoMensual = esPagoMensual;
	}

	public Datebox getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Datebox fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Label getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Label empleado) {
		this.empleado = empleado;
	}

	public Datebox getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Datebox fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Label getMesL() {
		return mesL;
	}

	public void setMesL(Label mesL) {
		this.mesL = mesL;
	}

	public Label getAnioL() {
		return anioL;
	}

	public void setAnioL(Label anioL) {
		this.anioL = anioL;
	}

	public Label getFechaPagoDL() {
		return fechaPagoDL;
	}

	public void setFechaPagoDL(Label fechaPagoDL) {
		this.fechaPagoDL = fechaPagoDL;
	}

	public Label getFechaPagoHL() {
		return fechaPagoHL;
	}

	public void setFechaPagoHL(Label fechaPagoHL) {
		this.fechaPagoHL = fechaPagoHL;
	}

}
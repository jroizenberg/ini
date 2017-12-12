package com.institucion.model;

import java.text.DecimalFormat;

import org.zkoss.zul.Label;

import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class CajaTotalesCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label totales;
	private Label efectivo;
	private Label tarjeta;
	private Label debito;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public CajaTotalesCrud (){
		super();
		this.setStyle("  width:40%;  align:center;  margin-left: 20%; ");
		super.setStyle("  width:40%;   align:center;  margin-left: 20%;");
		this.makeFields();
	}
	    
	private void makeFields(){

		efectivo= new Label();
		efectivo.setValue("$"+formateador.format(0));
		debito= new Label();
		debito.setValue("$"+formateador.format(0));
		tarjeta= new Label();
		tarjeta.setValue("$"+formateador.format(0));
		
		totales= new RequiredLabel("$"+formateador.format(0));
		totales.setValue("$"+formateador.format(0));
		this.addFieldUnique(new RequiredLabel("Efectivo:"),efectivo,new RequiredLabel("Debito:"),debito , new RequiredLabel("Tarjeta:"),tarjeta);	
//		this.addFieldUnique(new Label(" "),new RequiredLabel("OS Ioscor:"),ObraSocialIoscor, new RequiredLabel("OS Swiss M:"),ObraSocialSwiss ,new Label(" "));	
		this.addFieldUnique(new Label(" "),new Label("  "),new RequiredLabel("IMP TOTAL:"),totales ,new Label("       ") , new Label(" "));
		
	}

	public Label getTotales() {
		return totales;
	}

	public void setTotales(Label totales) {
		this.totales = totales;
	}

	public Label getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Label efectivo) {
		this.efectivo = efectivo;
	}

	public Label getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Label tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Label getDebito() {
		return debito;
	}

	public void setDebito(Label debito) {
		this.debito = debito;
	}

}
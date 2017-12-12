package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class DeudaTotalCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label totales;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public DeudaTotalCrud (){
		super();
		this.setStyle("  width:40%;  align:center;  margin-left: 20%; ");
		super.setStyle("  width:40%;   align:center;  margin-left: 20%;");
		this.makeFields();
	}
	    
	private void makeFields(){

	}
	
	public void agregarCampos(double sumaCantidadDineroTotal){
		clear();
		if(sumaCantidadDineroTotal >0){
			String dada=String.valueOf(sumaCantidadDineroTotal );
			if(dada!= null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				int aa= Integer.parseInt(sas);
				totales= new RequiredLabel("$"+formateador.format(aa));

			}else{
				totales= new RequiredLabel("$"+formateador.format(sumaCantidadDineroTotal));
			}
			this.addFieldUnique(new RequiredLabel("DEUDA TOTAL:"),totales );
		}

	}
	
	
	public void clear(){
		Rows rows =this.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
		} 
	}
}
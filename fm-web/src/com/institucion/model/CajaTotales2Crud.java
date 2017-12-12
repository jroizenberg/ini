package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class CajaTotales2Crud extends GridCrud{

	private static final long serialVersionUID = 1L;
	private Label totales;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public CajaTotales2Crud (){
		super();
		this.setStyle("  width:40%;  align:center;  margin-left: 20%; ");
		super.setStyle("  width:40%;   align:center;  margin-left: 20%;");
		this.makeFields();
	}
	    
	private void makeFields(){

	}
	
	public void agregarCampos(ArrayList<SumasTotalObraSocial> lista){
		clear();
//		int cantTotalAgregados=0;
		double sumaCantidadDineroTotal=0;
		Label lab1=null;Label val1=null;Label lab2=null;Label val2=null;Label lab3= null;Label val3=null;
		if(lista != null && lista.size() >0){
			int posicion=0;
			for (SumasTotalObraSocial sumasTotalObraSocial : lista) {
				
				if(posicion== 0){
					val1= new RequiredLabel(sumasTotalObraSocial.getObraSocial()+":");
					
					lab1= new Label();
					if(sumasTotalObraSocial.getTotal() != null){
						
						String dada=String.valueOf(sumasTotalObraSocial.getTotal() );

						if(dada!= null && dada.endsWith(",0")){
							String sas=dada.substring(0, dada.indexOf(","));
							int aa= Integer.parseInt(sas);
							
							lab1.setValue("$"+formateador.format(aa));//"$ "+ sas);

						}else if(sumasTotalObraSocial.getTotal() != null){
							lab1.setValue("$"+formateador.format(sumasTotalObraSocial.getTotal()));

						}else{
							lab1.setValue("$"+formateador.format(0));
						}

						sumaCantidadDineroTotal= sumaCantidadDineroTotal+ sumasTotalObraSocial.getTotal();
					}else
						lab1.setValue("$"+formateador.format(0));
					
							
				}else if(posicion== 1){
					val2= new RequiredLabel(sumasTotalObraSocial.getObraSocial()+":");
					
					lab2= new Label();
					if(sumasTotalObraSocial.getTotal() != null){
						
						
						String dada=String.valueOf(sumasTotalObraSocial.getTotal() );

						if(dada!= null && dada.endsWith(",0")){
							String sas=dada.substring(0, dada.indexOf(","));
							int aa= Integer.parseInt(sas);
							
							lab2.setValue("$"+formateador.format(aa));//"$ "+ sas);
						}else if(sumasTotalObraSocial.getTotal() != null){
							lab2.setValue("$"+formateador.format(sumasTotalObraSocial.getTotal()));

						}else{
							lab2.setValue("$"+formateador.format(0));
						}
						
						
				
						sumaCantidadDineroTotal= sumaCantidadDineroTotal+ sumasTotalObraSocial.getTotal();
					}else
						lab2.setValue("$"+formateador.format(0));
				
				}else if(posicion== 2){
					val3= new RequiredLabel(sumasTotalObraSocial.getObraSocial()+":");
					
					lab3= new Label();
					if(sumasTotalObraSocial.getTotal() != null){
						
						
						String dada=String.valueOf(sumasTotalObraSocial.getTotal() );

						if(dada!= null && dada.endsWith(",0")){
							String sas=dada.substring(0, dada.indexOf(","));
							int aa= Integer.parseInt(sas);
							
							lab3.setValue("$"+formateador.format(aa));//"$ "+ sas);

						}else if(sumasTotalObraSocial.getTotal() != null){
							lab3.setValue("$"+formateador.format(sumasTotalObraSocial.getTotal()));

						}else{
							lab3.setValue("$"+formateador.format(0));
						}
				
						sumaCantidadDineroTotal= sumaCantidadDineroTotal+ sumasTotalObraSocial.getTotal();
					}else
						lab3.setValue("$"+formateador.format(0));
				} 
				posicion++;
//				cantTotalAgregados++;
				if( posicion== 3){
					this.addFieldUnique(val1,lab1,val2,lab2,val3,lab3);
					lab1=null; 	lab2=null;	lab3=null;	val1=null;	val2=null;	val3=null;
					posicion=0;	
				
				}				
			}
			// si llego al ultimo y no llego a cumplir los 3
			if(val1 == null)  	val1= new Label(" ");
			if(lab1 == null)  	lab1= new Label(" ");
			if(val2 == null)  	val2= new Label(" ");
			if(lab2 == null)  	lab2= new Label(" ");
			if(val3 == null)  	val3= new Label(" ");
			if(lab3 == null)  	lab3= new Label(" ");
			
			this.addFieldUnique(val1,lab1,val2,lab2,val3,lab3);
			lab1=null; 	lab2=null;	lab3=null;	val1=null;	val2=null;	val3=null;
			posicion=1;		
		}
		
		if(sumaCantidadDineroTotal >0){
			
			String dada=String.valueOf(sumaCantidadDineroTotal );

			if(dada!= null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				int aa= Integer.parseInt(sas);
				totales= new RequiredLabel("$"+formateador.format(aa));

			}else{
				totales= new RequiredLabel("$"+formateador.format(sumaCantidadDineroTotal));
			}
			this.addFieldUnique(new Label(" "),new Label("  "),new RequiredLabel("IMP TOTAL OS:"),totales ,new Label("       ") , new Label(" "));
		}

	}
	
	
	public void clear(){
		Rows rows =this.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
		} 
	}
}
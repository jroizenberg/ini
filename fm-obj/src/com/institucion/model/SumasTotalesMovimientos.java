package com.institucion.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SumasTotalesMovimientos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double total;
	private Double efectivo;
	private Double tarjeta;
	private Double debito;
	private ArrayList<SumasTotalObraSocial> listaObrasSociales;
	
	public Double getTotal() {
		if(total != null){
			Double doo=Math.rint(total*100)/100;
			return doo;
		}else{
			total=(double)0;
		}
		return total;
	}
	
	public Object getTotalInt(){
		if(total != null){
			Double doo=Math.rint(total*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				return Integer.valueOf(sas);
			}else{
				return doo;
			}
		}else{
			total=(double)0;
		}
		return total;
	}
	
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public Double getEfectivo() {
		if(efectivo != null){
			Double doo=Math.rint(efectivo*100)/100;
			return doo;
		}else{
			efectivo=(double)0;
		}
		return efectivo;
	}
	
	public Object getEfectivoInt(){
		if(efectivo != null){
			Double doo=Math.rint(efectivo*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				return Integer.valueOf(sas);
			}else{
				return doo;
			}
		}else{
			efectivo=(double)0;
		}
		return efectivo;
	}
	
	public void setEfectivo(Double efectivo) {
		this.efectivo = efectivo;
	}
	
	public Double getTarjeta() {
		if(tarjeta != null){
			Double doo=Math.rint(tarjeta*100)/100;
			return doo;
		}else
			tarjeta=(double)0;
		return tarjeta;
	}
	
	
	public Object getTarjetaInt(){
		if(tarjeta != null){
			Double doo=Math.rint(tarjeta*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				return Integer.valueOf(sas);
			}else{
				return doo;
			}
		}else{
			tarjeta=(double)0;
		}
		return tarjeta;
	}
	public void setTarjeta(Double tarjeta) {
		this.tarjeta = tarjeta;
	}
	
	
	public Object getDebitoInt(){
		if(debito != null){
			Double doo=Math.rint(debito*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				return Integer.valueOf(sas);
			}else{
				return doo;
			}
		}else{
			debito=(double)0;
		}
			
		return debito;
	}
	
	public Double getDebito() {
		if(debito != null){
			Double doo=Math.rint(debito*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				
				return Double.valueOf(sas);
			}else{
				return doo;
			}
		}else{
			debito=(double)0;
		}
		return debito;
	}
	
	public void setDebito(Double debito) {
		this.debito = debito;
	}
	
	public ArrayList<SumasTotalObraSocial> getListaObrasSociales() {
		return listaObrasSociales;
	}
	
	public void setListaObrasSociales(ArrayList<SumasTotalObraSocial> listaObrasSociales) {
		this.listaObrasSociales = listaObrasSociales;
	}	
}
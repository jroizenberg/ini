package com.institucion.model;


public class SumasTotalObraSocial {
	private String obraSocial;
	private Double total;
	
	public Double getTotal() {
		if(total != null){
		       return Math.rint(total*100)/100;
		}
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getObraSocial() {
		return obraSocial;
	}
	public void setObraSocial(String obraSocial) {
		this.obraSocial = obraSocial;
	}

}
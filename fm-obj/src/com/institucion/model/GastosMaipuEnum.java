package com.institucion.model;


public enum GastosMaipuEnum {

	SUELDOS(1), CANTINA(2), TRANSPORTE(3); // , OTROS(12);

    private int id;

    private GastosMaipuEnum(int id) {
        this.id = id;
    }
    
	public int toInt() { return id; }
	 
	public static GastosMaipuEnum fromInt(int value) {    
	   if(SUELDOS.toInt() == value)
        	return SUELDOS;
        else if(CANTINA.toInt() == value)
        	return CANTINA;
        else if(TRANSPORTE.toInt() == value)
        	return TRANSPORTE;
//        else if(OTROS.toInt() == value)
//        	return OTROS;
        return null;
   }
	
	public static String toString(int value) {
		
		  if(SUELDOS.toInt() == value)
	        	return "Sueldos";
	        else if(CANTINA.toInt() == value)
	        	return "Cantina";
	        else if(TRANSPORTE.toInt() == value)
	        	return "Transporte";
//	        else if(OTROS.toInt() == value)
//	        	return "Otros";
        return null;
	}
	
}    
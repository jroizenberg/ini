package com.institucion.model;


public enum ClaseEstadoEnum {

	NO_INICIADA(0), INICIADA(1);

    private int id;

    private ClaseEstadoEnum(int id) {
        this.id = id;
    }
    
	public int toInt() { return id; }
	 
	public static ClaseEstadoEnum fromInt(int value) {    
		   if(NO_INICIADA.toInt() == value)
	        	return NO_INICIADA;
	        else if(INICIADA.toInt() == value)
	        	return NO_INICIADA;
	        return null;
   }
	
}    
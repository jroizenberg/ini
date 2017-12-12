package com.institucion.model;

public enum ClasesEnum {

	NATACION_INFANTIL_10hrs(0), NATACION_INFANTIL_12hrs(1),NATACION_INFANTIL_13hrs(2),  NATACION_ADULTOS(4), ETC(5);
   // agregar aca mas nombres de clasesssssssss
	
    private int id;

    private ClasesEnum(int id) {
        this.id = id;
    }
    
    public static ClasesEnum getEnumToString(int id) {
        if(NATACION_INFANTIL_10hrs.toInt() == id)
        	return NATACION_INFANTIL_10hrs;
        else if(NATACION_INFANTIL_12hrs.toInt() == id)
        	return NATACION_INFANTIL_12hrs;
        else if(NATACION_INFANTIL_13hrs.toInt() == id)
        	return NATACION_INFANTIL_13hrs;
        else if(NATACION_ADULTOS.toInt() == id)
        	return NATACION_ADULTOS;
        else if(ETC.toInt() == id)
        	return ETC;
   
        return null;
    }
    
    
    public int toInt() { return id; }
}    
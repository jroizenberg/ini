package com.institucion.model;

public enum SucursalEnum {
	
	MAIPU(1), CENTRO(0), TODAS(3);

    private int id;

    private SucursalEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static SucursalEnum fromInt(int id) {
        if(MAIPU.toInt() == id)
        	return MAIPU;
        else if(CENTRO.toInt() == id)
        	return CENTRO;
        else if(TODAS.toInt() == id)
        	return TODAS;

        return null;
    }
    
    public static String toString(int id) {
        if(MAIPU.toInt() == id)
        	return "Maipu 5500-Cursos verano-";
        else if(CENTRO.toInt() == id)
        	return "Centro";      
        return null;
    }
}    
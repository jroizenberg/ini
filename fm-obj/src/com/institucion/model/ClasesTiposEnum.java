package com.institucion.model;

public enum ClasesTiposEnum {

	CLASES_UNITARIAS_AL_MES(0), CLASES_UNITARIAS_POR_SEMANA(1), LIBRE(2);
    private int id;

    private ClasesTiposEnum(int id) {
        this.id = id;
    }
    
	public static ClasesTiposEnum fromInt(int id) {    
		  if(CLASES_UNITARIAS_AL_MES.toInt() == id)
	        	return CLASES_UNITARIAS_AL_MES;
	        else if(CLASES_UNITARIAS_POR_SEMANA.toInt() == id)
	        	return CLASES_UNITARIAS_POR_SEMANA;
	        else if(LIBRE.toInt() == id)
	        	return LIBRE;
		  
	        return null;
	}
	
  
    public int toInt() { return id; }
}    
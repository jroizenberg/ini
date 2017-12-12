package com.institucion.model;

public enum EstadoCursoEnum {
	
	ADEUDA(0), C_CLASES_DISPONIBLES(1), S_CLASES_DISPONIBLES(2);

    private int id;

    private EstadoCursoEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static EstadoCursoEnum fromInt(int id) {
        if(ADEUDA.toInt() == id)
        	return ADEUDA;
        else if(C_CLASES_DISPONIBLES.toInt() == id)
        	return C_CLASES_DISPONIBLES;
        else if(S_CLASES_DISPONIBLES.toInt() == id)
        	return S_CLASES_DISPONIBLES;
      
        return null;
    }
    
    public static String toString(int id) {
        if(ADEUDA.toInt() == id)
        	return "Adeuda";
        else if(C_CLASES_DISPONIBLES.toInt() == id)
        	return "c/clases Disponibles";
        else if(S_CLASES_DISPONIBLES.toInt() == id)
        	return "s/clases Disponibles";
      
        return null;
    }
}    
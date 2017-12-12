package com.institucion.model;

public enum ClienteEstadoEnum {

	ADEUDA(2), C_CLASES_DISP(3), S_CLASES_DISP(4);

    private int id;

    private ClienteEstadoEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static ClienteEstadoEnum fromInt(int id) {
        if(ADEUDA.toInt() == id)
        	return ADEUDA;
        else if(C_CLASES_DISP.toInt() == id)
        	return C_CLASES_DISP;
        else if(S_CLASES_DISP.toInt() == id)
        	return S_CLASES_DISP;  
        return null;
    }
    
    public static String toString(int id) {
         if(ADEUDA.toInt() == id)
        	 return "Adeuda";
         else if(C_CLASES_DISP.toInt() == id)
        	 return "Con clases Disp.";
         else if(S_CLASES_DISP.toInt() == id)
        	 return "Sin clases Disp.";  
        
        return null;
    }
    
}    
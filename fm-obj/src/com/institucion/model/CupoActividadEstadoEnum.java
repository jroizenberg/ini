package com.institucion.model;

public enum CupoActividadEstadoEnum {
	//NUEVA(4), 
	ADEUDA_Y_SIN_CUPOS(0), VENCIDA_CON_DEUDA(1), C_CUPOS(3), VENCIDA(5), ANULADA(6), TODAS(7), S_CUPOS(8), C_DEUDAS_Y_C_CUPOS(10);
    
	// Ver de todas estos estados, como se cuales pueden tener cu
	private int id;

    private CupoActividadEstadoEnum(int id) {
        this.id = id;
    }
    
    public int toInt() { return id; }
    
    /*
     * Si esta en estado AD
     */
    
    public static CupoActividadEstadoEnum fromInt(int id) {
        if(ADEUDA_Y_SIN_CUPOS.toInt() == id)
        	return ADEUDA_Y_SIN_CUPOS;
//        else if(NUEVA.toInt() == id)
//        	return NUEVA;
        else if(C_CUPOS.toInt() == id)
        	return C_CUPOS;
        else if(S_CUPOS.toInt() == id)
        	return S_CUPOS;
        else if(VENCIDA.toInt() == id)
        	return VENCIDA;  
        else if(VENCIDA_CON_DEUDA.toInt() == id)
        	return VENCIDA_CON_DEUDA;
        else if(ANULADA.toInt() == id)
        	return ANULADA;  
        else if(TODAS.toInt() == id)
        	return TODAS;  
        else if(C_DEUDAS_Y_C_CUPOS.toInt() == id)
        	return C_DEUDAS_Y_C_CUPOS;
        return null;
    }
    
    public static String toString(int id) {
        if(ADEUDA_Y_SIN_CUPOS.toInt() == id)
       	 	return "Adeuda-Sin Cupos Disp.";
        else if(C_CUPOS.toInt() == id)
       	 	return "Con cupos Disp.";
        else if(S_CUPOS.toInt() == id)
        	return "Sin cupos Disp.";  
        else if(VENCIDA.toInt() == id)
          	 return "Vencida";  
        else if(VENCIDA_CON_DEUDA.toInt() == id)
          	 return "Adeuda- Vencida";  
        else if(ANULADA.toInt() == id)
          	 return "Anulada";  
        else if(C_DEUDAS_Y_C_CUPOS.toInt() == id)
          	 return "Adeuda-Con cupos Disp.";  
        else if(TODAS.toInt() == id)
         	 return "Todas";  
       return null;
   }
    
}    
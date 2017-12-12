package com.institucion.model;

public enum PagoRapidoSubscripcionEnum {

	TODO_EN_EFECTIVO(0), TODO_CON_DEBITO(1), CUOTAS_1_CON_TARJETA(2), 
//	CUOTAS_3_CON_TARJETA(3), 
	PAGO_COMBINADO(4), PROMOCIONES(5), CUOTAS_6_CON_TARJETA(6);
    private int id;

    private PagoRapidoSubscripcionEnum(int id) {
        this.id = id;
    }
    
    public static PagoRapidoSubscripcionEnum fromInt(int id) {
        if(TODO_EN_EFECTIVO.toInt() == id)
        	return TODO_EN_EFECTIVO;
        else if(TODO_CON_DEBITO.toInt() == id)
        	return TODO_CON_DEBITO;
        else if(CUOTAS_1_CON_TARJETA.toInt() == id)
        	return CUOTAS_1_CON_TARJETA;
//        else if(CUOTAS_3_CON_TARJETA.toInt() == id)
//        	return CUOTAS_3_CON_TARJETA;
        else if(CUOTAS_6_CON_TARJETA.toInt() == id)
        	return CUOTAS_6_CON_TARJETA;

        else if(PAGO_COMBINADO.toInt() == id)
        	return PAGO_COMBINADO;
        else if(PROMOCIONES.toInt() == id)
        	return PROMOCIONES;
        
        return null;
    }
    public int toInt() { return id; }
    
    public static String toString(int id) {
    	 if(TODO_EN_EFECTIVO.toInt() == id)
         	return "1 Pago en Efectivo";
         else if(TODO_CON_DEBITO.toInt() == id)
         	return "1 Pago con Debito";
         else if(CUOTAS_1_CON_TARJETA.toInt() == id)
         	return "Cuotas con Tarjeta- %15 interés";
//         else if(CUOTAS_3_CON_TARJETA.toInt() == id)
//         	return "3 cuotas con Tarjeta- %15 interés";
         else if(CUOTAS_6_CON_TARJETA.toInt() == id)
          	return "Cuotas con Tarjeta- %20 interés";

         else if(PAGO_COMBINADO.toInt() == id)
          	return "Pago Combinado";
    	 
         return null; 	
    }
    
    
}    
package com.institucion.model;

public enum FormasDePagoSubscripcionEnum {

	EFECTIVO(0), DEBITO(1), TARJETA_15(2), TARJETA_20(3);
    private int id;

    private FormasDePagoSubscripcionEnum(int id) {
        this.id = id;
    }
    
    public static FormasDePagoSubscripcionEnum fromInt(int id) {
        if(EFECTIVO.toInt() == id)
        	return EFECTIVO;
        else if(DEBITO.toInt() == id)
        	return DEBITO;
        else if(TARJETA_15.toInt() == id)
        	return TARJETA_15;
        else if(TARJETA_20.toInt() == id)
        	return TARJETA_20;
        return null;
    }
    public int toInt() { return id; }
    
    
    public static String fromIntToString(int id) {
        if(EFECTIVO.toInt() == id)
        	return EFECTIVO.toString();
        else if(DEBITO.toInt() == id)
        	return DEBITO.toString();
        else if(TARJETA_15.toInt() == id)
        	return "Tarjeta con %15";
        else if(TARJETA_20.toInt() == id)
        	return "Tarjeta con %20";
        return null;
    }
}    
package com.institucion.model;

public enum TipoMovimientoCajaEnum {

	EGRESO(0), INGRESO(1),  TODOS(2);

    private int id;

    private TipoMovimientoCajaEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static TipoMovimientoCajaEnum fromInt(int id) {
        if(EGRESO.toInt() == id)
        	return EGRESO;
        else if(INGRESO.toInt() == id)
        	return INGRESO;
        else if(TODOS.toInt() == id)
        	return TODOS;
        return null;
    }
}    
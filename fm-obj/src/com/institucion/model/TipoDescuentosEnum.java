package com.institucion.model;

public enum TipoDescuentosEnum {

	GENERAL(0), OBRA_SOCIAL(1), NO(2);

    private int id;

    private TipoDescuentosEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static TipoDescuentosEnum fromInt(int id) {
        if(GENERAL.toInt() == id)
        	return GENERAL;
        else if(OBRA_SOCIAL.toInt() == id)
        	return OBRA_SOCIAL;
        else if(NO.toInt() == id)
        	return NO;
        return null;
    }
}    
package com.institucion.model;

public enum TipoCursoEnum {

	NATACION(0), TRATAMIENTOS_KINESICOS(1), MASAJES(2), ACTIVIDADES_FISICAS(3), OTROS(5), COMBO(4);

    private int id;

    private TipoCursoEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static TipoCursoEnum fromInt(int id) {
        if(NATACION.toInt() == id)
        	return NATACION;
        else if(TRATAMIENTOS_KINESICOS.toInt() == id)
        	return TRATAMIENTOS_KINESICOS;
        else if(MASAJES.toInt() == id)
        	return MASAJES;
        else if(ACTIVIDADES_FISICAS.toInt() == id)
        	return ACTIVIDADES_FISICAS;  
        else if(COMBO.toInt() == id)
        	return COMBO;  
        else if(OTROS.toInt() == id)
        	return OTROS;  
        return null;
    }
}    
package com.institucion.model;

public enum AnularSubscripcionTipoDevolucionEnum {

	CURSO_SOLO(0), MATRICULA_SOLA(1), CURSO_Y_MATRICULA(2);

    private int id;

    private AnularSubscripcionTipoDevolucionEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static AnularSubscripcionTipoDevolucionEnum fromInt(int id) {
        if(CURSO_SOLO.toInt() == id)
        	return CURSO_SOLO;
        else if(MATRICULA_SOLA.toInt() == id)
        	return MATRICULA_SOLA;
        else if(CURSO_Y_MATRICULA.toInt() == id)
        	return CURSO_Y_MATRICULA;  
        return null;
    }
    
    public static String toString(int id) {
         if(CURSO_SOLO.toInt() == id)
        	 return "Curso solo";
         else if(MATRICULA_SOLA.toInt() == id)
        	 return "Matrícula sola.";
         else if(CURSO_Y_MATRICULA.toInt() == id)
        	 return "Curso y Matrícula.";  
        
        return null;
    }
    
}    
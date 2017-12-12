package com.institucion.model;

public enum VencimientoCursoEnum {

	// solo 3 veces vence al mes
	// solo 1 vez unica
	// 3 veces a la semana x vence al mes

	// 1 sola vez
	
	// Libre todo el mes la cantidad de veces que quiera
	// 3 veces por semana vence al mes
	
	// Libre toda la quincena.
	// 3 veces x semana vence a la quincena
	
	LIBRE_VENCE_A_LA_SEMANA(0),LIBRE_VENCE_AL_MES(1),LIBRE_VENCE_A_LA_QUINCENA(7),LIBRE_VENCE_A_LOS_3_MES(10), 
	SEMANAL_VENCE_A_LA_SEMANA(2),SEMANAL_VENCE_AL_MES(3),SEMANAL_VENCE_A_LOS_3_MES(11), SEMANAL_VENCE_AL_ANo(16), 
	MENSUAL_VENCE_AL_MES(4), MENSUAL_VENCE_A_LOS_3_MES(12), MENSUAL_VENCE_AL_ANo(15),
	QUINCENAL_VENCE_A_LA_QUINCENA(5);
    
	private int id;

    private VencimientoCursoEnum(int id) {
        this.id = id;
    }
    
    public static VencimientoCursoEnum fromInt(int id) {
        if(LIBRE_VENCE_A_LA_SEMANA.toInt() == id)
        	return LIBRE_VENCE_A_LA_SEMANA;
        else if(LIBRE_VENCE_AL_MES.toInt() == id)
        	return LIBRE_VENCE_AL_MES;
        else if(SEMANAL_VENCE_A_LA_SEMANA.toInt() == id)
        	return SEMANAL_VENCE_A_LA_SEMANA;
        else if(SEMANAL_VENCE_AL_MES.toInt() == id)
        	return SEMANAL_VENCE_AL_MES;
        else if(MENSUAL_VENCE_AL_MES.toInt() == id)
        	return MENSUAL_VENCE_AL_MES;
        else if(MENSUAL_VENCE_AL_ANo.toInt() == id)
        	return MENSUAL_VENCE_AL_ANo;
        else if(SEMANAL_VENCE_AL_ANo.toInt() == id)
        	return SEMANAL_VENCE_AL_ANo;
        else if(QUINCENAL_VENCE_A_LA_QUINCENA.toInt() == id)
        	return QUINCENAL_VENCE_A_LA_QUINCENA;
//        else if(SOLO_POR_CLASE_VENCE_AL_MES.toInt() == id)
//        	return SOLO_POR_CLASE_VENCE_AL_MES;
        else if(LIBRE_VENCE_A_LA_QUINCENA.toInt() == id)
        	return LIBRE_VENCE_A_LA_QUINCENA;
        else if(LIBRE_VENCE_A_LOS_3_MES.toInt() == id)
        	return LIBRE_VENCE_A_LOS_3_MES;
        else if(SEMANAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return SEMANAL_VENCE_A_LOS_3_MES;
        else if(MENSUAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return MENSUAL_VENCE_A_LOS_3_MES;
          return null;
    }
    
    
    public static String toString(int id) {
        if(LIBRE_VENCE_A_LA_SEMANA.toInt() == id)
        	return "Libre- Vence a la Semana";
        else if(LIBRE_VENCE_AL_MES.toInt() == id)
        	return "Libre- Vence al Mes";
        else if(LIBRE_VENCE_A_LA_QUINCENA.toInt() == id)
        	return "Libre- Vence a la Quincena";
        else if(LIBRE_VENCE_A_LOS_3_MES.toInt() == id)
        	return "Libre- Vence a los 3 Meses";
        
        
        else if(SEMANAL_VENCE_A_LA_SEMANA.toInt() == id)
        	return "Cant. clases con Carnet- Vence a la Semana";
        else if(SEMANAL_VENCE_AL_MES.toInt() == id)
        	return "Cant. clases con Carnet- Vence al Mes";
        else if(SEMANAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return "Cant. clases con Carnet- Vence a lo 3 Meses";
        
        
        else if(MENSUAL_VENCE_AL_MES.toInt() == id)
        	return "Cant. clases por Mes- Vence al Mes";
        else if(MENSUAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return "Cant. clases por Mes- Vence a lo 3 Meses";
 
        else if(QUINCENAL_VENCE_A_LA_QUINCENA.toInt() == id)
        	return "Cant. clases por Quincena- Vence a la Quincena";
        
        else if(MENSUAL_VENCE_AL_ANo.toInt() == id)
        	return "Cant. clases por Mes- Vence al año";
        else if(SEMANAL_VENCE_AL_ANo.toInt() == id)
        	return "Cant. clases con Carnet- Vence al año";
        
        return null;
    }
    
    public static String toStringMes(int id) {
        if(LIBRE_VENCE_A_LA_SEMANA.toInt() == id)
        	return "A la Semana";
        else if(LIBRE_VENCE_AL_MES.toInt() == id)
        	return "Al Mes";
        else if(LIBRE_VENCE_A_LA_QUINCENA.toInt() == id)
        	return "A la Quincena";
        else if(LIBRE_VENCE_A_LOS_3_MES.toInt() == id)
        	return "A los 3 Meses";
        
        
        else if(SEMANAL_VENCE_A_LA_SEMANA.toInt() == id)
        	return "A la Semana";
        else if(SEMANAL_VENCE_AL_MES.toInt() == id)
        	return "Al Mes";
        else if(SEMANAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return "A los 3 Meses";
        
        
        else if(MENSUAL_VENCE_AL_MES.toInt() == id)
        	return "Al Mes";
        else if(MENSUAL_VENCE_A_LOS_3_MES.toInt() == id)
        	return "A los 3 Meses";
 
        else if(QUINCENAL_VENCE_A_LA_QUINCENA.toInt() == id)
        	return "A la Quincena";
        
        else if(MENSUAL_VENCE_AL_ANo.toInt() == id)
        	return "Al año";
        else if(SEMANAL_VENCE_AL_ANo.toInt() == id)
        	return "Al año";
        
        return null;
    }
    public int toInt() { return id; }
}    
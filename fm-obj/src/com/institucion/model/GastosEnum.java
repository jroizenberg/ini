package com.institucion.model;


public enum GastosEnum {

	SERVICIO_LUZ(0), SERVICIO_AGUA(1), SERVICIO_GAS_A_GRANEL(2), 
//	SERVICIO_GAS_ENVASADO(3), 
	SUELDOS(4), MANTENIMIENTO(5), MATERIAL_DE_CONSUMO(6)
	, MARKETING(7), 
//	TERCEROS(8), 
	IMPUESTOS(9), ALQUILER(10), 
//	REFORMAS(11), 
	OTROS(12);

    private int id;

    private GastosEnum(int id) {
        this.id = id;
    }
    
	public int toInt() { return id; }
	 
	public static GastosEnum fromInt(int value) {    
	   if(SERVICIO_LUZ.toInt() == value)
        	return SERVICIO_LUZ;
        else if(SERVICIO_AGUA.toInt() == value)
        	return SERVICIO_AGUA;
        else if(SERVICIO_GAS_A_GRANEL.toInt() == value)
        	return SERVICIO_GAS_A_GRANEL;
//        else if(SERVICIO_GAS_ENVASADO.toInt() == value)
//        	return SERVICIO_GAS_ENVASADO;
        else if(SUELDOS.toInt() == value)
        	return SUELDOS;
        else if(MANTENIMIENTO.toInt() == value)
        	return MANTENIMIENTO;
        else if(MATERIAL_DE_CONSUMO.toInt() == value)
        	return MATERIAL_DE_CONSUMO;
        else if(MARKETING.toInt() == value)
        	return MARKETING;
//        else if(TERCEROS.toInt() == value)
//        	return TERCEROS;
        else if(IMPUESTOS.toInt() == value)
        	return IMPUESTOS;
        else if(ALQUILER.toInt() == value)
        	return ALQUILER;
//        else if(REFORMAS.toInt() == value)
//        	return REFORMAS;
        else if(OTROS.toInt() == value)
        	return OTROS;
        return null;
   }
	
	public static String toString(int value) {
		
		  if(SERVICIO_LUZ.toInt() == value)
	        	return "Servicio de Luz";
	        else if(SERVICIO_AGUA.toInt() == value)
	        	return "Servicio de Agua";
	        else if(SERVICIO_GAS_A_GRANEL.toInt() == value)
	        	return "Servicio de Gas a Granel";
//	        else if(SERVICIO_GAS_ENVASADO.toInt() == value)
//	        	return "Servicio de Gas envasado";
	        else if(SUELDOS.toInt() == value)
	        	return "Sueldos";
	        else if(MANTENIMIENTO.toInt() == value)
	        	return "Mantenimiento";
	        else if(MATERIAL_DE_CONSUMO.toInt() == value)
	        	return "Material de Consumo";
	        else if(MARKETING.toInt() == value)
	        	return "Marketing";
//	        else if(TERCEROS.toInt() == value)
//	        	return "Terceros";
	        else if(IMPUESTOS.toInt() == value)
	        	return "Impuestos";
	        else if(ALQUILER.toInt() == value)
	        	return "Alquiler";
//	        else if(REFORMAS.toInt() == value)
//	        	return "Reformas";
	        else if(OTROS.toInt() == value)
	        	return "Otros";
	        return null;
		
		
		
	}
	
	
}    
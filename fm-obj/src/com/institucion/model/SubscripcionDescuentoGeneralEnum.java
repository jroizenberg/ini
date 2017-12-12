package com.institucion.model;

public enum SubscripcionDescuentoGeneralEnum {
	
	SIN_DESC(7), GENERAL_5(0),GENERAL_10(1),GENERAL_15(2),GENERAL_20(3), GENERAL_25(4), GENERAL_30(5), GENERAL_35(6), 
	GENERAL_50(12), GENERAL_70(8), GENERAL_80(9), GENERAL_90(10), GENERAL_100(11);

    private int id;

    private SubscripcionDescuentoGeneralEnum(int id) {
        this.id = id;
    }
    public int toInt() { return id; }
    
    public static SubscripcionDescuentoGeneralEnum fromInt(int id) {
        if(GENERAL_5.toInt() == id)
        	return GENERAL_5;
        else if(GENERAL_10.toInt() == id)
        	return GENERAL_10;
        else if(GENERAL_15.toInt() == id)
        	return GENERAL_15;
        else if(GENERAL_20.toInt() == id)
        	return GENERAL_20;
        else if(GENERAL_25.toInt() == id)
        	return GENERAL_25;
        else if(GENERAL_30.toInt() == id)
        	return GENERAL_30;
        else if(GENERAL_35.toInt() == id)
        	return GENERAL_35;
        else if(GENERAL_50.toInt() == id)
        	return GENERAL_50;
        else if(GENERAL_70.toInt() == id)
        	return GENERAL_70;
        else if(GENERAL_80.toInt() == id)
        	return GENERAL_80;
        else if(GENERAL_90.toInt() == id)
        	return GENERAL_90;
        else if(GENERAL_100.toInt() == id)
        	return GENERAL_100;
       
        return null;
    }
    
    public static String toString(int id) {
        if(GENERAL_5.toInt() == id)
        	return "%5";
        else if(GENERAL_10.toInt() == id)
        	return "%10";
        else if(GENERAL_15.toInt() == id)
        	return "%15";
        else if(GENERAL_20.toInt() == id)
        	return "%20";
        else if(GENERAL_25.toInt() == id)
        	return "%25";
        else if(GENERAL_30.toInt() == id)
        	return "%30";
        else if(GENERAL_35.toInt() == id)
        	return "%35";
        else if(GENERAL_50.toInt() == id)
        	return "%50";
        else if(GENERAL_70.toInt() == id)
        	return "%70";
        else if(GENERAL_80.toInt() == id)
        	return "%80";
        else if(GENERAL_90.toInt() == id)
        	return "%90";
        else if(GENERAL_100.toInt() == id)
        	return "%100";
        else if(SIN_DESC.toInt() == id)
        	return " ";
        return null;
    }
    
    public static int getPorcentaje(int id){
    	 if(GENERAL_5.toInt() == id)
         	return 5;
         else if(GENERAL_10.toInt() == id)
         	return 10;
         else if(GENERAL_15.toInt() == id)
         	return 15;
         else if(GENERAL_20.toInt() == id)
         	return 20;
         else if(GENERAL_25.toInt() == id)
         	return 25;
         else if(GENERAL_30.toInt() == id)
         	return 30;
         else if(GENERAL_35.toInt() == id)
         	return 35;
         else if(GENERAL_50.toInt() == id)
          	return 50;
         else if(GENERAL_70.toInt() == id)
          	return 70;
         else if(GENERAL_80.toInt() == id)
          	return 80;
         else if(GENERAL_90.toInt() == id)
          	return 90;
         else if(GENERAL_100.toInt() == id)
          	return 100;
         return 0;
    }
    
}    
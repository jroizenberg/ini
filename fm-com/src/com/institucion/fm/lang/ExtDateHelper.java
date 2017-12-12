package com.institucion.fm.lang;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ExtDateHelper implements Serializable{

	private static Log log = LogFactory.getLog(ExtDateHelper.class);
	
	private static final long serialVersionUID = 1L;

   
    private static Map<String, ExtDateTimeZone> timeZoneMap= new HashMap();
    
	private ExtDateHelper(){
		 Calendar calendar = Calendar.getInstance();
         int years= calendar.get(Calendar.YEAR);
	}

	private ExtDateHelper(int years){
	}
	   	/**
	 * @return ExtDate
	 */
	public static ExtDate newDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		return new ExtDate(cal.getTime());
	}
	
	public static ExtDate setMillisecondsZero(ExtDate extDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(extDate);
		cal.set(Calendar.MILLISECOND, 0);
		return new ExtDate(cal.getTime());
	}
	
}

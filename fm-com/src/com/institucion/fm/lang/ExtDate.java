package com.institucion.fm.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Esta clase extiende las funcionalidades de la clase Date de Java.
 */
public class ExtDate extends Date
{
	private static Log log = LogFactory.getLog(ExtDate.class);
	
	private static final long serialVersionUID = 1L;

	private static String sqlpattern = "yyyyMMddHHmmss";
	private static String datePatter="EEE MMM d HH:mm:ss yyyy";
//	private static String sqlpattern = "yyyy-MM-dd HH:mm:ss";
	private static DateFormat sqlDateFormat = new SimpleDateFormat(sqlpattern);
    private static String userName;

	public ExtDate() {
		Calendar cal = null;
		try {
	
			DateFormat format = new SimpleDateFormat(datePatter);  
			DateFormat format2 = new SimpleDateFormat(datePatter);  
			format.setCalendar(cal);
			String str=format.format(new Date());
			setTime(format2.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public ExtDate(long milliseconds) { super(milliseconds); }

	public ExtDate(Date date) { 
		this(date.getTime());
	}

	/**
	 * Crear una nueva fecha a partir de una cadena de caracteres en formato YYYYMMDDHHMMSS.
	 */
	public ExtDate(String sqlDate)
	{
		setSQLString(sqlDate);			
	}

	public ExtDate(int year, int month, int day)
	{
		Calendar calendar = new GregorianCalendar(year, month-1, day);
		super.setTime(calendar.getTimeInMillis());
		calendar = null;
	}

	public ExtDate(int year, int month, int day, int hour, int minute)
	{
		Calendar calendar = new GregorianCalendar(year, month-1, day, hour, minute);
		super.setTime(calendar.getTimeInMillis());
		calendar = null;
	}

	/**
	 * Sumar (<code>year</code> positivo) o restar(<code>year</code> negativo)
	 * una cantidad de anios.
	 * @return la nueva fecha con la diferencia en anios
	 */
	public ExtDate addYear(int year) { return addCalendar(Calendar.YEAR, year);	}

	/**
	 * Sumar (<code>month</code> positivo) o restar(<code>month</code> negativo)
	 * una cantidad de meses.
	 * @return la nueva fecha con la diferencia en meses
	 */
	public ExtDate addMonth(int month) { return addCalendar(Calendar.MONTH, month); }

	/**
	 * Sumar (<code>day</code> positivo) o restar(<code>day</code> negativo)
	 * una cantidad de dias.
	 * @return la nueva fecha con la diferencia en dias
	 */
	public ExtDate addDay(int day) { return addCalendar(Calendar.DAY_OF_YEAR, day); }

	/**
	 * Sumar (<code>hour</code> positivo) o restar(<code>hour</code> negativo)
	 * una cantidad de horas.
	 * @return la nueva fecha con la diferencia en horas
	 */
	public ExtDate addHour(int hour) { return addCalendar(Calendar.HOUR_OF_DAY, hour); }

	/**
	 * Sumar (<code>minute</code> positivo) o restar(<code>minute</code> negativo)
	 * una cantidad de minutos.
	 * @return la nueva fecha con la diferencia en minutos
	 */
	public ExtDate addMinute(int minute) { return addCalendar(Calendar.MINUTE, minute); }

	/** Metodo de ayuda para los addXXX. */
	private ExtDate addCalendar(int field, int value) {
		Calendar addDate = Calendar.getInstance();
		addDate.setTime(this);
		addDate.add(field, value);
		return new ExtDate(addDate.getTimeInMillis());
	}

	public int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		return calendar.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		return calendar.get(Calendar.MONTH)+1;
	}

	public int getDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Crea un fecha pero asignando la hora, minutos y segundos al inicio del dia.
	 * @return una fecha DDMMYYYY:00:00:00
	 */
	public ExtDate am() {
		Calendar amDate = Calendar.getInstance();
		amDate.setTime(this);
		amDate.set(amDate.get(Calendar.YEAR), amDate.get(Calendar.MONTH), amDate.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return new ExtDate(amDate.getTimeInMillis());
	}

	/**
	 * Crea un fecha pero asignando la hora, minutos y segundos al final del dia.
	 * @return una fecha DDMMYYYY:24:59:59
	 */
	public ExtDate pm() {
		Calendar pmDate = Calendar.getInstance();
		pmDate.setTime(this);
		pmDate.set(pmDate.get(Calendar.YEAR), pmDate.get(Calendar.MONTH), pmDate.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		return new ExtDate(pmDate.getTimeInMillis());
	}


	
	/**
	 * Diferencia en dias entre las dos fechas.
	 * @return >0 si la fecha diffDate es menor;
	 *         <0 si la fecha diffDate es mayor 
	 */ 
	public int diffDays(Date diffDate) {
		long diffTime = this.milliseconds() - diffDate.getTime();
		//         milseg     seg    min  hora dia
		diffTime = diffTime / 1000 / 60 / 60 / 24;
		return (int) diffTime;
	}

	/**
	 * Diferencia en horas entre las dos fechas.
	 * @return >0 si la fecha diffDate es menor;
	 *         <0 si la fecha diffDate es mayor 
	 */ 
	public int diffHours(Date diffDate) {
		long diffTime = this.milliseconds() - diffDate.getTime();
		//         milseg     seg    min  hora dia
		diffTime = diffTime / 1000 / 60 / 60;
		return (int) diffTime;
	}

	/**
	 * Diferencia en horas entre las dos fechas.
	 * @return >0 si la fecha diffDate es menor;
	 *         <0 si la fecha diffDate es mayor 
	 */ 
	public int diffMinutes(Date diffDate) {
		long diffTime = this.milliseconds() - diffDate.getTime();
		//         milseg     seg    min  hora dia
		diffTime = diffTime / 1000 / 60;
		return (int) diffTime;
	}

	/**
	 * return <code>true</code> si la fecha es menor a <code>compareDate</code>;
	 *        <code>false</code> si la fecha es mayor
	 */
	public boolean lessThan(Date compareDate) {
		return this.milliseconds() < compareDate.getTime();
	}

	/**
	 * return <code>true</code> si la fecha es menor o igual a <code>compareDate</code>;
	 *        <code>false</code> si la fecha es mayor
	 */
	public boolean lessOrEqualThan(Date compareDate) {
		return this.milliseconds() <= compareDate.getTime();
	}

	/**
	 * return <code>true</code> si la fecha es mayor a <code>compareDate</code>;
	 *        <code>false</code> si la fecha es menor
	 */
	public boolean greaterThan(Date compareDate) {
		return this.milliseconds() > compareDate.getTime();
	}

	/**
	 * return <code>true</code> si la fecha es mayor o igual a <code>compareDate</code>;
	 *        <code>false</code> si la fecha es menor
	 */
	public boolean greaterOrEqualThan(Date compareDate) {
		return this.milliseconds() >= compareDate.getTime();
	}

	/** La cantidad de milisegundos que tiene la fecha. */
	public long milliseconds() { return getTime(); }

//	/** Imprimir la fecha con el formato DD/MM/YYYY HH:MM:SS. */
//	public String toString() { return dateFormat.format(this); }

	/** Imprimir la fecha con el formato YYYY/MM/DD HH:MM:SS. */
	public String toSQLString() { return sqlDateFormat.format(this); }

	public void setSQLString(String sqldate) {
		try	{
			super.setTime(sqlDateFormat.parse(sqldate).getTime());
		} catch (ParseException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new com.institucion.fm.conf.exception.ParseException("Error de conversion de fecha '"+sqldate+"'", e);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * @param d1
	 * @param d2
	 * @return boolean, compara dos fechas, solo DD/MM/YY HH:MM no incluye Segundos
	 */
	public boolean compareDate(Date d1, Date d2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(d2);

		if (cal1.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)&& 
				cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)&&
					cal1.get(Calendar.DAY_OF_MONTH)==cal2.get(Calendar.DAY_OF_MONTH)&& 
						cal1.get(Calendar.HOUR)==cal2.get(Calendar.HOUR)&& 
							cal1.get(Calendar.MINUTE)==cal2.get(Calendar.MINUTE)){
			return true;
		}
		return false;
	}
	
	public static String getUserName() {
       if(userName == null)
           return "admin";
       else
           return userName;
    }

    public static void setUserName(String userName) {
        ExtDate.userName = userName;
    }

    /**
	 * @param d1
	 * @return bolean, compara una fecha con la instancia de extdate,  solo DD/MM/YY HH:MM no incluye Segundos
	 */
	public boolean compareDate(Date d1) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(d1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(this);
		if (cal1.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)&& 
				cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)&&
					cal1.get(Calendar.DAY_OF_MONTH)==cal2.get(Calendar.DAY_OF_MONTH)&& 
						cal1.get(Calendar.HOUR)==cal2.get(Calendar.HOUR)&& 
							cal1.get(Calendar.MINUTE)==cal2.get(Calendar.MINUTE)){
			return true;
		}
		return false;
	}
	
	
}
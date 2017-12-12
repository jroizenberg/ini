package com.institucion.fm.confsys.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.fm.lang.ExtDate;

public class ConfigRegional implements Serializable {

	private static Log log = LogFactory.getLog(ConfigRegional.class);
	
	private static final long serialVersionUID = 1767706208232646478L;

	/** Cada pais tiene un solo configRegional. */
	private static ConfigRegional configRegional;

	private Long id;
	private int lang;
	//private String phonePattern;
	private String hourPattern;
	private String datePattern;
	private String decimalPattern;
	private String numberPattern;
	private String currencySymbol;

	public static final int CASTELLANO_LANG = 0;
	public static final int PORTUGUES_LANG  = 1;
	public static final int ENGLISH_LANG    = 2;

	public static Locale spanishLocale = new Locale("es");
	public static Locale englishLocale = new Locale("en");
	public static Locale portugueseLocale = new Locale("pt");
	
	public static ConfigRegional instance()
	{
		return configRegional;
	}

	/**
	 * Este metodo se debe llamar cada vez que se modifica la configuracion regional. 
	 */
	public static void setConfigRegional(ConfigRegional configRegional)
	{
		ConfigRegional.configRegional = configRegional;
	}

	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	
	/*public String getPhonePattern() {
		return phonePattern;
	}

	public void setPhonePattern(String phonePattern) {
		this.phonePattern = phonePattern;
	}*/

	public String getHourPattern() {
		return hourPattern;
	}

	public void setHourPattern(String hourPattern) {
		this.hourPattern = hourPattern;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public String getDateTimePattern() {
		return datePattern+" "+hourPattern;
	}

	public String getNumberPattern() { return numberPattern; }
	public void setNumberPattern(String numberPattern) {
		if (numberPattern == null) {
			this.numberPattern = null;
		} else {
			this.numberPattern = numberPattern.trim();
		}
	}

	public String getDecimalPattern() {
		return decimalPattern;
	}

	public void setDecimalPattern(String decimalPattern) {
		if (decimalPattern == null) {
			this.decimalPattern = null;
		} else {
			this.decimalPattern = decimalPattern.trim();
		}
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		if (currencySymbol == null) {
			this.currencySymbol = null;
		} else {
			this.currencySymbol = currencySymbol.trim();
		}
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public int getLang() {
		return lang;
	}

	public Date getDate(String strdate)
	{
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		Date date = null;
		try { date = dateFormat.parse(strdate); }
		catch (ParseException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		ExtDate extdate = new ExtDate(date.getTime());
		return extdate;
	}

	public Date getDate(String strdate, String strhour)
	{
		DateFormat dateFormat = new SimpleDateFormat(datePattern+" "+hourPattern);
		Date date = null;
		try { date = dateFormat.parse(strdate+" "+strhour); }
		catch (ParseException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
		ExtDate extdate = new ExtDate(date.getTime());
		return extdate;
	}

	public int hashCode() {
		if (this.id == null) {
			return System.identityHashCode(this);
		}
		return this.id.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ConfigRegional)) {
			return false;
		}
		return obj.hashCode() == this.hashCode();
	}

	public Locale getLocale()
	{
		switch (getLang())
		{
			case ConfigRegional.ENGLISH_LANG:
				return englishLocale;
			case ConfigRegional.PORTUGUES_LANG:
				return portugueseLocale;
			default:
				return spanishLocale;
		}
	}
}
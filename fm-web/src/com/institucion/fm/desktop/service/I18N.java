package com.institucion.fm.desktop.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.zkoss.zk.ui.Sessions;

import com.institucion.fm.conf.InstanceConf;
import com.institucion.fm.conf.Session;
import com.institucion.fm.confsys.model.ConfigRegional;

/**
 * Realiza la internacionalizacion de los textos de la aplicacion. Los recursos
 * los toma de WEB-INF/i3-label_<language>.properties
 */
public class I18N {
	public static String getLabel(String key) {
		return org.zkoss.util.resource.Labels.getLabel(key);
	}

	/**
	 * Devuelve una etiqueta que tiene una variable en su texto, por ejemplo,
	 * selector.title=Listado de {0}. La expresion {0} es reemplazada por 'var'.
	 */
	public static String getLabel(String key, String var) {
		Object[] params = { var };
		return org.zkoss.util.resource.Labels.getLabel(key, params);
	}

	/**
	 * Devuelve una etiqueta que tiene una variable en su texto, por ejemplo,
	 * selector.title=Listado de {0}. La expresion {0} es reemplazada por 'var'.
	 */
	public static String getLabel(String key, Object[] params) {
		return org.zkoss.util.resource.Labels.getLabel(key, params);
	}

	/**
	 * Si el texto tiene al principio el caracter '$' entonces lo considera como
	 * una clave y se fija en el archivo de idiomas.
	 */
	public static String getStringLabel(String text) {
		if (!text.isEmpty() && text.charAt(0) == '$')
			text = getLabel(text.substring(1));
		return text;
	}

	public static Locale getLocale() {
		return getLocale(Sessions.getCurrent());
	}

	/**
	 * @return ConfigRegional.CASTELLANO_LANG|ConfigRegional.PORTUGUES_LANG|
	 *         ConfigRegional.ENGLISH_LANG
	 */
	public static int getLanguage() {
		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		return configRegional.getLang();
	}

	public static Locale getLocale(org.zkoss.zk.ui.Session session) {
		if (session == null || session.getAttribute("ConfigRegional") == null) {
			return new Locale(InstanceConf.getInstanceConfigurationProperty(InstanceConf.DEFAULT_LOCALE));
		}
		Locale locale = (Locale) session.getAttribute("language");
		if (locale != null)
			return locale;

		ConfigRegional configRegional = (ConfigRegional) session
				.getAttribute("ConfigRegional");
		switch (configRegional.getLang()) {
		case ConfigRegional.ENGLISH_LANG:
			locale = new Locale("en");
			break;
		case ConfigRegional.PORTUGUES_LANG:
			locale = new Locale("pt");
			break;
		default:
			locale = new Locale("es");
			break;
		}
		session.setAttribute("language", locale);

		return locale;
	}

	public static String formatDateTime(Date date) {
		if (date == null)
			return "";

		DateFormat dateTimeFormat = (DateFormat) Sessions.getCurrent()
				.getAttribute("dateTimeFormat");

		if (dateTimeFormat != null)
			return dateTimeFormat.format(date);

		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		dateTimeFormat = new SimpleDateFormat(configRegional
				.getDateTimePattern());
		Sessions.getCurrent().setAttribute("dateTimeFormat", dateTimeFormat);
		return dateTimeFormat.format(date);
	}

	public static String formatDate(Date date) {
		if (date == null)
			return "";

		DateFormat dateFormat = (DateFormat) Sessions.getCurrent()
				.getAttribute("dateFormat");

		if (dateFormat != null)
			return dateFormat.format(date);

		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		dateFormat = new SimpleDateFormat(configRegional.getDatePattern());
		Sessions.getCurrent().setAttribute("dateFormat", dateFormat);
		return dateFormat.format(date);
	}

	public static String formatDateTimeMilliseconds(Date date) {
		if (date == null)
			return "";

		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SS").format(date)
				.toString();
	}

	public static Date formatDate(String strdate) {
		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent().getAttribute("ConfigRegional");
		return configRegional.getDate(strdate);
	}

	public static Date formatDate(String date, String time) {
		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		return configRegional.getDate(date, time);
	}

	public static String formatTime(Date date) {
		if (date == null)
			return "NULL";

		DateFormat timeFormat = (DateFormat) Session.getAttribute("timeFormat");

		if (timeFormat != null)
			return timeFormat.format(date);

		ConfigRegional configRegional = (ConfigRegional) Session
				.getAttribute("ConfigRegional");
		timeFormat = new SimpleDateFormat(configRegional.getHourPattern());
		Sessions.getCurrent().setAttribute("timeFormat", timeFormat);
		return timeFormat.format(date);
	}

	public static ConfigRegional getConfigRegional() {
		return (ConfigRegional) Sessions.getCurrent().getAttribute(
				"ConfigRegional");
	}

	public static String getDecimalPattern() {
		String decimalPattern = (String) Sessions.getCurrent().getAttribute(
				"decimalPattern");

		if (decimalPattern != null)
			return decimalPattern;

		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		decimalPattern = configRegional.getDecimalPattern();
		return decimalPattern;
	}

	public static String getNumberPattern() {
		String numberPattern = (String) Sessions.getCurrent().getAttribute(
				"numberPattern");

		if (numberPattern != null)
			return numberPattern;

		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		numberPattern = configRegional.getNumberPattern();
		return numberPattern;
	}

	public static String getDateFormat() {
		String datePattern = (String) Sessions.getCurrent().getAttribute(
				"datePattern");

		if (datePattern != null)
			return datePattern;

		ConfigRegional configRegional = (ConfigRegional) Sessions.getCurrent()
				.getAttribute("ConfigRegional");
		datePattern = configRegional.getDatePattern();
		return datePattern;
	}

	/**
	 * Formatea un valor double a un String con formato. Por ejemplo: 1234.56 ->
	 * 1.234,56
	 */
	public static String getString(Double doubleValue) {
		if (doubleValue == null)
			return "";

		DecimalFormat format = new DecimalFormat(getDecimalPattern());
		format.setParseBigDecimal(true);
		return format.format(doubleValue);
	}
}
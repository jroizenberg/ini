package com.institucion.fm.resource;

import java.util.Locale;

public class I18NServer {
	public static MessageResources mr = new MessageResources();

	static {
		mr.setResourceFile("com/institucion/fm/resource/i3-label-server");
	}

	public static String getLabel(Locale locale, String key) {
		return mr.getMessage(locale, key);
	}

	/**
	 * Devuelve una etiqueta que tiene una variable en su texto, por ejemplo,
	 * selector.title=Listado de {0}. La expreson {0} es reemplazada por 'var'.
	 */
	public static String getLabel(Locale locale, String key, String var) {
		Object[] params = { var };
		return mr.getMessage(locale, key, params);
	}

	/**
	 * Devuelve una etiqueta que tiene una variable en su texto, por ejemplo,
	 * selector.title=Listado de {0}. La expresion {0} es reemplazada por 'var'.
	 */
	public static String getLabel(Locale locale, String key, Object[] params) {
		return mr.getMessage(locale, key, params);
	}
}
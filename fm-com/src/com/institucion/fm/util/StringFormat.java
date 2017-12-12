package com.institucion.fm.util;

/**
 * Dar formato a las cadenas de caracteres.
 *
 * @author  Anibal Potenza
 * @version 1.0, 04/06/2007
 * @since   1.0, 17/01/2006
 */
public class StringFormat{
	/**
	 * Retorna la propiedad con la primera letra en mayusculas para formar los
	 * metodos <code>set</code>, <code>get</code> e <code>is</code>.
	 * 
	 * @param property  una propiedad java al estilo 'razonSocial'
	 * @return la propiedad con la primera letra en mayusculas
	 */
	public static String firstUpper(String property){
		return property.substring(0,1).toUpperCase() + property.substring(1, property.length());
	}

	/**
	 * Retorna el nombre de la clase con la primera letra en minuscula para
	 * usarla como nombre de variable.
	 * 
	 * @param nameClass  el nombre de una clase
	 * @return el nombre de la clase con la primera letra en minuscula
	 */
	public static String firstLower(String nameClass){
		return nameClass.substring(0,1).toLowerCase() + nameClass.substring(1, nameClass.length());
	}

	/**
	 * Convertir un vector en una cadena de caracteres.
	 */
	public static String vectorToString(Object[] vector){
		StringBuffer sb= new StringBuffer();
		for (int i = 0; i < vector.length; i++)
			sb.append("[").append(vector[i].toString()).append("]");
		return sb.toString();
	}

	/**
	 * Completa una cadena de caracteres <code>s</code> con el caracter <code>c</code>
	 * hasta completar una longitud <code>length</code>. Si la cadena de caracteres es
	 * mayor que la longitud dada, se devuelve el misma cadena de caracteres.
	 * Ejemplo: padLeft(123, '0', 6) -> 000123.
	 *
	 * @param s  cadena de caracteres a completar
	 * @param c  caracter para llenar los espacios sobrantes
	 * @param length  longitud deseada de la cadena de caracteres
	 * @return  la nueva cadena de caracteres completada con el caracter <code>c</code>
	 */
	public static String padLeft(String s, char c, int length){
		int strLength = s.length();
		if (strLength >= length)
			return s;
		for (int i = strLength; i < length; i++)
			s = c + s;
		return s;
	}

	/**
	 * Completa una cadena de caracteres <code>s</code> con el caracter <code>c</code>
	 * hasta completar una longitud <code>length</code>. Si la cadena de caracteres es
	 * mayor que la longitud dada, se devuelve el misma cadena de caracteres.
	 * Ejemplo: padLeft(123, '0', 6) -> 123000.
	 *
	 * @param s  cadena de caracteres a completar
	 * @param c  caracter para llenar los espacios sobrantes
	 * @param length  longitud deseada de la cadena de caracteres
	 * @return  la nueva cadena de caracteres completada con el caracter <code>c</code>
	 */
	public static String padRight(String s, char c, int length)	{
		int strLength = s.length();
		if (strLength >= length)
			return s;
		
		StringBuffer sb= new StringBuffer();
		for (int i = strLength; i < length; i++)
			sb.append(c);
		s=s + sb.toString();
		return s;
	}
	
	public static String replaceCharacter(String character, String by, String string) {
		String result = string.replaceAll("[" + character + "]", by);
		return result;
	}

}

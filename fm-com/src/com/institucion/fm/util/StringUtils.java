package com.institucion.fm.util;

public class StringUtils {

	public static boolean contains(String string, String substring) {
		if (string == null || substring == null || string.equalsIgnoreCase("") || substring.equalsIgnoreCase(""))
			return false;
		string = string.toLowerCase();
		substring = substring.toLowerCase();
		return string.contains(substring);
	}

}

package com.institucion.fm.util;

public class Cipher {

	public static String encrypt(String value) {
		if (value == null) {
			return null;
		}
		return Base64Coder.encodeString(value);
	}
	
	public static String decrypt(String value) {
		if (value == null) {
			return null;
		}
		return Base64Coder.decodeString(value);
	}
}

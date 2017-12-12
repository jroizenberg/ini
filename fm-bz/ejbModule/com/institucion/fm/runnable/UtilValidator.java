package com.institucion.fm.runnable;


public class UtilValidator {

	public final static String SIMPLE_NUMERIC_VALIDATOR = "^\\d*[0-9](|.\\d*[0-9]|)*$";
	final static String DOUBLE_NUMERIC_VALIDATOR = "^\\s*[+-]?\\s*(?:\\d{1,3}(?:(,?)\\d{3})?(?:\\1\\d{3})*(\\.\\d*)?|\\.\\d+)\\s*$";
//	final static String DATE_VALIDATOR = "^(?0[1-9]|[1-9]|1[012])(?[- /.])(?0[1-9]|[1-9]|[12][0-9]|3[01])(?[- /.])(?(19|20)\\d\\d)$";
//	SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
	public final static int DATE_SIZE = 6;
	final static char DELIMETER = ';';
	
	public static boolean validateStringField (String field, int size, boolean nullable) {
		return field != null && field.length()>0? field.length()<=size : nullable;
	}
	
	public static boolean validateField (String field, String validator, boolean nullable) {
		return field != null ? field.matches(validator) : nullable;
	}
	
	
}

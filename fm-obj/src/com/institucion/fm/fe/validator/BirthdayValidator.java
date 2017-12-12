package com.institucion.fm.fe.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.institucion.fm.fe.validator.ValidationException;

public class BirthdayValidator extends Validation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(Object value) throws ValidationException {

 
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		if (value !=null){
			try {
	 
				//if not valid, it will throw ParseException
				Date date = sdf.parse((String)value);
	//			System.out.println(date);
	 
			} catch (ParseException e) {
	 
				throw new ValidationException("$validation.birthday");
			}
		}
		
		
	}

}

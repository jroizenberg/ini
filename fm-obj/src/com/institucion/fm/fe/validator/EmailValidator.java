package com.institucion.fm.fe.validator;

import com.institucion.fm.fe.validator.ValidationException;
import com.institucion.fm.util.RegularExpressions;

public class EmailValidator extends Validation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void validate(Object value) throws ValidationException {
		boolean error = false;

		if (value != null)
		{
			if (value instanceof String && !((String) value).matches(RegularExpressions.EMAIL_VALID_EXPRESSION))
				error = true;
		}
		else
		{
			error = false;
		}

		if (error) {
			throw new ValidationException("$error.invalid.email.format");
		}
		
		
		
	}

}

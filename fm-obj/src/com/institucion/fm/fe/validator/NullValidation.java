package com.institucion.fm.fe.validator;

import com.institucion.fm.fe.validator.ValidationException;

public class NullValidation extends Validation
{
	private static final long serialVersionUID = 1L;

	public NullValidation() { }

	public void validate(Object value) throws ValidationException
	{
		boolean error = false;

		if (value != null)
		{
			if (value instanceof String && ((String) value).length() == 0)
				error = true;
		}
		else
		{
			error = true;
		}

		if (error) {
			throw new ValidationException("$validation.null");
		}
	}
}
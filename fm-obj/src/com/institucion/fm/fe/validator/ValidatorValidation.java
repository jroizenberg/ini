package com.institucion.fm.fe.validator;

import java.io.Serializable;

/**
 * Esta clase representa una relación NaN entre la tabla Validator y Validation.
 * Los objetos Validation no se guardan en tablas porque son fijos, entonces, se
 * guardan en archivos XML. Esta clase no se tiene que usar funcionalmente, solamente
 * se utiliza para cumplir con Hibernate.
 */
public class ValidatorValidation implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Validator validator;
	private String validationName;

	public ValidatorValidation() {}

	public Validator getValidator() { return validator; }
	public void setValidator(Validator validator) { this.validator = validator; }

	public String getValidationName() { return validationName; }
	public void setValidationName(String validationName) { this.validationName = validationName; }
}
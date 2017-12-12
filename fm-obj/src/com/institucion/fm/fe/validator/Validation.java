package com.institucion.fm.fe.validator;

import java.io.Serializable;

import com.institucion.fm.fe.validator.ValidationException;

public abstract class Validation implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String name;

	public Validation() {}

	public abstract void validate(Object value) throws ValidationException;

	public String getName() { return name; }
	public void setName(String name)
	{
		this.name = name;
	}

	public boolean equals(Object value)
	{
		if (value == null)
			return false;

		if (value instanceof Class)
			return this.getClass().equals(value);

		return super.equals(value);
	}
}
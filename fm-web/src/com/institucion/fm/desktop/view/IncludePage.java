package com.institucion.fm.desktop.view;

import org.zkoss.zul.Include;

public class IncludePage extends Include
{
	private static final long serialVersionUID = 1L;

	private String message;

	public IncludePage() { }

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
}

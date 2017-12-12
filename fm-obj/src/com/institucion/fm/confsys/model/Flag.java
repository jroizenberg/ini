package com.institucion.fm.confsys.model;

import java.io.Serializable;

public class Flag implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private boolean reloadPermission;

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public void setReloadPermission(boolean reloadPermission)
	{
		this.reloadPermission = reloadPermission;
	}

	public boolean isReloadPermission()
	{
		return reloadPermission;
	}
}
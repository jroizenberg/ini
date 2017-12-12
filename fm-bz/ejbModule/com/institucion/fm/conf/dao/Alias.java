package com.institucion.fm.conf.dao;

import java.io.Serializable;

public class Alias implements Serializable {
	private static final long serialVersionUID = 1L;

	private String alias;
	private Class<? extends Serializable> clazz;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Class<? extends Serializable> getClazz() {
		return clazz;
	}
	public void setClazz(Class<? extends Serializable> clazz) {
		this.clazz = clazz;
	}
	
	public Alias(String alias, Class<? extends Serializable> clazz) {
		this.alias = alias;
		this.clazz = clazz;
	}
}

package com.institucion.fm.conf.dao;

import com.institucion.fm.conf.model.HibernateTemplate;

public abstract class TemplateHolder {
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	
}

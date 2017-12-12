package com.institucion.fm.conf.spring;

import org.zkoss.zkplus.spring.DelegatingVariableResolver;

public class BeanFactory {
	@SuppressWarnings("unchecked")
	public static synchronized <T> T getObject(String beanName)	{
		DelegatingVariableResolver dvr = new DelegatingVariableResolver();
		T obj = (T)dvr.resolveVariable(beanName);
		return obj;
	}
}
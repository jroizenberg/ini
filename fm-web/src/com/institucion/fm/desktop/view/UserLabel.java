package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;

import com.institucion.fm.conf.Session;

public class UserLabel extends Label implements AfterCompose{
	private static final long serialVersionUID = 1L;

	public void afterCompose(){
		String userName = Session.getUsername();
		this.setValue("usuario: "+ userName);
	}
	
}

package com.institucion.fm.desktop.view;

//import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;

import com.institucion.fm.desktop.service.I18N;

public class ErrorMessageLabel extends Label implements AfterCompose
{
	private static final long serialVersionUID = 1L;

	public ErrorMessageLabel()
	{
		this.setValue("");
		this.setStyle("color:#FF0000");
	}
	
	public void afterCompose()
	{
		Object error = com.institucion.fm.conf.Session.getAttributeFromCall("error");
		Component errorbox = this.getSpaceOwner().getFellow("error"); 

		if (error == null)
		{
			errorbox.setVisible(false);
		}
		else
		{
			this.setValue(I18N.getLabel("login."+error));
			errorbox.setVisible(true);
		}
	}
}

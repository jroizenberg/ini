package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Toolbarbutton;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.service.I18N;

public class PopupButton extends Toolbarbutton
{
	private static final long serialVersionUID = 1L;

	public PopupButton()
	{
		setId("popupbutton");
		setImage("/img/icon/popupoff_16.png");
		setTooltiptext(I18N.getLabel("menu.button.popup"));
	}

	public void onClick(Event evt)
	{
		Session.getDesktopPanel().setSrc("/bb/bulletinboard-selector.zul");
		Session.getDesktopPanel().setMessage("");
	}
}
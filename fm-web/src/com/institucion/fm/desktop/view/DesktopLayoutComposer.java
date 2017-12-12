package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Toolbarbutton;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.model.MenuItemModel;

public class DesktopLayoutComposer extends GenericForwardComposer
{
	private IncludePage desktoppanel;
	private Toolbarbutton popupbutton;

	public void onCreate()
	{
		Session.setDesktopPanel(desktoppanel);
		Session.setPopupButton(popupbutton);
	}

	public void onSelectMenuItem(Event event) throws Exception
	{
		MenuItemModel menuItem = (MenuItemModel) event.getData();
		desktoppanel.setSrc(menuItem.getAction());
		desktoppanel.setMessage("menu");
	}
}
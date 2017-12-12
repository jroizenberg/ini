package com.institucion.fm.desktop.service;

import org.zkoss.zk.ui.util.GenericAutowireComposer;

import com.institucion.fm.conf.Session;

public abstract class ReportComposer extends GenericAutowireComposer
{
	public void gotoPage(String zulpage)
	{
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage("");
	}

	public void gotoPage(String zulpage, String message)
	{
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage(message);
	}
}
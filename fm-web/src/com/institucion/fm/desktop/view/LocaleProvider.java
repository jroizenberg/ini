package com.institucion.fm.desktop.view;

import java.util.Locale;

import org.zkoss.web.Attributes;

import com.institucion.fm.desktop.service.I18N;

public class LocaleProvider implements org.zkoss.zk.ui.util.RequestInterceptor
{
   public void request(org.zkoss.zk.ui.Session session, Object request, Object response)
	{
//   	Locale locale = org.zkoss.util.Locales.getLocale(I18N.getLocale());
   	Locale locale = org.zkoss.util.Locales.getLocale(I18N.getLocale(session));
   	session.setAttribute(Attributes.PREFERRED_LOCALE, locale);
	}
}
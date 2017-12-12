package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;

import com.institucion.fm.conf.ApplicationInfo;
import com.institucion.fm.conf.spring.BeanFactory;

public class VersionLabel extends Label implements AfterCompose
{
	private static final long serialVersionUID = 1L;

	public void afterCompose()
	{
		ApplicationInfo info = BeanFactory.<ApplicationInfo>getObject("fm.conf.appInfo");
//		this.setValue(I18N.getLabel("title.version")+":"+info.getVersionInfo());
		this.setValue(info.getVersionInfo());
	}
}
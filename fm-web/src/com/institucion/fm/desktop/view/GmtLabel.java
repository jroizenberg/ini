package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Label;

import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.confsys.bz.ConfigSystemEJB;

public class GmtLabel extends Label implements AfterCompose{

	private static final long serialVersionUID = 1L;
    private ConfigSystemEJB configSystemEjb = null;

	public void afterCompose(){
		String gmt_id = getService().getTimeZoneGmtId();
		this.setValue(gmt_id);
	}
	
	private ConfigSystemEJB getService(){
	        if (this.configSystemEjb == null) {
	            this.configSystemEjb = BeanFactory.<ConfigSystemEJB>getObject("fm.ejb.configSystem");
	        }
	        return this.configSystemEjb;
	    }
	    
}

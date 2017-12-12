package com.institucion.fm.security.model;

import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.desktop.view.dad.DadPanel;
import com.institucion.model.Cliente;

public class ClienteDaDPanel extends DadPanel<Cliente> {
	private static final long serialVersionUID = 1L;

	protected String getDadFilterClassName() {
		return ClienteDaDFilter.class.getCanonicalName();
	}

	public ClienteDaDFilter getPanelFilter() {
		return (ClienteDaDFilter)this.getPanelDaDFilter();
	}
	
	protected DadList<Cliente> getDadList() {
		return new ClienteDaDList();
	}

}

package com.institucion.fm.security.model;

import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.desktop.view.dad.DadPanel;
import com.institucion.fm.security.model.Permission;

public class PermissionDaDPanel extends DadPanel<Permission> {
	private static final long serialVersionUID = 1L;

	protected String getDadFilterClassName() {
		return PermissionDaDFilter.class.getCanonicalName();
	}

	protected DadList<Permission> getDadList() {
		return new PermissionDaDList();
	}

}

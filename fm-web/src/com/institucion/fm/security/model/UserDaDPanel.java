package com.institucion.fm.security.model;

import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.fm.desktop.view.dad.DadPanel;
import com.institucion.fm.security.model.User;

public class UserDaDPanel extends DadPanel<User> {
	private static final long serialVersionUID = 1L;

	public UserDaDPanel() {
		super();
	}

	protected String getDadFilterClassName() {
		return UserDaDFilter.class.getCanonicalName();
	}

	protected DadList<User> getDadList() {
		return new UserDaDList();
	}
	
}

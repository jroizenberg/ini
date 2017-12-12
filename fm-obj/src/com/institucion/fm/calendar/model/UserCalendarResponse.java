package com.institucion.fm.calendar.model;

import java.io.Serializable;

public class UserCalendarResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean isAPM;
	private UserModel principalUser;
	private UserModel[] users;

	public void finalize() throws Throwable {
		super.finalize();
	}
	public UserModel getPrincipalUser() {
		return principalUser;
	}

	public void setPrincipalUser(UserModel principalUser) {
		this.principalUser = principalUser;
	}

	public UserModel[] getUsers() {
		return users;
	}

	public void setUsers(UserModel[] users) {
		this.users = users;
	}

	public boolean isAPM() {
		return isAPM;
	}

	public void setAPM(boolean isAPM) {
		this.isAPM = isAPM;
	}

}

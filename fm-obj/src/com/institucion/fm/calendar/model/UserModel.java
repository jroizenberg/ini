package com.institucion.fm.calendar.model;

import java.io.Serializable;

public class UserModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fullName;
	private String userName;
	private Long idUser;

	public void finalize() throws Throwable {
		super.finalize();
	}
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

}

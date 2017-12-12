package com.institucion.fm.cobj;

import java.io.Serializable;

public class ApmBandBoxCObj implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private String loginName;
	
	public ApmBandBoxCObj() {
	
	}

	
	
	
	
	public ApmBandBoxCObj(String firstName, String lastName, String loginName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginName = loginName;
	}





	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
		
	
	
}

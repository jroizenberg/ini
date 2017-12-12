package com.institucion.fm.security.model;

import java.io.Serializable;

public class UserSecurity implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private int denyAccessCount;
	private User user;
	private String oldPassword1;
	private String oldPassword2;
	private String oldPassword3;
	private Boolean firstLogin;
	
	public UserSecurity(){
		firstLogin= false;
	}
	public String getOldPassword1() {
		return oldPassword1;
	}
	private void setOldPassword1(String oldPassword1) {
		this.oldPassword1 = oldPassword1;
		
	}
	public String getOldPassword2() {
		return oldPassword2;
	}
	private void setOldPassword2(String oldPassword2) {
		this.oldPassword2 = oldPassword2;
		
	}
	public String getOldPassword3() {
		return oldPassword3;
	}
	private void setOldPassword3(String oldPassword3) {
		this.oldPassword3 = oldPassword3;
		
	}
	public void addOldPassword(String pass){
		
		this.setOldPassword3(this.getOldPassword2());
		this.setOldPassword2(this.getOldPassword1());
		this.setOldPassword1(pass);
	}
	
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public void setDenyAccessCount(int denyAccessCount) { this.denyAccessCount = denyAccessCount; }
	public int getDenyAccessCount() { return denyAccessCount; }

	public void setUser(User user) { this.user = user; }
	public User getUser() { return user; }
	/*
	 * this field store a boolean value, represent if the user have to change the 
	 * password at the first Login or not
	 * */
	public Boolean isFirstLogin() {return firstLogin;}
	public void setFirstLogin(Boolean firsLogin) {this.firstLogin = firsLogin;}
}
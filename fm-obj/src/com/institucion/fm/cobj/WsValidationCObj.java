package com.institucion.fm.cobj;

import java.io.Serializable;

import com.institucion.fm.security.model.UserState;
import com.institucion.fm.wf.model.RoleType;

public class WsValidationCObj implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Boolean isFirstLogin;
	private UserState state;
	private Long wfUserId;
	private RoleType type;
	
	
	public WsValidationCObj() {
	
	}
	
	
	
	
	public WsValidationCObj(Boolean isFirstLogin, UserState state,
			Long wfUserId, RoleType type) {
		super();
		this.isFirstLogin = isFirstLogin;
		this.state = state;
		this.wfUserId = wfUserId;
		this.type = type;
	}




	public Long getWfUserId() {
		return wfUserId;
	}




	public void setWfUserId(Long wfUserId) {
		this.wfUserId = wfUserId;
	}








	public RoleType getType() {
		return type;
	}




	public void setType(RoleType type) {
		this.type = type;
	}




	public WsValidationCObj(Boolean isFirstLogin, UserState state) {
		super();
		this.isFirstLogin = isFirstLogin;
		this.state = state;
	}


	public Boolean getIsFirstLogin() {
		return isFirstLogin;
	}
	
	
	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}
	
	
	
	
	
}

package com.institucion.fm.promotion.model;

import com.institucion.fm.wf.model.Role;
import com.institucion.fm.wf.model.RoleType;


public class WFUserView {
	public String username;
	public String fullName;
	public RoleType type;
	public Long id;
	private Role role;
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RoleType getType() {
		return type;
	}
	public void setType(RoleType type) {
		this.type = type;
	}
	
	

}

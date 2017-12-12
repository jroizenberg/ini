package com.institucion.fm.cobj;

import java.io.Serializable;

import com.institucion.fm.security.model.PermissionType;

public class WsPermissionCObj implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String permissionName;
	private PermissionType type;
	
	
	
	
	
	public WsPermissionCObj(String permissionName, PermissionType type) {
		super();
		this.permissionName = permissionName;
		this.type = type;
	}

	public String getPermissionName() {
		return permissionName;
	}
	
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	
	public PermissionType getType() {
		return type;
	}
	
	public void setType(PermissionType type) {
		this.type = type;
	}
	
	
	

}

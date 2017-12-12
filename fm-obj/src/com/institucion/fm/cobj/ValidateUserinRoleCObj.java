package com.institucion.fm.cobj;

import java.io.Serializable;

public class ValidateUserinRoleCObj implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String treeCode;
	private String fatherTreeCode;
	private Long grandFatherId;
	private Long fatherRoleId;
	
	
	
	
	


	public Long getFatherRoleId() {
		return fatherRoleId;
	}

	public void setFatherRoleId(Long fatherRoleId) {
		this.fatherRoleId = fatherRoleId;
	}

	public ValidateUserinRoleCObj(Long id, Long fatherRoleId) {
		super();
		this.id = id;
		this.fatherRoleId = fatherRoleId;
	}

	public ValidateUserinRoleCObj(Long id, String treeCode,
			String fatherTreeCode, Long grandFatherId) {
		super();
		this.id = id;
		this.treeCode = treeCode;
		this.fatherTreeCode = fatherTreeCode;
		this.grandFatherId = grandFatherId;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTreeCode() {
		return treeCode;
	}
	
	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	
	public String getFatherTreeCode() {
		return fatherTreeCode;
	}
	
	public void setFatherTreeCode(String fatherTreeCode) {
		this.fatherTreeCode = fatherTreeCode;
	}
	
	public Long getGrandFatherId() {
		return grandFatherId;
	}
	
	public void setGrandFatherId(Long grandFatherId) {
		this.grandFatherId = grandFatherId;
	}
	
	
	
	

	
	
	
}

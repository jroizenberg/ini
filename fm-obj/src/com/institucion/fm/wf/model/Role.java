package com.institucion.fm.wf.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Role implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	private Role parent;
	private Set<Role> children = new LinkedHashSet<Role>();
	private String name;
	private String description;
	private Set<User> wfusers = new LinkedHashSet<User>();
	private Set<Long> wfusersId = new LinkedHashSet<Long>();
	private RoleType type;
	private String treeCode;
	
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}
	public Role getParent() {
		return parent;
	}
	public void setParent(Role parent) {
		this.parent = parent;
	}
	public Set<Role> getChildren() {
		return children;
	}
	public void setChildren(Set<Role> children) {
		this.children = children;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setWfusers(Set<User> wfusers) {
		this.wfusers = wfusers;
	}
	public Set<User> getWfusers() {
		return wfusers;
	}
	public Set<Long> getWfusersId() {
		return wfusersId;
	}
	public void setWfusersId(Set<Long> wfusersId) {
		this.wfusersId = wfusersId;
	}
	public User getWorkFlowUser()
	{
		return (User) wfusers.toArray()[0];
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}	
	
	public RoleType getType() {
		return this.type;
	}
	public void setType(RoleType type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	
	public int hashCode() {
		if (this.id == null) {
			return System.identityHashCode(this);
		}
		return this.id.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		return obj.hashCode() == this.hashCode();
	}
	public String getTreeCode() {
		return treeCode;
	}
	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}
	

}

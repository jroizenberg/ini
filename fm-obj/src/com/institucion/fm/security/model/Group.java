package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Group implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private RoleType role;
	private Set<User> users = new LinkedHashSet<User>();
	private Set<Permission> permissions = new HashSet<Permission>();

	public Group() {}

	public Group(String name) { setName(name); }

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) {
		if(name == null){
			this.name = null;
		}
		else{
			this.name = name.trim();
		}
	}

	public String getDescription() { return description; }
	public void setDescription(String description) {
		if(description == null){
			this.description = null;
		}
		else{
			this.description = description.trim();
		}
	}

	public Set<User> getUsers() { return users; }
	public void setUsers(Set<User> users) { this.users = users; }

	public Set<Permission> getPermissions() { return permissions; }
	public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }

	public RoleType getRole() {
		return this.role;
	}

	public void setRole(RoleType role) {
		this.role = role;
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
		if (!(obj instanceof Group)) {
			return false;
		}
		return obj.hashCode() == this.hashCode();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("id=[").append(id).append("]");
		buf.append(", name=[").append(name).append("]");
		buf.append(", description=[").append(description).append("]");
		return buf.toString();
	}
}
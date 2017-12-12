package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Permission implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String description;
	private PermissionType type;
	private String token;
	private Set<Group> groups = new LinkedHashSet<Group>();

	public Permission() { }

	public Long getId() { return id; }
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String permission) { this.name = permission; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public PermissionType getType() { return type; }
	public void setType(PermissionType type) { this.type = type; }

	public String getToken() { return token; }
	public void setToken(String token) { this.token = token; }

	public Set<Group> getGroups() { return groups; }
	public void setGroups(Set<Group> groups) { this.groups = groups; }

	public String toString()
	{
		return name+" "+token;
	}

	public int hashCode()
	{
		if (token == null) {
			return System.identityHashCode(this);
		}
		return token.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		return this.hashCode() == obj.hashCode(); 
	}
}
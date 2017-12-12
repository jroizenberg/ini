package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.PermissionType;

/**
 * Por la seguridad Acegi se tuvo que separar la implementación del
 * Application Server y el Web Container. 
 */
public class PermissionWrapper implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Permission permission;

	private Set<GroupWrapper> groups;

	public PermissionWrapper(Permission permission)
	{
		this.permission = permission;
	}

	public String getName() { return permission.getName(); }
	public void setName(String name) { permission.setName(name); }

	public String getDescription() { return permission.getDescription(); }
	public void setDescription(String description) { permission.setDescription(description); }

	public PermissionType getType() { return permission.getType(); }
	public void setType(PermissionType type) { permission.setType(type); }

	public String getToken() { return permission.getToken(); }
	public void setToken(String token) { permission.setToken(token); }

	public Set<GroupWrapper> getGroups()
	{
		if (groups == null)
		{
			groups = new HashSet<GroupWrapper>();
			Iterator<Group> it = permission.getGroups().iterator();
			while (it.hasNext())
				groups.add(new GroupWrapper(it.next()));
		}
		return groups;
	}

	public void setGroups(Set<GroupWrapper> groups)
	{
		this.groups = groups;
	}

	public void addGroup(GroupWrapper group)
	{
		if (groups == null)
			groups = new LinkedHashSet<GroupWrapper>();
		groups.add(group);
	}
}
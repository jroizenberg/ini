package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;

import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.User;

/**
 * Por la seguridad Acegi se tuvo que separar la implementación del
 * Application Server y el Web Container. 
 */
public class GroupWrapper implements GrantedAuthority, Serializable
{
	private static final long serialVersionUID = 1L;

	private Group group;

	private Set<UserWrapper> users;
	private Set<PermissionWrapper> permissions;

	public GroupWrapper(Group group)
	{
		this.group = group;
	}

	public String getName() { return "ROLE_"+group.getName().toUpperCase(); }
	public void setName(String name) { group.setName(name); }

	public String getDescription() { return group.getDescription(); }
	public void setDescription(String description) { group.setDescription(description); }

	public Set<UserWrapper> getUsers()
	{
		if (users == null)
		{
			users = new HashSet<UserWrapper>();
			Iterator<User> it = group.getUsers().iterator();
			while (it.hasNext())
				users.add(new UserWrapper(it.next()));
		}
		return users;
	}

	public void setUsers(Set<UserWrapper> users)
	{
		this.users = users;
	}

	public Set<PermissionWrapper> getPermission()
	{
		if (permissions == null)
		{
			permissions = new HashSet<PermissionWrapper>();
			Iterator<Permission> it = group.getPermissions().iterator();
			while (it.hasNext())
				permissions.add(new PermissionWrapper(it.next()));
		}
		return permissions;
	}

	public void setPermissions(Set<PermissionWrapper> permissions)
	{
		this.permissions = permissions;
	}

	public String getAuthority()
	{
		return getName();
	}
}
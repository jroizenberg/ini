package com.institucion.fm.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;

import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.User;

/**
 * Por la seguridad Acegi se tuvo que separar la implementación del
 * Application Server y el Web Container. 
 */
public class UserWrapper implements Serializable
{
	private static final long serialVersionUID = 1L;

	private User user;
	private Set<GroupWrapper> groups;

	public UserWrapper(User user)
	{
		this.user = user;
	}

	public String getName() { return user.getName(); }
	public void setName(String name) { user.setName(name); }

	public String getPassword() { return user.getPassword(); }
	public void setPassword(String password) { user.setPassword(password); }

	public boolean isEnabled() { return user.isEnabled(); }

	public Set<GroupWrapper> getGroups()
	{
		if (groups == null)
		{
			groups = new HashSet<GroupWrapper>();
			Iterator<Group> itgroup = user.getGroups().iterator();
			while (itgroup.hasNext())
				groups.add(new GroupWrapper(itgroup.next()));
		}
		return groups;
	}

	public GrantedAuthority[] getGrantedAuthorityGroups()
	{
		if (getGroups() == null) return new GrantedAuthority[0];
		GrantedAuthority[] authority = new GrantedAuthority[getGroups().size()]; 

		int i = 0;
		Iterator<GroupWrapper> itgroup = getGroups().iterator();
		while (itgroup.hasNext())
			authority[i++] = itgroup.next();

		return authority;
	}

	public void setGroups(Set<GroupWrapper> groups)
	{
		this.groups = groups;
	}
}
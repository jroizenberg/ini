package com.institucion.fm.security.service;

import java.util.List;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.memory.UserMap;
import org.springframework.dao.DataAccessException;

import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserWrapper;

public class AuthorizationService implements UserDetailsService
{
	private UserMap userMap;
	private SecurityAAEJB securityService;

	public AuthorizationService() { }

	public SecurityAAEJB getSecurityService()
	{
		return securityService;
	}

	public void setSecurityService(SecurityAAEJB securityService)
	{
		this.securityService = securityService;
		userMap = getUsersFromService();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		return userMap.getUser(username);
	}

   public UserMap getUsersFromService()
	{
   	UserMap userMap = new UserMap();
   	List<User> users = securityService.getUsers();

   	for (int i = 0; i < users.size(); i++)
   	{
   		UserWrapper user = new UserWrapper(users.get(i));
			UserDetails userDetails = new org.acegisecurity.userdetails.User(
					user.getName(), user.getPassword(), user.isEnabled(),
					true, true, true, user.getGrantedAuthorityGroups());
			userMap.addUser(userDetails);
   	}

		return userMap;
	}

   public void refresh()
   {
   	if (securityService != null)
   		userMap = getUsersFromService();
   }
   
}
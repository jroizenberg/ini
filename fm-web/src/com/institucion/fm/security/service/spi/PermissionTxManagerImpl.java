package com.institucion.fm.security.service.spi;

import java.util.List;

import com.institucion.fm.conf.Session;
import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.service.PermissionTxManager;

public class PermissionTxManagerImpl implements PermissionTxManager {
	
	private SecurityAAEJB securityService;

	public SecurityAAEJB getSecurityService() {
		return this.securityService;
	}

	public void setSecurityService(SecurityAAEJB securityService) {
		this.securityService = securityService;
	}

	public boolean havePermission(String token) {
		List<String>perms=(List<String>)Session.getAttribute(PERMISSION_MAP_KEY);
		if (perms==null){
			perms=getSecurityService().getUserTxPermissionsTokens(Session.getUsername());
			Session.setAttribute(PERMISSION_MAP_KEY, perms);
		}
		return perms.contains(token);
	}
	
	public void refreshUserPermission() {
		List<String>perms=getSecurityService().getUserTxPermissionsTokens(Session.getUsername());
		Session.setAttribute(PERMISSION_MAP_KEY, perms);
	}

}
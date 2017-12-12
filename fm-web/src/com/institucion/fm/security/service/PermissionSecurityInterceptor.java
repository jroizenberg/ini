package com.institucion.fm.security.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.web.FilterInvocationDefinitionSource;
import org.acegisecurity.intercept.web.FilterSecurityInterceptor;
import org.acegisecurity.intercept.web.PathBasedFilterInvocationDefinitionMap;

import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.GroupWrapper;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.PermissionType;
import com.institucion.fm.security.model.PermissionWrapper;

public class PermissionSecurityInterceptor extends FilterSecurityInterceptor
{
	private SecurityAAEJB securityService;
	private List<PermissionWrapper> staticPermissions;
	private List<String> kernelPermissions;

	public SecurityAAEJB getSecurityService() { return securityService; }
	public void setSecurityService(SecurityAAEJB securityService) { this.securityService = securityService; }

	/**
	 * Estos permisos se asignan en acegi-security.xml y son obligatorios. Sin ellos
	 * no funciona la seguridad. Esto evita tener permisos como /desktop.zul en la base
	 * de datos. Al ponerlos en acegi-security.xml es transparante al programador.
	 */
	public void setInstitucionStaticPermission(List<String> kernelPermissions)
	{
		this.kernelPermissions = kernelPermissions;

		staticPermissions = new ArrayList<PermissionWrapper>();
		List<Group> groups = getSecurityService().getGroups();

		for (int i = 0; i < kernelPermissions.size(); i++)
		{
			String url = kernelPermissions.get(i);
			Permission permission = new Permission();
			permission.setToken(url);
			permission.setDescription(url);
			permission.setType(PermissionType.WEB);

			for (int j = 0; j < groups.size(); j++)
				permission.getGroups().add(groups.get(j));

			PermissionWrapper permissionWrapper = new PermissionWrapper(permission);
			
			staticPermissions.add(permissionWrapper);
		}
	}

	public void setObjectDefinitionSource(FilterInvocationDefinitionSource kernelPermissions)
	{
		PathBasedFilterInvocationDefinitionMap acegiPermissions = new PathBasedFilterInvocationDefinitionMap();
		acegiPermissions.setConvertUrlToLowercaseBeforeComparison(true);

		List<PermissionWrapper> permissionList = this.getPermissions();
		for (int i = 0; i < permissionList.size(); i++)
		{
			PermissionWrapper permission = permissionList.get(i);
			Set<GroupWrapper> groupSet = permission.getGroups();

			// armar los grupos para acegi
			ConfigAttributeDefinition acegiGroups = new ConfigAttributeDefinition();

			Iterator<GroupWrapper> itgroup = groupSet.iterator();
			while (itgroup.hasNext())
			{
				GroupWrapper group = itgroup.next();

				ConfigAttribute acegiGroup = new SecurityConfig(group.getName());
				acegiGroups.addConfigAttribute(acegiGroup);
			}

			// pharmacy-selector.zul -> pharmacy*
			String permissionToken = permission.getToken();
			int separatorIndex = permissionToken.indexOf('-'); 
			if (separatorIndex > 0)
				permissionToken = permissionToken.substring(0, separatorIndex)+"*";
			
			acegiPermissions.addSecureUrl(permissionToken, acegiGroups);
		}

		super.setObjectDefinitionSource(acegiPermissions);
	}

	private List<PermissionWrapper> getPermissions()
	{
		List<PermissionWrapper> permissionsWrapper = new ArrayList<PermissionWrapper>();
		List<Permission> permissions = getSecurityService().getPermissions();

		for (int i = 0; i < permissions.size(); i++)
		{
			permissionsWrapper.add(new PermissionWrapper(permissions.get(i)));
		}
		for (int i = 0; i < staticPermissions.size(); i++)
		{
			permissionsWrapper.add(staticPermissions.get(i));
		}

		return permissionsWrapper;
	}

   public void refresh()
   {
   	setInstitucionStaticPermission(kernelPermissions);
   	setObjectDefinitionSource(null);
   }
}
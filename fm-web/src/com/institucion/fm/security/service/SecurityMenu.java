package com.institucion.fm.security.service;

import java.util.Iterator;
import java.util.List;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.model.MenuBarModel;
import com.institucion.fm.desktop.model.MenuComponentModel;
import com.institucion.fm.desktop.model.MenuItemModel;
import com.institucion.fm.desktop.model.MenuModel;
import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.model.SucursalEnum;

public class SecurityMenu
{
	private SecurityAAEJB securityService;
//	private Set<Permission> userperm;
//	private List<Permission> allperm;

	private List<String>userperm;
	private List<String>allperm;
	
	
	public SecurityMenu() {}

	public SecurityMenu(SecurityAAEJB securityService)
	{
		this.securityService = securityService;
	}

	public SecurityAAEJB getSecurityService()
	{
		return securityService;
	}

	public void setSecurityService(SecurityAAEJB securityService)
	{
		this.securityService = securityService;
	}

	public MenuBarModel applySecurity(MenuBarModel menuTree)
	{
		MenuBarModel secMenuTree = menuTree.clone();

		// traer los permisos de usuario desde el servidor
		String username = Session.getUsername();
		userperm = securityService.getUserPermissionsTokens(username);//securityService.getUserPermissions(username);
		allperm = securityService.getAllPermissionTokens();//securityService.getPermissions();

		applySecurity(secMenuTree.getMenuComponents());
		return secMenuTree;
	}

	private void applySecurity(List<MenuComponentModel> menuComponents)
	{
		Iterator<MenuComponentModel> itMenuCmp = menuComponents.iterator();

		while (itMenuCmp.hasNext())
		{
			MenuComponentModel menuComponent = itMenuCmp.next();
			if (menuComponent instanceof MenuModel)
			{
				MenuModel menu = (MenuModel) menuComponent;
				applySecurity(menu.getMenuComponents());
				if (menu.getMenuComponents().size() == 0)
					itMenuCmp.remove();
			}
			else // MenuItemModel
			{
				if (!isMenuPermitted((MenuItemModel) menuComponent))
				{
					itMenuCmp.remove();
				}
			}
		}
	}

	private boolean isMenuPermitted(MenuItemModel menuItem)
	{
		boolean exist = allperm.contains(menuItem.getAction());
		if (userperm == null) return false;
		
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			if(Session.getAttribute("sucursalPrincipalSeleccionada") instanceof SucursalEnum 
					&&  ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).toInt() == SucursalEnum.MAIPU.toInt()){
				if(menuItem.getAction() != null 
						&& (  menuItem.getAction().equalsIgnoreCase("/institucion/alumnosEnClase-selector.zul")  
							|| menuItem.getAction().equalsIgnoreCase("/institucion/productos-venta-selector.zul")
							|| menuItem.getAction().equalsIgnoreCase("/security/generar-codigos-barras-crud.zul")
							|| menuItem.getAction().equalsIgnoreCase("/institucion/cumples-selector.zul"))){
					return false;
				}	
				
			}
			
			
		}

		boolean permitted = userperm.contains(menuItem.getAction());
		return !exist || exist && permitted;
	}
}
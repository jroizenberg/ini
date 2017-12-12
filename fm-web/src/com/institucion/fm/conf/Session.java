package com.institucion.fm.conf;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Toolbarbutton;

import com.institucion.fm.desktop.view.IncludePage;
import com.institucion.model.SucursalEnum;

/**
 * Provee acceso a la sesión del usuario. En esta clase se puede guardar variables
 * que el usuario puede compartir en distintas partes del programa.
 */
public class Session
{
	public static String getUsername()
	{
		
		return (String) Sessions.getCurrent().getAttribute("username");
	}
	
	public static SucursalEnum getsucursalDelLoguin()
	{
		
		return (SucursalEnum) Sessions.getCurrent().getAttribute("sucursalDelLoguin");
	}
	public static Long getUsernameID()
	{
		
		return (Long) Sessions.getCurrent().getAttribute("usernameID");
	}
	public static void setDesktopPanel(IncludePage desktoppanel)
	{
		setAttribute("desktoppanel", desktoppanel);
	}

	public static IncludePage getDesktopPanel()
	{
		return (IncludePage) getAttribute("desktoppanel");
	}

	public static void setPopupButton(Toolbarbutton popupbutton)
	{
		setAttribute("popupbutton", popupbutton);
	}

	public static Toolbarbutton getPopupButton()
	{
		return (Toolbarbutton) getAttribute("popupbutton");
	}

	public static void setAttribute(String key, Object value)
	{
		Sessions.getCurrent().setAttribute(key, value);
	}

	public static Object getAttribute(String key)
	{
		return Sessions.getCurrent().getAttribute(key);
	}

	public static String getStringAttribute(String key)
	{
		return (String) getAttribute(key);
	}

	public static int getIntAttribute(String key)
	{
		return (Integer) getAttribute(key);
	}

	/**
	 * Ejemplo: Executions.sendRedirect("user-crud.zul?update=1");
	 */
	public static Object getAttributeFromCall(String key)
	{
		return Executions.getCurrent().getParameter(key);
	}

	public static boolean existAttributeFromCall(String key)
	{
		return Executions.getCurrent().getParameter(key) != null;
	}
}
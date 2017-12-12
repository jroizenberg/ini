package com.institucion.fm.desktop.model;

import com.institucion.fm.security.model.Permission;

/**
 * Un men� visual (window o web) tiene �tems que contienen la etiqueta del �tem
 * y la acci�n si se ejecuta. Tambi�n puede tener otras propiedades como una im�gen.
 */
public class MenuItemModel extends MenuComponentModel
{
	/** �cono a la izquierda del men�. */
	private String pathIcon;

	/** Acci�n del men�. */
	private String action;

	/** En formularios WEB se suelen usar Frames. El target indica el frame donde
	 * debe ejecutarse la acci�n. Targets especiales: '_top'
	 */
	private String target;

	private String image;

	public MenuItemModel() {}

	public MenuItemModel(String text, String action)
	{
		super(text);
		this.setAction(action);
	}

	public MenuItemModel(String text, String action, String pathIcon)
	{
		this(text, action);
		this.setPathIcon(pathIcon);
	}

	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }

	public String getPathIcon() { return pathIcon; }
	public void setPathIcon(String pathIcon) { this.pathIcon = pathIcon; }

	public String getTarget() { return target == null ? "main" : target; }
	public void setTarget(String target) { this.target = target; }

	public String getImage() { return image; }
	public void setImage(String image) { this.image = image; }

	public MenuItemModel clone()
	{
		MenuItemModel menuItem = new MenuItemModel(super.getText(), action);
		menuItem.pathIcon = pathIcon;
		menuItem.target = target;
		menuItem.image = image;

		return menuItem;
	}

	public boolean equals(Object value)
	{
		if (value instanceof Permission)
			return ((Permission) value).getToken().equals(action);
		return super.equals(value);
	}

	public int hashCode()
	{
		return action.hashCode();
	}
}
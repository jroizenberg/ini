package com.institucion.fm.desktop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Un menu se aniade a una barra de menu y contiene items de menu. Tambien puede
 * tener sub-menues
 */
public class MenuModel extends MenuComponentModel
{
	private List<MenuComponentModel> components;

	/** icono a la izquierda del menu. */
	private String pathIcon;

	public MenuModel()
	{
		components = new ArrayList<MenuComponentModel>();
	}

	public MenuModel(String text)
	{
		super(text);
		components = new ArrayList<MenuComponentModel>();
	}

	public MenuModel(String text, String pathIcon)
	{
		this(text);
		setPathIcon(pathIcon);
	}

	public void addMenuComponent(MenuComponentModel component)
	{
		components.add(component);
	}

	public List<MenuComponentModel> getMenuComponents()
	{
		return components;
	}

	public String getPathIcon() { return pathIcon; }
	public void setPathIcon(String pathIcon) { this.pathIcon = pathIcon; }

	public MenuModel clone()
	{
		MenuModel menuModel = new MenuModel(getText(), pathIcon);

		for (int i = 0; i < components.size(); i++)
			menuModel.addMenuComponent(components.get(i).clone());

		return menuModel;
	}
}
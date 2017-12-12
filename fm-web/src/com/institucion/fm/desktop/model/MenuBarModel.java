package com.institucion.fm.desktop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de una menu.
 */
public class MenuBarModel
{
	private List<MenuComponentModel> menuComponents;

	public MenuBarModel()
	{
		menuComponents = new ArrayList<MenuComponentModel>();
	}

	public void addMenuComponent(MenuComponentModel menu)
	{
		menuComponents.add(menu);
	}

	public List<MenuComponentModel> getMenuComponents()
	{
		return menuComponents;
	}

	public MenuBarModel clone()
	{
		MenuBarModel menuBar = new MenuBarModel();

		for (int i = 0; i < menuComponents.size(); i++)
		{
			menuBar.addMenuComponent(menuComponents.get(i).clone());
		}

		return menuBar;
	}
}
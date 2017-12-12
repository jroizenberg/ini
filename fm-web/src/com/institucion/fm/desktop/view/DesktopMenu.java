package com.institucion.fm.desktop.view;

import java.util.Iterator;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.model.MenuBarDigester;
import com.institucion.fm.desktop.model.MenuBarModel;
import com.institucion.fm.desktop.model.MenuComponentModel;
import com.institucion.fm.desktop.model.MenuItemModel;
import com.institucion.fm.desktop.model.MenuModel;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.security.service.SecurityMenu;

public class DesktopMenu extends Tabbox implements AfterCompose
{
	private static final long serialVersionUID = 1L;

	private int tabcount = 0;

	public DesktopMenu() { }

	public void afterCompose()
	{
		MenuBarModel menuTree = MenuBarDigester.getInstitucionMenu();
		SecurityMenu securityMenu = BeanFactory.<SecurityMenu>getObject("fm.view.security.menu.securityMenu");
		MenuBarModel secMenuTree = securityMenu.applySecurity(menuTree);
		buildMenu(secMenuTree);
	}

	private void buildMenu(MenuBarModel menuTree)
	{
		Tabs tabs = new Tabs();
		this.appendChild(tabs);
		Tabpanels tabpanels = new Tabpanels();
		this.appendChild(tabpanels);
		
		Iterator<MenuComponentModel> itMenuCmp = menuTree.getMenuComponents().iterator();

		while (itMenuCmp.hasNext())
		{
			MenuComponentModel menuComponent = itMenuCmp.next();
			if (menuComponent instanceof MenuModel)
			{
				MenuModel menu = (MenuModel) menuComponent;
				// componente ZK
				tabs.appendChild(new Tab(I18N.getStringLabel(menu.getText())));
				Tabpanel menuTabpanel = buildTabPanel(menu); 
				tabpanels.appendChild(menuTabpanel);
			}
			else // MenuItemModel
				throw new RuntimeException("El menú está mal formado. Debe tener dos niveles");
		}
	}

	private Tabpanel buildTabPanel(MenuModel menu)
	{
		Tabpanel tabpanel = new Tabpanel();
		Listbox listbox = new Listbox();
		listbox.setOddRowSclass("non-odd");
		listbox.setSclass("menu-items");
		listbox.setId("listbox"+(tabcount++));

		tabpanel.appendChild(listbox);

		listbox.setItemRenderer(
			new ListitemRenderer()
			{
				public void render(Listitem listItem, Object data)
				{
					MenuItemModel item = (MenuItemModel) data;
					Listcell listCell = new Listcell();
					listItem.setValue(item);
					listCell.setHeight("25px");
//					listCell.setImage(item.getImage());
					listCell.setLabel(I18N.getStringLabel(item.getText()));
					listCell.setParent(listItem);
					Component nullComponent = null;
					listItem.addForward("onClick", nullComponent, "onSelectMenuItem", item);
				}
			});

		ListModel listModel = new ListModelList(menu.getMenuComponents());
		listbox.setModel(listModel);
		listbox.renderAll();

		return tabpanel;
	}
}
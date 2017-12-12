package com.institucion.fm.desktop.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

public class MenuBarDigester
{
	private static Log log = LogFactory.getLog(MenuBarDigester.class);
	
	public static MenuBarModel getInstitucionMenu()	{
		try{
			InputStream in = MenuBarDigester.class.getResourceAsStream("/menutree.xml");
			MenuBarModel menuTree = parse(in);
			return menuTree;
		}	catch (Exception e)	{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			return null;
		}
	}

	public static MenuBarModel parse(File file) throws IOException, SAXException	{
		return parse(new FileReader(file));
	}

	public static MenuBarModel parse(InputStream inputStream) throws IOException, SAXException	{
		return parse(new InputStreamReader(inputStream));
	}

	public static MenuBarModel parse(Reader reader) throws IOException, SAXException{
		Digester digester = new Digester();
		digester.addObjectCreate("menu-tree", MenuBarModel.class);

		digester.addObjectCreate("menu-tree/menu", MenuModel.class);
		digester.addSetProperties("menu-tree/menu");
		digester.addSetNext("menu-tree/menu", "addMenuComponent");

		digester.addObjectCreate("menu-tree/menu/menu-item", MenuItemModel.class);
		digester.addSetProperties("menu-tree/menu/menu-item");
		digester.addSetNext("menu-tree/menu/menu-item", "addMenuComponent");

		return (MenuBarModel) digester.parse(reader);
	}
}
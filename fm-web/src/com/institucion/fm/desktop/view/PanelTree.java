package com.institucion.fm.desktop.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

public class PanelTree extends Panel implements AfterCompose
{
	private static Log log = LogFactory.getLog(PanelTree.class);
	
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelTree";

	private String usetree;
	private TreeList treelist;

	public PanelTree()
	{
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getUsetree() { return usetree; }
	public void setUsetree(String usetree) { this.usetree = usetree; }

	@Override
	public void afterCompose()
	{
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		try
		{
			Class<?> treeClass = Class.forName(usetree);
			treelist = (TreeList) treeClass.newInstance();
			if (!getId().equals(ID))
				treelist.setId(getId()+"Tree");
		}
		catch (Exception e)
		{
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new RuntimeException("'"+usetree+"' no existe o no extiende de TreeList", e);
		}

		panelChild.appendChild(treelist);		
	}

	public TreeList getTreeList()
	{
		return treelist;
	}
}
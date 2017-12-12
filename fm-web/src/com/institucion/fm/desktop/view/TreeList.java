package com.institucion.fm.desktop.view;


import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;

public class TreeList extends Tree
{
	private static final long serialVersionUID = 1L;
	private String ID = "treeList";

	public TreeList()
	{
		this.setId(ID);
		setZclass("z-dottree");
		this.setWidth("auto");
		this.setHeight("auto");
		this.setFixedLayout(true);
		this.setMultiple(true);
		this.setCheckmark(true);
		this.setMold("paging");
		this.setPageSize(10);
		Treecols cols = new Treecols();
		cols.setSizable(true);
		this.appendChild(cols);
		super.appendChild(new Treechildren());
	}

	public void addCol(Treecol col)
	{
		this.getTreecols().appendChild(col);
	}

	public void disable()
	{
		for(Object item: this.getItems()){
			((Treeitem)item).setDisabled(true);
		}
	}
	public void enable()
	{
		for(Object item: this.getItems()){
			((Treeitem)item).setDisabled(false);
		}
	}	
	
	public void removeAll() {
		super.clear();
	}

}
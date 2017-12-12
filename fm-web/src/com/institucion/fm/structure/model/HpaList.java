package com.institucion.fm.structure.model;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridViewList;

public class HpaList extends GridViewList{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HpaList()
	{
		super();
		this.setCheckmark(false);
		
		super.addHeader(new Listheader(I18N.getLabel("hpa.view.name")));
		super.addHeader(new Listheader(I18N.getLabel("hpa.view.lastname")));
		super.removeAll();
	}

	public void setList(List<ViewHpa> list)
	{
		
		super.removeAll();	
		Iterator<ViewHpa> itHpa = list.iterator();
		while (itHpa.hasNext())
		{
			ViewHpa hpaView = itHpa.next();
			Listitem row = new Listitem();
			row.setValue(hpaView);
			row.appendChild(new Listcell(hpaView.getName()));
			row.appendChild(new Listcell(hpaView.getLastName()));
			super.addRow(row);
		}
	}


}

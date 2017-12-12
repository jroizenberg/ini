package com.institucion.fm.contact.model;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.cobj.ApmBandBoxCObj;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.bb.BandBoxList;

public class ApmBbList extends BandBoxList<ApmBandBoxCObj> {

	/**
	 * 
	 */
	
	
	public ApmBbList() {
		super();
	}
	private static final long serialVersionUID = 1L;

	@Override
	protected void addHeaders() {
		this.addHeader(new Listheader(I18N.getLabel("apm.name")));
		
	}

	@Override
	protected void addRow(ApmBandBoxCObj item) {
		Listitem row = new Listitem();
		row.setValue(item);
		Listcell usernameCell = new Listcell(item.getLastName()+ " "+ item.getFirstName());
		row.appendChild(usernameCell);
		addRow(row);
	}

}

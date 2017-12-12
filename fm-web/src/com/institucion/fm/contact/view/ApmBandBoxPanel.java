package com.institucion.fm.contact.view;

import com.institucion.fm.cobj.ApmBandBoxCObj;
import com.institucion.fm.contact.model.ApmBbList;
import com.institucion.fm.desktop.view.bb.BandBoxList;
import com.institucion.fm.desktop.view.bb.BandBoxPanel;

public class ApmBandBoxPanel extends BandBoxPanel<ApmBandBoxCObj> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ApmBandBoxCObj cobj;
	
	public ApmBandBoxCObj getCobj() {
		return cobj;
	}
	public ApmBandBoxPanel() {
		super("apmBbPanel",ApmBbFilter.class.getCanonicalName());
	}
	public ApmBandBoxPanel(String panelId) {
		super("apmBbPanel", ApmBbFilter.class.getCanonicalName());

	}

	@Override
	protected String getBandBoxFilterClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BandBoxList<ApmBandBoxCObj> getBandBoxList() {
		return new ApmBbList();
	}

	@Override
	protected int getPanelWidth() {
		return 350;
	}

	@Override
	public String stringValue(ApmBandBoxCObj value) {
		this.cobj=value;
		return value.getLastName()+ " " +value.getFirstName();
	}

}

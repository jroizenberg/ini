package com.institucion.fm.desktop.view.bb;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridFilter;

public abstract class BandBoxFilter extends GridFilter {
	private static final long serialVersionUID = 1L;
	private Textbox master;

	public BandBoxFilter() {
		super();
		this.buildFilter();
	}
	
	public Textbox getMaster() {
		return this.master;
	}
	
	protected void buildFilter() {
		Row row1 = new Row();
		row1.appendChild(this.getMasterFieldLabel());
		this.master = new Textbox();
		this.master.setConstraint(new TextConstraint());
		this.master.setMaxlength(20);
		this.master.addForward("onOK", (Component) null, this.getOnFindEventName());
		row1.appendChild(master);
		this.addRow(row1);
	}

	abstract protected Label getMasterFieldLabel();
	abstract protected String getOnFindEventName();
	
	public void clear() {
		this.master.setText("");
	}
}

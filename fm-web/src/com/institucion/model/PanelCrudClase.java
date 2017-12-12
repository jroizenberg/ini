package com.institucion.model;

import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridSchedule;

public class PanelCrudClase extends Panel implements AfterCompose{
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelCrudFEContact";

	private String feclass;
	private GridSchedule gridSchedule;
	
	public PanelCrudClase(){
		setId(ID);
		setWidth("auto");
		setHeight("auto");
		setBorder("normal");
		setCollapsible(false);
	}

	public String getFeclass() { return feclass; }
	public void setFeclass(String feclass) { this.feclass = feclass; }

	@Override
	public void afterCompose()
	{
		Panelchildren panelChild = new Panelchildren();
		this.appendChild(panelChild);

		gridSchedule = new GridSchedule();
	
		Panel panel = new Panel();
		panel.setTitle(I18N.getLabel("panelcrud.schedule"));
		
		Panelchildren panelChild1 = new Panelchildren();
		
		panelChild1.appendChild(gridSchedule);
	
		panel.appendChild(panelChild1);
		
		panelChild.appendChild(panel);
	
	}

	public GridSchedule getGridSchedule() {
		return gridSchedule;
	}
}
package com.institucion.fm.contact.view;

import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.institucion.fm.cobj.ApmBandBoxCObj;
import com.institucion.fm.desktop.view.bb.CustomBandBox;


public class LoadApmPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CustomBandBox<ApmBandBoxCObj> apmBB;
	private ApmBandBoxPanel bandBoxPanel;
	
	
	public void onCreate(){
		Panelchildren pc =new Panelchildren();
		bandBoxPanel=new ApmBandBoxPanel();
		apmBB= new CustomBandBox<ApmBandBoxCObj>(bandBoxPanel);
		Label l=new Label("Apm");
		pc.appendChild(l);
		pc.appendChild(apmBB);
		this.appendChild(pc);
	}
	
	public String getTextInCustomBandBox(){
		return apmBB.getText();
	}
	
	
	public ApmBandBoxPanel getBandBoxPanel() {
		return bandBoxPanel;
	}
	
	public ApmBandBoxPanel getCustonBB(){
		return bandBoxPanel;
	}
	
}

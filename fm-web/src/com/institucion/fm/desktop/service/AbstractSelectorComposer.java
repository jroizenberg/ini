package com.institucion.fm.desktop.service;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;

import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public abstract class AbstractSelectorComposer extends GenericAutowireComposer{
	private boolean callFromMenu = false;

	public void gotoPage(String zulpage){
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage("");
	}

	public void gotoPage(String zulpage, String message){
		Session.getDesktopPanel().setSrc(zulpage);
		Session.getDesktopPanel().setMessage(message);
	}

	public boolean isCallFromMenu() { 
		return callFromMenu; 
	}
	
	public void setCallFromMenu(boolean callFromMenu) { this.callFromMenu = callFromMenu; }

	public abstract void onFind(Event evt);

	public PanelFilter getPanelFilter() { return (PanelFilter) super.spaceOwner.getFellowIfAny(PanelFilter.ID); }

	public GridFilter getGridFilter() { return (GridFilter) super.spaceOwner.getFellowIfAny(GridFilter.ID); }

	public void onClearFilter(Event evt){
		if (getGridFilter() != null){
			getGridFilter().clear();
			if (isCallFromMenu())
				getPanelFilter().clear();
		}
		onFind(null);
	}

	public CriteriaClause getCriteriaFilter(){
		return getCriteriaFilter(GridFilter.ID);
	}

	public CriteriaClause getCriteriaFilter(String gridFilterID){
		GridFilter grid = (GridFilter) super.spaceOwner.getFellowIfAny(gridFilterID);
		CriteriaClause filterClause = grid.getCriteriaFilters();
		return filterClause;
	}
	
	public Map<String, String> getLineSegmentFilter(){
		return getLineSegmentFilter(GridFilter.ID);
	}
	
	public Map<String, String> getLineSegmentFilter(String gridFilterID){
//		HealthProfessionalFilter grid = (HealthProfessionalFilter) super.spaceOwner.getFellowIfAny(gridFilterID);
//		Map<String, String> lineSegmentFilter = new HashMap<String, String> ();
//		if (grid != null)
//			lineSegmentFilter = grid.getHPLineSegmentFilter();
		return null;
	}
}
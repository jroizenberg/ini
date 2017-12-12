package com.institucion.fm.desktop.service;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.fm.desktop.view.TextToolbar;
import com.institucion.fm.desktop.view.TextToolbarButton;
import com.institucion.fm.desktop.view.WindowSelector;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.service.PermissionTxManager;

public abstract class SelectorComposer extends AbstractSelectorComposer {

	private static Log log = LogFactory.getLog(SelectorComposer.class);
	
	private PermissionTxManager permTxMgr = null;

	public PermissionTxManager getManager() {
		if (permTxMgr == null) {
			permTxMgr = BeanFactory.<PermissionTxManager>getObject("fm.mgr.tx.permission");
		}
		return permTxMgr;
	}

	public void saveHistory() {
		saveHistory(PanelFilter.ID, "das");
	}

	public void saveHistory(String panelFilterId) {
		saveHistory(panelFilterId, "das");
	}

	public void saveHistory(String panelFilterId, String panelAdvFilterId) {
		PanelFilter panelFilter = (PanelFilter) super.spaceOwner.getFellowIfAny(panelFilterId);
		if (panelFilter != null)
			panelFilter.saveHistory();
//		PanelFilterAdv panelGridAdv = (PanelFilterAdv) super.spaceOwner.getFellowIfAny(panelAdvFilterId);
//		if (panelGridAdv != null)
//			panelGridAdv.saveHistory();
	}

//	public PanelFilterAdv getPanelFilterAdv() { return (PanelFilterAdv) super.spaceOwner.getFellowIfAny(PanelFilterAdv.ID); }

//	public GridFilterAdv getGridFilterAdv() { return (GridFilterAdv) super.spaceOwner.getFellowIfAny(GridFilterAdv.ID); }

	public void onClearFilter(Event evt) {
//		GridFilterAdv gridFilterAdv = getGridFilterAdv();
//		if (gridFilterAdv != null) {
//			gridFilterAdv.clear();
//			if (isCallFromMenu())
//				getPanelFilterAdv().clear();
//		}

		super.onClearFilter(evt);
	}

	public CriteriaClause getCriteriaFilter() {
		return getCriteriaFilter(GridFilter.ID, "sas");
	}

	public CriteriaClause getCriteriaFilter(String gridFilterID, String gridFilterAdvID) {
		CriteriaClause filterClause = super.getCriteriaFilter(gridFilterID);

//		GridFilterAdv gridadv = (GridFilterAdv) super.spaceOwner.getFellowIfAny(gridFilterAdvID);
//		if (gridadv != null && gridadv.isVisible()) {
//			CriteriaClause filterAdvClause = gridadv.getCriteriaFilters();
//			filterClause.addCriteria(filterAdvClause);
//		}

		return filterClause;
	}

	public void onCreate() {
		try {
			setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
			if (isCallFromMenu())
				onClearFilter(null);
			else
				onFind(null);
			setCallFromMenu(false);
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}
	}

	public void onFindAdv(Event evt) throws Exception {
		switchAdvancedPanel(WindowSelector.ID, "asas", PanelList.ID);
	}

	public  void switchAdvancedPanel(String windowId, String advpanelId, String beforeId) {
		Page page = (Page) super.page;
		Window win = (Window) page.getFellow(windowId);
	
		Component panel = win.getFellow(advpanelId);
		panel.setVisible(!panel.isVisible());
		win.insertBefore(panel, win.getFellow(beforeId));

		TextToolbarButton button = (TextToolbarButton) win.getFellow(TextToolbar.ID_BUTTON_FINDADV);
		if (panel.isVisible()) {
			button.setLabelAndTooltip(I18N.getLabel("toolbarselector.closefindadv"),I18N.getLabel("toolbarselector.closefindadv.tooltip"));
		}
		else {
			button.setLabelAndTooltip(I18N.getLabel("toolbarselector.openfindadv"), I18N.getLabel("toolbarselector.openfindadv.tooltip"));
		}

	}

	protected boolean hasSelectedItem(GridList gridlist) {
		if (gridlist.getSelectedItem() != null)
			return true;
		MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
		return false;
	}

	/**
	 * Identificador para recuperar la entidad seleccionada cuando se vuelve de
	 * algun reporte.
	 */
	private String getSelectedEntitySessionId() {
		return "EntitySelected_"+this.getClass().getCanonicalName();
	}
	public void setSelectedEntity(GridList gridlist) {
		if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
			Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(),gridlist.getSelectedItem().getValue());

		}
	}


	protected void  savePage(Integer page) {
		Sessions.getCurrent().setAttribute("pagi",page);
	}

	protected Integer loadPage() {
		if((Integer) Sessions.getCurrent().getAttribute("pagi")!=null){
			return (Integer) Sessions.getCurrent().getAttribute("pagi");
		}else{
			return 0;
		}
	}

	/**
	 * Retornar la entidad seleccionada antes del reporte y eliminarla de la sesion. 
	 */
	protected Object getSelectedEntity2() {
		Object entity = Sessions.getCurrent().getAttribute(getSelectedEntitySessionId());

		return entity;
	}

	/**
	 * Retornar la entidad seleccionada antes del reporte y eliminarla de la sesion. 
	 */
	protected Object getSelectedEntity() {
		Object entity = Sessions.getCurrent().getAttribute(getSelectedEntitySessionId());
		Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(), null);
		return entity;
	}

//	protected boolean isFromHistory(ContactView cv) {
//		return cv.isHistory();
//	}
	
	protected boolean hasSelectedOneItem(GridList gridlist) {
	 	if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
			Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(),gridlist.getSelectedItem().getValue());
			return true;
		}
		MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
		return false;
	}

	
	
	
	protected boolean hasSelectedOneItemSinMensaje(GridList gridlist) {
	 	if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
			Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(),gridlist.getSelectedItem().getValue());
			return true;
		}
		return false;
	}
	
	protected boolean hasSelectedOneItem(Listbox gridlist) {
		if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
			Sessions.getCurrent().setAttribute(getSelectedEntitySessionId(),gridlist.getSelectedItem().getValue());
			return true;
		}
		MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
		return false;
	}

	   protected boolean hasSelectedItem(Listbox gridlist) {
	        if (gridlist.getSelectedItem() != null)
	            return true;
	        MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),
	                        I18N.getLabel("selector.actionwithoutitem.title"));
	        return false;
	    }
	
	protected boolean doAction(GridList gridlist) {
		if (!hasAprovalPendingItems(gridlist)) {
			if (gridlist.getSelectedItems().size() != 0)
				return true;
		
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
		}
		return false;
	}

	protected boolean hasAprovalPendingItems(GridList gridList) {
		boolean withAprovalPending = false;

		@SuppressWarnings("unchecked")
		Iterator<Listitem> itemsIt = gridList.getSelectedItems().iterator();
		while (itemsIt.hasNext() && !withAprovalPending) {
//			FEBase febase = (FEBase) itemsIt.next().getValue();
//			if (febase.getState().equals(FEState.APROVAL_PENDING)) {
//				withAprovalPending = true;
//			}
		}
		if (withAprovalPending) {
			MessageBox.info(I18N.getLabel("selector.aprovpend.information"), I18N.getLabel("selector.aprovpend.title"));
			return true;
		}
		return false;
	}

	public  void switchFindDuplicatePanel(String windowId, String panelId, String beforeId) {
		Page page = (Page) super.page;
		Window win = (Window) page.getFellow(windowId);
	
		Component panel = win.getFellow(panelId);
		panel.setVisible(!panel.isVisible());
		win.insertBefore(panel, win.getFellow(beforeId));
	}

}
package com.institucion.fm.security.controller;

import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listitem;

import com.institucion.fm.conf.CallContextHelper;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.desktop.view.PanelList;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.SecurityEJB;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.GroupCrud;
import com.institucion.fm.security.model.GroupFilter;
import com.institucion.fm.security.model.GroupList;
import com.institucion.fm.security.service.PermissionTxManager;

public class GroupComposer extends SelectorComposer {
	
	private static Log log = LogFactory.getLog(GroupComposer.class);
	
	private PanelFilter filter;
	private PanelList list;
	private SecurityEJB securityEjb = null;

	private SecurityEJB getService()
	{
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}

	public void onCreate() {
		this.getGroupList().sort(false);
		super.onCreate();
	}

	private GroupList getGroupList() {
		return (GroupList)(list.getGridList());
	}

	private GroupFilter getGroupFilter() {
		return (GroupFilter)(filter.getGridFilter());
	}

	public void onFind(Event evt) {
		//log.debug("onFind: [" + this.getGroupFilter().getId() + "]");
		CriteriaClause criteria = getCriteriaFilter(this.getGroupFilter().getId());
		//log.debug("criteria {" + criteria+ "}");
		List<Group> groups = this.getService().getGroups(CallContextHelper.getCallContext(), criteria);
		this.getGroupList().setList(groups);
		this.getGroupList().sort(true);
		super.saveHistory("filter");
	}

	public void onClearFilter(Event evt)
	{
		if (super.isCallFromMenu())
			filter.clear();
		this.getGroupFilter().clear();
		this.onFind(null);
		super.saveHistory("filter");
	}

	public void onDelete(Event event) throws Exception {
		try{
			GroupList groupList = this.getGroupList();
			if (groupList.getSelectedItem() != null && groupList.getSelectedItems().size() > 0){
				if (MessageBox.question(I18N.getLabel("selector.delete.question"), I18N.getLabel("selector.delete.title",I18N.getLabel("selector.group.generic.title"))))				
				{
					List<Long> groups = new LinkedList<Long>();
					for (Object item: this.getGroupList().getSelectedItems()){
						groups.add((Long)(((Listitem)item).getValue()));
					}

					this.getService().deleteAllGroups(CallContextHelper.getCallContext(), groups);


					MessageBox.info(I18N.getLabel("selector.group.deleted"),I18N.getLabel("selector.group.delete.title"));
					// redibuja los elementos que han quedado y aplica los filtros si existen
					this.onFind(null);
				}
			}
			else{
				MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
			}

		}catch (EJBException ex) {
			log.error("Mensaje: " + ex.getMessage() + "StackTrace: " + ex.getStackTrace());
			WebExceptionHandler.handleThis(ex);
		}
	}

	public void onInsert (Event event) throws Exception {
		Sessions.getCurrent().setAttribute(GroupCrud.SESSION_GROUP_ID, null);
		//		super.saveHistory("filter");
		super.gotoPage("/security/group-crud.zul");
	}

	public void onUpdate(Event event) throws Exception {
		GroupList gl = this.getGroupList();
		if (gl.getSelectedItem() != null && gl.getSelectedItems().size() == 1) {
			// GOTO group-crud
			Sessions.getCurrent().setAttribute(GroupCrud.SESSION_GROUP_ID, gl.getSelectedItem().getValue());
			//			super.saveHistory("filter");
			super.gotoPage("/security/group-crud.zul");
		} else {
			// msg de seleccionar uno (no selecciona nada o mas de uno)
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
		}
	}	


	public void onDoubleClickEvt(Event event) throws Exception {
		if (this.getManager().havePermission(PermissionTxManager.TX_GROUP_MODIFY)) {
			this.onUpdate(event);
		}
	}
}

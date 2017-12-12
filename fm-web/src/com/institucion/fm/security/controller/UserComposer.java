package com.institucion.fm.security.controller;

import java.util.Iterator;
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
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserCrud;
import com.institucion.fm.security.model.UserFilter;
import com.institucion.fm.security.model.UserList;
import com.institucion.fm.security.model.UserState;
import com.institucion.fm.security.service.AuthorizationService;
import com.institucion.fm.security.service.PermissionTxManager;

public class UserComposer extends SelectorComposer {
	
	private static Log log = LogFactory.getLog(UserComposer.class);
	
	private PanelFilter filter;
	private PanelList list;
	private SecurityEJB securityEjb = null;

	private SecurityEJB getService(){
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}

	public void onCreate() {
		this.getUserList().sort(false);
		super.onCreate();
	}

	private UserList getUserList() {
		return (UserList)(list.getGridList());
	}

	private UserFilter getUserFilter() {
		return (UserFilter)(filter.getGridFilter());
	}

	public void onFind(Event evt) {
		//log.debug("onFind: [" + this.getUserFilter().getId() + "]");
		CriteriaClause criteria = this.getCriteriaFilter(this.getUserFilter().getId());
		//log.debug("criteria {" + criteria+ "}");
		List<User> users = this.getService().getUsers(CallContextHelper.getCallContext(), criteria);
		this.getUserList().setList(users);
		this.getUserList().sort(true);
		super.saveHistory("filter");
	}

	public void onUpdate(Event event) throws Exception {
		UserList userList = this.getUserList();
		if (userList.getSelectedItem() != null && userList.getSelectedItems().size() == 1) {
			// GOTO user-crud
			if(((User)userList.getSelectedItem().getValue()).getState().equals(UserState.ACTIVE)){
				Sessions.getCurrent().setAttribute(UserCrud.SESSION_USER_ID, ((User) userList.getSelectedItem().getValue()).getId());
				super.gotoPage("/security/user-crud.zul", "update");
			}else{
				MessageBox.info(I18N.getLabel("selector.User.Inactive"),I18N.getLabel("selector.User.Inactive.title"));
			}
		} else {
			// msg de seleccionar uno (no selecciona nada o mas de uno
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
		}
	}	


	public void onDoubleClickEvt(Event event) throws Exception {
		if (this.getManager().havePermission(PermissionTxManager.TX_USER_MODIFY)) {
			this.onUpdate(event);
		}
	}

	public void onClearFilter(Event evt){
		if (super.isCallFromMenu())
			filter.clear();
		this.getUserFilter().clear();
		this.onFind(null);
		super.saveHistory("filter");
	}

	public void onInsert (Event event) throws Exception {
		Sessions.getCurrent().setAttribute(UserCrud.SESSION_USER_ID, null);
		super.gotoPage("/security/user-crud.zul");
	}

	public void onDelete(Event event) throws Exception {
		try{
			UserList userList = this.getUserList();
			if (userList.getSelectedItem() != null && userList.getSelectedItems().size() > 0){
				if (MessageBox.question(I18N.getLabel("selector.delete.question"), I18N.getLabel("selector.delete.title",I18N.getLabel("selector.user.generic.title"))))					{
					List<Long> users = new LinkedList<Long>();
					for (Object item: this.getUserList().getSelectedItems()){
						users.add((Long)( ((User) ((Listitem)item).getValue()).getId() ));
					}
					this.getService().deleteAllUsers(CallContextHelper.getCallContext(), users);
					MessageBox.info(I18N.getLabel("selector.user.deleted"), I18N.getLabel("selector.user.delete.title"));
					this.getUserList().setList(this.getService().getUsers(CallContextHelper.getCallContext()));
					this.getUserList().sort(true);	
				}
			}
			else{
				// msg de seleccionar uno (no selecciona nada o mas de uno
				MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"),I18N.getLabel("selector.actionwithoutitem.title"));
			}
		}
		catch (EJBException ex) {
			log.error("Mensaje: " + ex.getMessage() + "StackTrace: " + ex.getStackTrace());
			WebExceptionHandler.handleThis(ex);
		}
	}


	public void onActivate(Event event) throws Exception{
		if (super.hasSelectedItem(list.getGridList())){
			if (MessageBox.question(I18N.getLabel("selector.activate.question"), I18N.getLabel("selector.activate.title",I18N.getLabel("module.user")))){
				@SuppressWarnings("unchecked")
				Iterator<Listitem> itemsIt = list.getGridList().getSelectedItems().iterator();
				while (itemsIt.hasNext()){
					User user = (User) itemsIt.next().getValue();
					getService().activate(CallContextHelper.getCallContext(), user);
					refreshAcegiUsers();
				}
				onFind(null);
			}
		}
	}

	public void onDeactivate(Event event) throws Exception{
		if (super.hasSelectedItem(list.getGridList())){
			if (MessageBox.question(I18N.getLabel("selector.deactivate.question"), I18N.getLabel("selector.deactivate.title",I18N.getLabel("module.user")))){
				@SuppressWarnings("unchecked")
				Iterator<Listitem> itemsIt = list.getGridList().getSelectedItems().iterator();
				while (itemsIt.hasNext()){
					User user = (User) itemsIt.next().getValue();
					getService().deactivate(CallContextHelper.getCallContext(), user);
					refreshAcegiUsers();
				}
				onFind(null);
			}
		}
	}

	private void refreshAcegiUsers(){
		AuthorizationService authService = BeanFactory.<AuthorizationService>getObject("userDetailsService");
		authService.refresh();
	}
}
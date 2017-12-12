package com.institucion.fm.security.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;

import com.institucion.fm.conf.CallContextHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.SecurityEJB;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.GroupCrud;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.PermissionDaDPanel;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.service.PermissionSecurityInterceptor;

public class GroupCrudComposer extends CrudComposer {
	
	private static Log log = LogFactory.getLog(GroupCrudComposer.class);
	
	private PermissionDaDPanel leftPanel;
	private PermissionDaDPanel rightPanel;
	private PanelCrud crud; 
	private SecurityEJB securityEjb = null;
	private Group group;
	List<Permission> perms = null;
	
	public GroupCrudComposer() {
		//log.debug("GroupCrudComposer,GroupCrudComposer");
	}
	
	private SecurityEJB getService()
	{
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}
	
	private GroupCrud getGroupCrud() {
		return (GroupCrud)(crud.getGridCrud());
	}
	
	private List<Permission> getPerms() {
		if (perms == null) {
			perms = this.getService().getPermissions(CallContextHelper.getCallContext());
		}
		return perms;
	}
	
	private List<Permission> getPermissionNotInListRight(List<Permission> allPerms ){
		List<Permission> permisosAAgregar = new ArrayList();
		
		if(rightPanel == null || rightPanel.getList()== null || rightPanel.getList().size()== 0 ){
			return allPerms;
		}
		for (Permission permission : allPerms) {
			if(!exist(permission))
				permisosAAgregar.add(permission);
		}
	return 	permisosAAgregar;
	} 
	
	private boolean exist(Permission per){
		
		for (Permission permission : rightPanel.getList()) {
			if(permission.getId().intValue() == per.getId().intValue() )
				return true;
			
		}
			
		return false;
	}
	
	
	public void onFind(Event evt) throws Exception {
		
		if(evt == null){
			List<Permission> allPerms = getPermissionNotInListRight(this.getPerms());
			if(allPerms != null && allPerms.size() >0){
				// No pide criterio, traigo todo
				leftPanel.addList(allPerms);
				return;	
			}
			return;
		}else{
			CriteriaClause criteria = leftPanel.getCriteriaFilters();
			List<Permission> allPerms = this.getPerms();
			// No pide criterio, traigo todo
			if (criteria.getPredicates().size() == 0) {
				leftPanel.setList(allPerms);
				return;
			}

			// Dado todos los perms, reviso
			String text = criteria.getPredicates().iterator().next().getExpression1();
			List<Permission> perms = new ArrayList<Permission>();
			Pattern p = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
			for (Permission permission : allPerms) {
				boolean found = false;
				// busca que no esta del otro lado
				for (Permission permission2 : this.rightPanel.getList()) {
					if (permission.getId().equals(permission2.getId())) {
						found = true;
						break;
					}
				}
				if (found) {
					// esta en el panel derecho, lo ignoro
					continue;
				}
				// busco el patron en la descripcion 
				String desc = I18N.getStringLabel(permission.getDescription());
				if(desc != null){
					Matcher m = p.matcher(desc);
					if (m.find()) {
						perms.add(permission);
					}	
				}
			}
			leftPanel.setList(perms);
		}
		
	}

	public void onCreate() throws Exception {
		Long id = (Long)Sessions.getCurrent().getAttribute(GroupCrud.SESSION_GROUP_ID);
		if (id == null) {
			//log.debug("new");
			this.crud.setTitle(I18N.getLabel("crud.group.newgroup.title"));
		} else {
			//log.debug("modify");
			this.fromModelToView(id);
			this.crud.setTitle(I18N.getLabel("crud.group.oldgroup.title", this.getGroupCrud().getName()));
		}
		onFind(null);
	}
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.getGroupCrud().setFocus();
	}

	private void fromModelToView(Long id) {
		this.group = this.getService().getGroupById(CallContextHelper.getCallContext(), id);
		this.getGroupCrud().setName(this.group.getName());
		this.getGroupCrud().setDescription(this.group.getDescription());
		this.getGroupCrud().setRole(this.group.getRole());
		this.rightPanel.setSet(this.group.getPermissions());
	}
	
	private void fromViewToModel(Group grp) {
		grp.setName(this.getGroupCrud().getName());
		grp.setDescription(this.getGroupCrud().getDescription());
		grp.setRole(this.getGroupCrud().getRole());
		Set<Long> ids = new HashSet<Long>();
		for (Iterator<?> it = this.rightPanel.getList().iterator(); it.hasNext();) {
			Permission item = (Permission) it.next();
			ids.add(item.getId());
		}
		grp.setPermissions(this.getService().getPermissionsById(CallContextHelper.getCallContext(), ids));
	}
	
	public void onSave(Event event) { //throws Exception {
		//log.debug("save");
		try {
			if (Sessions.getCurrent().getAttribute(GroupCrud.SESSION_GROUP_ID) == null) {
				// NEW
				Group group = new Group();
				this.fromViewToModel(group);
				if (group.getPermissions().isEmpty()){// this validation check if the group one or more permission, if not, a message display on the screen
					MessageBox.info(I18N.getLabel("crud.group.mustselect.permission"),I18N.getLabel("crud.group.permission"));
					return; 
				}
				this.getService().saveGroup(CallContextHelper.getCallContext(), group);
				super.gotoPage("/security/group-selector.zul");
			} else {
				// MODIFY
				this.fromViewToModel(this.group);
				if (group.getPermissions().isEmpty()){// this validation check if the group one or more permission, if not, a message display on the screen
					MessageBox.info(I18N.getLabel("crud.group.mustselect.permission"),I18N.getLabel("crud.group.permission"));
					return; 
				}
				this.getService().saveGroup(CallContextHelper.getCallContext(), this.group);
				super.gotoPage("/security/group-selector.zul");
			}
			refreshAcegiGroups();
			
			User user = getService().loadLazyUser(getService().getUserByName(CallContextHelper.getCallContext(), Session.getUsername()));
			if (haveUserLoguedGroupModified(user.getGroups(), group))
				getManager().refreshUserPermission();
	
		} catch (EJBException ex) {
			log.error("Mensaje: " + ex.getMessage() + "StackTrace: " + ex.getStackTrace());
			WebExceptionHandler.handleThis(ex);
		}
	}

	public boolean haveUserLoguedGroupModified(Set<Group> groups,Group group ){
		for (Group groupl : groups) {
			if(group != null && group.getId() != null){
				if(groupl.getId() != null && groupl.getId().intValue() == group.getId().intValue())
					return true;	
			}
			
		}
		return false;
	}
	
	public void onBack(Event event) throws Exception {
		//log.debug("back");
		super.gotoPage("/security/group-selector.zul");
	}
	
	public void onMoveRight(Event event) throws Exception {
		List<Permission> perms = leftPanel.getSelectedAndRemoveThem();
		rightPanel.addList(perms);
	}

	public void onMoveAllRight(Event event) throws Exception {
		leftPanel.selectAllItems();
		this.onMoveRight(event);
	}

	public void onMoveLeft(Event event) throws Exception {
		List<Permission> perms = rightPanel.getSelectedAndRemoveThem();
		leftPanel.addList(perms);
	}

	public void onMoveAllLeft(Event event) throws Exception {
		rightPanel.selectAllItems();
		this.onMoveLeft(event);
	}

	private void refreshAcegiGroups()
	{
		PermissionSecurityInterceptor permissionService = BeanFactory.<PermissionSecurityInterceptor>getObject("filterInvocationInterceptor");
		permissionService.refresh();
	}
}
package com.institucion.fm.security.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tabbox;

import com.institucion.fm.conf.CallContextHelper;
import com.institucion.fm.conf.InstanceConf;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.security.bz.SecurityEJB;
import com.institucion.fm.security.model.DraggableGroupList;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserCrud;
import com.institucion.fm.security.model.UserSecurity;
import com.institucion.fm.security.service.AuthorizationService;
import com.institucion.fm.util.Cipher;
import com.institucion.model.SucursalEnum;

public class UserCrudComposer extends CrudComposer {
	
	private static Log log = LogFactory.getLog(UserCrudComposer.class);
	
	private Listbox from;
	private Listbox to;
	private PanelCrud crud; 
	private SecurityEJB securityEjb = null;
	private User user;
	private Tabbox tabpanel;
//	private PromotionZoneUserDaDPanel promotionZoneLeftPanel;
//	private PromotionZoneUserDaDPanel promotionZoneRightPanel;
//	private SpecialityUserDaDPanel specialityLeftPanel;
//	private SpecialityUserDaDPanel specialityRightPanel;
//	private StructuresEJB structuresEJB = null;
	
//	private StructuresEJB getStructuresService(){
//		if (this.structuresEJB == null)
//			this.structuresEJB = BeanFactory.<StructuresEJB>getObject("fmEjbStructure");
//		return this.structuresEJB;
//	}
	
	private SecurityEJB getService(){
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}

	private DraggableGroupList getDraggableGroupList(Listbox lbox) {
		return (DraggableGroupList)lbox;
	}

	private UserCrud getUserCrud() {
		return (UserCrud)(crud.getGridCrud());
	}
	
	public void onCreate() {
		if (InstanceConf.getInstanceConfigurationProperty(InstanceConf.LENGTH_USERS_CODE) == null){
			MessageBox.validation(I18N.getLabel("crud.user.error",I18N.getLabel("crud.user.error.code")));
			super.gotoPage("/security/user-selector.zul");
		}
		Long id = (Long)Sessions.getCurrent().getAttribute(UserCrud.SESSION_USER_ID);
		if (id == null) {
			this.crud.setTitle(I18N.getLabel("crud.user.newuser.title"));
			this.getDraggableGroupList(to).setList(this.getService().getGroups(CallContextHelper.getCallContext()));
		} else {
			this.fromModelToView(id);
			this.crud.setTitle(I18N.getLabel("crud.user.olduser.title", this.getUserCrud().getName()));
		}
	}
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.getUserCrud().setFocus();
	}

	private void fromModelToView(Long id) {
		this.user = this.getService().getUserById(CallContextHelper.getCallContext(), id);
		//log.debug(user);
		this.getUserCrud().setName(this.user.getName());
		this.getUserCrud().setPassword("__________");
		this.getUserCrud().setFirstName(this.user.getFirstName());
		this.getUserCrud().setLastName(this.user.getLastName());
		this.getUserCrud().setTelephone(this.user.getTelephone());
		this.getUserCrud().setCellphone(this.user.getCellphone());
		this.getUserCrud().setAddress(this.user.getAddress());
		this.getUserCrud().setEmail(this.user.getEmail());
		this.getUserCrud().setState(this.user.getState());
		this.getUserCrud().setValidatePass(this.user.isValidatePass());
		
		this.getUserCrud().setSucursal(SucursalEnum.fromInt(this.user.getSucursal()));

		
		//log.debug("isUserWF [" + this.user.isUserWF() + "]");
		this.getUserCrud().setUserWF(this.user.isUserWF());

		this.getDraggableGroupList(from).setList(this.user.getGroups());
		this.getDraggableGroupList(to).setList(this.getService().getGroupsNotAssigned(CallContextHelper.getCallContext(), this.user));
	}
	
	private void fromViewToModel(User user) {
		user.setName(this.getUserCrud().getName());
		user.setFirstName(this.getUserCrud().getFirstName());
		user.setLastName(this.getUserCrud().getLastName());
		user.setTelephone(this.getUserCrud().getTelephone());
		user.setCellphone(this.getUserCrud().getCellphone());
		user.setEmail(this.getUserCrud().getEmail());
		user.setAddress(this.getUserCrud().getAddress());
		user.setConfigRegional(I18N.getConfigRegional());
		user.setState(this.getUserCrud().getState());
		
		if(this.getUserCrud().getSucursal().getSelectedItem() != null){
			user.setSucursal(((SucursalEnum)this.getUserCrud().getSucursal().getSelectedItem().getValue()).toInt());
		}
		user.setValidatePass(this.getUserCrud().getValidatePass().getItemAtIndex(0).isSelected());
		
		user.setUserWF(this.getUserCrud().getUserWF().getItemAtIndex(0).isSelected());
		
		if(!this.getUserCrud().getPassword().equals("__________")){
			user.setPassword(Cipher.encrypt(this.getUserCrud().getPassword()));
		}
		Set<Long> ids = new HashSet<Long>();
		for (Iterator<?> it = from.getItems().iterator(); it.hasNext();) {
			Listitem item = (Listitem) it.next();
			ids.add((Long)item.getValue());
		}
		user.setGroups(this.getService().getGroupsById(CallContextHelper.getCallContext(), ids));
		UserSecurity userSecurity = user.getUserSecurity() != null ? user.getUserSecurity() : new UserSecurity();
		user.setUserSecurity(userSecurity);
		userSecurity.setUser(user);
	}
	
	public void onSave(Event event) throws Exception {
		//log.debug("save");
		try{
			if (Sessions.getCurrent().getAttribute(UserCrud.SESSION_USER_ID) == null) {
				// NEW
				User user = new User();
				this.fromViewToModel(user);
				//this method add a new parameter, current password
				if (user.getGroups().isEmpty()){// this validation check if the user select one or more Groups, if not, a message display on the screen
					MessageBox.info(I18N.getLabel("crud.user.mustselect.group"), I18N.getLabel("crud.user.group"));
					return; 
				}
				
				if(user.getsucursalEnum() == null){
					MessageBox.info("Debe seleccionar una sucursal", "Error");
					return; 
				}
				this.getService().saveUser(CallContextHelper.getCallContext(), user, this.getUserCrud().getPassword());
				super.gotoPage("/security/user-selector.zul");
			} else {
				// MODIFY
				this.fromViewToModel(user);
				if (user.getGroups().isEmpty()){// this validation check if the user select one or more Groups, if not, a message display on the screen
					MessageBox.info(I18N.getLabel("crud.user.mustselect.group"), I18N.getLabel("crud.user.group"));
					return; 
				}
				if(!this.getUserCrud().getPassword().equals("__________")){//this validation check if the user change the password on the modification process
					this.getService().saveUser(CallContextHelper.getCallContext(), this.user,this.getUserCrud().getPassword());
				}else{
					this.getService().saveUser(CallContextHelper.getCallContext(), this.user);
				}
	
				if(user.getsucursalEnum() == null){
					MessageBox.info("Debe seleccionar una sucursal", "Error");
					return; 
				}
				if(user.getName().equals(Session.getUsername()))
					getManager().refreshUserPermission();
				
				super.gotoPage("/security/user-selector.zul");
			}
			refreshAcegiUsers();
		} catch (EJBException ex) {
			log.error("Mensaje: " + ex.getMessage() + "StackTrace: " + ex.getStackTrace());
			WebExceptionHandler.handleThis(ex);
		}
	}

	public void onBack(Event event) throws Exception {
		//log.debug("back");
		super.gotoPage("/security/user-selector.zul");
	}
	
	public void onDrop(DropEvent event) throws Exception {
		Component dragged = event.getDragged(); 
		Component target = event.getTarget();
		if (dragged instanceof Listitem) {
			target.getParent().insertBefore(dragged, target);
		} else {
			//log.debug("Sorry, no food here");
		}
	}

	public void onMoveTo(Event evt) throws Exception {
		//log.debug("onMoveTo(Event)");
		int idx = from.getSelectedIndex();
		if (idx == -1) {
			MessageBox.info(I18N.getLabel("crud.user.mustselect.group"), I18N.getLabel("crud.user.group"));
			return; 
		}
		while(idx > -1) {
		   Listitem item = from.getItemAtIndex(idx);
		   to.insertBefore(item, null);
		   from.removeChild(item);
		   item.setSelected(false);
		   idx = from.getSelectedIndex();
		}
	}
	
	public void onMoveAllTo(Event evt) throws Exception {
		//log.debug("onMoveAllTo(evt)");
		from.selectAll();
		this.onMoveTo(evt);
	}

	public void onMoveFrom(Event evt) throws Exception {
		//log.debug("onMoveFrom(Event)");
		int idx = to.getSelectedIndex();
		if (idx == -1) {
			MessageBox.info(I18N.getLabel("crud.user.mustselect.group"), I18N.getLabel("crud.user.group"));
			return; 
		}
		while(idx > -1) {
		   Listitem item = to.getItemAtIndex(idx);
		   from.insertBefore(item, null);
		   to.removeChild(item);
		   item.setSelected(false);
		   idx = to.getSelectedIndex();
		}
	}
	
	public void onMoveAllFrom(Event evt) throws Exception {
		//log.debug("onMoveAllFrom(evt)");
		to.selectAll();
		this.onMoveFrom(evt);
	}

	private void refreshAcegiUsers()
	{
		AuthorizationService authService = BeanFactory.<AuthorizationService>getObject("userDetailsService");
		authService.refresh();
	}
	
}
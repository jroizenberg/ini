package com.institucion.fm.security.bz.spi;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.institucion.fm.cobj.ValidateUserinRoleCObj;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.bz.CallContext;
import com.institucion.fm.security.bz.SecurityEJBLocal;
import com.institucion.fm.security.bz.SecurityEJBRemote;
import com.institucion.fm.security.dao.GroupDAO;
import com.institucion.fm.security.dao.PermissionDAO;
import com.institucion.fm.security.dao.UserDAO;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.RoleType;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserState;
import com.institucion.fm.util.Cipher;
import com.institucion.fm.wf.model.ApprovalType;

@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class, EJBExceptionHandler.class })

public class SecurityEJBImpl implements SecurityEJBRemote, SecurityEJBLocal{
	
	@Autowired
	private UserDAO userDao;
	@Autowired
	private GroupDAO groupDao;
	@Autowired
	private PermissionDAO permissionDao;
	@Autowired
	private PasswordValidation validation; 
	@Autowired
	private com.institucion.fm.wf.dao.UserDAO wfuserDao;

	public SecurityEJBImpl() { }


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Group> getGroups(CallContext ctx){
		return groupDao.findAll();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Permission> getPermissions(CallContext ctx){
		return permissionDao.findAll();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Group> getGroups(CallContext ctx, CriteriaClause criteria) {
		return groupDao.findByCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAllGroups(CallContext ctx, Collection<Long> groupIds) {
		for (Long id : groupIds) {
			//log.debug("id: " + id);
			Group group = groupDao.findById(id);
			//log.debug("group: " + group);
			groupDao.delete(group);
		
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveGroup(CallContext ctx, Group group) {
		groupDao.save(group);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Permission> getPermissionsById(CallContext ctx, Collection<Long> permissionsIds) {
		Set<Permission> permissions = new HashSet<Permission>();
		
		for (Long permissionId : permissionsIds) {
			permissions.add(permissionDao.findById(permissionId));
		}

		return permissions;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Group getGroupById(CallContext ctx, Long id) {
		return groupDao.findById(id);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Permission> getPermissionsNotAssigned(CallContext ctx, Group group) {
		List<Permission> all = permissionDao.findAll();
		Set<Permission> notAssigned = new HashSet<Permission>();
		for (Permission perm : all) {
			if (!group.getPermissions().contains(perm)) {
				notAssigned.add(perm);
			}
		}
		return notAssigned;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<User> getUsers(CallContext ctx, CriteriaClause criteria) {
		//log.debug("security.getUsers, criteria [" + criteria.getCriteria() + "] params [" + criteria.getCriteriaParameters() + "]");
		return userDao.findByCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(CallContext ctx, User user, String pass) {
		user.setPassword(Cipher.encrypt(pass));
		validation.validatePass(pass,user); //This method validate the password
		user.getUserSecurity().addOldPassword(user.getPassword()); // 	This method is used to record the last password entered
		
	
		user.getUserSecurity().setFirstLogin(!user.getUserSecurity().isFirstLogin());// this flag indicates that the user will have to change the actual password at first entry into the application.
		
		userDao.save(user);
		
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveUser(CallContext ctx, User user) {
		userDao.save(user);
		
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Group> getGroupsById(CallContext ctx, Collection<Long> groupIds) {
		Set<Group> groups = new HashSet<Group>();
		
		for (Long groupId : groupIds) {
			groups.add(groupDao.findById(groupId));
		}
		return groups;
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUserById(CallContext ctx, Long id) {
		
		return userDao.findById(id);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<User> getUsersById(CallContext callContext, Set<Long> ids) {
		Set<User> users = new HashSet<User>();
		
		for (Long userId : ids) {
			users.add(userDao.findById(userId));
		}
		return users;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUserByName(CallContext ctx, String name)	{
		return userDao.findByName(name);
	}
	
	
	public com.institucion.fm.security.model.User getSupervisorName(String userName){
		return userDao.getSupervisorName(userName);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public com.institucion.fm.wf.model.RoleType getWFRoleType(CallContext ctx)	{
		return wfuserDao.getRoleType(ctx.getUserID());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Group> getGroupsNotAssigned(CallContext ctx, User user) {
		List<Group> all = groupDao.findAll();
		Set<Group> notAssigned = new HashSet<Group>();
		for (Group group : all) {
			if (!user.getGroups().contains(group)) {
				notAssigned.add(group);
			}
		}
		return notAssigned;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<User> getUsers(CallContext ctx) {
			return userDao.findAll();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteAllUsers(CallContext ctx, Collection<Long> userIds) {
		for (Long id : userIds) {
			User user = userDao.findById(id);
			userDao.delete(user);
//			com.institucion.fm.wf.model.User wfUser = this.promotionEjb.getWfUserByUserIdSimple(ctx, user.getId());
//			HistoryTemp ht =promotionEjb.createHistoryTemp(wfUser, null, OperationHistoryTypeEnum.DELETE_USER);
//			historyTempDao.save(ht);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void activate(CallContext ctx, User user){
		user.setState(UserState.ACTIVE);
		user.getUserSecurity().setDenyAccessCount(0);
		userDao.update(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deactivate(CallContext ctx, User user){
		user.setState(UserState.INACTIVE);
		userDao.update(user);
//		com.institucion.fm.wf.model.User wfUser = this.promotionEjb.getWfUserByUserIdSimple(ctx, user.getId());
//		HistoryTemp ht =promotionEjb.createHistoryTemp(wfUser, null, OperationHistoryTypeEnum.DEACTIVATE_USER );
//		historyTempDao.save(ht);

	}
	

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User loadLazyUser (User user){
//		userDao.loadLazyAttribute(user.getSubdivision());
		userDao.loadLazyAttribute(user.getGroups());
//		userDao.loadLazyAttribute(user.getPromotionZones());
		return user;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean containsRole(CallContext ctx, String username, RoleType roleType) {
		return userDao.containsRole(username, roleType);
	}
	
	public Boolean validateFirstRoleUser(CallContext ctx, ApprovalType type){
//		User user = securityEJB.getUserByName(ctx, ctx.getUserID());
		return  wfuserDao.validateUserInRole(ctx.getUserID(),type);
		
		
		//FM - SYS - WF
		/*if (user != null) {
			user = securityEJB.loadLazyUser(user);
			for (Group group : user.getGroups()) {
				if(group.getRole() == RoleType.FMADMIN || group.getRole() == RoleType.SYSADMIN || group.getRole() == RoleType.WFMANAGER)
					return true;
			}
		}
		
		com.institucion.fm.wf.model.User wfUser = daoUserWF.findByName(ctx.getUserID());
		//USER Comun, sin SSFF
		// controlar que el usuario pertenezca al circuito de WorkFlow
		if (wfUser == null)
			throw new WorkFlowException("wf.notwfuser");
		
		
		
		daoUserWF.loadLazyAttribute(wfUser.getRole());
		daoUserWF.loadLazyAttribute(wfUser.getRole().getParent().getWfusers());
		
		
		//Siguiente al ROOT
		if(wfUser.getRole().getParent().getParent() == null)
			return true;
		
		
		//USER SSFF
		if(type == ApprovalType.SSFF)
			validateUserImmediately(wfUser, daoUserWF);
		
		return false;*/
	}
	
	public ValidateUserinRoleCObj validateWfUser(String userName)throws LocalizeValidationException {
		return wfuserDao.validateWfUser(userName);
	}
	
	@Override
	public Long getWfUserIdByRoleId(Long roleId) {
		return wfuserDao.getWfUserIdByRoleId(roleId);
	}
	


	public Long isInAdminRoles(String name){
		return wfuserDao.isInAdminRoles(name);
	}
	
	public ValidateUserinRoleCObj getWfUserAsCObj(String name){
		return wfuserDao.getWfUserAsCObj(name);
	}
	
	
	public void validateWorkflowUser(String userName){
		wfuserDao.validateWorkfowUser(userName);
		
	}
	
	public com.institucion.fm.wf.model.User getWfUserId(String userName){
		return wfuserDao.getWfUserId(userName);
	}
	
	public User getUserById(Long id){
		return userDao.findById(id);
	}
	
	

	
	public List<com.institucion.fm.security.model.User>getSupervisorDependants(String userName){
		return wfuserDao.getSupervisorDependants(userName);
	}
	
//	public String getSupervisorFullName(String userName){
//		
//	}
	
	@Override
	public List<Long> getPromotionZonesIds(String userName) {
		return userDao.getPromotionZonesIds(userName);
	}

	@Override
	public List<Long> getSpecialitiesId(String userName) {
		return userDao.getSpecialitiesId(userName);
	}
	
	@Override
	public User getSupervisorNameByActionId(Long id) {
		return userDao.getSupervisorNameByActionId(id);
	}
	
}
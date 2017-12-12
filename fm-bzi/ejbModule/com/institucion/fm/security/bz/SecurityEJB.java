package com.institucion.fm.security.bz;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.institucion.fm.cobj.ValidateUserinRoleCObj;
import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.RoleType;
import com.institucion.fm.security.model.User;
import com.institucion.fm.wf.model.ApprovalType;

public interface SecurityEJB
{
	// USER: SELECTOR + CRUD
	Set<Group> getGroupsById(CallContext ctx, Collection<Long> groupIds);
	List<User> getUsers(CallContext ctx);
	List<User> getUsers(CallContext ctx, CriteriaClause criteria);
	User getUserById(CallContext ctx, Long id);
	Set<User> getUsersById(CallContext callContext, Set<Long> ids);
	User getUserByName(CallContext ctx, String name);
	public com.institucion.fm.wf.model.RoleType getWFRoleType(CallContext ctx);
	void saveUser(CallContext ctx, User user, String pass);
	void saveUser(CallContext ctx, User user);
	void deleteAllUsers(CallContext ctx, Collection<Long> userIds);
	Set<Group> getGroupsNotAssigned(CallContext ctx, User user);
	void activate(CallContext ctx, User user);
	void deactivate(CallContext ctx, User user);
	boolean containsRole(CallContext ctx, String username, RoleType roleType);
//	public boolean getUserWFByUserName(CallContext ctx, String username);
	// GROUP: SELECTOR + CRUD
	List<Permission> getPermissions(CallContext ctx);
	List<Group> getGroups(CallContext ctx, CriteriaClause criteria);
	void deleteAllGroups(CallContext ctx, Collection<Long> groupIds);
	void saveGroup(CallContext ctx, Group group);
	Set<Permission> getPermissionsById(CallContext ctx, Collection<Long> permissionsIds);
	Group getGroupById(CallContext ctx, Long id);
	Set<Permission> getPermissionsNotAssigned(CallContext ctx, Group group);
	public User getUserById(Long id);
	public List<User>getSupervisorDependants(String userName);
	public ValidateUserinRoleCObj validateWfUser(String userName)throws LocalizeValidationException;
	
	// Comun
	List<Group> getGroups(CallContext ctx);
	User loadLazyUser (User user);
//	public Boolean existsUser(String name);
	public Long isInAdminRoles(String name);
	public Long getWfUserIdByRoleId(Long roleId);
	public ValidateUserinRoleCObj getWfUserAsCObj(String name);
	public List<Long> getPromotionZonesIds(String userName);

	public List<Long> getSpecialitiesId(String userName);
	public com.institucion.fm.security.model.User getSupervisorNameByActionId(Long id);
	public com.institucion.fm.security.model.User getSupervisorName(String userName);
	public Boolean validateFirstRoleUser(CallContext ctx, ApprovalType type);
	public void validateWorkflowUser(String userName);

}
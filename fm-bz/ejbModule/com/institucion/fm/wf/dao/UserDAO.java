package com.institucion.fm.wf.dao;

import java.sql.Connection;
import java.util.List;

import com.institucion.fm.cobj.UserClObj;
import com.institucion.fm.cobj.ValidateUserinRoleCObj;
import com.institucion.fm.cobj.WsPermissionCObj;
import com.institucion.fm.cobj.WsValidationCObj;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.LocalizeValidationException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.wf.model.ApprovalType;
import com.institucion.fm.wf.model.Role;
import com.institucion.fm.wf.model.RoleType;
import com.institucion.fm.wf.model.User;

public interface UserDAO {
	// override de estos metodos para evitar el parametro Class<T>
	List<User> findAll() throws DAOException;
	User findById(Long id) throws DAOException;
	public void loadLazyAttribute(Object attribute) throws DAOException;
	public void delete(User user) throws DAOException;
	User findWFUserByUserId(Long userid) throws DAOException;
	public List<User> findWFUserByUserId(List<Long> userIds) throws DAOException;
	public List<User> findWFUsersByWfUsersId(List<Integer> wfUsersIds) throws DAOException;
	public List<User> findByCriteria(CriteriaClause criteria) throws DAOException;

	public abstract User findByName(String username) throws DAOException;
	
	/**
	 * Busca un usuario ({@link User}}) por username
	 * @param username
	 * @return List de {@link User} con los siguientes atributos:
	 * usr.id, usr.user.id, usr.user.firstName, usr.user.lastName, usr.role
	 * @throws DAOException
	 */
	public User findSimpleUserByName(String username) throws DAOException;
	
	List<User> findUsersByTreeCode(CriteriaClause criteria)throws DAOException;
	List<User> findUserByCriteria(CriteriaClause criteria);
	List<Long> findUsersPerPromotionLine(Long promotionLineId)throws DAOException;
	
	public List<User> findAllSupervisors() throws DAOException;
	
	
	public List<User> findUsersByType(RoleType type) throws DAOException;
	
	public List<User> findUsersByRole(Role role) throws DAOException;
	
	/**
	 * Busca todos los supervisores
	 * 
	 * @return List de {@link User} solo con los siguientes atributos:
	 * 	usr.id, usr.user.id, usr.user.firstName, usr.user.lastName
	 * @throws DAOException
	 */
	public List<User> findAllSimpleSupervisors() throws DAOException;
//	public List<User> findSupervisorsByUser(String userName);
//	public List<User> findSupervisorsByPromotionLine (PromotionLine promotionLine) throws DAOException;
	public List<User> findUsersBySupervisor(User supervisor) throws DAOException;
	public List<User> findUsersBySupervisorsAndCriteria(List<User> supervisors,CriteriaClause criteria) throws DAOException;
	public List<User> findSimpleUsersBySupervisor(User supervisor) throws DAOException;
	public List<User> findAllSimpleUsers() throws DAOException;
	public List<Integer> findWfUsersIdByCriteriaFilters(String name, String description) throws DAOException;
	
	public abstract RoleType getRoleType(String username);
	
	public WsValidationCObj validateUserAndPassword(String name);
	public String getUserPassword(String name)throws Exception;
	public List<WsPermissionCObj> getPermissionsNames(String userName);

	public Boolean validateUserInRole(String name,ApprovalType taskType);
	public ValidateUserinRoleCObj validateWfUser(String userName)throws LocalizeValidationException;
	public Long getWfUserIdByRoleId(Long roleId);
	public User getWfUserByRoleIdAndNotPending(Long roleId) throws DAOException;
	public Long isInAdminRoles(String name);
	public ValidateUserinRoleCObj getWfUserAsCObj(String name);
	public void validateWorkfowUser(String userName);
	public List<com.institucion.fm.security.model.User>getSupervisorDependants(String userName);
	public User getWfUserId(String userName);
	public UserClObj getWfUserClObj(final String userName);
	public List<UserClObj> getAllWfUserClObj() ;
	public List<UserClObj> getUserClObjByHierachy(final String userRoleTreeCode) ;
	public Boolean validateWfuserWithoutException(String name);
	
	public com.institucion.fm.wf.model.User save(com.institucion.fm.wf.model.User userWf) throws DAOException;
	public com.institucion.fm.wf.model.User update(com.institucion.fm.wf.model.User userWf) throws DAOException;
	public void update(Connection cnx, com.institucion.fm.wf.model.User userWf) throws DAOException;
	public User getWfUserById(Long wfUserId) throws DAOException;
	
}
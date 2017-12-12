package com.institucion.fm.security.bz.spi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.fm.cobj.WsPermissionCObj;
import com.institucion.fm.cobj.WsValidationCObj;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.security.bz.CallContext;
import com.institucion.fm.security.bz.SecurityAAEJBLocal;
import com.institucion.fm.security.bz.SecurityAAEJBRemote;
import com.institucion.fm.security.dao.GroupDAO;
import com.institucion.fm.security.dao.PermissionDAO;
import com.institucion.fm.security.dao.UserDAO;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.PermissionType;
import com.institucion.fm.security.model.User;
import com.institucion.fm.security.model.UserSecurity;
import com.institucion.fm.security.model.UserState;

@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,
	EJBExceptionHandler.class })
	/*
	 * Este servicio interactua con el framework Acegi para aplicar la seguridad de
	 * autenticacion y autorizacion en menus.
	 */
	public class SecurityAAEJBImpl implements SecurityAAEJBLocal,
	SecurityAAEJBRemote {
	@Autowired
	private UserDAO userDao;
	@Autowired
	private GroupDAO groupDao;
	@Autowired
	private PermissionDAO permissionDao;
	@Autowired
	private com.institucion.fm.wf.dao.UserDAO wfuserDao;
	@Autowired
	AbstractSessionFactoryBean session;
		
	private static Log log = LogFactory.getLog(SecurityAAEJBImpl.class);

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<User> getUsers() {
		List<User> users = userDao.findAll();
		for (User user : users) {
			userDao.loadLazyAttribute(user.getGroups());
		}
		return users;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<User> getUsersWithOutCollections() {
		List<User> users = userDao.getUsersWithOutCollections();
		return users;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUser(String username) {
		User user = userDao.findByName(username);
		return user;
	}
	
	public User getUserWithOutCollections(String userName){
		return userDao.getUserWithOutCollections(userName);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Permission> getUserPermissions(String username) {
		User user = userDao.findByName(username);
		if (user != null) {
			userDao.loadLazyAttribute(user.getGroups());
			log.debug("cargo los permisos de cada grupo");
			for (Group group : user.getGroups()) {
				groupDao.loadLazyAttribute(group.getPermissions());
			}
			return user.getPermissions();
		}
		return null;
	}
	
	
	public List<String>getUserPermissionsTokens(String userName){
		return userDao.getUserPermissionsTokens(userName);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Permission> getPermissions() {
		List<Permission> perms = permissionDao.findAll();
		log.debug("cargo los grupos de cada permiso");
		for (Permission permission : perms) {
			permissionDao.loadLazyAttribute(permission.getGroups());
		}
		return perms;
	}
	
	public List<String>getAllPermissionTokens(){
		return permissionDao.getAllPermissionTokens();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Group> getGroups() {
		return groupDao.findAll();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void block(User user) {
		user.setState(UserState.BLOCKED);
		userDao.update(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addDenyAccess(User user) {
		UserSecurity userSecurity = user.getUserSecurity();
		int newDenyAccessCount = userSecurity.getDenyAccessCount() + 1;
		userSecurity.setDenyAccessCount(newDenyAccessCount);
		userDao.update(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cleanDenyAccessCount(User user) {
		UserSecurity userSecurity = user.getUserSecurity();
		userSecurity.setDenyAccessCount(0);
		userDao.update(user);
	}
	
	public void setAccesCountToZero(User user){
		userDao.setAccesCountToZero(user);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public User getUserWs(String username) {
		//
		User user = userDao.findByName(username);

		if (user != null) {
			userDao.loadLazyAttribute(user.getGroups());
			log.debug("cargo los permisos de cada grupo");
			for (Group group : user.getGroups()) {
				groupDao.loadLazyAttribute(group.getPermissions());
			}
		}else{
			throw new RuntimeException("error de usuario");
		}

		return user;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Set<Permission> getUserTXPermissions(String username) {
		User user = userDao.findByName(username);
		if (user != null) {
			userDao.loadLazyAttribute(user.getGroups());
			for (Group group : user.getGroups()) {
				groupDao.loadLazyAttribute(group.getPermissions());
			}
			return user.getPermissionsByType(PermissionType.TX);
		}
		return null;
	}
	
	public List<String>getUserTxPermissionsTokens(String userName){
		return userDao.getUserTxPermissionsTokens(userName);
	}

	


	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public User getUserWithGroups(String userID) {
		User user = userDao.getUserWithOutCollections(userID);
		user.setGroups( new HashSet<Group>(groupDao.getGroupsByUser(user.getId())) );
		
		return user;
	}

	public User loadLazy(User user) {
//		promotionalActionDAO.loadLazyAttribute(user.getGroups());
		return user;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public com.institucion.fm.wf.model.User getWfUserByUserIdSimple(CallContext ctx, String username) {
		com.institucion.fm.wf.model.User user = wfuserDao.findSimpleUserByName(username);
		if(user!=null){
			wfuserDao.loadLazyAttribute(user.getRole());
		}
		return user;
	}


	@Override
	public List<com.institucion.fm.wf.model.User> getSupervisors(CallContext ctx) {
		return wfuserDao.findAllSupervisors();
	}

	@Override
	public List<com.institucion.fm.wf.model.User> getAllUsers(CallContext ctx) {
		return wfuserDao.findAll();
	}

	
	public Map<String, Integer> generateChart(CallContext callContext, Map<String, String> filters) throws SQLException {
		Map<String,Integer> syncroList= null;
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
//			Cycle cycle = null;
			syncroList = null; 
		}finally{
			if(cnx != null)
				cnx.close();	
		}
		return syncroList;
	}
	
	@Override
	public WsValidationCObj validateUserAndPassword(String name)
			throws Exception {
			
		return wfuserDao.validateUserAndPassword(name);
	}
	
	@Override
	public String getUserPassword(String name) throws Exception{
		return wfuserDao.getUserPassword(name);
	}
	
	@Override
	public List<WsPermissionCObj> getPermissionsNames(String userName) {
		// TODO Auto-generated method stub
		return wfuserDao.getPermissionsNames(userName);
	}
	
	@Override
	public Boolean isFirstLogin(User user) {
		return userDao.isFirstLogin(user);
	}
	
	
	@Override
	public void handleWrongPassword(User u,Integer denyAccessLimit) {
	
		userDao.handleWrongPassword(u, denyAccessLimit);
	}
	

}
	
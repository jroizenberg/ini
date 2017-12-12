package com.institucion.fm.security.bz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.institucion.fm.cobj.WsPermissionCObj;
import com.institucion.fm.cobj.WsValidationCObj;
import com.institucion.fm.security.model.Group;
import com.institucion.fm.security.model.Permission;
import com.institucion.fm.security.model.User;

/**
 * Este servicio interactua con el framework Acegi para aplicar la seguridad
 * de autenticacion y autorizacion en menus. 
 */
public interface SecurityAAEJB {
	List<User> getUsers();
	public List<User> getUsersWithOutCollections();
	public List<com.institucion.fm.wf.model.User> getAllUsers(CallContext ctx);	
	User getUser(String username);
	List<Permission> getPermissions();
	List<Group> getGroups();
	Set<Permission> getUserPermissions(String username);
	Set<Permission> getUserTXPermissions(String username);
	void block(User user);
	void addDenyAccess(User user);
	void cleanDenyAccessCount(User user);
	
	// para WebServices
	User getUserWs(String username);
	
	public User loadLazy(User user);
	public com.institucion.fm.wf.model.User getWfUserByUserIdSimple(CallContext ctx, String username);
	public User getUserWithGroups(String userID);
	List<com.institucion.fm.wf.model.User> getSupervisors(CallContext ctx);
	Map<String, Integer> generateChart(CallContext callContext,
			Map<String, String> filters) throws SQLException;
	
	public WsValidationCObj validateUserAndPassword(String name)throws Exception;
	public String getUserPassword(String name)throws Exception;
	public List<WsPermissionCObj>getPermissionsNames(String userName);
	public List<String>getUserTxPermissionsTokens(String userName);
	public List<String>getUserPermissionsTokens(String userName);
	public List<String>getAllPermissionTokens();
	public User getUserWithOutCollections(String userName);
	public void setAccesCountToZero(User user);
	public Boolean isFirstLogin(User user);
	public void handleWrongPassword(User u,Integer denyAccessLimit);
	
}
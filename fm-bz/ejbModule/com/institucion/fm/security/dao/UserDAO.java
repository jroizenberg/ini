package com.institucion.fm.security.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.*;

public interface UserDAO {
	// override de estos metodos para evitar el parametro Class<T>
	List<User> findAll() throws DAOException;
	public User save(User c) throws DAOException;
	public User update(User c) throws DAOException;
	User findById(Long id) throws DAOException;
	void loadLazyAttribute(Object attribute) throws DAOException;
	void delete(User user) throws DAOException;
	public User findByName(Integer userID) throws DAOException;	
	// Metodo propio
	User findByName(String name) throws DAOException;
	List<User> findByCriteria(CriteriaClause criteria) throws DAOException;

	public boolean containsRole(String username, RoleType roleType);
	public List<String>getUserTxPermissionsTokens(String userName);
	public User getSupervisorName(String userName);
	
	public List<Long> getPromotionZonesIds(String userName);

	public List<Long> getSpecialitiesId(String userName);
	public List<String>getUserPermissionsTokens(String userName);
	public User getUserWithOutCollections(String userName);
	public void setAccesCountToZero(User user);
	public Boolean isFirstLogin(User user);
	public void handleWrongPassword(User u,Integer debyAccessLimit); 
	public com.institucion.fm.security.model.User getSupervisorNameByActionId(Long id);
	
	List<User> getUsersWithOutCollections();
}
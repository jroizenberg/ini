package com.institucion.fm.wf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.wf.model.Role;

public interface RoleDAO {
	// override de estos metodos para evitar el parametro Class<T>
	List<Role> findAll() throws DAOException;
	Role findById(Long id) throws DAOException;
	public Role save(Role c) throws DAOException;
	public Role update(Role c) throws DAOException;
	public void update(Connection cnx, Role c) throws DAOException;
	public void delete(Role c) throws DAOException;
	void loadLazyAttribute(Object attribute) throws DAOException;
	
	// Metodo propio
	Role findByName(String name) throws DAOException;
	public ResultSet findRoles(Connection cnx) throws DAOException, SQLException;
	Role findRoot() throws DAOException;
//	public List<RoleModel> getRoleModel(Connection cnx) throws DAOException, SQLException;
	public ResultSet findWFUser(Connection cnx, Long roleId) throws DAOException,SQLException;
}

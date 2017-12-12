package com.institucion.fm.security.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.security.model.*;

public interface PermissionDAO {
	// override de estos metodos para evitar el parametro Class<T>
	List<Permission> findAll() throws DAOException;
	public Permission save(Permission c) throws DAOException;
	Permission findById(Long id) throws DAOException;
	void loadLazyAttribute(Object attribute) throws DAOException;
	public List<String>getAllPermissionTokens();
	
}
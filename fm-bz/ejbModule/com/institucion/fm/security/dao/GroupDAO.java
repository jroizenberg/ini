package com.institucion.fm.security.dao;

import java.util.Collection;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.Group;

public interface GroupDAO {
	// override de estos metodos para evitar el parámetro Class<T>
	List<Group> findAll() throws DAOException;
	Group findById(Long id) throws DAOException;
	Group save(Group c) throws DAOException;
	void delete(Group c) throws DAOException;
	void loadLazyAttribute(Object attribute) throws DAOException;

	
	// Método propio
	Group findByName(String name) throws DAOException;
	List<Group> findByCriteria(CriteriaClause criteria) throws DAOException;
	void deleteAll(Collection<Group> groups);
	List<Group> getGroupsByUser(Long id);
}
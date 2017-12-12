package com.institucion.fm.filteradv.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.User;

public interface CriteriaClauseDAO
{
	// override de estos metodos para evitar el parametro Class<T>
	List<CriteriaClause> findAll() throws DAOException;
	public CriteriaClause save(CriteriaClause c) throws DAOException;

	public CriteriaClause findByName(String name, User user) throws DAOException;

	public List<CriteriaClause> findByPage(String page, User user) throws DAOException;

	public void delete(CriteriaClause clause);
	
	public List<CriteriaClause> findByPage(String page, String userName) throws DAOException;
}
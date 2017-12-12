package com.institucion.fm.filteradv.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.Predicate;

public interface PredicateDAO
{
	// override de estos metodos para evitar el parámetro Class<T>
	List<Predicate> findAll() throws DAOException;
	public Predicate save(Predicate c) throws DAOException;
}
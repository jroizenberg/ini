package com.institucion.fm.bb.dao;

import java.util.List;

import com.institucion.fm.bb.model.*;
import com.institucion.fm.conf.exception.DAOException;

public interface EmailDAO
{
	public abstract void delete(Email email) throws DAOException;
	public abstract List<Email> findAll() throws DAOException;
	public abstract Email findById(Long id) throws DAOException;	
	public abstract List<Email> findByCriteria(String criteria) throws DAOException;
	public abstract Email save(Email p) throws DAOException;
	public abstract void saveIfNotExists(Email email) throws DAOException;
	public abstract Email getNextPendingEmail() throws DAOException;
}
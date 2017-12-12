package com.institucion.fm.confsys.dao;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.confsys.model.Flag;

public interface FlagDAO
{
	public abstract Flag save(Flag c) throws DAOException;
	public abstract void delete(Flag c) throws DAOException;
	public abstract Flag getFlag() throws DAOException;
}
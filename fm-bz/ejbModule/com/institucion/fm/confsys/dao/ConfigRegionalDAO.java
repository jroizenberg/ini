package com.institucion.fm.confsys.dao;

import java.util.*;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.confsys.model.ConfigRegional;


public interface ConfigRegionalDAO
{
	// override de estos metodos para evitar el parámetro Class<T>
	List<ConfigRegional> findAll() throws DAOException;
	public ConfigRegional save(ConfigRegional c) throws DAOException;
	public ConfigRegional update(ConfigRegional c) throws DAOException;
	public void delete(ConfigRegional s) throws DAOException;
	ConfigRegional findById(Long id) throws DAOException;
	/**
	 * Traer el único registro ConfigRegional para obtener el objeto Locale.
	 * Si no existe devuelve el Locale.SPANISH
	 */
	public abstract Locale getLocale();
}
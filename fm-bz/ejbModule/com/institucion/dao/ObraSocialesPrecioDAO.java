package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.ObraSocialesPrecio;

public interface ObraSocialesPrecioDAO{

	public ObraSocialesPrecio create(ObraSocialesPrecio c) throws DAOException;
	public void delete(ObraSocialesPrecio c) throws DAOException;
	public ObraSocialesPrecio save(ObraSocialesPrecio c) throws DAOException;
	public ObraSocialesPrecio findById(Long id) throws DAOException;
	
	public List<ObraSocialesPrecio> findAll() throws DAOException;
	public List<ObraSocialesPrecio> findAllByObraSocial(Long idObraSocial) throws DAOException;
	
}
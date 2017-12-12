package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Actividad;
import com.institucion.model.ObraSocial;

public interface ObraSocialDAO{

	public ObraSocial create(ObraSocial c) throws DAOException;
	public void delete(ObraSocial c) throws DAOException;
	public ObraSocial save(ObraSocial c) throws DAOException;
	public ObraSocial findById(Long id) throws DAOException;
	
	public List<ObraSocial> findAll() throws DAOException;
	public List<ObraSocial> findAllByActividadDisponible(Actividad act) throws DAOException;
	public List<ObraSocial> findAllConJdbc(String filters) throws DAOException;
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException;

}
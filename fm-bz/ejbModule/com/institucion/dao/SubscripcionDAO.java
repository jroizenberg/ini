package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Subscripcion;

public interface SubscripcionDAO{

	//public Serializable create(Subscripcion c) throws DAOException;
	public void delete(Subscripcion c) throws DAOException;
	public Subscripcion save(Subscripcion c) throws DAOException;
	public Subscripcion findById(Long id) throws DAOException;
	
	public List<Subscripcion> findAll() throws DAOException;
	public List<Subscripcion> findAllByClient(Long idClient) throws DAOException;
	public List<Subscripcion> findAllConJdbc(String filters) throws DAOException;
	public Subscripcion create(Subscripcion c) throws DAOException;
	public Long createSubs(Subscripcion c) throws DAOException;
//	public List<SubscripcionDeClasesPorActividad> findAllSubscripcionDeClasesPorActividad(Long idSubscripcion, Long idCurso) throws DAOException;

}
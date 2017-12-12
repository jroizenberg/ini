package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Inscripcion;

public interface InscripcionDAO{

	public Inscripcion create(Inscripcion c) throws DAOException;
	public void delete(Inscripcion c) throws DAOException;
	public Inscripcion save(Inscripcion c) throws DAOException;
	public Inscripcion findById(Long id) throws DAOException;
	
	public List<Inscripcion> findAll() throws DAOException;
	public List<Inscripcion> findByIdClienteAndAnio(Long idCliente) throws DAOException;

}
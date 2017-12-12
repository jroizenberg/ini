package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.PagosPorSubscripcion;

public interface PagosPorSubscripcionDAO{

	public PagosPorSubscripcion create(PagosPorSubscripcion c) throws DAOException;
	public void delete(PagosPorSubscripcion c) throws DAOException;
	public PagosPorSubscripcion save(PagosPorSubscripcion c) throws DAOException;
	public void save(List<PagosPorSubscripcion> listPagos) throws DAOException;
	public PagosPorSubscripcion findById(Long id) throws DAOException;
	
	public List<PagosPorSubscripcion> findAll() throws DAOException;

}
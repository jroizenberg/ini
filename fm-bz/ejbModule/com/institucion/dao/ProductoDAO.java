package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Producto;

public interface ProductoDAO{

	public Producto create(Producto c) throws DAOException;
	public void delete(Producto c) throws DAOException;
	public Producto save(Producto c) throws DAOException;
	public Producto findById(Long id) throws DAOException;
	
	public List<Producto> findAll() throws DAOException;
	public List<Producto> findAllConJdbc(String filters) throws DAOException;

	
}
package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Gastos;

public interface GastosDAO{

	public Gastos create(Gastos c) throws DAOException;

	public void delete(Gastos c) throws DAOException;
	public Gastos save(Gastos c) throws DAOException;
	public Gastos findById(Long id) throws DAOException;
	
	public List<Gastos> findAll() throws DAOException;
	public List<Gastos> findAllConJdbc(String filters) throws DAOException;

}
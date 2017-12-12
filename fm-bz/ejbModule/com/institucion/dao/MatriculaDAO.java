package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Matricula;

public interface MatriculaDAO{

	public Matricula create(Matricula c) throws DAOException;
	public void delete(Matricula c) throws DAOException;
	public Matricula save(Matricula c) throws DAOException;
	public Matricula findById(Long id) throws DAOException;
	
	public List<Matricula> findAll() throws DAOException;
	public boolean findAllIsImprimible() throws DAOException;
	public void saveImprimible(boolean bool) throws DAOException;
		
}
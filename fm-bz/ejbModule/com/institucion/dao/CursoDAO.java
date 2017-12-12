package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Curso;
import com.institucion.model.SubsYCurso;

public interface CursoDAO{

	public Curso create(Curso c) throws DAOException;
	public void delete(Curso c) throws DAOException;
	public Curso save(Curso c) throws DAOException;
	public Curso findById(Long id) throws DAOException;
	
	public List<Curso> findAll() throws DAOException;
	public List<Curso> findAllConJdbc(String filters) throws DAOException;
	public List<Curso> findAll2() throws DAOException;
	public List<Curso> findAll2WithDisponible(boolean esMaipu, boolean esCentro) throws DAOException;
	public List<SubsYCurso> findAllConJdbcSubsYCurso(String filters) throws DAOException;
	
}
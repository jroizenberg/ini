package com.institucion.bz;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Actividad;
import com.institucion.model.Curso;
import com.institucion.model.Matricula;
import com.institucion.model.SubsYCurso;

public interface CursoEJB{
	
	public void create(Curso c) throws DAOException;
	public void delete(Curso c) throws DAOException;
	public void save(Curso c) throws DAOException;
	public Curso findById(Long id) throws DAOException;
	public List<Curso> findAll2() throws DAOException;
	
	public List<Curso> findAll() throws DAOException;
	public List<Curso> findAll2WithDisponible(boolean esMaipu, boolean esCentro) throws DAOException;
	
	public void create(Actividad c) throws DAOException;
	public void delete(Actividad c) throws DAOException;
	public void save(Actividad c) throws DAOException;
	public Actividad findByIdActividad(Long id) throws DAOException;
	
	public List<Actividad> findAllActividad() throws DAOException;
	public List<Actividad> findAllActividadQueTomenLista() throws DAOException;
	public List<Actividad> findAllActividadQueConObraSocial() throws DAOException;

	public List<Actividad> findAActividadByIdTipoCurso(Long idTipoActividad) throws DAOException;
	
	public List<Curso> findAllConJdbc(String filters) throws DAOException;
	public List<Actividad> findAllActividadQueUsaCarnet(Integer idTipoCurso) throws DAOException;
	public List<SubsYCurso> findAllConJdbcSubsYCurso(String filters) throws DAOException;
	public Matricula findMatricula() throws DAOException;
	public void saveMatricula(Matricula mat) throws DAOException;	
	public void saveImprimible(Boolean bool) throws DAOException;	

	public List<Actividad> findAllActividadParaPagoSueldos() throws DAOException;
	public boolean findImprimible() throws DAOException;
}
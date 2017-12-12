package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Actividad;

public interface ActividadDAO{

	public Actividad create(Actividad c) throws DAOException;
	public void delete(Actividad c) throws DAOException;
	public Actividad save(Actividad c) throws DAOException;
	public Actividad findByIdActividad(Long id) throws DAOException;
	
	public List<Actividad> findAllActividad() throws DAOException;
	public List<Actividad> findAllActividadQueTomenLista() throws DAOException;

	public List<Actividad> findAllActividadByTipoActividad(Long idTipoActividad) throws DAOException;
	public List<Actividad> findAllActividadQueConObraSocial() throws DAOException;
	public List<Actividad> findAllActividadQueUsaCarnet(Integer idTipoCurso) throws DAOException;
	public List<Actividad> findAllActividadParaPagoSueldos() throws DAOException;

}
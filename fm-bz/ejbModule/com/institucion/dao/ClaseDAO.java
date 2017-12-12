package com.institucion.dao;

import java.util.Date;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.Quincena;

public interface ClaseDAO{

	public Clase create(Clase c) throws DAOException;
	public void delete(Clase c) throws DAOException;
	public Clase save(Clase c) throws DAOException;
	public Clase findById(Long id) throws DAOException;
	public List<Clase> findAllConActividadTomaLista() throws DAOException;	
	public List<Clase> findAll() throws DAOException;
	public List<Clase> findAllByIdActividad(Long idActividad, int day) throws DAOException;
	public List<Clase> findAllConJdbc(String filters) throws DAOException;
	public List<ClienteListaEncontradoEnPileta> findAllByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public ClaseConListaAlumnos findAllClaseConListaAlumnosByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public ClaseConListaAlumnosHistorico findAllClaseConListaAlumnosByClaseAndFechaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;

	public List<ClienteNoEncontradoEnPileta> findAllByClaseAndFechaClienteNoEncontradoEnPileta(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public void loadLazyAttribute(Object attribute) throws DAOException;
	public void save(ClaseConListaAlumnos c) throws DAOException;
	public List<ClienteListaEncontradoEnPiletaHistorico> findAllByClaseAndFechaHistorico(Long idListaClase,Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public List<ClienteNoEncontradoEnPiletaHistorico> findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	  public abstract List<Clase> findAllConActividadTomaListaDelDia(boolean isFromReports)
			    throws DAOException;

	public void delete(ClaseConListaAlumnos c) throws DAOException;
	public List<Quincena> findAllConActividadTomaListaDelDiaNombreCurso() throws DAOException;

}
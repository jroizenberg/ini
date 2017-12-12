package com.institucion.bz;

import java.util.Date;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
import com.institucion.model.Quincena;

public interface ClaseEJB{
	
	public void create(Clase c) throws DAOException;
	public void delete(Clase c) throws DAOException;
	public void save(Clase c) throws DAOException;
	public Clase findById(Long id) throws DAOException;
	
	public List<Clase> findAll() throws DAOException;
	public List<Clase> findAllByActividad(Long idActividad, int day) throws DAOException;
	public List<ClienteListaEncontradoEnPileta> findAllByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public ClaseConListaAlumnos findAllClaseConListaAlumnosByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public ClaseConListaAlumnosHistorico findAllClaseConListaAlumnosByClaseAndFechaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public List<ClienteNoEncontradoEnPileta> findAllByClaseAndFechaClienteNoEncontradoEnPileta(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;

	public List<Clase> findAllConJdbc(String filters) throws DAOException;
	public void save(ClaseConListaAlumnos  c) throws DAOException;
	public List<ClienteListaEncontradoEnPiletaHistorico> findAllByClaseAndFechaHistorico(Long idListaClase, Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public List<ClienteNoEncontradoEnPiletaHistorico> findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException;
	public List<Clase> findAllConActividadTomaLista() throws DAOException;
	public List<Clase> findAllConActividadTomaListaDelDia(boolean isFromReports) throws DAOException;
	public List<Quincena> findAllConActividadTomaListaDelDiaNombreCurso() throws DAOException;

	// Ingreso a Clase
	public void create(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public void delete(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public void save(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public Boolean existeClienteEnClaseSinFecha(Cliente cli, Clase cla) throws DAOException;
	public List<IngresoAClasesSinFechasAlumnos> findAllByClienteyClase(Cliente cli, Clase clase) throws DAOException;

	
	public List<IngresoAClasesSinFechasAlumnos> findAllByCliente(Cliente cli) throws DAOException;
	public List<IngresoAClasesSinFechasAlumnos> findAllByClase(Clase clase) throws DAOException;
}
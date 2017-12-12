package com.institucion.bz;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.security.model.User;
import com.institucion.model.Inscripcion;
import com.institucion.model.Matricula;
import com.institucion.model.ObraSocial;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.Quincena;
import com.institucion.model.Subscripcion;

public interface InscripcionEJB{
	
	public void create(ObraSocialesPrecio c) throws DAOException;
	public void create(Inscripcion c) throws DAOException;

	public void delete(ObraSocialesPrecio c) throws DAOException;
	public void save(ObraSocialesPrecio c) throws DAOException;
	public ObraSocialesPrecio findObraSocialesPrecioById(Long id) throws DAOException;
	
	public List<ObraSocialesPrecio> findAllObraSocialesPrecio() throws DAOException;	
	public ObraSocial findByIdObraSocial(Long id) throws DAOException;
	
	public List<ObraSocial> findAllObraSociales() throws DAOException;
	
	public void create(Subscripcion c) throws DAOException;
	public Long createSubs(Subscripcion c) throws DAOException;

	public void delete(Subscripcion c) throws DAOException;
	public void save(Subscripcion c) throws DAOException;
	
	public void save(Inscripcion c) throws DAOException;
	
	public Subscripcion findSubscripcionById(Long id) throws DAOException;
	public List<Subscripcion> findAllSubscripciones() throws DAOException;
	public List<Subscripcion> findAllSubscripcionesByClient(Long idClient) throws DAOException;
	
	public List<Matricula> findAllMatriculas() throws DAOException;
	List<ObraSocialesPrecio> findObraSocialesByObraSocialPrecioById(Long id)throws DAOException;
	public List<Subscripcion> findAllConJdbc(String filters) throws DAOException;
	public List<Inscripcion> findByIdClienteAndAnio(Long idCliente) throws DAOException;
	public void delete(Inscripcion c) throws DAOException;

	public void actualizarEstadosDeJob();
	public void actualizarEstadosSubsYClientesDeJob();
	public List<Quincena> obtenerQuincenalesList();
	
	public User findByName(Integer userID) throws DAOException;
}
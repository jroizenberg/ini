package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;

public interface IngresoAClasesSinFechasAlumnosDAO{

	public IngresoAClasesSinFechasAlumnos create(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public void delete(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public IngresoAClasesSinFechasAlumnos save(IngresoAClasesSinFechasAlumnos c) throws DAOException;
	public IngresoAClasesSinFechasAlumnos findByIdActividad(Long id) throws DAOException;
	
	public List<IngresoAClasesSinFechasAlumnos> findAllByCliente(Cliente cli) throws DAOException;
	public List<IngresoAClasesSinFechasAlumnos> findAllByClase(Clase clase) throws DAOException;
	public Boolean existeClienteEnClaseSinFecha(Clase clase, Cliente cliente) throws DAOException;
	public List<IngresoAClasesSinFechasAlumnos> findAllByClienteClase(Clase clase, Cliente cliente) throws DAOException;
	public void eliminarTodosLosQueLaClaseEsNula() throws DAOException;

	
}
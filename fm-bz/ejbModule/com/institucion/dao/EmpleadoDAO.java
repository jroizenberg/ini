package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Actividad;
import com.institucion.model.Empleado;

public interface EmpleadoDAO{

	public Empleado create(Empleado c) throws DAOException;
	public void delete(Empleado c) throws DAOException;
	public Empleado save(Empleado c) throws DAOException;
	public Empleado findById(Long id) throws DAOException;
	public void loadLazy(Empleado e) throws DAOException;

	public List<Empleado> findAll() throws DAOException;
	public List<Empleado> findAllByActividadDisponible(Actividad act) throws DAOException;
	public List<Empleado> findAllConJdbc(String filters) throws DAOException;
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException;

}
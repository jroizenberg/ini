package com.institucion.bz;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Actividad;
import com.institucion.model.Empleado;

public interface EmpleadoEJB{
	
	public void create(Empleado c) throws DAOException;
	public void delete(Empleado c) throws DAOException;
	public void save(Empleado c) throws DAOException;
	public Empleado findById(Long id) throws DAOException;
	
	public List<Empleado> findAll() throws DAOException;
	public List<Empleado> findAllConJdbc(String filters) throws DAOException;
	public List<Empleado> findAllByActividadDisponible(Actividad act) throws DAOException;
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException;
	public Empleado loadLazy(Empleado emp);

}
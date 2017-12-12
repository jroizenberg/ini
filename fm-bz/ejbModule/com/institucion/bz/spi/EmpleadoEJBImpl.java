package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.EmpleadoEJBLocal;
import com.institucion.bz.EmpleadoEJBRemote;
import com.institucion.dao.EmpleadoDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Actividad;
import com.institucion.model.Empleado;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class EmpleadoEJBImpl implements EmpleadoEJBRemote,EmpleadoEJBLocal {

	private static Log log = LogFactory.getLog(EmpleadoEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private EmpleadoDAO obraSocialDao = null;
	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public EmpleadoEJBImpl() {	}

	
	@Override
	public void create(Empleado c) throws DAOException{
		obraSocialDao.create(c);
	}
	
	@Override
	public void delete(Empleado c) throws DAOException{
		obraSocialDao.delete(c);
	}
	
	@Override
	public void save(Empleado c) throws DAOException{
		obraSocialDao.save(c);	
	}
	
	@Override
	public Empleado findById(Long id) throws DAOException{
        return obraSocialDao.findById(id);
    }
	
	@Override
	public List<Empleado> findAll() throws DAOException{
        return obraSocialDao.findAll();
	}

	@Override
	public List<Empleado> findAllByActividadDisponible(Actividad act) throws DAOException{
        return obraSocialDao.findAllByActividadDisponible(act);
	}

	@Override
	public List<Empleado> findAllConJdbc(String filters) throws DAOException{
        return obraSocialDao.findAllConJdbc(filters);
	}

	@Override
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException{
        return obraSocialDao.findActividadesAllByActividadDisponible();
	}
	
	@Override
	public Empleado loadLazy(Empleado emp){
		obraSocialDao.loadLazy(emp);
		return emp;
	}
}
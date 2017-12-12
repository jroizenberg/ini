package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.ObraSocialEJBLocal;
import com.institucion.bz.ObraSocialEJBRemote;
import com.institucion.dao.ObraSocialDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Actividad;
import com.institucion.model.ObraSocial;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class ObraSocialEJBImpl implements ObraSocialEJBRemote,ObraSocialEJBLocal {

	private static Log log = LogFactory.getLog(ObraSocialEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private ObraSocialDAO obraSocialDao = null;
	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public ObraSocialEJBImpl() {	}

	
	@Override
	public void create(ObraSocial c) throws DAOException{
		obraSocialDao.create(c);
	}
	
	@Override
	public void delete(ObraSocial c) throws DAOException{
		obraSocialDao.delete(c);
	}
	
	@Override
	public void save(ObraSocial c) throws DAOException{
		obraSocialDao.save(c);	
	}
	
	@Override
	public ObraSocial findById(Long id) throws DAOException{
        return obraSocialDao.findById(id);
    }
	
	@Override
	public List<ObraSocial> findAll() throws DAOException{
        return obraSocialDao.findAll();
	}

	@Override
	public List<ObraSocial> findAllByActividadDisponible(Actividad act) throws DAOException{
        return obraSocialDao.findAllByActividadDisponible(act);
	}

	@Override
	public List<ObraSocial> findAllConJdbc(String filters) throws DAOException{
        return obraSocialDao.findAllConJdbc(filters);
	}

	@Override
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException{
        return obraSocialDao.findActividadesAllByActividadDisponible();
	}
	
}
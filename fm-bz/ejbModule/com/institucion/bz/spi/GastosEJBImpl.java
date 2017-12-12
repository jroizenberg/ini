package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.GastosEJBLocal;
import com.institucion.bz.GastosEJBRemote;
import com.institucion.dao.GastosDAO;
import com.institucion.dao.GastosMaipuDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Gastos;
import com.institucion.model.GastosMaipu;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class GastosEJBImpl implements GastosEJBRemote,GastosEJBLocal {

	private static Log log = LogFactory.getLog(GastosEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private GastosDAO gastosDao = null;
	
	@Autowired
	private GastosMaipuDAO gastosMaipuDao = null;

	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public GastosEJBImpl() {	}

	
	@Override
	public Gastos create(Gastos c) throws DAOException{
		return gastosDao.create(c);
	}
	
	
	@Override
	public void delete(Gastos c) throws DAOException{
		gastosDao.delete(c);
	}
	
	@Override
	public Gastos save(Gastos c) throws DAOException{
		return gastosDao.save(c);	
	}
	
	@Override
	public Gastos findById(Long id) throws DAOException{
        return gastosDao.findById(id);
    }
	
	@Override
	public List<Gastos> findAll() throws DAOException{
        return gastosDao.findAll();
	}
	@Override
	public List<Gastos> findAllConJdbc(String filters) throws DAOException{
        return gastosDao.findAllConJdbc(filters);
	}
	
	@Override
	public List<GastosMaipu> findAllConJdbcMaipu(String filters) throws DAOException{
        return gastosMaipuDao.findAllConJdbc(filters);
	}
	
	@Override
	public GastosMaipu createMaipu(GastosMaipu c) throws DAOException{
		return gastosMaipuDao.create(c);
	}
	
	
	@Override
	public void deleteMaipu(GastosMaipu c) throws DAOException{
		gastosMaipuDao.delete(c);
	}
	
	@Override
	public GastosMaipu saveMaipu(GastosMaipu c) throws DAOException{
		return gastosMaipuDao.save(c);	
	}
	
	@Override
	public GastosMaipu findByIdMaipu(Long id) throws DAOException{
        return gastosMaipuDao.findById(id);
    }
	
	@Override
	public List<GastosMaipu> findAllMaipu() throws DAOException{
        return gastosMaipuDao.findAll();
	}
}
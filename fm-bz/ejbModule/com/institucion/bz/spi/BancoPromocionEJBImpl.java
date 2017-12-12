package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.BancoPromocionEJBLocal;
import com.institucion.bz.BancoPromocionEJBRemote;
import com.institucion.dao.BancoPromocionDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.BancoPromocion;

@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class BancoPromocionEJBImpl implements BancoPromocionEJBRemote,BancoPromocionEJBLocal {

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private BancoPromocionDAO bancoPromocionDao = null;
	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public BancoPromocionEJBImpl() {	}

	
	@Override
	public void create(BancoPromocion c) throws DAOException{
		bancoPromocionDao.create(c);
	}
	
	@Override
	public void save(BancoPromocion c) throws DAOException{
		bancoPromocionDao.save(c);	
	}
	
	@Override
	public BancoPromocion findById(Long id) throws DAOException{
        return bancoPromocionDao.findById(id);
    }
	
	@Override
	public List<BancoPromocion> findAll() throws DAOException{
        return bancoPromocionDao.findAll();
	}


	@Override
	public List<BancoPromocion> findAllConJdbc(String filters)
			throws DAOException {
		return null;
	}
}
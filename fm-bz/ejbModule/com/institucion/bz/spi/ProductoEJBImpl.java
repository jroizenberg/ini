package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.ProductoEJBLocal;
import com.institucion.bz.ProductoEJBRemote;
import com.institucion.dao.ProductoDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Producto;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class ProductoEJBImpl implements ProductoEJBRemote,ProductoEJBLocal {

	private static Log log = LogFactory.getLog(ProductoEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private ProductoDAO productoDao = null;
	
	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public ProductoEJBImpl() {	}

	
	@Override
	public void create(Producto c) throws DAOException{
		productoDao.create(c);
	}
	
	@Override
	public void delete(Producto c) throws DAOException{
		productoDao.delete(c);
	}
	
	@Override
	public void save(Producto c) throws DAOException{
		productoDao.save(c);	
	}
	
	@Override
	public Producto findById(Long id) throws DAOException{
        return productoDao.findById(id);
    }
	
	@Override
	public List<Producto> findAll() throws DAOException{
        return productoDao.findAll();
	}
	
	@Override
	public List<Producto> findAllConJdbc(String filters) throws DAOException{
        return productoDao.findAllConJdbc(filters);
	}
	
}
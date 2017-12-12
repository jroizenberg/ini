package com.institucion.dao.spi;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.PagosPorSubscripcionDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.PagosPorSubscripcion;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class PagosPorSubscripcionDAOImpl extends ClientDao<PagosPorSubscripcion> implements PagosPorSubscripcionDAO {

	private static Log log = LogFactory.getLog(CajaDAOImpl.class);
	
	/** The sql. */
	private SqlExecutor sql;

	
	@Autowired
	AbstractSessionFactoryBean session;
	
	/**
	 * Gets the sql.
	 *
	 * @return the sql
	 */
	public SqlExecutor getSql() {
		return this.sql;
	}

	/**
	 * Sets the sql.
	 *
	 * @param sql the new sql
	 */
	public void setSql(SqlExecutor sql) {
		this.sql = sql;
	}

	@Override
	public PagosPorSubscripcion create(PagosPorSubscripcion c) throws DAOException {
		return super.create(c);
		
	}

	public List<PagosPorSubscripcion> findAll() throws DAOException{
		return this.findAll(PagosPorSubscripcion.class);
	}

	
	public PagosPorSubscripcion findById(Long id) throws DAOException{
		return this.findById(id, PagosPorSubscripcion.class);
	}

	@Override
	public void delete(PagosPorSubscripcion c) throws DAOException{
		super.delete(c);
	}

	@Override
	public PagosPorSubscripcion save(PagosPorSubscripcion c) throws DAOException{
		return super.save(c);
	}

	@Override
	public void save(List<PagosPorSubscripcion> listPagos) throws DAOException {
		if(listPagos != null){
			for (PagosPorSubscripcion pagosPorSubscripcion : listPagos) {
				super.save(pagosPorSubscripcion);				
			}		
		}
	}	
}
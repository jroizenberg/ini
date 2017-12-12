package com.institucion.dao.spi;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.dao.VentaProductoPorProductoDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.VentaProductoPorProducto;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class VentaProductoPorProductoDAOImpl extends ClientDao<VentaProductoPorProducto> implements VentaProductoPorProductoDAO {

	private static Log log = LogFactory.getLog(ObraSocialDAOImpl.class);
	
	/** The sql. */
	private SqlExecutor sql;

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
	public VentaProductoPorProducto create(VentaProductoPorProducto c) throws DAOException {
		return super.create(c);
		
	}

	public List<VentaProductoPorProducto> findAll() throws DAOException{
		return this.findAll(VentaProductoPorProducto.class);
	}
	
	
	
	public VentaProductoPorProducto findById(Long id) throws DAOException{
		return this.findById(id, VentaProductoPorProducto.class);
	}

	@Override
	public void delete(VentaProductoPorProducto c) throws DAOException{
		super.delete(c);
	}

	@Override
	public VentaProductoPorProducto save(VentaProductoPorProducto c) throws DAOException{
		return super.save(c);
	}
	
}
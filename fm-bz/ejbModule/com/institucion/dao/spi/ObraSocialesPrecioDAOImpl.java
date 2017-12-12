package com.institucion.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.dao.ObraSocialesPrecioDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.ObraSocialesPrecio;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class ObraSocialesPrecioDAOImpl extends ClientDao<ObraSocialesPrecio> implements ObraSocialesPrecioDAO {

	private static Log log = LogFactory.getLog(ObraSocialesPrecioDAOImpl.class);
	
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
	public ObraSocialesPrecio create(ObraSocialesPrecio c) throws DAOException {
		return super.create(c);
		
	}

	public List<ObraSocialesPrecio> findAll() throws DAOException{
		return this.findAll(ObraSocialesPrecio.class);
	}
	
	public List<ObraSocialesPrecio> findAllByObraSocial(Long idObraSocial) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("obraSocialID", idObraSocial);
	
		StringBuffer sql = new StringBuffer();
		sql.append("from com.institucion.model.ObraSocialesPrecio " );
		sql.append("WHERE obraSocial.id = :obraSocialID ");
		return super.find(sql.toString(), params);
	}
	
	public ObraSocialesPrecio findById(Long id) throws DAOException{
		return this.findById(id, ObraSocialesPrecio.class);
	}

	@Override
	public void delete(ObraSocialesPrecio c) throws DAOException{
		super.delete(c);
	}

	@Override
	public ObraSocialesPrecio save(ObraSocialesPrecio c) throws DAOException{
		return super.save(c);
	}
	
}
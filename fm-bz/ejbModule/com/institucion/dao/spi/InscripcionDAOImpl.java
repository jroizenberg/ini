package com.institucion.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.dao.InscripcionDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Inscripcion;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class InscripcionDAOImpl extends ClientDao<Inscripcion> implements InscripcionDAO {

	private static Log log = LogFactory.getLog(InscripcionDAOImpl.class);
	
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
	public Inscripcion create(Inscripcion c) throws DAOException {
		return super.create(c);
		
	}

	public List<Inscripcion> findAll() throws DAOException{
		return this.findAll(Inscripcion.class);
	}

	
	public Inscripcion findById(Long id) throws DAOException{
		return this.findById(id, Inscripcion.class);
	}

	public List<Inscripcion> findByIdClienteAndAnio(Long idCliente ) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clienteID", idCliente);

		if(idCliente == null){
			return null;
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append("from com.institucion.model.Inscripcion " );
			sql.append(" WHERE cliente.id = :clienteID  order by fecha");
			List list = super.find(sql.toString(), params);
			return list;
		}
	}
	
	@Override
	public void delete(Inscripcion c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Inscripcion save(Inscripcion c) throws DAOException{
		return super.save(c);
	}
	
}
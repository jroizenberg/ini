package com.institucion.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.dao.ActividadDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Actividad;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class ActividadDAOImpl extends ClientDao<Actividad> implements ActividadDAO {
	
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
	public Actividad create(Actividad c) throws DAOException {
		return super.create(c);	
	}

	@Override
	public void delete(Actividad c) throws DAOException {
		super.delete(c);
		
	}

	@Override
	public Actividad save(Actividad c) throws DAOException {
		return super.save(c);
		
	}

	@Override
	public Actividad findByIdActividad(Long id) throws DAOException {
		return this.findById(id, Actividad.class);

	}

	@Override
	public List<Actividad> findAllActividad() throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("esparasueldos",false );

		return this.find("from Actividad where esparasueldos = :esparasueldos order by nombre ", params);	

		
	}
	
	@Override
	public List<Actividad> findAllActividadQueTomenLista() throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tomalista",true );
		params.put("esparasueldos",false );

		return this.find("from Actividad where tomalista = :tomalista and esparasueldos = :esparasueldos order by nombre", params);	

		
	}
	
	@Override
	public List<Actividad> findAllActividadQueConObraSocial() throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tomalista",true );
		params.put("esparasueldos",false );

		return this.find("from Actividad where usaEnObraSocial = :tomalista and esparasueldos = :esparasueldos order by nombre ", params);	

		
	}

	@Override
	public List<Actividad> findAllActividadParaPagoSueldos() throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("tomalista",true );
		return this.find("from Actividad order by esparasueldos, nombre ", params);	

		
	}
	@Override
	public List<Actividad> findAllActividadQueUsaCarnet(Integer idTipoCurso) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tomalista",true );
		params.put("esparasueldos",false );

		if(idTipoCurso != null)
			return this.find("from Actividad where usaCarnet = :tomalista   AND idTipoCurso="+idTipoCurso  +" and esparasueldos = :esparasueldos  order by nombre", params);
		else	
			return this.find("from Actividad where usaCarnet = :tomalista and esparasueldos = :esparasueldos  order by nombre ", params);	
	}

	@Override
	public List<Actividad> findAllActividadByTipoActividad(Long idTipoActividad) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTipoActividad",idTipoActividad );
		params.put("esparasueldos",false );

		return this.find("from Actividad where idTipoCurso = :idTipoActividad and esparasueldos = :esparasueldos  order by nombre ", params);	
	}
	
}
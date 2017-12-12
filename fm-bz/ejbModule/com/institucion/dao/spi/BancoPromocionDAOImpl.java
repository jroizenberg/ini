package com.institucion.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.dao.BancoPromocionDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.BancoPromocion;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class BancoPromocionDAOImpl extends ClientDao<BancoPromocion> implements BancoPromocionDAO {
	
	private SqlExecutor sql;
	
	public SqlExecutor getSql() {
		return this.sql;
	}

	public void setSql(SqlExecutor sql) {
		this.sql = sql;
	}

	@Override
	public BancoPromocion create(BancoPromocion c) throws DAOException {
		return super.create(c);
	}

	public List<BancoPromocion> findAll() throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
	
		StringBuffer sql = new StringBuffer();
		sql.append("from com.institucion.model.BancoPromocion " );
		sql.append("WHERE disponible is true ");
		return super.find(sql.toString(), params);
	}
	
	public List<BancoPromocion> findAllByBanco(Long idBanco) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("BancoID", idBanco);
	
		StringBuffer sql = new StringBuffer();
		sql.append("from com.institucion.model.BancoPromocion " );
		sql.append("WHERE banco.id = :BancoID  and  disponible is true ");
		return super.find(sql.toString(), params);
	}
	
	public BancoPromocion findById(Long id) throws DAOException{
		return this.findById(id, BancoPromocion.class);
	}

	@Override
	public BancoPromocion save(BancoPromocion c) throws DAOException{
		return super.save(c);
	}
	
}
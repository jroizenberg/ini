package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.SubscripcionDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Subscripcion;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class SubscripcionDAOImpl extends ClientDao<Subscripcion> implements SubscripcionDAO {

	private static Log log = LogFactory.getLog(SubscripcionDAOImpl.class);
	
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

	public Subscripcion create(Subscripcion c) throws DAOException {
		return super.create(c);
	}

	public Long createSubs(Subscripcion c) throws DAOException {
		return super.createSubs(c);
		
	}
	
	public List<Subscripcion> findAll() throws DAOException{
		return this.findAll(Subscripcion.class);
	}

	public List<Subscripcion> findAllByClient(Long idClient) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("clienteID", idClient);
	
		StringBuffer sql = new StringBuffer();
		sql.append("from com.institucion.model.Subscripcion " );
		sql.append("WHERE cliente.id = :clienteID   order by  fechaingresosubscripcion asc ");
		return super.find(sql.toString(), params);

	}
		
	public Subscripcion findById(Long id) throws DAOException{
		return this.findById(id, Subscripcion.class);
	}

	@Override
	public void delete(Subscripcion c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Subscripcion save(Subscripcion c) throws DAOException{
		return super.save(c);
	}
	
	public List<Subscripcion> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Subscripcion> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Subscripcion cli=findById(new Long(integer));
					if(cli != null){
						if(clientList == null)
							clientList= new ArrayList<Subscripcion>();
						clientList.add(cli);
					}
						
				}				
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {    
			try {
				if(cnx != null)
					cnx.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			}
	    }
		return clientList;
	}
	
	
	private  List<Integer> getClientsByFilters( Connection cnx, String filters){
		
		Statement stat=null;
		ResultSet rs = null;
		List <Integer> ids = new ArrayList<Integer>();
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(filters.toString());
			
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
			return ids;
		
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try {
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
				if(cnx != null)
					cnx.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			}
		}
		return null;
		
	}
}
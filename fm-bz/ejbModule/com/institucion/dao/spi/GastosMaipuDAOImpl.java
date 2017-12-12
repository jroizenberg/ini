package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.GastosMaipuDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.GastosMaipu;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class GastosMaipuDAOImpl extends ClientDao<GastosMaipu> implements GastosMaipuDAO {

	private static Log log = LogFactory.getLog(GastosMaipuDAOImpl.class);
	
	
	@Autowired
	AbstractSessionFactoryBean session;
	
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
	public GastosMaipu create(GastosMaipu c) throws DAOException {
		return super.create(c);
		
	}

	public List<GastosMaipu> findAll() throws DAOException{
		 return this.find("from com.institucion.model.GastosMaipu order by anio, quincena desc , tipogasto",  null);
	}

	public List<GastosMaipu> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<GastosMaipu> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					GastosMaipu cli=findById(new Long(integer));
					if(cli != null){
						if(clientList == null)
							clientList= new ArrayList<GastosMaipu>();
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
	
	
	public GastosMaipu findById(Long id) throws DAOException{
		return this.findById(id, GastosMaipu.class);
	}

	@Override
	public void delete(GastosMaipu c) throws DAOException{
		super.delete(c);
	}

	@Override
	public GastosMaipu save(GastosMaipu c) throws DAOException{
		return super.save(c);
	}
	
}
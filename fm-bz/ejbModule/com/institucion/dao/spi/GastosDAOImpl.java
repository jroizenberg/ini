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

import com.institucion.dao.GastosDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Gastos;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class GastosDAOImpl extends ClientDao<Gastos> implements GastosDAO {

	private static Log log = LogFactory.getLog(GastosDAOImpl.class);
	
	
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
	public Gastos create(Gastos c) throws DAOException {
		return super.create(c);
		
	}

	public List<Gastos> findAll() throws DAOException{
		 return this.find("from com.institucion.model.Gastos order by anio, mes desc , tipogasto",  null);
	}

	public List<Gastos> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Gastos> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Gastos cli=findById(new Long(integer));
					if(cli != null){
						if(clientList == null)
							clientList= new ArrayList<Gastos>();
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
	
	
	public Gastos findById(Long id) throws DAOException{
		return this.findById(id, Gastos.class);
	}

	@Override
	public void delete(Gastos c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Gastos save(Gastos c) throws DAOException{
		return super.save(c);
	}
	
}
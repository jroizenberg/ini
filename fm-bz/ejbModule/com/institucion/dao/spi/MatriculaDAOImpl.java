package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.MatriculaDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Matricula;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class MatriculaDAOImpl extends ClientDao<Matricula> implements MatriculaDAO {

	private static Log log = LogFactory.getLog(MatriculaDAOImpl.class);
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
	public Matricula create(Matricula c) throws DAOException {
		return super.create(c);
		
	}

	public List<Matricula> findAll() throws DAOException{
		return this.findAll(Matricula.class);
	}
	public boolean findAllIsImprimible() throws DAOException{
		Connection cnx 			= null;
		Statement stat=null;
		ResultSet rs = null;
		boolean imprimible= false;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(" select habilitado from imprimible ");
			
			if (rs.next()) {
				imprimible= rs.getBoolean("habilitado");	
			}
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
			return imprimible;
	}

	
	public Matricula findById(Long id) throws DAOException{
		return this.findById(id, Matricula.class);
	}

	@Override
	public void delete(Matricula c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Matricula save(Matricula c) throws DAOException{
		return super.save(c);
	}
	
	@Override
	public void saveImprimible(boolean bool) throws DAOException{
		Connection cnx 			= null;
		Statement stat=null;
		ResultSet rs = null;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			stat.execute(" update imprimible set habilitado= "+bool);
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
	}

}
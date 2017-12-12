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

import com.institucion.dao.CursoDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Curso;
import com.institucion.model.SubsYCurso;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class CursoDAOImpl extends ClientDao<Curso> implements CursoDAO {

	private static Log log = LogFactory.getLog(CursoDAOImpl.class);
	
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
	public Curso create(Curso c) throws DAOException {
		return super.create(c);
		
	}

	public List<Curso> findAll() throws DAOException{
		return this.findAll(Curso.class);
	}

	public List<Curso> findAll2() throws DAOException{
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.id from curso c   "); 
				sql.append(" inner join ActividadYClase ac on (ac.idcurso=c.id)  "); 
				sql.append(" order by c.descripcion, ac.cantclases  desc ");	
		
		return findAllConJdbc(sql.toString());
	}
	
	public List<Curso> findAll2WithDisponible(boolean esMaipu, boolean esCentro) throws DAOException{
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select c.id from curso c   "); 
			sql.append(" inner join ActividadYClase ac on (ac.idcurso=c.id)  "); 
			sql.append(" where c.disponible is true   ");
				
		if(esMaipu){
			sql.append(" and c.nombre ilike '%ini%'   ");
		}else if (esCentro){
			sql.append(" and c.nombre not ilike '%ini%'   ");
		}
		sql.append(" order by c.descripcion, ac.cantclases  desc ");	
		
		return findAllConJdbc(sql.toString());
	}
	
	public Curso findById(Long id) throws DAOException{
		return this.findById(id, Curso.class);
	}

	@Override
	public void delete(Curso c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Curso save(Curso c) throws DAOException{
		return super.save(c);
	}
	
	public List<SubsYCurso> findAllConJdbcSubsYCurso(String filters) throws DAOException{
		Connection cnx 			= null;
		Statement stat=null;
		ResultSet rs = null;
		List<SubsYCurso> claseList= new ArrayList<SubsYCurso>();
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(filters.toString());
		while (rs.next()) {
				SubsYCurso aa= new SubsYCurso();
				aa.setIdCurso(rs.getLong(2));
				aa.setIdSubscripcion(rs.getLong(1));
				claseList.add(aa);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return claseList;
	}
	
	
	public List<Curso> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Curso> claseList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Curso cli=findById(new Long(integer));
					if(cli != null){
						if(claseList == null)
							claseList= new ArrayList<Curso>();
						claseList.add(cli);
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
		return claseList;
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
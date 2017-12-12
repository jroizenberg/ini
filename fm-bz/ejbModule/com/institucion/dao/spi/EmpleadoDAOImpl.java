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

import com.institucion.dao.ActividadDAO;
import com.institucion.dao.EmpleadoDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Actividad;
import com.institucion.model.Empleado;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class EmpleadoDAOImpl extends ClientDao<Empleado> implements EmpleadoDAO {

	private static Log log = LogFactory.getLog(EmpleadoDAOImpl.class);
	
	@Autowired
	AbstractSessionFactoryBean session;
	
	@Autowired
	ActividadDAO actDAO;

	
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
	public Empleado create(Empleado c) throws DAOException {
		return super.create(c);
		
	}

	public List<Empleado> findAll() throws DAOException{
		 return this.find("from com.institucion.model.Empleado order by nombre",  null);
	}

	public List<Empleado> findAllByActividadDisponible(Actividad act) throws DAOException{
		if(act != null){
			StringBuilder actionConditions= new StringBuilder( "select distinct(clase.id) from Empleado clase  ");
			actionConditions.append(" inner join EmpleadoActividades osp on (osp.idEmpleado = clase.id )  ");
			actionConditions.append(" where 1=1  ");
			actionConditions.append(" AND  osp.idActividad = "+ act.getId());	
			actionConditions.append(" AND  osp.disponible is true  order by clase.nombre ");
//			actionConditions.append(" order by clase.nombre ");
			return findAllConJdbc(actionConditions.toString());
		
		}else{
			return null;
		}
	}
	
	
	public List<Actividad> findActividadesAllByActividadDisponible() throws DAOException{
		return actDAO.findAllActividadQueConObraSocial();
	}
	
	
	public Empleado findById(Long id) throws DAOException{
		return this.findById(id, Empleado.class);
	}

	@Override
	public void delete(Empleado c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Empleado save(Empleado c) throws DAOException{
		return super.save(c);
	}
	
	public List<Empleado> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Empleado> claseList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Empleado cli=findById(new Long(integer));
					if(cli != null){
						if(claseList == null)
							claseList= new ArrayList<Empleado>();
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

	@Override
	public void loadLazy(Empleado e) throws DAOException {
		// TODO Auto-generated method stub
		
	}
}
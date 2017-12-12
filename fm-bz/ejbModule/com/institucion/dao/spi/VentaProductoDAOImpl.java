package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.institucion.dao.VentaProductoDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.VentaProducto;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class VentaProductoDAOImpl extends ClientDao<VentaProducto> implements VentaProductoDAO {

	private static Log log = LogFactory.getLog(ObraSocialDAOImpl.class);
	
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
	public VentaProducto create(VentaProducto c) throws DAOException {
		return super.create(c);
		
	}

	public List<VentaProducto> findAll() throws DAOException{
		return this.findAll(VentaProducto.class);
	}
	
	public List<VentaProducto> findAllConJdbcByDates(Connection cnx , Date fechaDesde, Date fechaHasta) throws DAOException{
		List <VentaProducto> ventass= new ArrayList<VentaProducto>();

		try {
			List<Integer> list =findVentasPorFecha(cnx, fechaDesde, fechaHasta);

			if(list != null && list.size() >0){
				for (Integer integer : list) {
					VentaProducto cli=findById(new Long(integer));
					if(cli != null){
						if(ventass == null)
							ventass= new ArrayList<VentaProducto>();
						ventass.add(cli);
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
		return ventass;
	}
	
	public List<Integer> findVentasPorFecha(Connection cnx , Date fechaDesde, Date fechaHasta) throws DAOException{

		StringBuilder actionConditions= new StringBuilder("select id from  ventaproductos  ventaprod");

		actionConditions.append(" where 1=1  ");

		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			actionConditions.append( " and ((to_char(ventaprod.fechayhoracompra,'YYYY-MM-DD') <= '"+ fechaH+"'   ");  
				actionConditions.append( " and to_char(ventaprod.fechayhoracompra,'YYYY-MM-DD') >='"+ fechaD+"'  ) ");
				actionConditions.append( " or  ");
				actionConditions.append( " (to_char(ventaprod.fechayhoraanulacion,'YYYY-MM-DD') <='"+ fechaH+"'  ");  
				actionConditions.append( " and to_char(ventaprod.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  )) ");	
		}
		
		Statement stat=null;
		ResultSet rs = null;
		List <Integer> ids = new ArrayList<Integer>();
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(actionConditions.toString());
			
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
	public Long createVenta(VentaProducto c) throws DAOException{
		return super.createVenta(c);
	}

	
	public VentaProducto findById(Long id) throws DAOException{
		return this.findById(id, VentaProducto.class);
	}

	@Override
	public void delete(VentaProducto c) throws DAOException{
		super.delete(c);
	}

	@Override
	public VentaProducto save(VentaProducto c) throws DAOException{
		return super.save(c);
	}
	
}
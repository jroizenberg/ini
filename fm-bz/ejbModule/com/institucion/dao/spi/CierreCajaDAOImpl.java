package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.CierreCajaDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.CierreCaja;
import com.institucion.model.SucursalEnum;

public class CierreCajaDAOImpl extends ClientDao<CierreCaja> implements CierreCajaDAO {

	private static Log log = LogFactory.getLog(CierreCajaDAOImpl.class);
		
	/** The sql. */
	private SqlExecutor sql;
	
	@Autowired
	AbstractSessionFactoryBean session;

	public SqlExecutor getSql() {
		return this.sql;
	}

	public void setSql(SqlExecutor sql) {
		this.sql = sql;
	}
	
	public CierreCaja findCierreCajaById(Long id) throws DAOException{
		return this.findById(id, CierreCaja.class);
	}

	
	@Override
	public CierreCaja create(CierreCaja c) throws DAOException {
		return super.create(c);
		
	}

	@Override
	public void delete(CierreCaja c) throws DAOException{
		super.delete(c);
	}

	@Override
	public CierreCaja save(CierreCaja c) throws DAOException{
		return super.save(c);
	}
		
	
	public List<CierreCaja> findCierresCaja( SucursalEnum suc){
		Statement stat=null;
		ResultSet rs = null;
		Connection cnx 			= null;
		List<CierreCaja> listCierraCaja= null;
		try {
			cnx	= session.getDataSource().getConnection();

			stat = cnx.createStatement();
			String query=" select * from CierreCaja  ";
			
			if(suc != null){
				query= query+ " where sucursal= "+ suc.toInt() ;
			}
			query= query+ " order by id desc limit 10 ";

			rs = stat.executeQuery(query);

			listCierraCaja= new ArrayList <CierreCaja>(); 
			
			while (rs.next()) {
				CierreCaja cierraCaja= new CierreCaja();

				if(rs.getObject("id") != null)
					cierraCaja.setId(rs.getLong("id"));

				if(rs.getObject("comentario") != null)
					cierraCaja.setComentario(rs.getString("comentario"));
				
				if(rs.getObject("fecha") != null)
					cierraCaja.setFecha(rs.getTimestamp("fecha"));
					
				if(rs.getObject("valor") != null)
					cierraCaja.setValor(rs.getInt("valor"));
				
				if(rs.getObject("sucursal") != null)
					cierraCaja.setSucursal(SucursalEnum.fromInt(rs.getInt("sucursal")));
				
				if(rs.getObject("idUsuarioGeneroMovimiento") != null)
					cierraCaja.setIdUsuarioGeneroMovimiento(rs.getInt("idUsuarioGeneroMovimiento"));
				
				listCierraCaja.add(cierraCaja);
			}
			
			return listCierraCaja;
		
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
	
	
	public CierreCaja getUltimoCierre( SucursalEnum suc) throws DAOException{
		
		Statement stat=null;
		ResultSet rs = null;
		Connection cnx 			= null;
		CierreCaja cierraCaja= null;
		try {
			cnx	= session.getDataSource().getConnection();

			stat = cnx.createStatement();
			String query=" select * from CierreCaja where id in  ";
			
			if(suc != null){
				query= query+ " ( select max(id) from CierreCaja where sucursal= "+ suc.toInt() + " ) " ;
			}else
				query= query+ " ( select max(id) from CierreCaja) " ;
			
			rs = stat.executeQuery(query);
			cierraCaja= new CierreCaja();
			
			while (rs.next()) {
				
				if(rs.getObject("id") != null)
					cierraCaja.setId(rs.getLong("id"));
				
				if(rs.getObject("fecha") != null)
					cierraCaja.setFecha(rs.getTimestamp("fecha"));
				
				if(rs.getObject("comentario") != null)
					cierraCaja.setComentario(rs.getString("comentario"));
				

				if(rs.getObject("valor") != null)
					cierraCaja.setValor(rs.getInt("valor"));
				
				if(rs.getObject("sucursal") != null)
					cierraCaja.setSucursal(SucursalEnum.fromInt(rs.getInt("sucursal")));

				if(rs.getObject("idUsuarioGeneroMovimiento") != null)
					cierraCaja.setIdUsuarioGeneroMovimiento(rs.getInt("idUsuarioGeneroMovimiento"));
				
				return cierraCaja;
			}
			
			cierraCaja.setValor(0);
			cierraCaja.setIdUsuarioGeneroMovimiento(1);
			cierraCaja.setFecha(new Date());
			return cierraCaja;
		
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
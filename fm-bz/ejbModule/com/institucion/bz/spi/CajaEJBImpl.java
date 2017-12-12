package com.institucion.bz.spi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.CajaEJBLocal;
import com.institucion.bz.CajaEJBRemote;
import com.institucion.dao.CajaDAO;
import com.institucion.dao.CierreCajaDAO;
import com.institucion.dao.PagosPorSubscripcionDAO;
import com.institucion.dao.VentaProductoDAO;
import com.institucion.dao.VentaProductoPorProductoDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.security.model.User;
import com.institucion.model.Actividad;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.CajaMovimientoView;
import com.institucion.model.CierreCaja;
import com.institucion.model.Concepto;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.PulsoClinica;
import com.institucion.model.PulsoClinicaDetalle;
import com.institucion.model.PulsoMaipuDetalle;
import com.institucion.model.SucursalEnum;
import com.institucion.model.SumasTotalesMovimientos;
import com.institucion.model.TipoMovimientoCajaEnum;
import com.institucion.model.VentaProducto;
import com.institucion.model.VentaProductoPorProducto;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class CajaEJBImpl implements CajaEJBRemote,CajaEJBLocal {

	private static Log log = LogFactory.getLog(CajaEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private CajaDAO claseDao = null;
	
	@Autowired
	private VentaProductoDAO ventaProductoDao = null;
	
	@Autowired
	private VentaProductoPorProductoDAO ventaProductoPorProductoDao = null;
	
	@Autowired
	private PagosPorSubscripcionDAO pagosPorSubscripcionDao = null;
	
	@Autowired
	private CierreCajaDAO cierreCajaDao = null;

	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public CajaEJBImpl() {	}

	
	@Override
	public void save(Concepto c) throws DAOException{
		claseDao.save(c);
	}
	
	@Override
	public void save(VentaProducto c) throws DAOException{
		ventaProductoDao.save(c);	
	}
	
	@Override
	public void save(VentaProductoPorProducto c) throws DAOException{
		ventaProductoPorProductoDao.save(c);	
	}

	@Override
	public void saveAll(List<VentaProductoPorProducto> c) throws DAOException{
		if(c != null){
			for (VentaProductoPorProducto ventaProductoPorProducto : c) {
				ventaProductoPorProductoDao.save(ventaProductoPorProducto);		
			}
		}
	}

	@Override
	public void savePagosPorSubscripcionAll(List<PagosPorSubscripcion> c) throws DAOException{
		if(c != null){
			for (PagosPorSubscripcion ventaProductoPorProducto : c) {
				pagosPorSubscripcionDao.save(ventaProductoPorProducto);		
			}
		}
	}

	public void savePagosPorSubscripcion( PagosPorSubscripcion c) throws DAOException{
		if(c != null){
				pagosPorSubscripcionDao.save(c);		
		}
	}

	
	
	@Override
	public void create(CajaMovimiento c) throws DAOException{
		claseDao.create(c);
	}
	
	@Override
	public void delete(CajaMovimiento c) throws DAOException{
		claseDao.delete(c);
	}
	
	@Override
	public void save(CajaMovimiento c) throws DAOException{
		claseDao.save(c);	
	}
	
	@Override
	public CajaMovimiento findById(Long id) throws DAOException{
        return claseDao.findById(id);
    }
	
	@Override
	public VentaProducto findByIdVentaProducto(Long id) throws DAOException{
        return ventaProductoDao.findById(id);
	}
	
	@Override
	public List<CajaMovimiento> findAll() throws DAOException{
        return claseDao.findAll();
	}
	
	public List<VentaProducto> findVentasDelDia(Date fechaDesde, Date fechaHasta) throws DAOException{
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			return ventaProductoDao.findAllConJdbcByDates(cnx, fechaDesde,fechaHasta); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
		}
		return null;
	}
	
	@Override
	public List<CajaMovimiento> findAllConJdbc(String filters) throws DAOException{
        return claseDao.findAllConJdbc(filters);
	}

	public List<CajaMovimientoView> findAllConJdbcAControlar(Date fechaDesde , Date fechaHasta){
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			return claseDao.findAllConJdbcAControlar(cnx, fechaDesde,fechaHasta); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		return null;
	}

	
	@Override
	public SumasTotalesMovimientos findAllConJdbc(Date fechaDesde, Date fechaHasta, Actividad curso, boolean tieneSeteadaActividad, 
			TipoMovimientoCajaEnum tipoMov, User usuarioSelected, boolean tieneFiltroSoloObraSocial, Long promocionBancoID,
			int userName, SucursalEnum suc) throws DAOException{
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			
			CierreCaja cierre= cierreCajaDao.getUltimoCierre(suc);
			Date fechaYHoraDesde= null;
//			if(cierre != null && cierre.getIdUsuarioGeneroMovimiento().intValue() == userName ){
				// tengo que sumar solamente los que son mayor a la fecha y hora 
				fechaYHoraDesde= cierre.getFecha();
//			}

			return claseDao.findAllConJdbc(cnx, fechaDesde,fechaHasta,curso, tieneSeteadaActividad, tipoMov, usuarioSelected, 
					tieneFiltroSoloObraSocial,promocionBancoID, userName, fechaYHoraDesde, suc); 
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}	
			}
		}
		return null;
	}
	
	@Override
	public List<PulsoClinica> obtenerResumenDeTotalesJdbc(Long anio) throws DAOException{
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			return claseDao.obtenerResumenDeTotalesJdbc(cnx, anio); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		return null;
	}

	
	public List<PulsoMaipuDetalle> obtenerResumenDetalleDeTotalesJdbcMaipu(Long anio) throws DAOException{
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			return claseDao.obtenerResumenDetalleDeTotalesJdbcMaipu(cnx, anio); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		return null;
	}

	@Override
	public List<PulsoClinicaDetalle> obtenerResumenDetalleDeTotalesJdbc(Long anio) throws DAOException{
		Connection cnx= null;
		try{
			cnx = session.getDataSource().getConnection();
			return claseDao.obtenerResumenDetalleDeTotalesJdbc(cnx, anio); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(cnx != null){
				try {
					cnx.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		return null;
	}

	@Override
	public Long createVenta(VentaProducto c) throws DAOException{
		return ventaProductoDao.createVenta(c);
	}
	
	public void create(CierreCaja c) throws DAOException{
		 cierreCajaDao.create(c);
	}
	public void delete(CierreCaja c) throws DAOException{
		cierreCajaDao.delete(c);
	}
	public void save(CierreCaja c) throws DAOException{
		cierreCajaDao.save(c);
	}
	public List<CierreCaja> findCierresCaja(SucursalEnum suc){
		return cierreCajaDao.findCierresCaja(suc);
	}

	public CierreCaja getUltimoCierre(SucursalEnum suc) throws DAOException{
		return cierreCajaDao.getUltimoCierre(suc);
		
	}

	public CierreCaja findCierreCajaById(Long id) throws DAOException{
		return cierreCajaDao.findCierreCajaById(id);
	}

}
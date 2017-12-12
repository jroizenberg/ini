package com.institucion.bz;

import java.util.Date;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
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

public interface CajaEJB{
	
	public void create(CajaMovimiento c) throws DAOException;
	public void delete(CajaMovimiento c) throws DAOException;
	public void save(CajaMovimiento c) throws DAOException;
	
	public void save(Concepto c) throws DAOException;
	
	public CajaMovimiento findById(Long id) throws DAOException;
	
	public List<CajaMovimiento> findAll() throws DAOException;

	public List<CajaMovimiento> findAllConJdbc(String filters) throws DAOException;
	public SumasTotalesMovimientos findAllConJdbc(Date fechaDesde, Date fechaHasta, Actividad curso,  boolean tieneSeteadaActividad,
			TipoMovimientoCajaEnum tipoMov, User usuarioSelected, boolean tieneFiltroSoloObraSocial, Long promocionBancoID, 
			int userName, SucursalEnum suc) throws DAOException;
	
	public void save(VentaProducto c) throws DAOException;
	public List<VentaProducto> findVentasDelDia(Date fechaDesde, Date fechaHasta) throws DAOException;
	public VentaProducto findByIdVentaProducto(Long id) throws DAOException;

	public void save(VentaProductoPorProducto c) throws DAOException;
	public void saveAll(List<VentaProductoPorProducto> c) throws DAOException;
	
	public void savePagosPorSubscripcionAll(List<PagosPorSubscripcion> c) throws DAOException;
	public void savePagosPorSubscripcion(PagosPorSubscripcion c) throws DAOException;
	public List<PulsoClinica> obtenerResumenDeTotalesJdbc(Long anio) throws DAOException;
	public List<PulsoClinicaDetalle> obtenerResumenDetalleDeTotalesJdbc(Long anio) throws DAOException;

	public List<PulsoMaipuDetalle> obtenerResumenDetalleDeTotalesJdbcMaipu(Long anio) throws DAOException;

	public Long createVenta(VentaProducto c) throws DAOException;
	public List<CajaMovimientoView> findAllConJdbcAControlar(Date fechaDesde , Date fechaHasta);

	public void create(CierreCaja c) throws DAOException;
	public void delete(CierreCaja c) throws DAOException;
	public void save(CierreCaja c) throws DAOException;
	public List<CierreCaja> findCierresCaja(SucursalEnum suc);
	public CierreCaja getUltimoCierre(SucursalEnum suc) throws DAOException;
	public CierreCaja findCierreCajaById(Long id) throws DAOException;

}
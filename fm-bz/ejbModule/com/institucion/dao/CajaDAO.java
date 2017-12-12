package com.institucion.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.security.model.User;
import com.institucion.model.Actividad;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.CajaMovimientoView;
import com.institucion.model.Concepto;
import com.institucion.model.PulsoClinica;
import com.institucion.model.PulsoClinicaDetalle;
import com.institucion.model.PulsoMaipuDetalle;
import com.institucion.model.SucursalEnum;
import com.institucion.model.SumasTotalesMovimientos;
import com.institucion.model.TipoMovimientoCajaEnum;

public interface CajaDAO{

	public CajaMovimiento create(CajaMovimiento c) throws DAOException;
	public void delete(CajaMovimiento c) throws DAOException;
	public CajaMovimiento save(CajaMovimiento c) throws DAOException;
	
	public void save(Concepto c) throws DAOException;
	
	
	public CajaMovimiento findById(Long id) throws DAOException;
	
	public List<CajaMovimiento> findAll() throws DAOException;
	public List<CajaMovimiento> findAllConJdbc(String filters) throws DAOException;
	public SumasTotalesMovimientos findAllConJdbc(Connection cnx , Date fechaDesde, Date fechaHasta, Actividad curso,  boolean tieneSeteadaActividad,
			TipoMovimientoCajaEnum tipoMov, User usuarioSelected, boolean tieneFiltroSoloObraSocial, Long promocionBancoID, int userName, 
			Date fechaYHoraDesde, SucursalEnum suc) throws DAOException;
	
	public List<CajaMovimientoView> findAllConJdbcAControlar(Connection cnx , Date fechaDesde , Date fechaHasta);

	public List<PulsoClinica> obtenerResumenDeTotalesJdbc(Connection cnx , Long anio) throws DAOException;
	public List<PulsoClinicaDetalle> obtenerResumenDetalleDeTotalesJdbc(Connection cnx , Long anio) throws DAOException;
	public List<PulsoMaipuDetalle> obtenerResumenDetalleDeTotalesJdbcMaipu(Connection cnx , Long anio) throws DAOException;


}
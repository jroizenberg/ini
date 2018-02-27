package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.CajaDAO;
import com.institucion.dao.ClaseDAO;
import com.institucion.dao.CursoDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.fm.security.model.User;
import com.institucion.model.Actividad;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.CajaMovimientoView;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.Concepto;
import com.institucion.model.Curso;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.GastosEnum;
import com.institucion.model.GastosMaipuEnum;
import com.institucion.model.PulsoClinica;
import com.institucion.model.PulsoClinicaDetalle;
import com.institucion.model.PulsoClinicaDetalleParte2;
import com.institucion.model.PulsoMaipuDetalle;
import com.institucion.model.PulsoMaipuDetalleParte2;
import com.institucion.model.Quincena;
import com.institucion.model.SucursalEnum;
import com.institucion.model.SumasTotalObraSocial;
import com.institucion.model.SumasTotalesMovimientos;
import com.institucion.model.TipoMovimientoCajaEnum;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class CajaDAOImpl extends ClientDao<CajaMovimiento> implements CajaDAO {

	private static Log log = LogFactory.getLog(CajaDAOImpl.class);
	
	public int ACTIVIDAD_MOVIMIENTO_CAJA=98;
	public int ACTIVIDAD_VENTA_PRODUCTOS=99;
	
	@Autowired
	private ClaseDAO claseDAO;

	@Autowired
	private CursoDAO cursoDAO;

	/** The sql. */
	private SqlExecutor sql;

	
	@Autowired
	AbstractSessionFactoryBean session;
	
//	@Autowired
//	CajaEJB cajaEJB;
	
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
	public CajaMovimiento create(CajaMovimiento c) throws DAOException {
		return super.create(c);
		
	}

	public List<CajaMovimiento> findAll() throws DAOException{
		return this.findAll(CajaMovimiento.class);
	}

	private List<PulsoClinicaDetalle> crearObjetosDetalleConMesesVacios(List<PulsoClinicaDetalle>  listaPulsoClinico, String mes, int id){
		
		if(listaPulsoClinico != null){
			PulsoClinicaDetalle pulso= new PulsoClinicaDetalle();
			pulso.setMes(mes);
			pulso.setMesId(id);
			
			listaPulsoClinico.add(pulso);
		}
		return listaPulsoClinico;
	}
		
	private List<PulsoMaipuDetalle> crearObjetosDetalleConMesesVacios2(List<PulsoMaipuDetalle>  listaPulsoClinico){
		
		// consulto todas las quincenas
		List<Quincena> quincenaList=claseDAO.findAllConActividadTomaListaDelDiaNombreCurso();
		if(listaPulsoClinico != null){
			int i=1;
			for (Quincena quincena2 : quincenaList) {
				PulsoMaipuDetalle pulso= new PulsoMaipuDetalle();
				pulso.setQuincena(quincena2.getNombre());
				pulso.setQuincenaId(quincena2.getId().intValue());
				pulso.setPosicionId(i);

				listaPulsoClinico.add(pulso);
				i= i+1;
			}
		}
		return listaPulsoClinico;
	}

	private List<PulsoClinicaDetalle> actualizarTotalesPulsoMesDetalle(List<PulsoClinicaDetalle> lista){
		
		if(lista != null){
			for (PulsoClinicaDetalle pulsoClinicaDetalle : lista) {
				if(pulsoClinicaDetalle.getList() != null){
					for (PulsoClinicaDetalleParte2 pulsoClinicaDetalleParte2 : pulsoClinicaDetalle.getList()) {
						double cantTotal=  pulsoClinicaDetalleParte2.getCantidadIngresoEfectivo() 
								-  pulsoClinicaDetalleParte2.getCantidadEgresoRRHH();
						pulsoClinicaDetalleParte2.setCantidadTotal(cantTotal);
					}
				}
			}		
		}
		return lista;
	}
	
	
	// HASTA EL MOMENTO ESTO ES SOLO PARA LAS ACTIVIDADES- NO VENTAS PRODUCTOS ni MOVIMIENTOS CAJA 
	private List<PulsoClinicaDetalle> actualizarPulsoMesDetalle(List<PulsoClinicaDetalle> lista, int mes, int idActividad, 
			Double dinero, boolean esSuma, boolean esObraSocial, boolean esSueldos ){
	
		if(lista != null){
			for (PulsoClinicaDetalle pulsoClinica : lista) {
				
				if(pulsoClinica.getMesId() == mes){
					
					if(pulsoClinica.getList() != null){
						
							for (PulsoClinicaDetalleParte2 pulsoClinicaDetalle : pulsoClinica.getList()) {
						
								if(pulsoClinicaDetalle.getIdActividad() == idActividad){
									
									if(esObraSocial){
										if(esSuma){
											pulsoClinicaDetalle.setCantidadIngresoObraSocial(pulsoClinicaDetalle.getCantidadIngresoObraSocial() + dinero);
											
										}else
											pulsoClinicaDetalle.setCantidadIngresoObraSocial(pulsoClinicaDetalle.getCantidadIngresoObraSocial() - dinero);
										
									}else{
										if(esSuma){
											if(esSueldos)
												pulsoClinicaDetalle.setCantidadEgresoRRHH(pulsoClinicaDetalle.getCantidadEgresoRRHH() + dinero.intValue());
											else
												pulsoClinicaDetalle.setCantidadIngresoEfectivo(pulsoClinicaDetalle.getCantidadIngresoEfectivo() + dinero);
										}else
											pulsoClinicaDetalle.setCantidadIngresoEfectivo(pulsoClinicaDetalle.getCantidadIngresoEfectivo() - dinero);
									}
									
									return lista;
								}
							}
					}else{
						pulsoClinica.setList(new ArrayList());
					}

					PulsoClinicaDetalleParte2 pulsoClinica2 = new PulsoClinicaDetalleParte2();
					pulsoClinica2.setIdActividad(idActividad);
					
					if(esObraSocial){
						if(esSuma){
							pulsoClinica2.setCantidadIngresoObraSocial( dinero);
						}else
							pulsoClinica2.setCantidadIngresoObraSocial(0- dinero);

					}else{
						if(esSuma){
							if(esSueldos){
								pulsoClinica2.setCantidadEgresoRRHH(dinero.intValue());
							}else
								pulsoClinica2.setCantidadIngresoEfectivo(dinero);
						}else
							pulsoClinica2.setCantidadIngresoEfectivo(new Double(0)- dinero);
					}

					pulsoClinica2.setCantidadTotal(new Double(0));
					pulsoClinica.getList().add(pulsoClinica2);
				}
			}
		}
		return lista;
	}	

	
	
	private List<PulsoMaipuDetalle> actualizarPulsoMaipuMesDetalleGastos(List<PulsoMaipuDetalle> lista, int quincena, int idCurso, 
			Double dinero, boolean esSuma, boolean esObraSocial, boolean esSueldos, TipoMovimientoCajaEnum tipo,  TipoMovimientoCajaEnum tipoIngreso, 
			int idTipo){
		GastosMaipuEnum gastoTipo= GastosMaipuEnum.fromInt(idTipo);
		if(idCurso == 99){
			if(lista != null){
				for (PulsoMaipuDetalle pulsoClinica : lista) {
					
					if(pulsoClinica.getQuincenaId() == quincena){
						
						if(pulsoClinica.getList() != null){
							
								for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : pulsoClinica.getList()) {
					
//									Curso cc=cursoDAO.findById(new Long(pulsoClinicaDetalle.getIdCurso()));
									if(pulsoClinicaDetalle.getTipoGasto() != null && pulsoClinicaDetalle.getTipoGasto().toInt() == gastoTipo.toInt()){
										pulsoClinicaDetalle.setCantidadEgresoRRHH(pulsoClinicaDetalle.getCantidadEgresoRRHH() + dinero.intValue());
										return lista;
									}
								}
						}else{
							pulsoClinica.setList(new ArrayList());
						}

						PulsoMaipuDetalleParte2 pulsoClinica2 = new PulsoMaipuDetalleParte2();
						pulsoClinica2.setIdCurso(idCurso);
						
						pulsoClinica2.setTipoGasto(gastoTipo);
						pulsoClinica2.setTipo(tipo);
						pulsoClinica2.setEsCursoQueLLevaSueldoYnoEgresos(false);
						pulsoClinica2.setCantidadEgresoRRHH(dinero.intValue());
						pulsoClinica2.setTipodeIngreso(tipoIngreso);
						pulsoClinica.getList().add(pulsoClinica2);
					}
				}
			}		
		}
		return lista;
	}	
	
	private List<PulsoMaipuDetalle> actualizarPulsoMaipuMesDetalle(List<PulsoMaipuDetalle> lista, int quincena, int idCurso, 
			Double dinero, boolean esSuma, boolean esObraSocial, boolean esSueldos, TipoMovimientoCajaEnum tipo,  TipoMovimientoCajaEnum tipoIngreso){
	
		if(lista != null){
			for (PulsoMaipuDetalle pulsoClinica : lista) {
				
				if(pulsoClinica.getQuincenaId() == quincena){
					
					if(pulsoClinica.getList() != null){
						
							for (PulsoMaipuDetalleParte2 pulsoClinicaDetalle : pulsoClinica.getList()) {
				
								if(pulsoClinicaDetalle.getIdCurso() == idCurso){
									
									if(esSuma){
										if(esSueldos)
											pulsoClinicaDetalle.setCantidadEgresoRRHH(pulsoClinicaDetalle.getCantidadEgresoRRHH() + dinero.intValue());
										else
											pulsoClinicaDetalle.setCantidadIngresoEfectivo(pulsoClinicaDetalle.getCantidadIngresoEfectivo() + dinero);
									}else
										pulsoClinicaDetalle.setCantidadIngresoEfectivo(pulsoClinicaDetalle.getCantidadIngresoEfectivo() - dinero);
									
									return lista;
								}
							}
					}else{
						pulsoClinica.setList(new ArrayList());
					}

					PulsoMaipuDetalleParte2 pulsoClinica2 = new PulsoMaipuDetalleParte2();
					pulsoClinica2.setIdCurso(idCurso);
					
					Curso cc=cursoDAO.findById(new Long(idCurso));
					if(cc != null && cc.getNombre().equalsIgnoreCase("INI VERANO")){
						pulsoClinica2.setEsCursoQueLLevaSueldoYnoEgresos(true);	
					}else{
						pulsoClinica2.setEsCursoQueLLevaSueldoYnoEgresos(false);
					}
					
					pulsoClinica2.setTipo(tipo);
					
					if(esSuma){
						if(esSueldos){
							pulsoClinica2.setCantidadEgresoRRHH(dinero.intValue());
						}else
							pulsoClinica2.setCantidadIngresoEfectivo(dinero);
					}else
						pulsoClinica2.setCantidadIngresoEfectivo(new Double(0)- dinero);

					pulsoClinica2.setTipodeIngreso(tipoIngreso);
					pulsoClinica.getList().add(pulsoClinica2);
				}
			}
		}
		return lista;
	}	
	
	public List<PulsoMaipuDetalle> obtenerResumenDetalleDeTotalesJdbcMaipu(Connection cnx , Long anio) throws DAOException{
	List<PulsoMaipuDetalle> listaDetalle= new ArrayList<PulsoMaipuDetalle>();
		
		listaDetalle=crearObjetosDetalleConMesesVacios2(listaDetalle);
		
		StringBuilder actionConditions= new StringBuilder("select a, b, c, d from ( ");
		
		// Si no se selecciono en la Actividad: Venta de Productos

		/* VENTA DE NUEVA SUBSCRIPCION */
		actionConditions.append(getQueryNuevaSubscripcionDetalleMaipu(false, anio));  
									
		/* ANULACION DE SUBSCRIPCION */
		actionConditions.append(getQueryAnulaSubscripcionDetalleMaipu(true, anio));
					
		/* INGRESO MOVIMIENTO CAJA */
		actionConditions.append(getQueryMovimientoDeCajaIngresosDetalleMaipu(true, anio)); 
		
		/* EGRESO MOVIMIENTO CAJA */
		actionConditions.append(getQueryMovimientoDeCajaEgresosDetalleMaipu(true, anio));

		// todos los gastos de Maipu
		actionConditions.append(getQuerySueldosDetalleMaipu(true, anio));
		
		actionConditions.append( " ) AS consulta_general    order by b ");
			
		Statement stat=null;
		ResultSet rs = null;
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(actionConditions.toString());

			while (rs.next()) {
				String tipo=rs.getString(1);
				Integer idCurso =rs.getInt(2);
				Double cantidadPago =rs.getDouble(3);
				String quincenaS =rs.getString(4);

				if(cantidadPago == null)
					cantidadPago= new Double(0);
				
				String dada=String.valueOf(cantidadPago );

				if(dada!= null && dada.endsWith(",0")){
					String sas=dada.substring(0, dada.indexOf(","));
					cantidadPago= new Double(sas);
				}else if(cantidadPago != null){
					cantidadPago= new Double(Math.rint(cantidadPago*100)/100);
				}				
				if(idCurso== null)
					idCurso=-1;
				
				int mes=Integer.parseInt(quincenaS);

				if(tipo.equalsIgnoreCase("nueva") ){		
					listaDetalle= actualizarPulsoMaipuMesDetalle(listaDetalle, mes, idCurso, cantidadPago, true, false, false, TipoMovimientoCajaEnum.INGRESO, null);
				}

				if(tipo.equalsIgnoreCase("gastosEgresos")){
					// idCurso es el tipo de gasto, por eso como curso le pongo 99
					listaDetalle= actualizarPulsoMaipuMesDetalleGastos(listaDetalle, mes, 99, cantidadPago, true, false, true, TipoMovimientoCajaEnum.EGRESO, null, idCurso);
				}
				
				// gastos varios
				if(tipo.equalsIgnoreCase("anulo")){
					listaDetalle= actualizarPulsoMaipuMesDetalle(listaDetalle, mes, idCurso, cantidadPago, false, false, false, TipoMovimientoCajaEnum.EGRESO, null);
				}
				
				if(tipo.equalsIgnoreCase("IngresosMovimientoCaja")){
					listaDetalle= actualizarPulsoMaipuMesDetalle(listaDetalle, mes, idCurso, cantidadPago, true, false, false, TipoMovimientoCajaEnum.INGRESO, TipoMovimientoCajaEnum.INGRESO);
				}
				
				if(tipo.equalsIgnoreCase("EgresosMovimientoCaja") ){
					listaDetalle= actualizarPulsoMaipuMesDetalle(listaDetalle, mes, idCurso, cantidadPago, true, false, true, TipoMovimientoCajaEnum.EGRESO, TipoMovimientoCajaEnum.EGRESO);
				}
			}			
			return listaDetalle;
			
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
	
	public List<PulsoClinicaDetalle> obtenerResumenDetalleDeTotalesJdbc(Connection cnx , Long anio) throws DAOException{
		List<PulsoClinicaDetalle> listaDetalle= new ArrayList<PulsoClinicaDetalle>();
		
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Enero",1);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Febrero",2);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Marzo",3);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Abril",4);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Mayo",5);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Junio",6);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Julio",7);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Agosto",8);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Septiembre",9);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Octubre",10);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Noviembre",11);
		listaDetalle=crearObjetosDetalleConMesesVacios(listaDetalle, "Diciembre",12);
		
		StringBuilder actionConditions= new StringBuilder("select a, b, c, d from ( ");
		
		// Si no se selecciono en la Actividad: Venta de Productos

		/* VENTA DE NUEVA SUBSCRIPCION */
		actionConditions.append(getQueryNuevaSubscripcionDetalle(false, anio));  
		
		/* VENTA DE POSPONE SUBSCRIPCION */
		actionConditions.append(getQueryPosponerSubscripcionDetalle(true, anio)); 
			
		/* VENTA DE SALDAR DEUDA SUBSCRIPCION*/
//		actionConditions.append(getQuerySaldaSubscripcionDetalle(true, anio)); 

		/*   VENTA OBRA SOCIAL SWISS y IOSCOR */
		actionConditions.append(getQueryVentaObraSocialesDetalle(true, anio)); 
				
		/* ANULACION DE SUBSCRIPCION */
		actionConditions.append(getQueryAnulaSubscripcionDetalle(true, anio));
			
		/*ANULACION DE SUBSCRIPCION CON SWISS*/
		actionConditions.append(getQueryAnulacionObraSocialesDetalle(true, anio)); 

		/* VENTA NUEVO PRODUCTO */
		actionConditions.append(getQueryVentaProductoDetalle(true, anio)); 
	
		/*ANULACION DE VENTA PRODUCTO*/
		actionConditions.append(getQueryAnulaProductoDetalle(true, anio)); 
		
		/* INGRESO MOVIMIENTO CAJA */
		actionConditions.append(getQueryMovimientoDeCajaIngresosDetalle(true, anio)); 
		
		/* EGRESO MOVIMIENTO CAJA */
		actionConditions.append(getQueryMovimientoDeCajaEgresosDetalle(true, anio));
	
		/* FALTA CONSULTAR LOS SUELDOS POR ACTIVIDAD*/
		actionConditions.append(getQuerySueldosDetalle(true, anio));   
		
		/* FALTA CONSULTAR LOS SUELDOS POR ACTIVIDAD*/
		actionConditions.append(getQueryGastosPrecioCostoProductosDetalle(true, anio));
		
		actionConditions.append( " ) AS consulta_general    order by b ");
			
		Statement stat=null;
		ResultSet rs = null;
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(actionConditions.toString());

			while (rs.next()) {
				String tipo=rs.getString(1);
				Integer idActividad =rs.getInt(2);
				Double cantidadPago =rs.getDouble(3);
				String mesS =rs.getString(4);

				if(cantidadPago == null)
					cantidadPago= new Double(0);
				
				
				String dada=String.valueOf(cantidadPago );

				if(dada!= null && dada.endsWith(",0")){
					String sas=dada.substring(0, dada.indexOf(","));
					cantidadPago= new Double(sas);
				}else if(cantidadPago != null){
					cantidadPago= new Double(Math.rint(cantidadPago*100)/100);
				}				
				if(idActividad == null)
					idActividad=-1;
				
				int mes=Integer.parseInt(mesS);

				if(tipo.equalsIgnoreCase("nueva") || tipo.equalsIgnoreCase("SaldarDeuda") || tipo.equalsIgnoreCase("Pospone") ){		
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, idActividad, cantidadPago, true, false, false);
				}
				
				if(tipo.equalsIgnoreCase("VentaProducto")){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, ACTIVIDAD_VENTA_PRODUCTOS, cantidadPago, true, false, false);
				}
				
				if(tipo.equalsIgnoreCase("IngresosMovimientoCaja")){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, ACTIVIDAD_MOVIMIENTO_CAJA, cantidadPago, true, false, false);
				}
				
				if(tipo.equalsIgnoreCase("ObraSociales") ){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, idActividad, cantidadPago, true, true, false);
				}
				
				if(tipo.equalsIgnoreCase("AnuloObraSociales") ){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, idActividad, cantidadPago, false, true, false);
				}
				
				if(tipo.equalsIgnoreCase("AnulaVentaProducto") ){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, ACTIVIDAD_VENTA_PRODUCTOS, cantidadPago, false, false, false);
				}
				
				if(tipo.equalsIgnoreCase("EgresosMovimientoCaja") ){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, ACTIVIDAD_MOVIMIENTO_CAJA, cantidadPago, true, false, true);
				}
				
				if(tipo.equalsIgnoreCase("anulo")){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, idActividad, cantidadPago, false, false, false);
				}
				
				if(tipo.equalsIgnoreCase("Sueldos")){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, idActividad, cantidadPago, true, false, true);
				}
				
				if(tipo.equalsIgnoreCase("gastosPrecioCostoProductos")){
					listaDetalle= actualizarPulsoMesDetalle(listaDetalle, mes, ACTIVIDAD_VENTA_PRODUCTOS, cantidadPago, true, false, true);
				}

			}
			
			listaDetalle=actualizarTotalesPulsoMesDetalle(listaDetalle);
			
			return listaDetalle;
			
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
	

	private List<PulsoClinica> actualizarPulsoMes(List<PulsoClinica> lista, int mes, int tipoMovimiento, Double dinero ){
		
		if(lista != null){
			for (PulsoClinica pulsoClinica : lista) {
				
				if(pulsoClinica.getMesId().intValue() == mes){
					if(tipoMovimiento == TipoMovimientoCajaEnum.EGRESO.toInt()){
						pulsoClinica.setCantidadIngreso(pulsoClinica.getCantidadIngreso() - dinero);
						
					}else if(tipoMovimiento == TipoMovimientoCajaEnum.INGRESO.toInt()){
						pulsoClinica.setCantidadIngreso(pulsoClinica.getCantidadIngreso() + dinero);	
					}
					return lista;
				}
			}
		}
		return lista;
	}	

	public List<CajaMovimientoView> findAllConJdbcAControlar(Connection cnx , Date fechaDesde , Date fechaHasta){
		List<CajaMovimientoView> nuevo= new ArrayList<CajaMovimientoView> ();
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String fechaD = format1.format(fechaDesde);    
		String fechaH = format1.format(fechaHasta); 
		
		String query= "select a, b, c,d,e, f, g from ( ";

		query=query+ "select 'Anula Inscripcion' a, to_char(caja.fecha,'YYYY-MM-DD-HH') b, caja.valor c,caja.concepto d, cupoac.cupos e,use.name ||' ' || use.lastname f,cli.nombre||' '|| cli.apellido g " +
				"   from cajamovimiento caja";  
		query=query+ "	inner join subscripcion  subs on (caja.idSubscripcion= subs.id) "; 
		query=query+ "	inner join cupoactividad  cupoac on (cupoac.idsubscripcion= subs.id) ";
		query=query+ "	inner join cliente cli on (caja.idcliente = cli.id) ";
		query=query+ "	inner join users use on (use.id= caja.idusuariogeneromovimiento) ";
		query=query+ "	where 1=1   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ";
		query=query+ "	and tipomovimiento=0   ";
		query=query+ "	 and upper(caja.concepto) like Upper('%Anul%') ";

		query=query+ "	union all  ";
		
		query=query+ "select 'Se Devolvio Cupo' a, to_char(caja.fecha,'YYYY-MM-DD-HH') b, caja.valor c,caja.concepto d, cupoac.cupos e,use.name ||' ' || use.lastname f,cli.nombre||' '|| cli.apellido g " +
				"   from cajamovimiento caja";  
		query=query+ "	inner join subscripcion  subs on (caja.idSubscripcion= subs.id) "; 
		query=query+ "	inner join cupoactividad  cupoac on (cupoac.idsubscripcion= subs.id) ";
		query=query+ "	inner join cliente cli on (caja.idcliente = cli.id) ";
		query=query+ "	inner join users use on (use.id= caja.idusuariogeneromovimiento) ";
		query=query+ "	where 1=1   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ";
		query=query+ "	and tipomovimiento=0   ";
		query=query+ "	 and upper(caja.concepto) like Upper('%Dev%') ";
		
		query=query+ "	union all  ";

		query=query+ "	select 'Matricula Gratis' a, to_char(caja.fecha,'YYYY-MM-DD-HH') b, caja.valor c,caja.concepto d , 0 e,use.name ||' ' || use.lastname f,' ' g  " +
				"  from cajamovimiento caja  ";
		query=query+ "	inner join users use on (use.id= caja.idusuariogeneromovimiento) ";
		query=query+ "	where 1=1    ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ";
		query=query+ "	and tipomovimiento=1  and valor=0 ";
		query=query+ "	and caja.idsubscripcion is null and caja.idventaproducto is null  ";
		query=query+ "	union all  ";

		query=query+ "	select 'Venta de Productos' a, to_char(caja.fecha,'YYYY-MM-DD-HH') b, caja.valor c,caja.concepto d , 0 e,use.name ||' ' || use.lastname f,' ' g  " +
				"  from cajamovimiento caja  ";
		query=query+ "	inner join users use on (use.id= caja.idusuariogeneromovimiento) ";
		query=query+ "	where 1=1    ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ";
		query=query+ "	and tipomovimiento=0  ";
		query=query+ "	and caja.idsubscripcion is null and caja.idventaproducto is not null  ";
		query=query+ "	union all  ";
		
		query=query+ "	select 'Egresos desde Caja' a, to_char(caja.fecha,'YYYY-MM-DD HH:MM') b, caja.valor c,caja.concepto d , 0 e,use.name ||' ' || use.lastname f,' ' g   " +
				" from cajamovimiento caja  ";
		query=query+ "	inner join users use on (use.id= caja.idusuariogeneromovimiento) ";
		query=query+ "	where 1=1    ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ";
		query=query+ "	 and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ";
		query=query+ "	and tipomovimiento=0  ";
		query=query+ "	and caja.idsubscripcion is null and caja.idventaproducto is null  ";

		query=query+ "	) AS consulta_general  ";
		query=query+ "	order by  a, b  ";

			
		Statement stat=null;
		ResultSet rs = null;
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
		
			while (rs.next()) {
			CajaMovimientoView cajaMov= new CajaMovimientoView();
				
				if(rs.getObject("a") != null){
					String tipo=rs.getString("a");
					cajaMov.setTipo(tipo);
				}
				if(rs.getObject("b") != null){
					String tipo=rs.getString("b");
					cajaMov.setFecha(tipo);
				}		
				if(rs.getObject("c") != null){
					Integer tipo=rs.getInt("c");
					if(tipo != null)
						cajaMov.setValor(String.valueOf(tipo));
				}
				if(rs.getObject("d") != null){
					String tipo=rs.getString("d");
					cajaMov.setConcepto(tipo);
				}
				if(rs.getObject("e") != null){
					Integer tipo=rs.getInt("e");
					if(tipo != null)
						cajaMov.setCantCuposTenia(String.valueOf(tipo));
				}
				if(rs.getObject("f") != null){
					String tipo=rs.getString("f");
					cajaMov.setResponsable(String.valueOf(tipo));
				}
				if(rs.getObject("g") != null){
					String tipo=rs.getString("g");
					cajaMov.setCliente(tipo);
				}
				nuevo.add(cajaMov);	
		}
			return nuevo;
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

	private boolean esUsuarioAdmnOGerente(Connection cnx, int userName){
		String query= " select 1 from users u inner join user_group ug on (u.id= ug.user_id) " +
				"where u.id="+userName +" and ug.group_id in(19, 1)";

		Statement stat=null;
		ResultSet rs = null;
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
		
			if (rs.next()) {
				return true;
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
//				if(cnx != null)
//					cnx.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			}
		}
		return false;
	}
	
	public SumasTotalesMovimientos findAllConJdbc(Connection cnx , Date fechaDesde, Date fechaHasta, Actividad curso, boolean esActividadSeteada, 
			TipoMovimientoCajaEnum tipoMov, User usuarioSelected, boolean tieneFiltroSoloObraSocial, Long promocionBancoID, int userName, 
			Date fechaYHoraDesde, SucursalEnum suc) throws DAOException{
		SumasTotalesMovimientos nuevo= new SumasTotalesMovimientos();

		StringBuilder actionConditions= new StringBuilder("select a, b, c from ( ");
		
		Calendar startC = Calendar.getInstance();
		startC.setTime(new Date());
		startC.set(Calendar.HOUR_OF_DAY, 00);
		startC.set(Calendar.MINUTE, 00);
		startC.set(Calendar.SECOND, 00);
		startC.set(Calendar.MILLISECOND, 00);
		
		Calendar start2 = Calendar.getInstance();
		start2.setTime(fechaDesde);
		start2.set(Calendar.HOUR_OF_DAY, 00);
		start2.set(Calendar.MINUTE, 00);
		start2.set(Calendar.SECOND, 00);
		start2.set(Calendar.MILLISECOND, 00);

		
		// verificar si el usuario es un usuario con perfil administrador o gerente 
		if(esUsuarioAdmnOGerente(cnx, userName) && startC.getTime().compareTo(start2.getTime())!= 0){
			fechaYHoraDesde= null;
		}

		
		if(!esActividadSeteada){
			// Si no se selecciono en la Actividad: Venta de Productos
			
			if(tieneFiltroSoloObraSocial){
				// se selecciono filtro de obtener por obra Social solamente , se considera que figuran TODOS 

				if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
					/*   VENTA OBRAS SOCIALES */
					actionConditions.append(getQueryVentaObraSocial(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));

					/*ANULACION DE SUBSCRIPCION CON OBRAS SOCIALES*/
					actionConditions.append(getQueryAnulacionObraSocial(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
				}
								
			}else if(tipoMov == null || 
					(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.TODOS.toInt())) {
				
				/* VENTA DE NUEVA SUBSCRIPCION */
				actionConditions.append(getQueryNuevaSubscripcion(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				/* VENTA DE NUEVA SUBSCRIPCION: COPAGOS */
				actionConditions.append(getQueryNuevaSubscripcionCopagos(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));

				/* VENTA DE POSPONE SUBSCRIPCION */
				actionConditions.append(getQueryPosponerSubscripcion(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				
				if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
					/* VENTA DE INGRESO A CLASE POR OS PAGA ADICIONAL */
					actionConditions.append(getQueryNuevoIngresoPorObraSocialAClase(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
					/* VENTA DE INGRESO A CLASE POR OS PAGA ADICIONAL */
					actionConditions.append(getQueryNuevoEgresoPorObraSocialDeClase(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));

					/*   VENTA OBRAS SOCIALES */
					actionConditions.append(getQueryVentaObraSocial(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
																
					/*ANULACION DE SUBSCRIPCION CON OBRAS SOCIALES*/
					actionConditions.append(getQueryAnulacionObraSocial(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
				}

				/* ANULACION DE SUBSCRIPCION */
				actionConditions.append(getQueryAnulaSubscripcion(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				actionConditions.append(getQueryGastos(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
				
				if(curso == null){
					
					if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
						/* VENTA NUEVO PRODUCTO */
						actionConditions.append(getQueryVentaProducto(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
						/*ANULACION DE VENTA PRODUCTO*/
						actionConditions.append(getQueryAnulaProducto(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					}
					/* INGRESO MOVIMIENTO CAJA */
					actionConditions.append(getQueryMovimientoDeCajaIngresos(true, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
					
					/* EGRESO MOVIMIENTO CAJA */
					actionConditions.append(getQueryMovimientoDeCajaEgresos(true, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				}
								
				
			}else if(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
				/* VENTA DE NUEVA SUBSCRIPCION */
				actionConditions.append(getQueryNuevaSubscripcion(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				actionConditions.append(getQueryNuevaSubscripcionCopagos(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));

				/* VENTA DE POSPONE SUBSCRIPCION */
				actionConditions.append(getQueryPosponerSubscripcion(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				
				if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
	
					/*   VENTA OBRA SOCIALES */
					actionConditions.append(getQueryVentaObraSocial(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
							
					/* VENTA DE INGRESO A CLASE POR OS PAGA ADICIONAL */
					actionConditions.append(getQueryNuevoIngresoPorObraSocialAClase(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
				}
				
				if(curso == null){
				
					if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
						/* VENTA NUEVO PRODUCTO */
						actionConditions.append(getQueryVentaProducto(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					}
					/* INGRESO MOVIMIENTO CAJA */
					actionConditions.append(getQueryMovimientoDeCajaIngresos(true, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));					
					
				}
				
				
			}else if(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.EGRESO.toInt()){
				
				/* ANULACION DE SUBSCRIPCION */
				actionConditions.append(getQueryAnulaSubscripcion(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
				
				actionConditions.append(getQueryGastos(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));

				if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
	
					/*ANULACION DE SUBSCRIPCION CON OBRAS SOCIALES*/
					actionConditions.append(getQueryAnulacionObraSocial(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
								
					/* VENTA DE INGRESO A CLASE POR OS PAGA ADICIONAL */
					actionConditions.append(getQueryNuevoEgresoPorObraSocialDeClase(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
				}
				
				if(curso == null){
					
					if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
	
						/*ANULACION DE VENTA PRODUCTO*/
						actionConditions.append(getQueryAnulaProducto(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					}
					
					/* EGRESO MOVIMIENTO CAJA */
					actionConditions.append(getQueryMovimientoDeCajaEgresos(true, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde, suc));
					
				}
					
			}
		
		}else{
			// SI Actividad tenia seteado 'VENTA DE PRODUCTOS'
			if(suc != null && suc.toInt() != SucursalEnum.MAIPU.toInt()){
	
				/* VENTA DE PRODUCTOS */
				if (tipoMov == null || 
						(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.TODOS.toInt())) {
		
					
					/* VENTA NUEVO PRODUCTO */
					actionConditions.append(getQueryVentaProducto(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
					/*ANULACION DE VENTA PRODUCTO*/
					actionConditions.append(getQueryAnulaProducto(true, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
				}else if(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
		
					/* VENTA NUEVO PRODUCTO */
					actionConditions.append(getQueryVentaProducto(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
				}else if(tipoMov != null && tipoMov.toInt() == TipoMovimientoCajaEnum.EGRESO.toInt()){
					
					/*ANULACION DE VENTA PRODUCTO*/
					actionConditions.append(getQueryAnulaProducto(false, curso, fechaDesde, fechaHasta, usuarioSelected, promocionBancoID, fechaYHoraDesde));
					
				}
			}
		}
			
		actionConditions.append( " ) AS consulta_general  ");
			
		Statement stat=null;
		ResultSet rs = null;
		ArrayList<SumasTotalObraSocial> listaObrasSociales= new ArrayList();
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(actionConditions.toString());
			
			double pagosPorTipoEfectivo=0;
			double pagosPorTipoDebito=0;
			double pagosPorTipoTarjeta=0;
			while (rs.next()) {
				String tipo=rs.getString(1);
				Integer tipoPago =rs.getInt(2);
				Double cantidadPago =rs.getDouble(3);
				
				if(tipo.equalsIgnoreCase("nueva") || tipo.equalsIgnoreCase("SaldarDeuda") 
							|| tipo.equalsIgnoreCase("Pospone") || tipo.equalsIgnoreCase("VentaProducto") || tipo.equalsIgnoreCase("copagos")  
							|| tipo.equalsIgnoreCase("IngresosMovimientoCaja") || tipo.equalsIgnoreCase("nuevoingresoOS") ){
					if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.DEBITO.toInt()){
						pagosPorTipoDebito= pagosPorTipoDebito+ cantidadPago;
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()){
						pagosPorTipoEfectivo= pagosPorTipoEfectivo+ cantidadPago;
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosPorTipoTarjeta= pagosPorTipoTarjeta+ cantidadPago;
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosPorTipoTarjeta= pagosPorTipoTarjeta+ cantidadPago;
					}
				}
				
				if(tipo.equalsIgnoreCase("anulo") || tipo.equalsIgnoreCase("AnulaVentaProducto") 
						|| tipo.equalsIgnoreCase("Gasto")
						|| tipo.equalsIgnoreCase("EgresosMovimientoCaja") || tipo.equalsIgnoreCase("nuevoegresoOS")){
					if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.DEBITO.toInt()){
						pagosPorTipoDebito= pagosPorTipoDebito- cantidadPago;
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()){
						pagosPorTipoEfectivo= pagosPorTipoEfectivo- cantidadPago;
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosPorTipoTarjeta= pagosPorTipoTarjeta- cantidadPago;		
					}else 	if(tipoPago.intValue() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosPorTipoTarjeta= pagosPorTipoTarjeta- cantidadPago;		
					}
				}
				
				if(tipo.startsWith("ObraSocial-")){
					String obraSocial=tipo.substring(11, tipo.length());
					
					SumasTotalObraSocial sumaobraSo=existe(listaObrasSociales, obraSocial);
					if(sumaobraSo != null){
						sumaobraSo.setTotal(sumaobraSo.getTotal() + cantidadPago);
						
					}else{
						SumasTotalObraSocial sts= new SumasTotalObraSocial();
						sts.setObraSocial(obraSocial);
						sts.setTotal(cantidadPago);
						
						listaObrasSociales.add(sts);						
					}
				}
				
				if(tipo.startsWith("AnuloObraSocial-")){
					String obraSocial=tipo.substring(16, tipo.length());
					SumasTotalObraSocial sumaobraSo=existe(listaObrasSociales, obraSocial);
					if(sumaobraSo != null){
						sumaobraSo.setTotal(sumaobraSo.getTotal() - cantidadPago);
						
					}else{
						SumasTotalObraSocial sts= new SumasTotalObraSocial();
						sts.setObraSocial(obraSocial);
						sts.setTotal(-cantidadPago);
						
						listaObrasSociales.add(sts);						
					}
				}
			}
			nuevo.setDebito(pagosPorTipoDebito);
			nuevo.setTarjeta(pagosPorTipoTarjeta);
			nuevo.setEfectivo(pagosPorTipoEfectivo);
			nuevo.setListaObrasSociales(listaObrasSociales);
			
			return nuevo;
			
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

		public 	SumasTotalObraSocial existe(ArrayList<SumasTotalObraSocial> listaObrasSociales, String obraSocial){
			
			if(listaObrasSociales != null){
				
				for (SumasTotalObraSocial sumasTotalObraSocial : listaObrasSociales) {
					
					if(sumasTotalObraSocial.getObraSocial().equalsIgnoreCase(obraSocial))
						return sumasTotalObraSocial;
					
					
				}
			}
			return null;
		}

		
		private String getQueryNuevaSubscripcionDetalle(boolean withUnion, Long anio){
			/* NUEVA SUBSCRIPCION */
			StringBuilder actionConditions = new StringBuilder();
			actionConditions.append( " select 'nueva' a,cupoPorAct.idActividad b, sum(ps.cantidaddinero + ps.adicional) c , to_char(subs.fechaingresosubscripcion,'MM') d " +
					"  from subscripcion subs " ); 
			actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
			actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id )  ");
			actionConditions.append( " inner join Curso  curso on (cupoPorAct.idCurso= curso.id )     ");

				actionConditions.append(" where 1=1  and curso.vencimiento!=5 ");

				Long nuevoAnio=anio +1;
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'  ");
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ anio+"-01-01' ");		
				
				actionConditions.append( " group by to_char(subs.fechaingresosubscripcion,'MM') ,cupoPorAct.idActividad  ");
										 
				return actionConditions.toString();
		}

		
		private String getQueryNuevaSubscripcionDetalleMaipu(boolean withUnion, Long anio){
			/* NUEVA SUBSCRIPCION */
			StringBuilder actionConditions = new StringBuilder();
			actionConditions.append( " 	select 'nueva' a, con.idcurso b, sum(con.importecondescuento) c , con.idquincena d  " +
					"  from subscripcion subs " ); 
			actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
			actionConditions.append( " inner join concepto  con on (con.idSubscripcion= subs.id )    ");
			actionConditions.append( " inner join Curso  curso on (con.idCurso= curso.id )     ");
				actionConditions.append(" where 1=1  and curso.vencimiento=5 and con.idquincena is not null	 ");

				Long nuevoAnio=anio +1;
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') <='"+ nuevoAnio+"-04-01'  ");
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ anio+"-04-01' ");		
				
				actionConditions.append( " group by con.idquincena ,con.idcurso  ");
										 
				return actionConditions.toString();
		}

		private String getQuerySueldosDetalleMaipu(boolean withUnion, Long anio){
			StringBuilder actionConditions = new StringBuilder();

			if(withUnion)
				actionConditions.append( " union all ");
			
			actionConditions.append( " select 'gastosEgresos' a, gs.tipogasto b, sum(gs.dinero) c, gs.quincena d  from ");  
			actionConditions.append( "  gastosmaipu gs  where 1=1 ");
			Long nuevoAnio=anio +1;
			actionConditions.append( " and anio="+ nuevoAnio );

			actionConditions.append( " group by gs.quincena ,gs.tipogasto  ");
			return actionConditions.toString();
		}
		
		private String getQueryNuevoIngresoPorObraSocialAClase(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, 
				User usuarioSelected, Long promocionBancoID, Date fechaYHoraDesde){
			/* NUEVO PAGO ADICIONAL */
			StringBuilder actionConditions = new StringBuilder();
			
			String fechaHastaPorCierreCaja=null;
			if(fechaYHoraDesde != null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
				fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
			}
			if(withUnion)
				actionConditions.append( " union all ");
			
			actionConditions.append( " select 'nuevoingresoOS' a,0 b, sum(caja.valor) c from cajamovimiento caja  " ); 
			actionConditions.append( "	 where 1=1 "); 
			actionConditions.append( "	 and  caja.tipomovimiento=1 AND  Upper(caja.concepto) like Upper('%Pago adicional%') ");
			
			if (fechaDesde != null && fechaHasta != null) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaD = format1.format(fechaDesde);    
				String fechaH = format1.format(fechaHasta); 

				if(fechaH.equalsIgnoreCase(fechaD)){
					actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}else{
					actionConditions.append( " and (( to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'  ");
					actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"' ))");
				}

			}
			
			if (curso!= null ){
				actionConditions.append( " and caja.idsubscripcion in (select subs.id from subscripcion subs ");
				actionConditions.append( 		" inner join concepto con on (con.idsubscripcion= subs.id)" );
				actionConditions.append( 		" where subs.id= caja.idsubscripcion  " );
				actionConditions.append( 		" and Upper(concepto) like Upper('%Pago Adicional%') and idactividad='"+curso.getId()+"' ) " );
			}
				
			if(usuarioSelected != null){
				actionConditions.append( " AND ( caja.idusuariogeneromovimiento= "+usuarioSelected.getId() +")" );
			}
			
			if(promocionBancoID != null){
				actionConditions.append( " AND caja.idsubscripcion in (select s.id from subscripcion s " +
															"	 inner join pagosporsubscripcion p on (s.id= p.idsubscripcion)	" +
															"	 where 1=1 and s.id=caja.idsubscripcion AND p.idbancopromocion='"+promocionBancoID + "' )");
			}
			
			if(fechaHastaPorCierreCaja != null){
				actionConditions.append( "  and caja.fecha >='"+fechaHastaPorCierreCaja + "'");
			}				
			return actionConditions.toString();
		}
		
		
		private String getQueryNuevoEgresoPorObraSocialDeClase(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, 
				User usuarioSelected, Long promocionBancoID, Date fechaYHoraDesde ){
			/* NUEVO PAGO ADICIONAL */
			StringBuilder actionConditions = new StringBuilder();
			String fechaHastaPorCierreCaja=null;
			if(fechaYHoraDesde != null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
				fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
			}

			if(withUnion)
				actionConditions.append( " union all ");
			
			actionConditions.append( " select 'nuevoegresoOS' a,0 b, sum(caja.valor) c from cajamovimiento caja  " ); 
			actionConditions.append( "	 where 1=1 "); 
			actionConditions.append( "	 and  caja.tipomovimiento=0 AND  Upper(caja.concepto) like Upper('%Pago adicional%') ");
			
			if (fechaDesde != null && fechaHasta != null) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaD = format1.format(fechaDesde);    
				String fechaH = format1.format(fechaHasta); 

				if(fechaH.equalsIgnoreCase(fechaD)){
					actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}else{
					actionConditions.append( " and (( to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'  ");
					actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"' ))");
				}
			}
			
			if (curso!= null ){
				actionConditions.append( " and caja.idsubscripcion in (select subs.id from subscripcion subs ");
				actionConditions.append( 		" inner join concepto con on (con.idsubscripcion= subs.id)" );
				actionConditions.append( 		" where subs.id= caja.idsubscripcion  " );
				actionConditions.append( 		" and Upper(concepto) like Upper('%Pago Adicional%') and idactividad='"+curso.getId()+"' ) " );
			}
				
			if(usuarioSelected != null){
				actionConditions.append( " AND ( caja.idusuariogeneromovimiento= "+usuarioSelected.getId() +")" );
			}
			
			if(promocionBancoID != null){
				actionConditions.append( " AND caja.idsubscripcion in (select s.id from subscripcion s " +
												"	 inner join pagosporsubscripcion p on (s.id= p.idsubscripcion)	" +
											"	 where 1=1 and s.id=caja.idsubscripcion AND p.idbancopromocion='"+promocionBancoID + "' )");
			}
			if(fechaHastaPorCierreCaja != null){
				actionConditions.append( "  and caja.fecha >='"+fechaHastaPorCierreCaja + "'");
			}
			return actionConditions.toString();
		}
		
	private String getQueryNuevaSubscripcion(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde, SucursalEnum suc){
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		
		/* NUEVA SUBSCRIPCION */
		StringBuilder actionConditions = new StringBuilder();
		actionConditions.append( " select 'nueva' a,ps.idtipodepago b, sum(ps.cantidaddinero + ps.adicional) c from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
			if (curso!= null ){
				actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id and idActividad= '"+curso.getId()+"' )  ");
			}
			actionConditions.append(" where 1=1  ");

			if (fechaDesde != null && fechaHasta != null) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaD = format1.format(fechaDesde);    
				String fechaH = format1.format(fechaHasta); 

				
				actionConditions.append( " and (( " );
				
				if(fechaHastaPorCierreCaja != null){
					actionConditions.append( " ( subs.fechayhoracreacion >='"+fechaHastaPorCierreCaja + "' OR " +
							"   (subs.fechayhorasaldasubscripcion >='"+fechaHastaPorCierreCaja + "' and  ps.saldadadeuda is true  ) )  ");
				}
								
				if(usuarioSelected != null){
					if(fechaHastaPorCierreCaja != null)
						actionConditions.append( " AND ");

					actionConditions.append( "  subs.idusuariocreosubscripcion= "+usuarioSelected.getId());
				}
				if(fechaH.equalsIgnoreCase(fechaD)){
					
					if(usuarioSelected != null)
						actionConditions.append( " AND ");

					actionConditions.append( " ps.saldadadeuda is not true  AND to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ fechaH+"'  ");
				}else{
					if(usuarioSelected != null)
						actionConditions.append( " AND ");

					actionConditions.append( " ps.saldadadeuda is not true  AND to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') <='"+ fechaH+"'  ");
					actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ fechaD+"' )");
				}
				
				
				actionConditions.append( " ) OR (  ");
				if(usuarioSelected != null){
					actionConditions.append( " subs.idusuariocreosubscripcion= "+usuarioSelected.getId() );
				}

				if(fechaHastaPorCierreCaja != null){
					if(usuarioSelected != null)
						actionConditions.append( " AND ");
					actionConditions.append( " ( ps.saldadadeuda is not true and  ps.fechaingresodefault >='"+fechaHastaPorCierreCaja + "' )  ");
				}

				if(fechaH.equalsIgnoreCase(fechaD)){
					if(fechaHastaPorCierreCaja != null)
						actionConditions.append( " AND ");

					actionConditions.append( "  to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"' ) ");
				}else{
					if(fechaHastaPorCierreCaja != null)
						actionConditions.append( " AND ");

					actionConditions.append( " to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ fechaH+"'   ");
					actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"' ) ");
				}

				
				actionConditions.append( " OR (  ");
				if(fechaHastaPorCierreCaja != null){
					
					actionConditions.append( " ( subs.fechayhoracreacion >='"+fechaHastaPorCierreCaja + "' OR " +
							"  ( subs.fechayhorasaldasubscripcion >='"+fechaHastaPorCierreCaja + "' and  ps.saldadadeuda is true ) )  ");
				}

				if(usuarioSelected != null){
					if(fechaHastaPorCierreCaja != null)
						actionConditions.append( " AND ");

					actionConditions.append( " subs.idusuariosaldasubscripcion= "+usuarioSelected.getId() );
				}
				
				if(fechaH.equalsIgnoreCase(fechaD)){
					if(usuarioSelected != null)
						actionConditions.append( " AND ");

					actionConditions.append( "  to_char(subs.fechayhorasaldasubscripcion,'YYYY-MM-DD') >='"+ fechaD+"' and  ps.saldadadeuda is true ) ");
				}else{
					if(usuarioSelected != null)
						actionConditions.append( " AND ");

					actionConditions.append( "  ps.saldadadeuda is true and to_char(subs.fechayhorasaldasubscripcion,'YYYY-MM-DD') <='"+ fechaH+"'   ");
					actionConditions.append( " and to_char(subs.fechayhorasaldasubscripcion,'YYYY-MM-DD') >='"+ fechaD+"'  )");
				}
			}
			
			actionConditions.append( " ) ");
			if(promocionBancoID != null){
				actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
			}
						
			if(suc != null){
				actionConditions.append( "  and ps.sucursal="+suc.toInt() );
			}
			
			actionConditions.append( " group by ps.idtipodepago  ");
		
			return actionConditions.toString();
	}
	
	private String getQueryNuevaSubscripcionCopagos(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde, SucursalEnum suc){
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
				
		/* COPAGOS */
		StringBuilder actionConditions = new StringBuilder();
		
		if(withUnion)
			actionConditions.append( " union all ");

		actionConditions.append( " select 'copagos' a,ps.idtipodepago b, sum(ps.cantidaddinero + ps.adicional) c from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
			if (curso!= null ){
				actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id and idActividad= '"+curso.getId()+"' )  ");
			}
			actionConditions.append(" where 1=1  ");

			if (fechaDesde != null && fechaHasta != null) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaD = format1.format(fechaDesde);    
				String fechaH = format1.format(fechaHasta); 

				actionConditions.append( " and  subs.idusuariocreosubscripcion != "+usuarioSelected.getId());

				if(fechaH.equalsIgnoreCase(fechaD)){
					actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}else{
					actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ fechaH+"'   ");
					actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}
				
				actionConditions.append( " and ps.escopago is true    ");
			}
			
			if(promocionBancoID != null){
				actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
			}
			
			if(fechaHastaPorCierreCaja != null){
				actionConditions.append( "  and  (ps.fechaingresodefault >='"+fechaHastaPorCierreCaja + "' )");
			}
			
			if(suc != null){
				actionConditions.append( "  and ps.sucursal="+suc.toInt() );
			}
			
			actionConditions.append( " group by ps.idtipodepago  ");
		
			return actionConditions.toString();
	}
	
	private String getQueryPosponerSubscripcionDetalle(boolean withUnion, Long anio){
		
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;
		/* POSPUSO SUBSCRIPCION */
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'Pospone' a, cupoPorAct.idActividad b, sum(cupoPorAct.posponeadicional + cupoPorAct.posponeadicional2) c, " +
				"  to_char(cupoPorAct.posponefechayhora,'MM') d " +
				"   from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
		actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id )  ");

		actionConditions.append(" where 1=1  ");

		actionConditions.append( " and ((to_char(cupoPorAct.posponefechayhora,'YYYY-MM-DD') <= '"+ nuevoAnio+"-01-01'   ");  
			actionConditions.append( " and to_char(cupoPorAct.posponefechayhora,'YYYY-MM-DD') >='"+ anio+"-01-01'  ) ");
			actionConditions.append( " or  ");
			actionConditions.append( " (to_char(cupoPorAct.posponefechayhora2,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'  ");  
			actionConditions.append( " and to_char(cupoPorAct.posponefechayhora2,'YYYY-MM-DD') >='"+ anio+"-01-01'  )) ");	

			actionConditions.append( " group by to_char(cupoPorAct.posponefechayhora,'MM') ,cupoPorAct.idActividad  ");
			
			return actionConditions.toString();
	}
	
	private String getQueryPosponerSubscripcion(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde, SucursalEnum suc){
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		
		StringBuilder actionConditions = new StringBuilder();

		/* POSPUSO SUBSCRIPCION */
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'Pospone' a, ps.idtipodepago b, sum(cupoPorAct.posponeadicional + cupoPorAct.posponeadicional2) c  " +
				" from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
		actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id )  ");
		actionConditions.append(" where 1=1  ");

		if (curso!= null ){
			actionConditions.append( " AND cupoPorAct.idActividad= '"+curso.getId()+"'   ");
		}
		
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			
			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and ((to_char(cupoPorAct.posponefechayhora,'YYYY-MM-DD') >='"+ fechaD+"'  ) ");
				actionConditions.append( " or  ");
				actionConditions.append( " (to_char(cupoPorAct.posponefechayhora2,'YYYY-MM-DD') >='"+ fechaD+"'  )) ");
			}else{
				actionConditions.append( " and ((to_char(cupoPorAct.posponefechayhora,'YYYY-MM-DD') <= '"+ fechaH+"'   ");  
				actionConditions.append( " and to_char(cupoPorAct.posponefechayhora,'YYYY-MM-DD') >='"+ fechaD+"'  ) ");
				actionConditions.append( " or  ");
				actionConditions.append( " (to_char(cupoPorAct.posponefechayhora2,'YYYY-MM-DD') <='"+ fechaH+"'  ");  
				actionConditions.append( " and to_char(cupoPorAct.posponefechayhora2,'YYYY-MM-DD') >='"+ fechaD+"'  )) ");
			}
		}
			
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  " +
					"  cupoPorAct.idusuarioposponesubscripcion = "+usuarioSelected.getId()   
					+" OR cupoPorAct.idusuarioposponesubscripcion2 = "+usuarioSelected.getId() +")" );
		}

		if(promocionBancoID != null){
			actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
		}
		
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( " and ( cupoPorAct.posponefechayhora >='"+ fechaHastaPorCierreCaja+"' or " +
											" cupoPorAct.posponefechayhora2 >='"+ fechaHastaPorCierreCaja+"' )");
		}
		
		if(suc != null){
			actionConditions.append( "  and ps.sucursal="+suc.toInt() );
		}
		actionConditions.append( " group by ps.idtipodepago  ");
		
		return actionConditions.toString();
	}

	private String getQuerySaldaSubscripcionDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'SaldarDeuda' a, cupoPorAct.idActividad b, sum(ps.cantidaddinero + ps.adicional) c , to_char(subs.fechayhorasaldasubscripcion,'MM') d" +
				"  from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
		actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id)  ");

			actionConditions.append(" where 1=1  ");
			actionConditions.append( " and to_char(subs.fechayhorasaldasubscripcion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
			actionConditions.append( " and to_char(subs.fechayhorasaldasubscripcion,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
						
			actionConditions.append( " and ps.saldadadeuda is true ");
			actionConditions.append( " group by to_char(subs.fechayhorasaldasubscripcion,'MM') ,cupoPorAct.idActividad  ");
			
			return actionConditions.toString();

	}


	private String getQueryVentaObraSocialesDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'ObraSociales' a, con.idactividad b, sum(con.precioPorClaseOSesionPagaObraSocial * con.cantSesiones) c, to_char(subs.fechaingresosubscripcion,'MM') d    " +
				" from subscripcion subs " ); 
		actionConditions.append( "		inner join concepto con on (con.idSubscripcion= subs.id )" ); 
		actionConditions.append(" where 1=1  ");

		actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
					
		actionConditions.append( " and con.tipodescuento in (1, 2) and con.idobrasocial in ( select id from ObraSocial)	");

		actionConditions.append( " group by to_char(subs.fechaingresosubscripcion,'MM'), con.idactividad	");

		return actionConditions.toString();
	}
	
	private String getQueryVentaObraSocial(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde){
		StringBuilder actionConditions = new StringBuilder();
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'ObraSocial-'||os.nombre a, 1 b, sum(con.precioPorClaseOSesionPagaObraSocial * con.cantSesiones) c   from subscripcion subs " ); 
		actionConditions.append( "		inner join concepto con on (con.idSubscripcion= subs.id " ); 
		if (curso!= null ){
			actionConditions.append( " and con.idactividad= '"+curso.getId()+"' ) ");
		}else{
			actionConditions.append( " ) ");
		}
		actionConditions.append(" inner join obrasocial os on (con.idobrasocial=os.id ) where 1=1  ");

		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(subs.fechaingresosubscripcion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}

					
		}
		
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  subs.idusuariocreosubscripcion= "+usuarioSelected.getId()+")" );
		}


		if(promocionBancoID != null){
			actionConditions.append( " AND subs.id in (select s.id from subscripcion s " +
													"	 inner join pagosporsubscripcion p on (s.id= p.idsubscripcion)	" +
													"	 where 1=1 and s.id=subs.id AND p.idbancopromocion='"+promocionBancoID + "' )");
		}
	
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and  (subs.fechayhoracreacion >='"+fechaHastaPorCierreCaja + "' OR " +
					"   (subs.fechayhorasaldasubscripcion >='"+fechaHastaPorCierreCaja + "'  ) )");
		}
		
		actionConditions.append( " and con.tipodescuento=1  group by os.nombre,con.idobrasocial	");

		return actionConditions.toString();
	}

	
	private String getQueryGastos(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde){
		StringBuilder actionConditions = new StringBuilder();
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		if(withUnion)
			actionConditions.append( " union all ");
		
		
		actionConditions.append("select 'Gasto' a, 0 b, sum(valor) c   from cajamovimiento caja  ");
		actionConditions.append(" where 1=1  ");
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}
			
					
		}
		actionConditions.append( "  and  caja.fecha >='"+fechaHastaPorCierreCaja + "'");
		actionConditions.append( " and Upper(caja.concepto) like Upper('%Gasto%') " +
				" and caja.idusuariogeneromovimiento= "+usuarioSelected.getId()+ " ");
		
		return actionConditions.toString();
	}
	
	private String getQueryAnulacionObraSocial(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde){
		StringBuilder actionConditions = new StringBuilder();

		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'AnuloObraSocial-'||os.nombre a, 1 b, sum(con.precioPorClaseOSesionPagaObraSocial * con.cantSesiones) c   from subscripcion subs " ); 
		actionConditions.append( "		inner join concepto con on (con.idSubscripcion= subs.id " ); 
		if (curso!= null ){
			actionConditions.append( " and con.idactividad= '"+curso.getId()+"' ) ");
		}else{
			actionConditions.append( " ) ");
		}
		actionConditions.append(" 	inner join obrasocial os on (con.idobrasocial=os.id )  where 1=1  ");

		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}
		}
			
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  subs.idusuarioanulosubscripcion= "+usuarioSelected.getId()+")" );
		}
		
		if(promocionBancoID != null){
			actionConditions.append( " AND subs.id in (select s.id from subscripcion s " +
														"	 inner join pagosporsubscripcion p on (s.id= p.idsubscripcion)	" +
														"	 where 1=1 and s.id=subs.id AND p.idbancopromocion='"+promocionBancoID + "' )");
		}
		
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and subs.fechayhoraanulacion >='"+fechaHastaPorCierreCaja + "'");
		}
		
		
		actionConditions.append( " and con.tipodescuento=1  group by os.nombre,con.idobrasocial	");
		
		return actionConditions.toString();
	}

	private String getQueryAnulacionObraSocialesDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'AnuloObraSociales' a, con.idactividad b, sum(con.precioPorClaseOSesionPagaObraSocial * con.cantSesiones) c, to_char(subs.fechayhoraanulacion,'MM') d   " +
				"  from subscripcion subs " ); 
		actionConditions.append( "		inner join concepto con on (con.idSubscripcion= subs.id) " ); 
			actionConditions.append(" where 1=1  ");

			actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
			actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
		
			actionConditions.append( " and con.tipodescuento in (1,2) and con.idobrasocial in(select id from obrasocial)	");
			
			actionConditions.append( "  group by to_char(subs.fechayhoraanulacion,'MM'), con.idactividad	");
			return actionConditions.toString();
	}
	
	private String getQueryAnulaSubscripcionDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'anulo' a, cupoPorAct.idActividad b, sum(subs.anulavalordevuelvo) c , to_char(subs.fechayhoraanulacion,'MM') d" +
				"   from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
		actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id )  ");
		actionConditions.append( " inner join Curso  curso on (cupoPorAct.idCurso= curso.id )  ");  

		actionConditions.append(" where 1=1  and curso.vencimiento!=5  ");

		actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");

		actionConditions.append( " group by to_char(subs.fechayhoraanulacion,'MM') ,cupoPorAct.idActividad  ");
			
			return actionConditions.toString();
	}
	
	private String getQueryAnulaSubscripcionDetalleMaipu(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'anulo' a, con.idcurso b, sum(subs.anulavalordevuelvo) c , con.idquincena d " +
				"   from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
		actionConditions.append( " inner join concepto  con on (con.idSubscripcion= subs.id )    ");
		actionConditions.append( " inner join Curso  curso on (con.idCurso= curso.id )  ");  

		actionConditions.append(" where 1=1   and curso.vencimiento=5 and con.idquincena is not null	 ");

		actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') <='"+ nuevoAnio+"-04-01'   ");
		actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ anio+"-04-01'  ");

		actionConditions.append( " group by con.idquincena, con.idcurso ");
			
			return actionConditions.toString();
	}
	private String getQueryAnulaSubscripcion(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde , SucursalEnum suc){
		StringBuilder actionConditions = new StringBuilder();
		
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'anulo' a, ps.idtipodepago b, sum(ps.cantidaddinero) c  from subscripcion subs " ); 
		actionConditions.append( "		inner join  PagosPorSubscripcion ps on (subs.id = ps.idSubscripcion ) "); 
			if (curso!= null ){
				actionConditions.append( " inner join CupoActividad  cupoPorAct on (cupoPorAct.idSubscripcion= subs.id and idActividad= '"+curso.getId()+"' )  ");
			}
			actionConditions.append(" where 1=1  ");

			if (fechaDesde != null && fechaHasta != null) {
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String fechaD = format1.format(fechaDesde);    
				String fechaH = format1.format(fechaHasta); 

				if(fechaH.equalsIgnoreCase(fechaD)){
					actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}else{
					actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') <='"+ fechaH+"'   ");
					actionConditions.append( " and to_char(subs.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
				}

				
				
			}
			
			if(fechaHastaPorCierreCaja != null){
				actionConditions.append( "  and subs.fechayhoraanulacion >='"+fechaHastaPorCierreCaja + "'");
			}
			
			if(usuarioSelected != null){
				actionConditions.append( " AND  ( subs.idusuarioanulosubscripcion = "+usuarioSelected.getId()+ ")" );
			}
			
			if(promocionBancoID != null){
				actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
			}
			if(suc != null){
				actionConditions.append( " and ps.sucursal="+ suc.toInt());
			}
			
			actionConditions.append( " group by ps.idtipodepago  ");
			
			return actionConditions.toString();
	}
	
	private String getQueryVentaProducto(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde){
		StringBuilder actionConditions = new StringBuilder();
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'VentaProducto' a, ps.idtipodepago b, sum(ps.cantidaddinero + ps.adicional) c  from ventaproductos vp ");
		actionConditions.append( " inner join  PagosPorSubscripcion ps on (vp.id = ps.idventaproducto ) "); 
		actionConditions.append( " where 1=1 ");
		
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}

			
					
		}
		
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  vp.idusuariocreoventa= "+usuarioSelected.getId() + ")" );
		}
		
		if(promocionBancoID != null){
			actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
		}
		
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and vp.fechayhoracompra >='"+fechaHastaPorCierreCaja + "'");
		}
		actionConditions.append( " group by ps.idtipodepago ");  
		
		return actionConditions.toString();
	}
	
	private String getQueryVentaProductoDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'VentaProducto' a, "+ ACTIVIDAD_VENTA_PRODUCTOS+" b, sum(ps.cantidaddinero + ps.adicional) c , to_char(vp.fechayhoracompra,'MM') d " +
				"   from ventaproductos vp ");
		actionConditions.append( " inner join  PagosPorSubscripcion ps on (vp.id = ps.idventaproducto ) "); 
		actionConditions.append( " where 1=1 ");
		actionConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
					
		actionConditions.append( " group by to_char(vp.fechayhoracompra,'MM') ");  
		
		return actionConditions.toString();
	}
	
	private String getQueryAnulaProductoDetalle(boolean withUnion, Long anio){
		// cuando se anula un Producto
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion) 
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'AnulaVentaProducto' a, "+ACTIVIDAD_VENTA_PRODUCTOS+" b, sum(vp.anulavalordevuelto) c, to_char(vp.fechayhoraanulacion,'MM') d " +
				"   from ventaproductos vp ");
		actionConditions.append( " inner join  PagosPorSubscripcion ps on (vp.id = ps.idventaproducto ) "); 
		actionConditions.append( " where 1=1 ");
		
		actionConditions.append( " and to_char(vp.fechayhoraanulacion,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(vp.fechayhoraanulacion,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
	
		actionConditions.append( " group by to_char(vp.fechayhoraanulacion,'MM') ");  
		return actionConditions.toString();

	}
	
	private String getQueryAnulaProducto(boolean withUnion, Actividad curso, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde){
		// cuando se anula un Producto
		StringBuilder actionConditions = new StringBuilder();
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		if(withUnion) 
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'AnulaVentaProducto' a, ps.idtipodepago b, sum(vp.anulavalordevuelto) c  from ventaproductos vp ");
		actionConditions.append( " inner join  PagosPorSubscripcion ps on (vp.id = ps.idventaproducto ) "); 
		actionConditions.append( " where 1=1 ");
		
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(vp.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(vp.fechayhoraanulacion,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(vp.fechayhoraanulacion,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}
		}
		
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  vp.idusuarioanuloventa= "+usuarioSelected.getId() + ")" );
		}
		
		if(promocionBancoID != null){
			actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
		}
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and vp.fechayhoraanulacion >='"+fechaHastaPorCierreCaja + "'");
		}
		actionConditions.append( " group by ps.idtipodepago ");  
		return actionConditions.toString();

	}
	
	private String getQueryMovimientoDeCajaIngresosDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'IngresosMovimientoCaja' a, "+ACTIVIDAD_MOVIMIENTO_CAJA+" b, sum(ps.cantidaddinero + ps.adicional) c, to_char(ps.fechaingresodefault,'MM') d " +
				"  from  ");
		actionConditions.append( " PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.INGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null ");
		actionConditions.append( " and  ps.sucursal!="+SucursalEnum.MAIPU.toInt() );
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");

		actionConditions.append( " group by to_char(ps.fechaingresodefault,'MM') ");  
		
		return actionConditions.toString();

	}
	
	private String getQueryMovimientoDeCajaIngresosDetalleMaipu(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'IngresosMovimientoCaja' a, "+ACTIVIDAD_MOVIMIENTO_CAJA+" b, sum(ps.cantidaddinero + ps.adicional) c, ps.quincena d " +
				"  from  ");
		actionConditions.append( " PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.INGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null ");
		actionConditions.append( " and  ps.sucursal="+SucursalEnum.MAIPU.toInt() );
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ nuevoAnio+"-04-01'   ");
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ anio+"-04-01'  ");

		actionConditions.append( " group by ps.quincena  ");  
		
		return actionConditions.toString();

	}
	
	private String getQueryMovimientoDeCajaIngresos(boolean withUnion, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde, SucursalEnum suc){
		StringBuilder actionConditions = new StringBuilder();
		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'IngresosMovimientoCaja' a, ps.idtipodepago b, sum(ps.cantidaddinero + ps.adicional) c  from  ");
		actionConditions.append( " PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.INGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null ");
		
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}

		}
		
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  ps.idUsuarioGeneroMovimientoCaja= "+usuarioSelected.getId() + ")" );
		}
		
		if(promocionBancoID != null){
			actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
		}
		
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and ps.fechaingresodefault >='"+fechaHastaPorCierreCaja + "'");
		}
		
		if(suc != null){
			actionConditions.append( "  and ps.sucursal="+suc.toInt() );
		}

		actionConditions.append( " group by ps.idtipodepago ");  
		
		return actionConditions.toString();

	}
	
	private String getQueryMovimientoDeCajaEgresosDetalleMaipu(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'EgresosMovimientoCaja' a, "+ACTIVIDAD_MOVIMIENTO_CAJA+" b, sum(ps.cantidaddinero + ps.adicional) c, ps.quincena d   ");
		actionConditions.append( " from PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.EGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null and ps.idGasto is null ");
		actionConditions.append( "and ps.idgasto is null   and ps.idgastomaipu is null ");  
		actionConditions.append( " and  ps.sucursal="+SucursalEnum.MAIPU.toInt() );
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ nuevoAnio+"-04-01'   ");
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ anio+"-04-01'  ");
	
		actionConditions.append( " group by ps.quincena ");  
		
		return actionConditions.toString();

	}
	
	private String getQueryMovimientoDeCajaEgresosDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();
		Long nuevoAnio=anio +1;

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'EgresosMovimientoCaja' a, "+ACTIVIDAD_MOVIMIENTO_CAJA+" b, sum(ps.cantidaddinero + ps.adicional) c, to_char(ps.fechaingresodefault,'MM') d   ");
		actionConditions.append( " from PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.EGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null and ps.idGasto is null ");
		actionConditions.append( "and ps.idgasto is null   and ps.idgastomaipu is null ");
		actionConditions.append( " and  ps.sucursal!="+SucursalEnum.MAIPU.toInt() );
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
	
		actionConditions.append( " group by to_char(ps.fechaingresodefault,'MM') ");  
		
		return actionConditions.toString();

	}

	private String getQuerySueldosDetalle(boolean withUnion, Long anio){
		StringBuilder actionConditions = new StringBuilder();

		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'Sueldos' a, gs.idActividad b, sum(gs.dinero) c, cast( (g.mes +1) as text) d  from  ");
		actionConditions.append( " gastos g  "); 
		actionConditions.append( " inner join gastossueldos gs on (g.id= gs.idgasto) where 1=1 ");
		
		actionConditions.append( " and  g.tipogasto="+ GastosEnum.SUELDOS.toInt());
		actionConditions.append( " and g.anio="+ anio);
	
		actionConditions.append( " group by cast( (g.mes +1) as text), gs.idActividad ");  
		 
		return actionConditions.toString();
	}
	

	
	private String getQueryGastosPrecioCostoProductosDetalle(boolean withUnion, Long anio){
		StringBuilder costosProductosConditions= new StringBuilder( " " );

		if(withUnion)
			costosProductosConditions.append( " union all ");
		Long nuevoAnio=anio +1;

		costosProductosConditions.append(" 	select 'gastosPrecioCostoProductos' a, "+ACTIVIDAD_VENTA_PRODUCTOS+" b, sum(cantidadprod * vppp.preciocostoProd) c  " + 
						" , to_char(vp.fechayhoracompra,'MM') d  " );
		costosProductosConditions.append( " from ventaproductos vp "); 
		costosProductosConditions.append( " 	inner join ventaproductoporproducto vppp on (vp.id = vppp.idventaproducto) ");  
		costosProductosConditions.append( " where vp.anulaventa is not true   " );
		costosProductosConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		costosProductosConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
		costosProductosConditions.append( " 	group   by to_char(vp.fechayhoracompra,'MM') ");  
	
		return costosProductosConditions.toString();
	}
	
	private String getQueryMovimientoDeCajaEgresos(boolean withUnion, Date fechaDesde , Date fechaHasta, User usuarioSelected, 
			Long promocionBancoID, Date fechaYHoraDesde, SucursalEnum suc){
		StringBuilder actionConditions = new StringBuilder();

		String fechaHastaPorCierreCaja=null;
		if(fechaYHoraDesde != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
			fechaHastaPorCierreCaja=format.format(fechaYHoraDesde);
		}
		if(withUnion)
			actionConditions.append( " union all ");
		
		actionConditions.append( " select 'EgresosMovimientoCaja' a, ps.idtipodepago b, sum(ps.cantidaddinero + ps.adicional) c  from  ");
		actionConditions.append( " PagosPorSubscripcion ps  "); 
		actionConditions.append( " where 1=1  and ps.tipoMovimiento="+ TipoMovimientoCajaEnum.EGRESO.toInt());
		actionConditions.append( " and  ps.idsubscripcion is null and ps.idventaproducto is null ");
		actionConditions.append( "and ps.idgasto is null   and ps.idgastomaipu is null ");
		if (fechaDesde != null && fechaHasta != null) {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String fechaD = format1.format(fechaDesde);    
			String fechaH = format1.format(fechaHasta); 

			if(fechaH.equalsIgnoreCase(fechaD)){
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}else{
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') <='"+ fechaH+"'   ");
				actionConditions.append( " and to_char(ps.fechaingresodefault,'YYYY-MM-DD') >='"+ fechaD+"'  ");
			}
		}
		
		if(usuarioSelected != null){
			actionConditions.append( " AND  (  ps.idUsuarioGeneroMovimientoCaja= "+usuarioSelected.getId() + ")" );
		}
		
		if(promocionBancoID != null){
			actionConditions.append( " AND  ps.idbancopromocion='"+promocionBancoID + "'");
		}
		
		if(fechaHastaPorCierreCaja != null){
			actionConditions.append( "  and ps.fechaingresodefault >='"+fechaHastaPorCierreCaja + "'");
		}
		
		if(suc != null){
			actionConditions.append( "  and ps.sucursal="+suc.toInt() );
		}

		actionConditions.append( " group by ps.idtipodepago ");  
		
		return actionConditions.toString();

	}

	public ClaseConListaAlumnos findAllClaseConListaAlumnosByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
//		if(!esConFechaExacta){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("claseID", idClase);
			Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTime(fecha);

			String fecha2= calendarDate.get(calendarDate.YEAR) +"-";
			Integer aa4=calendarDate.get(calendarDate.MONTH) +1;
			
			if(aa4 < 10)
				fecha2= fecha2+"0"+aa4+"-";
			else
				fecha2= fecha2+aa4+"-";
			
			Integer aa3=calendarDate.get(calendarDate.DAY_OF_MONTH);
			if(aa3 < 10)
				fecha2= fecha2+"0"+aa3;
			else
				fecha2= fecha2+aa3;
			params.put("fechaID", fecha2);

			StringBuffer sql = new StringBuffer();
			sql.append(" select new com.institucion.model.ClaseConListaAlumnos(id, profeNombre,comentarioGeneralDelProfe,cantPresentes,cantPresentesNoEncontrados )  from   ClaseConListaAlumnos " );
			sql.append(" WHERE clase.id = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
				
			List <ClaseConListaAlumnos> list= super.findClaseConListaAlumnos(sql.toString(), params);
			if(list != null && list.size() >0){
				return list.get(0);
			}
	
		return null;
	}

	public CajaMovimiento findById(Long id) throws DAOException{
		return this.findById(id, CajaMovimiento.class);
	}

	@Override
	public void delete(CajaMovimiento c) throws DAOException{
		super.delete(c);
	}

	@Override
	public CajaMovimiento save(CajaMovimiento c) throws DAOException{
		return super.save(c);
	}
	
	@Override
	public void save(Concepto c) throws DAOException{
		super.save(c);
	}
	
	public List<CajaMovimiento> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<CajaMovimiento> claseList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					CajaMovimiento cli=findById(new Long(integer));
					if(cli != null){
						if(claseList == null)
							claseList= new ArrayList<CajaMovimiento>();
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

	private List<PulsoClinica> crearObjetosConMesesVacios(List<PulsoClinica>  listaPulsoClinico, String mes, int id){
		
		if(listaPulsoClinico != null){
			PulsoClinica pulso= new PulsoClinica();
			pulso.setMes(mes);
			pulso.setMesId(id);
//			pulso.setCantidadEgresoGastosMes(0);
			pulso.setCantidadEgresoRRHH(0);
			pulso.setCantidadIngreso(new Double(0));
			pulso.setCantidadTotal(new Double(0));
			listaPulsoClinico.add(pulso);
		}
		return listaPulsoClinico;
	}
	

	public List<PulsoClinica> obtenerResumenDeTotalesJdbc(Connection cnx , Long anio) throws DAOException{
		List<PulsoClinica> listaPulsos= new ArrayList<PulsoClinica>();
				
		// CAMPO INGRESO y Egresos: Se tienen que restar los dos valores que se traen
		StringBuilder ventasConditions= new StringBuilder("select to_char(caja.fecha,'MM'), sum(valor), tipomovimiento from cajamovimiento caja  ");
		ventasConditions.append(" where 1=1  ");
			Long nuevoAnio=anio +1;
			ventasConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
			ventasConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
			ventasConditions.append( " and Upper(caja.concepto) not like Upper('%Gasto%')  " ); // and tipomovimiento=1 ");
			ventasConditions.append( " and Upper(caja.concepto) not like Upper('%Ingresos/Egresos%')  " ); // and tipomovimiento=1 ");
			ventasConditions.append( " group   by to_char(caja.fecha,'MM') , tipomovimiento  order by to_char(caja.fecha,'MM') ");
			
			
		// Sumar a gastos generales
		StringBuilder ingresosYEgresosDeCajaConditions= new StringBuilder("select to_char(caja.fecha,'MM'), sum(valor), tipomovimiento from cajamovimiento caja  ");
		ingresosYEgresosDeCajaConditions.append(" where 1=1  ");
		ingresosYEgresosDeCajaConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		ingresosYEgresosDeCajaConditions.append( " and to_char(caja.fecha,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
		ingresosYEgresosDeCajaConditions.append( " and Upper(caja.concepto) like Upper('%Ingresos/Egresos%')  ");
		ingresosYEgresosDeCajaConditions.append( " group   by to_char(caja.fecha,'MM') , tipomovimiento  order by to_char(caja.fecha,'MM') ");
			
		// cuanta plata tengo que descontar de COSTO por productos.
		StringBuilder costosProductosConditions= new StringBuilder(" 	select to_char(vp.fechayhoracompra,'MM'), sum(cantidadprod * vppp.preciocostoProd) " );
		costosProductosConditions.append( " from ventaproductos vp "); 
		costosProductosConditions.append( " 	inner join ventaproductoporproducto vppp on (vp.id = vppp.idventaproducto) ");  
		costosProductosConditions.append( " where vp.anulaventa is not true    ");
		costosProductosConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		costosProductosConditions.append( " and to_char(vp.fechayhoracompra,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
		costosProductosConditions.append( " 	group   by to_char(vp.fechayhoracompra,'MM') ");  
		
		
		
		// Gastos Sueldos (RRHH) y Egresos Generales
		StringBuilder gastosConditions= new StringBuilder(" select  (mes+1), sum(dinero), tipogasto from gastos    ");
			gastosConditions.append(" where anio= "+anio  + "   group by anio, (mes+1), tipogasto   ");
		
		// Gastos Maipu
		StringBuilder gastosMaipuConditions= new StringBuilder(" select  to_char(fecha,'MM'), sum(dinero), tipogasto from gastosmaipu    ");
		gastosMaipuConditions.append(" where  1=1 " );
//				" anio= "+anio  + "    ");
		gastosMaipuConditions.append( " and to_char(fecha,'YYYY-MM-DD') <='"+ nuevoAnio+"-01-01'   ");
		gastosMaipuConditions.append( " and to_char(fecha,'YYYY-MM-DD') >='"+ anio+"-01-01'  ");
		gastosMaipuConditions.append( " group by to_char(fecha,'MM'), tipogasto  ");

			
			
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Enero",1);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Febrero",2);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Marzo",3);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Abril",4);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Mayo",5);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Junio",6);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Julio",7);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Agosto",8);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Septiembre",9);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Octubre",10);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Noviembre",11);
		listaPulsos=crearObjetosConMesesVacios(listaPulsos, "Diciembre",12);
		
		Statement stat=null;
		ResultSet rs = null;
		
		try {
			// se condera por ventas ya anulaciones de cursos
			stat = cnx.createStatement();
			rs = stat.executeQuery(ventasConditions.toString());

			while (rs.next()) {
				String mes=rs.getString(1);
				Double sumaDinero=rs.getDouble(2);
				Integer tipoMovimiento =rs.getInt(3);
				listaPulsos=actualizarPulsoMes(listaPulsos, Integer.parseInt(mes), tipoMovimiento, sumaDinero);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try{
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
			} catch (SQLException e) {	}
		}
		
		try {
			stat = cnx.createStatement();
			rs = stat.executeQuery(gastosMaipuConditions.toString());
			while (rs.next()) {
				String mes=rs.getString(1);
				Integer sumaDinero=rs.getInt(2);
				Integer tipoGasto =rs.getInt(3);
				if(tipoGasto.intValue() == GastosMaipuEnum.SUELDOS.toInt() ){
					listaPulsos=actualizarEgresosRRHH(listaPulsos, Integer.parseInt(mes), sumaDinero);
				}else{
					listaPulsos=actualizarGastosGeneralesMes(listaPulsos, Integer.parseInt(mes), sumaDinero,TipoMovimientoCajaEnum.EGRESO.toInt() );
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try{
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
			} catch (SQLException e) {	}
		}
			
		try{
			stat = cnx.createStatement();
			rs = stat.executeQuery(gastosConditions.toString());
			while (rs.next()) {
				String mes=rs.getString(1);
				Integer sumaDinero=rs.getInt(2);
				Integer tipoGasto =rs.getInt(3);
				
				if(tipoGasto.intValue() == GastosEnum.SUELDOS.toInt() ){
					listaPulsos=actualizarEgresosRRHH(listaPulsos, Integer.parseInt(mes), sumaDinero);
				}else{
					listaPulsos=actualizarGastosGeneralesMes(listaPulsos, Integer.parseInt(mes), sumaDinero, TipoMovimientoCajaEnum.EGRESO.toInt());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try{
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
			} catch (SQLException e) {	}
		}
		
		try{
			// ingresos y egresos de caja
			stat = cnx.createStatement();
			rs = stat.executeQuery(ingresosYEgresosDeCajaConditions.toString());
			while (rs.next()) {
				String mes=rs.getString(1);
				Integer sumaDinero=rs.getInt(2);
				Integer tipoGasto =rs.getInt(3);
				listaPulsos=actualizarGastosGeneralesMes(listaPulsos, Integer.parseInt(mes), sumaDinero, tipoGasto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try{
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
			} catch (SQLException e) {	}
		}
		
		try{
			stat = cnx.createStatement();
			rs = stat.executeQuery(costosProductosConditions.toString());
			while (rs.next()) {
				String mes=rs.getString(1);
				Integer sumaDinero=rs.getInt(2);
				listaPulsos=actualizarGastosGeneralesMes(listaPulsos, Integer.parseInt(mes), sumaDinero,  TipoMovimientoCajaEnum.EGRESO.toInt());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
		}finally{
			try{
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
			} catch (SQLException e) {	}
		}
		
		if(listaPulsos != null){
			for (PulsoClinica pulsoClinica : listaPulsos) {
				double cantidadTot= pulsoClinica.getCantidadIngreso() +pulsoClinica.getCantidadEgresoGastosMes() +pulsoClinica.getCantidadEgresoRRHH();

				pulsoClinica.setCantidadTotal(cantidadTot);
			}
		}
		return listaPulsos;
	}
	
	
	private List<PulsoClinica> actualizarEgresosRRHH(List<PulsoClinica> lista, int mes, Integer dinero ){
		
		if(lista != null){
			for (PulsoClinica pulsoClinica : lista) {
				
				if(pulsoClinica.getMesId().intValue() == mes){
					pulsoClinica.setCantidadEgresoRRHH(pulsoClinica.getCantidadEgresoRRHH() - dinero.intValue());
					return lista;
				}
			}
		}
		return lista;
	}
	

	private List<PulsoClinica> actualizarGastosGeneralesMes(List<PulsoClinica> lista, int mes, Integer dinero, int tipoMovimiento){
		if(lista != null){
			for (PulsoClinica pulsoClinica : lista) {
				
				if(pulsoClinica.getMesId().intValue() == mes){
					if(tipoMovimiento == TipoMovimientoCajaEnum.EGRESO.toInt()){
						pulsoClinica.setCantidadEgresoGastosMes(pulsoClinica.getCantidadEgresoGastosMes() - dinero);
						
					}else if(tipoMovimiento == TipoMovimientoCajaEnum.INGRESO.toInt()){
						pulsoClinica.setCantidadEgresoGastosMes(pulsoClinica.getCantidadEgresoGastosMes() + dinero);	
					}
					return lista;
				}
			}
		}
		return lista;
	}
	
}
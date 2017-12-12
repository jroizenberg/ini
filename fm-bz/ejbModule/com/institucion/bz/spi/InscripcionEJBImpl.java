package com.institucion.bz.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.InscripcionEJBLocal;
import com.institucion.bz.InscripcionEJBRemote;
import com.institucion.dao.ClaseDAO;
import com.institucion.dao.ClienteDAO;
import com.institucion.dao.IngresoAClasesSinFechasAlumnosDAO;
import com.institucion.dao.InscripcionDAO;
import com.institucion.dao.MatriculaDAO;
import com.institucion.dao.ObraSocialDAO;
import com.institucion.dao.ObraSocialesPrecioDAO;
import com.institucion.dao.SubscripcionDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.fm.security.dao.UserDAO;
import com.institucion.fm.security.model.User;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
import com.institucion.model.Inscripcion;
import com.institucion.model.Matricula;
import com.institucion.model.ObraSocial;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.Quincena;
import com.institucion.model.Subscripcion;
import com.institucion.model.VencimientoCursoEnum;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class InscripcionEJBImpl implements InscripcionEJBRemote,InscripcionEJBLocal {

	private static Log log = LogFactory.getLog(InscripcionEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private ObraSocialDAO obraSocialDao = null;
	
	@Autowired
	private ObraSocialesPrecioDAO obraSocialesPrecioDao = null;
	
	@Autowired
	private SubscripcionDAO subscripcionDao = null;
	
	@Autowired
	private MatriculaDAO matriculaDao = null;
	
	@Autowired
	private InscripcionDAO inscripcionDao = null;
	
	@Autowired
	private ClienteDAO clienteDao = null;
	
	@Autowired
	private ClaseDAO claseDao = null;
	
	@Autowired
	private IngresoAClasesSinFechasAlumnosDAO ingresoAClasesDAO = null;
	
	@Autowired
	private UserDAO userDao = null;
	
	
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public InscripcionEJBImpl() {	}

	
	
	@Override
	public ObraSocial findByIdObraSocial(Long id) throws DAOException{
		return obraSocialDao.findById(id);
	}
	
	@Override
	public List<ObraSocial> findAllObraSociales() throws DAOException{
		return obraSocialDao.findAll();	
	}

	@Override
	public void create(ObraSocialesPrecio c) throws DAOException{
		obraSocialesPrecioDao.create(c);
	}
	
	@Override
	public void create(Inscripcion c) throws DAOException{
		inscripcionDao.create(c);
	}

	@Override
	public void delete(ObraSocialesPrecio c) throws DAOException{
		obraSocialesPrecioDao.delete(c);
	}
	
	@Override
	public void save(ObraSocialesPrecio c) throws DAOException{
		obraSocialesPrecioDao.save(c);	
	}
	
	@Override
	public void save(Inscripcion c) throws DAOException{
		inscripcionDao.save(c);
	}

	@Override
	public void delete(Inscripcion c) throws DAOException{
		inscripcionDao.delete(c);
	}
	
	@Override
	public List<Inscripcion> findByIdClienteAndAnio(Long idCliente) throws DAOException{
        return inscripcionDao.findByIdClienteAndAnio(idCliente);
    }
	
	@Override
	public ObraSocialesPrecio findObraSocialesPrecioById(Long id) throws DAOException{
        return obraSocialesPrecioDao.findById(id);
    }
	
	@Override
	public List<ObraSocialesPrecio> findObraSocialesByObraSocialPrecioById(Long idObraSocial) throws DAOException{
		
        return obraSocialesPrecioDao.findAllByObraSocial(idObraSocial);
    }
	
	@Override
	public List<ObraSocialesPrecio> findAllObraSocialesPrecio() throws DAOException{
        return obraSocialesPrecioDao.findAll();
	}	
	
	@Override
	public void create(Subscripcion c) throws DAOException{
		subscripcionDao.create(c);
	}
	
	@Override
	public Long createSubs(Subscripcion c) throws DAOException{
		return subscripcionDao.createSubs(c);
	}
	
	@Override
	public void delete(Subscripcion c) throws DAOException{
		subscripcionDao.delete(c);
	}
	
	@Override
	public void save(Subscripcion c) throws DAOException{
		subscripcionDao.save(c);	
	}
	
	@Override
	public Subscripcion findSubscripcionById(Long id) throws DAOException{
		Subscripcion subs=subscripcionDao.findById(id);

		return subs;
    }

	@Override
	public List<Subscripcion> findAllSubscripciones() throws DAOException{
		List<Subscripcion>  list =subscripcionDao.findAll();
		return list;
	}	
	
	@Override
	public List<Subscripcion> findAllSubscripcionesByClient(Long idClient) throws DAOException{
		List<Subscripcion>  list =subscripcionDao.findAllByClient(idClient);
		return list;
	}	
	
	@Override
	public List<Matricula> findAllMatriculas() throws DAOException{
        return matriculaDao.findAll();
	}	

	@Override
	public List<Subscripcion> findAllConJdbc(String filters) throws DAOException{
        return subscripcionDao.findAllConJdbc(filters);
	}
	
	private boolean tieneCosasAProcesar(Cliente cliente2){
		boolean estaVencida= false;
		if(cliente2.getSubscripcionesList() != null){
			for (Subscripcion subs : cliente2.getSubscripcionesList()) {
				
				
				for (CupoActividad cupoAct : subs.getCupoActividadList()) {
					
					if( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
						
					}else{
						if(cupoAct.getFechaFin() != null && cupoAct.getFechaFin().before(new Date())){
							return true;

						}else{
							estaVencida= false;
						
						}
	
					}
				}			
			}
		}
		return estaVencida;
	}

	private boolean tieneCursosQuincenalesAProcesar(Cliente cliente2){
		if(cliente2.getSubscripcionesList() != null){
			for (Subscripcion subs : cliente2.getSubscripcionesList()) {
				
				
				for (CupoActividad cupoAct : subs.getCupoActividadList()) {
					
					if( cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("INI VERANO") && cupoAct.getCupos() >0){
						if(( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt()) 
								|| ( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()))
							return true;
						
					}else if(cupoAct.getCurso() != null){
						long fechaInicialMs = subs.getFechaYHoraCreacion().getTime(); //Tanto fecha inicial como fecha final son Date.
						long fechaFinalMs = new Date().getTime();
//						long diferencia = fechaInicialMs- fechaFinalMs ;
//						double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
						
						long diferencia2 = fechaFinalMs- fechaInicialMs ;
						double dias = Math.floor(diferencia2 / (1000 * 60 * 60 * 24));
						if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt()
								&& dias >20){
							return true;
							
						}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()
								&& dias >15){
							return true;							
							
						}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()
								&& dias >100){
							return true;
							
						}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()
								&& dias >40){
							return true;
							
						}
					} 
				}			
			}
		}
		return false;
	}
	
	private boolean tieneCursosCumpleaniosParaProcesar(Cliente cliente2){
		boolean estaVencida= false;
		if(cliente2.getSubscripcionesList() != null){
			for (Subscripcion subs : cliente2.getSubscripcionesList()) {
				for (CupoActividad cupoAct : subs.getCupoActividadList()) {
					
					Date fechaCumpleanios=getFechaCumpleanios(subs, cupoAct.getCurso().getId() , cupoAct.getActividad().getId());

					if(fechaCumpleanios != null && new Date().after(fechaCumpleanios)){
						long fechaInicialMs = new Date().getTime(); //Tanto fecha inicial como fecha final son Date.
						long fechaFinalMs = fechaCumpleanios.getTime();
						long diferencia = fechaInicialMs- fechaFinalMs ;
						double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
						
						if(dias > 8){
							if( cupoAct.getCurso()!= null && cupoAct.getCurso().getNombre().contains("CUMPLEA")
									&&  (cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt() 
									 	|| cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt()  )){
								return true;
							}		
						}
					}
					
				}
			}
		}
		return estaVencida;
	}

	private Date getFechaCumpleanios(Subscripcion subs, Long idCurso, Long idActividad){
		if(true)System.out.println("");

		if(subs != null && subs.getConceptoList() != null){
			for (Concepto concep : subs.getConceptoList()) {
				
				if(concep.getCurso() != null && concep.getCurso().getId().equals(idCurso)
						&&  concep.getFechaCumple() != null &&
						concep.getActividadDelConcepto() != null && concep.getActividadDelConcepto().getId().equals(idActividad))
					return concep.getFechaCumple();
			}
		}
		return null;
	}
	@Override
	public void actualizarEstadosSubsYClientesDeJob(){
		/*	 
		 * Configurar el Quartz para: Ejecutar este Job 1 vez por dia.
		 * 	Se encargue de actualizar los estados de las subscripciones a VENCIDO o VENCIDO CON DEuDA
		 */
		List<Cliente> listClientes=clienteDao.findAll();
		if(listClientes != null){
			
			for (Cliente cliente2 : listClientes) {
				clienteDao.loadLazyAttribute(cliente2.getSubscripcionesList());
				for (Subscripcion subss: cliente2.getSubscripcionesList()) {
					clienteDao.loadLazyAttribute(subss.getCupoActividadList());
					clienteDao.loadLazyAttribute(subss.getConceptoList());
				}

				// actualiza los cursos de cumpleaños
				if(tieneCursosCumpleaniosParaProcesar(cliente2)){
					// si tiene cursos de cumpleanios, si la fecha del cumple es mayor a 10 dias de la fecha actual entonces 
					//  lo pongo vencido o vencido con deuda
					Cliente cliente=clienteDao.actualizarEstadosCumpleaniosACliente(cliente2);
					// guardar cliente
					clienteDao.save(cliente);		
				}
				
				if(tieneCosasAProcesar(cliente2)){
					Cliente cliente=clienteDao.actualizarEstadosVencidosACliente(cliente2);
					// guardar cliente
					clienteDao.save(cliente);		
				}
				
				// si tengo cursos que son LIBRES, a los 45 dias los deberia pasar a vencidos
				
				// si tiene cursos quincenales de maipu contratados viejos, los limpio y los dejo sin cupo 
				if(tieneCursosQuincenalesAProcesar(cliente2)){
					Cliente cliente=clienteDao.actualizarEstadosCursosIniACliente(cliente2);
					// guardar cliente
					clienteDao.save(cliente);		
				}
				
			}
		}
	}

	@Override
	public void actualizarEstadosDeJob(){
		/*
		 * Configurar el Quartz para:  Ejecutar el Job cada 10 minutos.
		 * Consultar todas las clases del dia en Cuestion:
		 * 
		 * Recorro las ClaseConListaAlumnos:
		 *          Cuando exista una diferencia mayor a 2 horas entre la hora actual y la hora de finalizacion: 
		 * 			- Guardo en Histororico todo el objeto entero ClientesEnClase y ClaseConListaalumnos 
		 * 				 . Hacer un load  Lazy de clases ya que esto va a crecer mucho
		 * 				 . Hacer lo necesario para que se pueda visualizar desde el Selector de Alumnos en clase, segun fechas viejas, 
		 * 				  El detalle de estas clases con los numero correctos
		 * 	
		 * Recorro del objeto Clase, las clienesEnClase : y quito a de aqui a los clientes que ya no estan en clase
		 * 			- Actualizo el estado de los clientes a 'NO EN CLASE'  
		 * 	
		 * 		- Revisar el tema de tomar lista, que pasa si no se toma lista ? quedan registradas las clases en algun lado ? 
		 * 		Aclarando por un lado "Alumnos tomados lista": 
		 * 		Alumnos encontrados , Alumnos no encontrados y por otro lado "Alumnos NO tomados lista"

					- PROBAR COMO QUEDAN DESPUES DE PASAR AL HISTORICO LAS CLASES PARA VOLVERSE A USAR.(EN QUE ESTADO, CANT ALUMNOS, ETC, ETC)
					- PROBAR COMO QUEDAN LOS CLIENTES DE LA CLASE DESUES DE PASAR A HISTORICO
			----------------------------------------------------------
		 *   Ver si hace falta hacer otro queartz para envio de Mails: Ver que haria falta enviar
		 *   	
		 */
			
		// Se obtienen TODAS las clases del dia en cuestion
		List<Clase> clases =claseDao.findAllConJdbc(getFilters(8));	
		if(clases != null){
			Date horaActual= new Date();	

			for (Clase clase : clases) {
				clienteDao.loadLazyAttribute(clase.getClaseConListaAlumnosHistoryList());
				clienteDao.loadLazyAttribute(clase.getClaseConListaAlumnosList());
				clienteDao.loadLazyAttribute(clase.getClienesEnClase());
				clienteDao.loadLazyAttribute(clase.getIngresoAClaseSinFechas());
				List<IngresoAClasesSinFechasAlumnos > clasesLista= new ArrayList <IngresoAClasesSinFechasAlumnos>();
				if(clase.getEsClaseSinHora()){
					// debe controlar que se procese todo UNICAMENTE cuando pasaron 3 horas de ingresada la 1 persona a clase
					if(clase.getIngresoAClaseSinFechas() != null && clase.getIngresoAClaseSinFechas().size() > 0) {
						for (IngresoAClasesSinFechasAlumnos ing : clase.getIngresoAClaseSinFechas()) {

							if(horaActual.getHours() < (ing.getFecha().getHours() - 4) ){
								if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
										|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
									clasesLista.add(ing);
							
							}else if(horaActual.getHours() >= (ing.getFecha().getHours() +2) ){
								if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
										|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
									clasesLista.add(ing);
							
							}else if(horaActual.getDate() < (ing.getFecha().getDate()) ){
								// si el dia actual es < al dia de la fecha
								if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
										|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
									clasesLista.add(ing);
							
							}else if(horaActual.getMonth() < (ing.getFecha().getMonth()) ){
								if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
										|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
									clasesLista.add(ing);
							}
						}
					}
					
					if(clasesLista != null && clasesLista.size() > 0 ){
						procesarLaClaseQuitandoSoloClientesPorParametro(clase, clasesLista);
					}
						
				}else{
					Calendar cActual= Calendar.getInstance();
					cActual.setTime(horaActual);
					cActual.set(Calendar.HOUR, 0);
					cActual.set(Calendar.HOUR_OF_DAY, 0);
					cActual.set(Calendar.MINUTE, 0);
					cActual.set(Calendar.SECOND, 0);
					
					if(clase.getActividad().isTomaLista()){
						
						if(horaActual.getHours() > (clase.getHoraDesde().getHours() + 3) ){
							// si la hora de ahora es mayor a la hora desde de la clase + 3  hago esto
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getHours() < (clase.getHoraDesde().getHours() -4) ){
							// si la hora de ahora es mayor a la hora desde de la clase + 3  hago esto
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getHours() >= (clase.getHoraHasta().getHours() +2)){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getDate() < (clase.getHoraHasta().getDate()) ){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
								procesarLaClase(clase);
						
						}else if(horaActual.getMonth() < (clase.getHoraHasta().getMonth()) ){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
								procesarLaClase(clase);
						}
					}else{
						if(horaActual.getHours() > (clase.getHoraDesde().getHours() + 3) ){
							// si la hora de ahora es mayor a la hora desde de la clase + 3  hago esto
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getHours() < (clase.getHoraDesde().getHours() -4) ){
							// si la hora de ahora es mayor a la hora desde de la clase + 3  hago esto
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getHours() >= (clase.getHoraHasta().getHours() +1)){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
							procesarLaClase(clase);
							
						}else if(horaActual.getDate() < (clase.getHoraHasta().getDate()) ){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
								procesarLaClase(clase);
						
						}else if(horaActual.getMonth() < (clase.getHoraHasta().getMonth()) ){
							if((clase.getClaseConListaAlumnosList() != null && clase.getClaseConListaAlumnosList().size() > 0)
									|| (clase.getClienesEnClase() != null && clase.getClienesEnClase().size() > 0)	)
								procesarLaClase(clase);
						}				
					}			
				}		
			}
		}
		
		// al terminar elimino todos los registros de IngresoAClasesSinFechasAlumnos donde el idclase es null
		ingresoAClasesDAO.eliminarTodosLosQueLaClaseEsNula();
	}
	
	private void procesarLaClase (Clase clase){
		
		
		if(clase.getActividad() != null && clase.getActividad().isTomaLista()){
			// llamo al proceso para Guardar historico de ClientesEnClase y ClaseconListaAlumnos
			if(clase.getClaseConListaAlumnosHistoryList() != null)
				clase.getClaseConListaAlumnosHistoryList().addAll(guardarHistoricoClientesEnClase(clase.getClaseConListaAlumnosList()));
			else
				clase.setClaseConListaAlumnosHistoryList(guardarHistoricoClientesEnClase(clase.getClaseConListaAlumnosList()));
			
			if(clase.getClaseConListaAlumnosList() != null){
				for (ClaseConListaAlumnos claseConListaAlumno : clase.getClaseConListaAlumnosList()) {
					claseDao.delete(claseConListaAlumno);	
				}
			}
			clase.setClaseConListaAlumnosList(new HashSet());
			clase.setClaseConListaAlumnosList(new HashSet());
			
			// llamo a otro proceso para recorrer el objeto ClientesEnClase y sacarlos de la clase, de las que saque antes.
//			if(clase.getClienesEnClaseHistory() != null){
//				clase.getClienesEnClaseHistory().addAll(clase.getClienesEnClase());
//			}else
//				clase.setClienesEnClaseHistory(clase.getClienesEnClase());			
			
		}
		clase.setClienesEnClase(new HashSet());
		clase.setIngresoAClaseSinFechas(new HashSet());

		claseDao.save(clase);
	}
	
	
	private void procesarLaClaseQuitandoSoloClientesPorParametro (Clase clase, List<IngresoAClasesSinFechasAlumnos> clientes){
		
		
		if(clase.getActividad() != null && clase.getActividad().isTomaLista()){
			// llamo al proceso para Guardar historico de ClientesEnClase y ClaseconListaAlumnos
			Set<ClaseConListaAlumnosHistorico>  settT=guardarHistoricoClientesEnClase(clase.getClaseConListaAlumnosList());
			if(clase.getClaseConListaAlumnosHistoryList() != null)
				clase.getClaseConListaAlumnosHistoryList().addAll(settT);
			else
				clase.setClaseConListaAlumnosHistoryList(settT);
			
			if(clase.getClaseConListaAlumnosList() != null){
				for (ClaseConListaAlumnos claseConListaAlumno : clase.getClaseConListaAlumnosList()) {
					claseDao.delete(claseConListaAlumno);	
				}
			}
			clase.setClaseConListaAlumnosList(new HashSet());
				
			clase.setClienesEnClase(new HashSet());
			clase.setIngresoAClaseSinFechas(new HashSet());

		}else{
			// se deberia quitar de la lista solamente a los clientes que se reciben por parametro
			if(clase.getIngresoAClaseSinFechas() != null){
				List<IngresoAClasesSinFechasAlumnos> ar=getIngresoAClasesSinFechasAlumnos(clase.getIngresoAClaseSinFechas() , clientes);
				List<IngresoAClasesSinFechasAlumnos> artoDelete=getIngresoAClasesSinFechasAlumnosToDelete(clase.getIngresoAClaseSinFechas() , clientes);

				List<Cliente> cli=getClientes(new ArrayList(clase.getClienesEnClase()) , clientes);
				clase.setClienesEnClase(new HashSet(cli));
				

				if(artoDelete != null && artoDelete.size() > 0){
					for (IngresoAClasesSinFechasAlumnos object : artoDelete) {
						ingresoAClasesDAO.delete(object);	
					}
				}
				clase.setIngresoAClaseSinFechas(new HashSet(ar));
			}
		}
		claseDao.save(clase);
	}
	
	private List<Cliente> getClientes(List<Cliente> clientesorig,List<IngresoAClasesSinFechasAlumnos> clientesAquitarDeLista ){
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		if(clientes != null){
			
			for (Cliente ingresoAClasesSinFechasAlumnos : clientesorig) {
				
				if(!existeIngresoAClasesClientes(ingresoAClasesSinFechasAlumnos, clientesAquitarDeLista)){
					clientes.add(ingresoAClasesSinFechasAlumnos);
				}
			}
		}
		return clientes;
		}
			
	private List<IngresoAClasesSinFechasAlumnos> getIngresoAClasesSinFechasAlumnos(Set<IngresoAClasesSinFechasAlumnos> clientesorig,
			List<IngresoAClasesSinFechasAlumnos> clientesAquitarDeLista ){
		
		List<IngresoAClasesSinFechasAlumnos> clientes = new ArrayList<IngresoAClasesSinFechasAlumnos>();
		if(clientes != null){
			
			for (IngresoAClasesSinFechasAlumnos ingresoAClasesSinFechasAlumnos : clientesorig) {
				
				if(!existeIngresoAClasesSinFechasAlumnos(ingresoAClasesSinFechasAlumnos, clientesAquitarDeLista)){
					clientes.add(ingresoAClasesSinFechasAlumnos);
				}
			}
		}
		return clientes;
	}
	
	private List<IngresoAClasesSinFechasAlumnos> getIngresoAClasesSinFechasAlumnosToDelete
		(Set<IngresoAClasesSinFechasAlumnos> clientesorig,List<IngresoAClasesSinFechasAlumnos> clientesAquitarDeLista ){
	
		List<IngresoAClasesSinFechasAlumnos> clientes = new ArrayList<IngresoAClasesSinFechasAlumnos>();
		if(clientes != null){
			
			for (IngresoAClasesSinFechasAlumnos ingresoAClasesSinFechasAlumnos : clientesorig) {
				
				if(existeIngresoAClasesSinFechasAlumnos(ingresoAClasesSinFechasAlumnos, clientesAquitarDeLista)){
					clientes.add(ingresoAClasesSinFechasAlumnos);
				}
			}
		}
		return clientes;
	}
	
	private boolean existeIngresoAClasesClientes(Cliente clienteso,List<IngresoAClasesSinFechasAlumnos> clientesAquitarDeLista ){

		if(clientesAquitarDeLista != null){
	
			for (IngresoAClasesSinFechasAlumnos ingresoAClasesSinFechasAlumnos : clientesAquitarDeLista) {
		
				if(clienteso.getId() != null && ingresoAClasesSinFechasAlumnos.getCliente().getId() != null &&
						clienteso.getId().intValue() == ingresoAClasesSinFechasAlumnos.getCliente().getId().intValue())
					return true;
				}
		}
		return false;
	}
	
	private boolean existeIngresoAClasesSinFechasAlumnos(IngresoAClasesSinFechasAlumnos clienteso,List<IngresoAClasesSinFechasAlumnos> clientesAquitarDeLista ){

		if(clientesAquitarDeLista != null){
	
			for (IngresoAClasesSinFechasAlumnos ingresoAClasesSinFechasAlumnos : clientesAquitarDeLista) {
		
				if(clienteso.getId() != null && ingresoAClasesSinFechasAlumnos.getId() != null &&
						clienteso.getId().intValue() == ingresoAClasesSinFechasAlumnos.getId().intValue())
					return true;
				}
		}
		return false;
	}
	
	private Set<ClaseConListaAlumnosHistorico> guardarHistoricoClientesEnClase(Set<ClaseConListaAlumnos> clases){
		Set listaClaseConListaalumnosHistory= new HashSet();
		
		if(clases != null){
			for (ClaseConListaAlumnos claseConListaAlumnos : clases) {
				
				ClaseConListaAlumnosHistorico claseConListaHistorico= new ClaseConListaAlumnosHistorico();
				claseConListaHistorico.setCantPresentes(claseConListaAlumnos.getCantPresentes());
				claseConListaHistorico.setCantPresentesNoEncontrados(claseConListaAlumnos.getCantPresentesNoEncontrados());
				claseConListaHistorico.setClase(claseConListaAlumnos.getClase());
				claseConListaHistorico.setComentarioGeneralDelProfe(claseConListaAlumnos.getComentarioGeneralDelProfe());
				claseConListaHistorico.setFecha(claseConListaAlumnos.getFecha());
				claseConListaHistorico.setProfeNombre(claseConListaAlumnos.getProfeNombre());
//				claseConListaHistorico.setId(claseConListaAlumnos.getId());
				
				if(claseConListaAlumnos.getClienesListaEncontradosEnPiletaList() != null){
					claseDao.loadLazyAttribute(claseConListaAlumnos.getClienesListaEncontradosEnPiletaList());
					for (ClienteListaEncontradoEnPileta clientListaenc : claseConListaAlumnos.getClienesListaEncontradosEnPiletaList() ) {
						ClienteListaEncontradoEnPiletaHistorico clienHis= new ClienteListaEncontradoEnPiletaHistorico();
						clienHis.setAsistencia(clientListaenc.getAsistencia());
						clienHis.setClase(claseConListaHistorico);
						clienHis.setCliente(clientListaenc.getCliente());
						clienHis.setComentario(clientListaenc.getComentario());
						clienHis.setFecha(clientListaenc.getFecha());
//						clienHis.setId(clientListaenc.getId());
						
						if(claseConListaHistorico.getClienesListaEncontradosEnPiletaList() == null)
							claseConListaHistorico.setClienesListaEncontradosEnPiletaList(new HashSet());
						
						claseConListaHistorico.getClienesListaEncontradosEnPiletaList().add(clienHis);
						
					}
				}
				
				if(claseConListaAlumnos.getClienesNoEncontradosEnPiletaList() != null){
					claseDao.loadLazyAttribute(claseConListaAlumnos.getClienesNoEncontradosEnPiletaList());
					for (ClienteNoEncontradoEnPileta clientListaenc : claseConListaAlumnos.getClienesNoEncontradosEnPiletaList() ) {
						ClienteNoEncontradoEnPiletaHistorico hjis= new ClienteNoEncontradoEnPiletaHistorico();
						hjis.setCelular(clientListaenc.getCelular());
						hjis.setClase(claseConListaHistorico);
						hjis.setComentario(clientListaenc.getComentario());
						hjis.setFecha(clientListaenc.getFecha());
						hjis.setNombre(clientListaenc.getNombre());
						hjis.setTelefono(clientListaenc.getTelefono());
						
						if(claseConListaHistorico.getClienesNoEncontradosEnPiletaList() == null)
							claseConListaHistorico.setClienesNoEncontradosEnPiletaList(new HashSet());
						
						claseConListaHistorico.getClienesNoEncontradosEnPiletaList().add(hjis);
					}	
				}
				
				listaClaseConListaalumnosHistory.add(claseConListaHistorico);
			}
		}
		return listaClaseConListaalumnosHistory; 
	}
	
	private String getFilters(int day){
		Calendar cal = Calendar.getInstance();

		if(day == -1){
			day=cal.getTime().getDay();
		}
		String aConcatenerDespues = null;
		if(day == 0) // dom
			aConcatenerDespues="domingo is true ";
		if(day == 1) // lunes
			aConcatenerDespues="lunes is true ";
		if(day == 2) // martes
			aConcatenerDespues="martes is true ";
		if(day == 3) // miercoles
			aConcatenerDespues="miercoles is true ";
		if(day == 4) // jueves
			aConcatenerDespues="jueves is true ";
		if(day == 5) // viernes
			aConcatenerDespues="viernes is true ";
		if(day == 6) // sabado
			aConcatenerDespues="sabado is true ";
		if(day == 8){ // Todos
			aConcatenerDespues=" ( domingo is true   or lunes is true or martes is true or miercoles is true or jueves is true or viernes is true " +
					"  or sabado is true   )";
		}
		
		StringBuilder actionConditions= new StringBuilder("	select clase.id from clase  " );
			actionConditions.append(" inner join actividad act on (clase.idactividad = act.id) ");

			actionConditions.append(" where 1=1  ");							
//			actionConditions.append(" and act.tomalista is true  and  "+ aConcatenerDespues );
			actionConditions.append(" and  "+ aConcatenerDespues );
			actionConditions.append(" order by clase.horadesde asc ");
				
			
		return actionConditions.toString();
	}
	
	public List<Quincena> obtenerQuincenalesList(){
		Statement stat=null;
		ResultSet rs = null;
		Connection cnx 			= null;
		List<Quincena> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			String filters= " select * from quincena  order by id ";
			rs = stat.executeQuery(filters);
			
			while (rs.next()) {
				Quincena cli= new Quincena();
				if(rs.getObject("id") != null){
					cli.setId(rs.getLong("id"));
				}
				if(rs.getObject("nombre") != null){
					cli.setNombre(rs.getString("nombre"));
				}
				if(clientList == null)
					clientList= new ArrayList();
				clientList.add(cli);
			}
			return clientList;
		
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
	
	
	public User findByName(Integer userID) throws DAOException{
		return userDao.findByName(userID);
	}
}
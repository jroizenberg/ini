package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.dao.ClienteDAO;
import com.institucion.dao.SubscripcionDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.fm.security.dao.UserDAO;
import com.institucion.fm.security.model.User;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.Subscripcion;
import com.institucion.model.VencimientoCursoEnum;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class ClienteDAOImpl extends ClientDao<Cliente> implements ClienteDAO {

	private static Log log = LogFactory.getLog(ClienteDAOImpl.class);
	
	
	@Autowired
	AbstractSessionFactoryBean session;
	
	@Autowired
	SubscripcionDAO subscripcionDao;
	
	@Autowired
	private UserDAO userDao = null;

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
	public Cliente create(Cliente c) throws DAOException {
		return super.create(c);
		
	}

	@Override
	public Long createSubs(Cliente c) throws DAOException {
		return super.createSubs(c);
		
	}
	public List<Cliente> findAll() throws DAOException{
		 return this.find("from com.institucion.model.Cliente order by apellido, nombre",  null);
	}

	public Long getCount(String query){
		Connection cnx = null;
		
		Statement stat=null;
		ResultSet rs = null;

		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
			
			while (rs.next()) {
				return (long) rs.getInt(1);
			}
			return (long) 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
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
		return (long) 0;
	}

	public Date getFechaVentaAdeuda(String query){
		Connection cnx = null;
		Statement stat=null;
		ResultSet rs = null;
		Date date= null;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
			while (rs.next()) {
				return rs.getDate(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
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
		return date;
	}

	public List<Integer> getResponsableAdeuda(String query){
		Connection cnx = null;
		
		Statement stat=null;
		ResultSet rs = null;
		List list= new ArrayList();
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
			
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
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
		return list;
	}

	
	public Map<Integer, LinkedList<String>> findAllActividadesConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		Statement stat=null;
		ResultSet rs = null;		
		Map<Integer, LinkedList<String>>  mapa = new HashMap<Integer, LinkedList<String>>(); 
		
		String query=
				" select a.nombre, cur.nombre, c.apellido, c.nombre , c.dni , s.fechaingresosubscripcion::text , " +
				" ca.fechacomienzo, ca.fechafin, ca.estado::text, ca.cupos::text  from cliente c  "+
				" inner join subscripcion s on (c.id= s.idcliente) "+
				" inner join cupoactividad ca on (s.id= ca.idsubscripcion) "+
				" inner join actividad a on (a.id= ca.idactividad) "+
				" inner join curso cur on (cur.id= ca.idcurso)  "+
			" where  "+
				" (s.anulasubscripcion is null or s.anulasubscripcion is false) ";
			
		if(filters != null)
			query= query +"  "+  filters; 

		query= query +" order by a.nombre, cur.nombre, c.apellido ,c.nombre , c.dni,  ca.cupos desc ";
		
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(query);
			Integer i=0;
			while (rs.next()) {
				LinkedList<String>  list= new LinkedList<String>();
			
				if(rs.getString(1) != null)
					list.add(rs.getString(1));
				else
					list.add(" ");
					
				if(rs.getString(2) != null)
					list.add(rs.getString(2));
				else
					list.add(" ");
					
				if(rs.getString(3) != null)
					list.add(rs.getString(3));
				else
					list.add(" ");
					
				if(rs.getString(4) != null)
					list.add(rs.getString(4));
				else
					list.add(" ");
					
				if(rs.getString(5) != null)
					list.add(rs.getString(5));
				else
					list.add(" ");	
					
				if(rs.getString(6) != null)
					list.add(rs.getString(6));
				else
					list.add(" ");	
				
				if(rs.getDate(7) != null){  // fecha comienzo
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(rs.getDate(7));
					int mes=ahoraCal.get(Calendar.MONTH) + 1;
	
					String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					list.add(fechaNac);
				}else
					list.add(" ");
				
				if(rs.getDate(8) != null){  // fecha fin
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(rs.getDate(8));
					int mes=ahoraCal.get(Calendar.MONTH) + 1;

					String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					list.add(fechaNac);
				}else
					list.add(" ");

				if(rs.getString(9) != null){
	    			String nombre=CupoActividadEstadoEnum.toString(Integer.parseInt(rs.getString(9)));
					list.add(nombre);
				}else
					list.add(" ");	
					
				if(rs.getString(10) != null)
					list.add(rs.getString(10));
				else
					list.add(" ");	
				
				mapa.put(i, list);
				i++;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {    
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
		return mapa;
	}

	
	public List<Cliente> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Cliente> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Cliente cli=findById(new Long(integer));
					if(cli != null){
						if(clientList == null)
							clientList= new ArrayList<Cliente>();
						clientList.add(cli);
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
		return clientList;
	}
	
	public List<ClienteView> findAllConJdbcSinHibernate(String filters) throws DAOException{
		Statement stat=null;
		ResultSet rs = null;
		ClienteView obj = null;
		List<ClienteView> list = null;
		Connection cnx= null;
		try {
			cnx = session.getDataSource().getConnection();
			stat = cnx.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stat.executeQuery(filters);
			list = new ArrayList<ClienteView>();
			while (rs.next()) {
				  obj= new ClienteView();
				  
				 if (rs.getObject("id")!=null){
					 obj.setId(rs.getLong("id"));
				 }
				 if (rs.getObject("apellido")!=null){
					  obj.setApellido(rs.getString("apellido"));
				 }
				 
				 if (rs.getObject("nombre")!=null){
					  obj.setNombre(rs.getString("nombre"));
				 }
				 
				 if (rs.getObject("dni")!=null){
					  obj.setDni(rs.getString("dni"));
				 }

				 if (rs.getObject("fechanacimiento")!=null){
					  obj.setFechaNacimiento(rs.getDate("fechanacimiento"));
				 }

				 if (rs.getObject("domicilio")!=null){
					  obj.setDomicilio(rs.getString("domicilio"));
				 }
				 if (rs.getObject("celular")!=null){
					  obj.setCelular(rs.getString("celular"));
				 }
				 if (rs.getObject("telefonofijo")!=null){
					  obj.setTelefonoFijo(rs.getString("telefonofijo"));
				 }
				 if (rs.getObject("facebook")!=null){
					  obj.setFacebook(rs.getString("facebook"));
				 }
				 if (rs.getObject("mail")!=null){
					  obj.setMail(rs.getString("mail"));
				 }				 
				 
				 if (rs.getObject("fechayhoracreacion")!=null){
					  obj.setFechaVenta(rs.getDate("fechayhoracreacion"));
				 }
				 
				 if (rs.getObject("idusuariocreosubscripcion")!=null){
					 Integer i= rs.getInt("idusuariocreosubscripcion");
						User u=userDao.findByName(i);
						if(u != null)
							obj.setResponsable(u.getName());
				 }
				 
				 list.add(obj);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(rs != null)
					rs.close();
				if(stat != null)
					stat.close();
				if(cnx != null)
					cnx.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;

	}
	
	public List<Cliente> findAllConJdbc2(String filters) throws DAOException{
		Statement stat=null;
		ResultSet rs = null;
		Connection cnx 			= null;
		List<Cliente> clientList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			filters= filters + " order by apellido, nombre";
			rs = stat.executeQuery(filters);
			
			while (rs.next()) {
				Cliente cli= new Cliente();
				if(rs.getObject("id") != null){
					cli.setId(rs.getLong("id"));
				}
				if(rs.getObject("apellido") != null){
					cli.setApellido(rs.getString("apellido"));
				}
				if(rs.getObject("nombre") != null){
					cli.setNombre(rs.getString("nombre"));

				}
				if(rs.getObject("fechanacimiento") != null){
					cli.setFechaNacimiento(rs.getDate("fechanacimiento"));

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
	
	
	public Cliente findById(Long id) throws DAOException{
		return this.findById(id, Cliente.class);
	}

	@Override
	public void delete(Cliente c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Cliente save(Cliente c) throws DAOException{
		return super.save(c);
	}
	
	
	private boolean tieneCosasIniAProcesar(Subscripcion subs){
		boolean estaVencida= false;
		
		for (CupoActividad cupoAct : subs.getCupoActividadList()) {
			
			if( cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("INI VERANO") && cupoAct.getCupos() >0){
				if(( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt()) 
						|| ( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()))
					return true;
			
			}else if(cupoAct.getCurso() != null){
				long fechaInicialMs = subs.getFechaIngresoSubscripcion().getTime(); //Tanto fecha inicial como fecha final son Date.
				long fechaFinalMs = new Date().getTime();
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
		return estaVencida;
	}

	
	
	
	private boolean tieneCosasAProcesar(Subscripcion subs){
		boolean estaVencida= false;
		
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

		return estaVencida;
	}
	
	
	public Cliente actualizarEstadosCumpleaniosACliente(Cliente cli){
		/*	 
		 * 	Hacer otro Quartz que se encargue de actualizar los estados de las subscripciones a VENCIDO o VENCIDO CON DEuDA
		 *   este debe actualizar solamente los cursos que son cumpleanios, 
		 *   los que comiencen con 
		 * 
		 */
		if(cli != null){
	
			if(cli.getSubscripcionesList() != null){
				for (Subscripcion subs : cli.getSubscripcionesList()) {
					subs=actualizarEstadoVencimientosDeSubscripcionDeCumpleanios(subs);
					//Grabo el Estado de la Subscripcion . VER si haCE FALTA HACER ESTO POR QUE DESPUES GUARDA IGUAL AL CLIENTE
						subscripcionDao.save(subs);
				}
			}
		}
		return cli;
		
	}
	
	public Cliente actualizarEstadosCursosIniACliente(Cliente cli){
		/*	 
		 * Configurar el Quartz para: Ejecutar este Job 1 vez por dia.
		 * 	Hacer otro Quartz que se encargue de actualizar los estados de las subscripciones a VENCIDO o VENCIDO CON DEuDA
		 *  
		 * 
		 */
		if(cli != null){
	
			if(cli.getSubscripcionesList() != null){
				for (Subscripcion subs : cli.getSubscripcionesList()) {
					
					if(tieneCosasIniAProcesar(subs)){
						subs=actualizarEstadoINIDeSubscripcion(subs);
						
						//Grabo el Estado de la Subscripcion . VER si haCE FALTA HACER ESTO POR QUE DESPUES GUARDA IGUAL AL CLIENTE
						subscripcionDao.save(subs);
					}
				}
			}
		}
		return cli;
	}
	
	
	public Cliente actualizarEstadosVencidosACliente(Cliente cli){
		/*	 
		 * Configurar el Quartz para: Ejecutar este Job 1 vez por dia.
		 * 	Hacer otro Quartz que se encargue de actualizar los estados de las subscripciones a VENCIDO o VENCIDO CON DEuDA
		 *  
		 * 
		 */
		if(cli != null){
	
			if(cli.getSubscripcionesList() != null){
				for (Subscripcion subs : cli.getSubscripcionesList()) {
					
					if(tieneCosasAProcesar(subs)){
						subs=actualizarEstadoVencimientosDeSubscripcion(subs);
						
						//Grabo el Estado de la Subscripcion . VER si haCE FALTA HACER ESTO POR QUE DESPUES GUARDA IGUAL AL CLIENTE
						subscripcionDao.save(subs);
					}
				}
			}
		}
		return cli;
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
	
	
	private Subscripcion actualizarEstadoVencimientosDeSubscripcionDeCumpleanios(Subscripcion subs){
		if(subs != null && subs.getCupoActividadList() != null){
				
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				 
				if(cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("CUMPLEA")){
					// si la fecha de pago de ls subcripcion es mayor a 7 dias de la fecha de cumpleaños.
						Date fechaCumpleanios=getFechaCumpleanios(subs, cupoAct.getCurso().getId() , cupoAct.getActividad().getId());

						if(fechaCumpleanios != null && new Date().after(fechaCumpleanios)){
							long fechaInicialMs = new Date().getTime(); //Tanto fecha inicial como fecha final son Date.
							long fechaFinalMs = fechaCumpleanios.getTime();
							long diferencia = fechaInicialMs- fechaFinalMs ;
							double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
							
							if(dias > 8){
									if( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt()
											||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
											||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt()
											||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt()
											||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
										
										// dejo el estado como estaba originalmente
										
									}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
										cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
										
									}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt() ){
										cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA);
										
									}
									// aca deberia ver de poner a la subscripcion vencida
								}																
						}
				}

			}
		}
		return subs;
	}
	
	private Subscripcion actualizarEstadoVencimientosDeSubscripcion(Subscripcion subs){
		if(subs != null && subs.getCupoActividadList() != null){
				
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(cupoAct.getFechaFin() != null && cupoAct.getFechaFin().before(new Date())){
					if( cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt()
							||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
						
						// dejo el estado como estaba originalmente
						
					}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
						cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
						
					}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt() ){
						cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA);
					}
					// aca deberia ver de poner a la subscripcion vencida
				}
			}
		}
		return subs;
	}
	
	private Subscripcion actualizarEstadoINIDeSubscripcion(Subscripcion subs){
		if(subs != null && subs.getCupoActividadList() != null){
				
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				
				if(cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("INI VERANO")){

					// si el mes actual es mayor o igual a abril y menos que noviembre, y si tiene cupos > 0, dejo los cupos en 0 y actualizo estado
					Calendar dd= Calendar.getInstance();
					int mes=dd.get(Calendar.MONTH);
					
					if(mes >=3 && mes <10 && cupoAct.getCupos() >0){
						cupoAct.setCupos(0);
						if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt() )
							cupoAct.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);	
						else	
							cupoAct.setEstado(CupoActividadEstadoEnum.S_CUPOS);
					}	
				}else if(cupoAct.getCurso() != null){
					long fechaInicialMs = subs.getFechaYHoraCreacion().getTime(); //Tanto fecha inicial como fecha final son Date.
					long fechaFinalMs = new Date().getTime();
					long diferencia2 = fechaFinalMs- fechaInicialMs ;
					double dias = Math.floor(diferencia2 / (1000 * 60 * 60 * 24));
					boolean seLoProcesa=false;
					if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt()
							&& dias >20){
						seLoProcesa=true;
					}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()
							&& dias >15){
						seLoProcesa=true;	
					}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()
							&& dias >100){
						seLoProcesa=true;
					}else if(cupoAct.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()
							&& dias >40){
						seLoProcesa=true;
					}
					if(seLoProcesa){
						if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt() ){
							cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
						}else{	
							cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA);
						}
						Calendar calendar = Calendar.getInstance();
					    calendar.add(Calendar.DAY_OF_YEAR, -2);  // numero de días a añadir, o restar en caso de días<0
						cupoAct.setFechaComienzo(calendar.getTime());
						
						Calendar calendarf = Calendar.getInstance();
					    calendarf.add(Calendar.DAY_OF_YEAR, -1);  // numero de días a añadir, o restar en caso de días<0

						cupoAct.setFechaFin(calendarf.getTime());
					}
				}
			}
		}
		return subs;
	}
	@Override
	public void loadLazyAttribute(Object attribute) throws DAOException {
		if (attribute != null)
			super.loadLazyAttribute(attribute);
	}
 
}
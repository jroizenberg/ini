package com.institucion.dao.spi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import com.institucion.dao.ClaseDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.Quincena;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class ClaseDAOImpl extends ClientDao<Clase> implements ClaseDAO {

	private static Log log = LogFactory.getLog(ClaseDAOImpl.class);
	
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

	public List<Quincena> findAllConActividadTomaListaDelDiaNombreCurso() throws DAOException{
        return findAllQuincenas("select id, nombre from quincena order by id ");
	}
	
	public List<Quincena> findAllQuincenas(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Quincena> claseList= null;
		Statement stat=null;
		ResultSet rs = null;
		try {
			cnx	= session.getDataSource().getConnection();
			stat = cnx.createStatement();
			rs = stat.executeQuery(filters.toString());
			
			while (rs.next()) {
				Quincena qq= new Quincena();
				if(claseList == null)
					claseList= new ArrayList();
				if(rs.getObject("id") != null)
					qq.setId(rs.getLong("id"));
				
				if(rs.getObject("nombre") != null)
					qq.setNombre(rs.getString("nombre"));
				claseList.add(qq);
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
		return claseList;
	}
	@Override
	public Clase create(Clase c) throws DAOException {
		return super.create(c);
		
	}

	public List<Clase> findAllConActividadTomaLista() throws DAOException{
		return this.findAll(Clase.class);
	}
	
	public List<Clase> findAll() throws DAOException{
		return this.findAll(Clase.class);
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
			sql.append(" select new com.institucion.model.ClaseConListaAlumnos(id, profeNombre,comentarioGeneralDelProfe,cantPresentes, cantPresentesNoEncontrados)  from   ClaseConListaAlumnos " );
			sql.append(" WHERE clase.id = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
				
			List <ClaseConListaAlumnos> list= super.findClaseConListaAlumnos(sql.toString(), params);
			if(list != null && list.size() >0){
				return list.get(0);
			}

		return null;
	}

	public ClaseConListaAlumnosHistorico findAllClaseConListaAlumnosByClaseAndFechaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
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
			sql.append(" select new com.institucion.model.ClaseConListaAlumnosHistorico(id, profeNombre,comentarioGeneralDelProfe,cantPresentes, cantPresentesNoEncontrados)  from   ClaseConListaAlumnosHistorico " );
			sql.append(" WHERE clase.id = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
				
			List <ClaseConListaAlumnosHistorico> list= super.findClaseConListaAlumnosHistorico(sql.toString(), params);
			if(list != null && list.size() >0){
				return list.get(0);
			}

		return null;
	}
	
	public List<ClienteListaEncontradoEnPileta> findAllByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		
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
			sql.append("from com.institucion.model.ClienteListaEncontradoEnPileta " );
			sql.append("WHERE idlistaclase = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
			
			return super.findClienteListaEncontradoEnPileta(sql.toString(), params);
		
	}
	
	public List<ClienteListaEncontradoEnPiletaHistorico> findAllByClaseAndFechaHistorico(Long idListaClase,Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		if(idListaClase != null){
			params.put("claseID", idListaClase);
		}else{
			params.put("claseID", idClase);
		}
		
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
		
		if(idListaClase != null){
			sql.append("from com.institucion.model.ClienteListaEncontradoEnPiletaHistorico " );
			sql.append("WHERE idlistaclase = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
		}else{
			// si la consulta se hizo por clase debo 
			sql.append("from com.institucion.model.ClienteListaEncontradoEnPiletaHistorico aa " );
			sql.append("WHERE aa.clase.clase.id = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
			
//			select * from ClienteListaEncontradoEnPiletaHistorico aa 
//			inner join clase c on (c.id= 94)  AND  to_char(fecha,'YYYY-MM-DD')='2014-07-15'
		}
		
		return super.findClienteListaEncontradoEnPiletaHistorico(sql.toString(), params);
	}
	
	public List<ClienteNoEncontradoEnPileta> findAllByClaseAndFechaClienteNoEncontradoEnPileta(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		
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
			sql.append("from com.institucion.model.ClienteNoEncontradoEnPileta " );
			sql.append("WHERE idlistaclase = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
			
			return super.findClienteNoEncontradoEnPileta(sql.toString(), params);
		
	}
	
	public List<ClienteNoEncontradoEnPiletaHistorico> findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		
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
			sql.append("from com.institucion.model.ClienteNoEncontradoEnPiletaHistorico " );
			sql.append("WHERE idlistaclase = :claseID  AND  to_char(fecha,'YYYY-MM-DD')= :fechaID  ");
			
			return super.findClienteNoEncontradoEnPiletaHistorico(sql.toString(), params);
		
	}
	public List<Clase> findAllByIdActividad(Long idActividad, int day) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actividadID", idActividad);
		Calendar cal = Calendar.getInstance();

		params.put("valorTrue", true);
		params.put("disponibleEs", true);

		if(day == -1){
			day=cal.getTime().getDay();
		}
		StringBuffer sql = new StringBuffer();
		sql.append("from com.institucion.model.Clase " );
		sql.append("WHERE actividad.id = :actividadID  AND  ");
		if(day == 0) // dom
			sql.append("domingo = :valorTrue");
		if(day == 1) // lunes
			sql.append("lunes= :valorTrue");
		if(day == 2) // martes
			sql.append("martes= :valorTrue");
		if(day == 3) // miercoles
			sql.append("miercoles= :valorTrue");
		if(day == 4) // jueves
			sql.append("jueves= :valorTrue");
		if(day == 5) // viernes
			sql.append("viernes= :valorTrue");
		if(day == 6) // sabado
			sql.append("sabado= :valorTrue");
		if(day == 8){ // Todos
			sql.append(" ( domingo = :valorTrue   or lunes= :valorTrue or martes= :valorTrue or miercoles= :valorTrue or jueves= :valorTrue or viernes= :valorTrue" +
					"  or sabado= :valorTrue   )");
		}
		sql.append(" and disponible= :disponibleEs ");
		sql.append(" order by actividad.nombre, horaDesde");
		
		return super.find(sql.toString(), params);
	}
	
	public Clase findById(Long id) throws DAOException{
		return this.findById(id, Clase.class);
	}

	@Override
	public void delete(Clase c) throws DAOException{
		super.delete(c);
	}

	@Override
	public Clase save(Clase c) throws DAOException{
		return super.save(c);
	}
	
	@Override
	public void save(ClaseConListaAlumnos c) throws DAOException{
		super.save(c);
	}
	@Override
	public void delete(ClaseConListaAlumnos c) throws DAOException{
		super.delete(c);
	}

	public List<Clase> findAllConJdbc(String filters) throws DAOException{
		Connection cnx 			= null;
		List<Clase> claseList= null;
		try {
			cnx	= session.getDataSource().getConnection();
			List<Integer> list =getClientsByFilters(cnx, filters);
			
			if(list != null){
				for (Integer integer : list) {
					Clase cli=findById(new Long(integer));
					if(cli != null){
						if(claseList == null)
							claseList= new ArrayList<Clase>();
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
	
	  public List<Clase> findAllConActividadTomaListaDelDia(boolean isFromReports)   throws DAOException  {
	    Map<String, Object> params = new HashMap();
	    Calendar calendarDate = Calendar.getInstance();
	    calendarDate.setTime(new Date());
	    List<Clase> list = new ArrayList();
	    int day = calendarDate.getTime().getDay();
	    StringBuffer sql = new StringBuffer();
	    String query=" select new com.institucion.model.Clase   (id, nombre,horaDesde,horaHasta, lunes, martes, miercoles, jueves, viernes, sabado, domingo)  " +
	    		" from   Clase  where esclasesinhora is false and  ";
	    if(!isFromReports){
	    	sql.append(query);
		    if (day == 0) {
			      sql.append("domingo is true");
		    }
		    if (day == 1) {
		      sql.append("lunes is true");
		    }
		    if (day == 2) {
		      sql.append("martes is true");
		    }
		    if (day == 3) {
		      sql.append("miercoles is true");
		    }
		    if (day == 4) {
		      sql.append("jueves is true");
		    }
		    if (day == 5) {
		      sql.append("viernes is true");
		    }
		    if (day == 6) {
		      sql.append("sabado is true");
		    }	 
		    sql.append(" order by horadesde ");
		    list = super.find(sql.toString(), params);
		    
	    }else{
		    List listLunes = super.find(query + " lunes is true order by horadesde", params);
		    List listMartes = super.find(query + " martes is true order by horadesde", params);
		    List listMiercoles = super.find(query + " miercoles is true order by horadesde", params);
		    List listJueves = super.find(query + " jueves is true order by horadesde", params);
		    List listViernes = super.find(query + " viernes is true order by horadesde", params);
		    List listSabado = super.find(query + " sabado is true order by horadesde", params);
		    List listDomingo = super.find(query + " domingo is true order by horadesde", params);

		    if(listLunes != null && listLunes.size() >0){
		    	list.addAll(listLunes);
		    }
		    if(listMartes != null && listMartes.size() >0){
		    	list.addAll(listMartes);
		    }
		    if(listMiercoles != null && listMiercoles.size() >0){
		    	list.addAll(listMiercoles);
		    }
		    if(listJueves != null && listJueves.size() >0){
		    	list.addAll(listJueves);
		    }
		    if(listViernes != null && listViernes.size() >0){
		    	list.addAll(listViernes);
		    }
		    if(listSabado != null && listSabado.size() >0){
		    	list.addAll(listSabado);
		    }
		    if(listDomingo != null && listDomingo.size() >0){
		    	list.addAll(listDomingo);
		    }
	    }
	    return list;
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
	
	public void loadLazyAttribute(Object attribute) throws DAOException {
		super.loadLazyAttribute(attribute);
	}
	
}
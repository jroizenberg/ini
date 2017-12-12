package com.institucion.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.dao.IngresoAClasesSinFechasAlumnosDAO;
import com.institucion.fm.conf.dao.SqlExecutor;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.dao.spi.ClientDao;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
/**
 * The Class HealthProfessionalDAOImpl.
 */
public class IngresoAClasesSinFechasAlumnosDAOImpl extends ClientDao<IngresoAClasesSinFechasAlumnos> implements IngresoAClasesSinFechasAlumnosDAO {
	
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
	public IngresoAClasesSinFechasAlumnos create(IngresoAClasesSinFechasAlumnos c) throws DAOException {
		return super.create(c);	
	}

	@Override
	public void delete(IngresoAClasesSinFechasAlumnos c) throws DAOException {
		super.delete(c);
		
	}

	@Override
	public IngresoAClasesSinFechasAlumnos save(IngresoAClasesSinFechasAlumnos c) throws DAOException {
		return super.save(c);
		
	}

	@Override
	public IngresoAClasesSinFechasAlumnos findByIdActividad(Long id) throws DAOException {
		return this.findById(id, IngresoAClasesSinFechasAlumnos.class);

	}
	
	@Override
	public List<IngresoAClasesSinFechasAlumnos> findAllByCliente(Cliente cli) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tomalista",true );
		return this.find("from Actividad where tomalista = :tomalista ", params);	

		
	}
	
	@Override
	public List<IngresoAClasesSinFechasAlumnos> findAllByClase(Clase clase) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tomalista",true );
		return this.find("from Actividad where usaEnObraSocial = :tomalista ", params);	
	}

	@Override
	public void eliminarTodosLosQueLaClaseEsNula() throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();

		List<IngresoAClasesSinFechasAlumnos> ins= this.find("from IngresoAClasesSinFechasAlumnos where idclase is null ", params);	
		if(ins != null){
			for (IngresoAClasesSinFechasAlumnos object : ins) {
				delete(object);
			}
		}
	}
	
	public List<IngresoAClasesSinFechasAlumnos> findAllByClienteClase(Clase clase, Cliente cliente) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		List lista=this.find("from IngresoAClasesSinFechasAlumnos where clase.id ="+clase.getId()  +"  AND cliente.id= "+ cliente.getId() , params);
		return lista;
	}

	public Boolean existeClienteEnClaseSinFecha(Clase clase, Cliente cli) throws DAOException {
		Map<String, Object> params = new HashMap<String, Object>();
		List lista=this.find("from IngresoAClasesSinFechasAlumnos where clase.id ="+clase.getId()  +"  AND cliente.id= "+ cli.getId() , params);
		if(lista != null && lista.size()> 0)
			return true;
		else 
			return false;
	}
}
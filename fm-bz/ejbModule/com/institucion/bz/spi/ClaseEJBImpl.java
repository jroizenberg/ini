package com.institucion.bz.spi;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.ClaseEJBLocal;
import com.institucion.bz.ClaseEJBRemote;
import com.institucion.dao.ClaseDAO;
import com.institucion.dao.IngresoAClasesSinFechasAlumnosDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Clase;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
import com.institucion.model.Quincena;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class ClaseEJBImpl implements ClaseEJBRemote,ClaseEJBLocal {

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private ClaseDAO claseDao = null;
	
	@Autowired
	private IngresoAClasesSinFechasAlumnosDAO ingresoAClaseSinFechasDao = null;
	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public ClaseEJBImpl() {	}

	
	@Override
	public void create(Clase c) throws DAOException{
		claseDao.create(c);
	}
	
	@Override
	public void delete(Clase c) throws DAOException{
		claseDao.delete(c);
	}
	
	@Override
	public void save(Clase c) throws DAOException{
		claseDao.save(c);	
	}
	
	@Override
	public Clase findById(Long id) throws DAOException{
        return claseDao.findById(id);
    }
	
	@Override
	public List<Clase> findAll() throws DAOException{
        return claseDao.findAll();
	}

	@Override
	public List<Clase> findAllConActividadTomaLista() throws DAOException{
        return claseDao.findAllConActividadTomaLista();
	}
	
	@Override
	public List<Clase> findAllConActividadTomaListaDelDia(boolean isFromReports) throws DAOException{
        return claseDao.findAllConActividadTomaListaDelDia(isFromReports);
	}
	
	@Override
	public List<Quincena> findAllConActividadTomaListaDelDiaNombreCurso() throws DAOException{
		return claseDao.findAllConActividadTomaListaDelDiaNombreCurso();

	}
	
	@Override
	public List<Clase> findAllByActividad(Long idActividad, int day) throws DAOException{
        return claseDao.findAllByIdActividad(idActividad, day);
	}
	
	@Override
	public List<Clase> findAllConJdbc(String filters) throws DAOException{
        return claseDao.findAllConJdbc(filters);
	}
	
	@Override
	public List<ClienteListaEncontradoEnPileta> findAllByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllByClaseAndFecha(idClase, fecha, esConFechaExacta);
	}

	@Override
	public List<ClienteListaEncontradoEnPiletaHistorico> findAllByClaseAndFechaHistorico(Long idListaClase, Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllByClaseAndFechaHistorico(idListaClase, idClase, fecha, esConFechaExacta);
	}

	@Override
	public ClaseConListaAlumnos findAllClaseConListaAlumnosByClaseAndFecha(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllClaseConListaAlumnosByClaseAndFecha(idClase, fecha, esConFechaExacta);
	}
	
	@Override
	public ClaseConListaAlumnosHistorico findAllClaseConListaAlumnosByClaseAndFechaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllClaseConListaAlumnosByClaseAndFechaHistorico(idClase, fecha, esConFechaExacta);
	}
	
	@Override
	public List<ClienteNoEncontradoEnPileta> findAllByClaseAndFechaClienteNoEncontradoEnPileta(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllByClaseAndFechaClienteNoEncontradoEnPileta(idClase, fecha, esConFechaExacta);
	}

	@Override
	public List<ClienteNoEncontradoEnPiletaHistorico> findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(Long idClase, Date fecha, boolean esConFechaExacta) throws DAOException{
		 return claseDao.findAllByClaseAndFechaClienteNoEncontradoEnPiletaHistorico(idClase, fecha, esConFechaExacta);
	}
	
	@Override
	public void save(ClaseConListaAlumnos  c) throws DAOException{
		claseDao.save(c);	
	}
	
	
	@Override
	public void create(IngresoAClasesSinFechasAlumnos c) throws DAOException{
		ingresoAClaseSinFechasDao.create(c);
	}
	
	@Override
	public void delete(IngresoAClasesSinFechasAlumnos c) throws DAOException{
		ingresoAClaseSinFechasDao.delete(c);
	}
	
	@Override
	public void save(IngresoAClasesSinFechasAlumnos c) throws DAOException{
		ingresoAClaseSinFechasDao.save(c);	
	}

	@Override
	public List<IngresoAClasesSinFechasAlumnos> findAllByCliente(Cliente cli) throws DAOException{
        return ingresoAClaseSinFechasDao.findAllByCliente(cli);
	}
	
	@Override
	public List<IngresoAClasesSinFechasAlumnos> findAllByClase(Clase clase) throws DAOException{
        return ingresoAClaseSinFechasDao.findAllByClase(clase);
	}
	
	@Override
	public List<IngresoAClasesSinFechasAlumnos> findAllByClienteyClase(Cliente cli, Clase clase) throws DAOException{
        return ingresoAClaseSinFechasDao.findAllByClienteClase(clase, cli);
	}
	
	@Override
	public Boolean existeClienteEnClaseSinFecha(Cliente cli, Clase cla) throws DAOException{
		return ingresoAClaseSinFechasDao.existeClienteEnClaseSinFecha(cla, cli);
	}
}
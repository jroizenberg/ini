package com.institucion.bz.spi;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.orm.hibernate3.AbstractSessionFactoryBean;

import com.institucion.bz.CursoEJBLocal;
import com.institucion.bz.CursoEJBRemote;
import com.institucion.dao.ActividadDAO;
import com.institucion.dao.CursoDAO;
import com.institucion.dao.MatriculaDAO;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.conf.exception.EJBExceptionHandler;
import com.institucion.model.Actividad;
import com.institucion.model.Curso;
import com.institucion.model.Matricula;
import com.institucion.model.SubsYCurso;

/**
 * The Class HealthProfessionalEJBImpl.
 */
@Stateless
@Interceptors( { SpringBeanAutowiringInterceptor.class,	EJBExceptionHandler.class })
public class CursoEJBImpl implements CursoEJBRemote,CursoEJBLocal {

	private static Log log = LogFactory.getLog(CursoEJBImpl.class);

	/** The session. */
	@Autowired
	AbstractSessionFactoryBean session;

	@Autowired
	private CursoDAO cursoDao = null;
	
	@Autowired
	private ActividadDAO actividadDao = null;
	@Autowired
	private MatriculaDAO matriculaDao = null;

	/**
	 * Instantiates a new health professional ejb impl.
	 */
	public CursoEJBImpl() {	}

	
	@Override
	public void create(Curso c) throws DAOException{
		cursoDao.create(c);
	}
	
	@Override
	public void delete(Curso c) throws DAOException{
		cursoDao.delete(c);
	}
	
	@Override
	public void save(Curso c) throws DAOException{
		cursoDao.save(c);	
	}
	
	@Override
	public Curso findById(Long id) throws DAOException{
        return cursoDao.findById(id);
    }
	
	@Override
	public List<Curso> findAll() throws DAOException{
        return cursoDao.findAll();
	}
	
	@Override
	public List<Curso> findAll2() throws DAOException{
        return cursoDao.findAll2();
	}
	
	
	@Override
	public List<Curso> findAll2WithDisponible(boolean esMaipu, boolean esCentro) throws DAOException{
        return cursoDao.findAll2WithDisponible(esMaipu, esCentro);
	}
	
	@Override
	public void create(Actividad c) throws DAOException {
		actividadDao.create(c);
	}


	@Override
	public void delete(Actividad c) throws DAOException {
		actividadDao.delete(c);
	}


	@Override
	public void save(Actividad c) throws DAOException {
		actividadDao.save(c);
		
	}


	@Override
	public Actividad findByIdActividad(Long id) throws DAOException {
		return actividadDao.findByIdActividad(id);
	}


	@Override
	public List<Actividad> findAllActividad() throws DAOException {
        return actividadDao.findAllActividad();
	}


	@Override
	public List<Actividad> findAllActividadQueTomenLista() throws DAOException {
        return actividadDao.findAllActividadQueTomenLista();
	}

	@Override
	public List<Actividad> findAllActividadQueUsaCarnet(Integer idTipoCurso) throws DAOException {
        return actividadDao.findAllActividadQueUsaCarnet(idTipoCurso);
	}
	
	
	@Override
	public List<Actividad> findAllActividadQueConObraSocial() throws DAOException {
        return actividadDao.findAllActividadQueConObraSocial();
	}

	@Override
	public List<Actividad> findAllActividadParaPagoSueldos() throws DAOException {
        return actividadDao.findAllActividadParaPagoSueldos();
	}

	
	@Override
	public List<Actividad> findAActividadByIdTipoCurso(Long idTipoActividad) throws DAOException {
        return actividadDao.findAllActividadByTipoActividad(idTipoActividad);
	}

	@Override
	public List<Curso> findAllConJdbc(String filters) throws DAOException{
        return cursoDao.findAllConJdbc(filters);
	}
	
	@Override
	public List<SubsYCurso> findAllConJdbcSubsYCurso(String filters) throws DAOException{
        return cursoDao.findAllConJdbcSubsYCurso(filters);
	}
	
	@Override
	public Matricula findMatricula() throws DAOException{
		List<Matricula> matric=matriculaDao.findAll();
		if(matric != null && matric.size() >0)
			return matric.get(0);
		else return null;
	}	

	@Override
	public boolean findImprimible() throws DAOException{
		return matriculaDao.findAllIsImprimible();
	}	

	
	@Override
	public void saveMatricula(Matricula mat) throws DAOException{
		matriculaDao.save(mat);
	}
	
	@Override
	public void saveImprimible(Boolean bool) throws DAOException{
		matriculaDao.saveImprimible(bool);
	}
}
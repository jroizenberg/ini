package com.institucion.fm.conf.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.ClaseConListaAlumnos;
import com.institucion.model.ClaseConListaAlumnosHistorico;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.ClienteListaEncontradoEnPiletaHistorico;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;
import com.institucion.model.Concepto;
import com.institucion.model.SubscripcionDeClasesPorActividad;

public abstract class BaseActiveService<T> extends TemplateHolder {// implements ActiveService<T> {

	private static Log log = LogFactory.getLog(BaseActiveService.class);
	
	// FINDERS (Getters)

	@SuppressWarnings("unchecked")
	public T findById(final Serializable id, final Class<T> type) throws DAOException {
		try {
			if (id == null || type == null) {
				throw new DAOException();
			}
			// load() da una exception si no existe. get() da null.
			T obj = (T) this.getHibernateTemplate().get(type, id);
			//			if (obj == null) {
			//				throw new DAOException("Entity: " + type.getName() + "(" + id.toString() + ")");
			//			}
			// el Load es full-lazy y necesitamos cargar toda la entidad
			// this.getHibernateTemplate().initialize(obj);
			return obj;
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(final Class<T> type) throws DAOException {
		try {
			if (type == null) {
				throw new DAOException();
			}
			//log.debug("BaseActiveService.findAll type: " + type);
			// El meotodo loadAll es lazy solo en las relaciones. Hay que
			// ponerlas lazy=false o recargar a pedido
			List<T> list = this.getHibernateTemplate().loadAll(type);

			//log.debug("BaseActiveService.findAll list.size: " + list.size());

			return list;
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findFirsts(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<T> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ClienteNoEncontradoEnPiletaHistorico> findFirstsClienteNoEncontradoEnPiletaHistorico(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClienteNoEncontradoEnPiletaHistorico> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ClienteNoEncontradoEnPileta> findFirstsClienteNoEncontradoEnPileta(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClienteNoEncontradoEnPileta> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ClaseConListaAlumnosHistorico> findFirstsClaseConListaAlumnosHistorico(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClaseConListaAlumnosHistorico> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<ClaseConListaAlumnos> findFirstsClaseConListaAlumnos(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClaseConListaAlumnos> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ClienteListaEncontradoEnPileta> findFirstsClienteListaEncontradoEnPileta(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClienteListaEncontradoEnPileta> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ClienteListaEncontradoEnPiletaHistorico> findFirstsClienteListaEncontradoEnPiletaHistorico(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<ClienteListaEncontradoEnPiletaHistorico> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<SubscripcionDeClasesPorActividad> findFirstsSubs(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params);

			if (maxResults > -1){
				List<SubscripcionDeClasesPorActividad> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public List<T> findMaxResult(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params,maxResults);

			if (maxResults > -1){
				List<T> list = this.getHibernateTemplate().executeFind(findCallBack);  
				return list.size() > maxResults ? list.subList(0, maxResults) : list;
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Long> findIds(final String criteria, final Map<String, Object> params, int maxResults) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:find(criteria): null or empty criteria");
			}
			FindCallBack findCallBack = new FindCallBack(criteria, params,maxResults);

			if (maxResults > -1){
				getHibernateTemplate().setMaxResults(maxResults);
				return this.getHibernateTemplate().executeFind(findCallBack);  
				//return (List<Long>) (list.size() > maxResults ? list.subList(0, maxResults) : list);
			}
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} finally{
			getHibernateTemplate().setMaxResults(0);
		}
		
	}
	
	public List<T> find(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirsts(criteria, params, -1);
	}

	public List<ClienteListaEncontradoEnPileta> findClienteListsaEncontradoEnPileta(final String criteria, final Map<String, Object> params) throws DAOException {
		return  this.findFirstsClienteListaEncontradoEnPileta(criteria, params, -1);
	}
	public List<SubscripcionDeClasesPorActividad> findSubsDeClasesPorAc(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsSubs(criteria, params, -1);
	}

	public List<ClienteListaEncontradoEnPileta> findClienteListaEncontradoEnPileta(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClienteListaEncontradoEnPileta(criteria, params, -1);
	}
	
	public List<ClienteListaEncontradoEnPiletaHistorico> findClienteListaEncontradoEnPiletaHistorico(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClienteListaEncontradoEnPiletaHistorico(criteria, params, -1);
	}
	public List<ClaseConListaAlumnos> findClaseConListaAlumnos(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClaseConListaAlumnos(criteria, params, -1);
	}
	
	public List<ClaseConListaAlumnosHistorico> findClaseConListaAlumnosHistorico(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClaseConListaAlumnosHistorico(criteria, params, -1);
	}
	
	public List<ClienteNoEncontradoEnPileta> findClienteNoEncontradoEnPileta(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClienteNoEncontradoEnPileta(criteria, params, -1);
	}
	
	public List<ClienteNoEncontradoEnPiletaHistorico> findClienteNoEncontradoEnPiletaHistorico(final String criteria, final Map<String, Object> params) throws DAOException {
		return this.findFirstsClienteNoEncontradoEnPiletaHistorico(criteria, params, -1);
	}
	
	@SuppressWarnings("unchecked")
	public T findUnique(final String criteria, final Map<String, Object> params) throws DAOException {
		try
		{
			if (criteria == null || "".equals(criteria))
				throw new DAOException();

			FindCallBack findCallBack = new FindCallBack(criteria, params);
			List<T> tList = this.getHibernateTemplate().executeFind(findCallBack);
			int size = tList.size();
			if (size == 0)
				return null;
			else if (size == 1)
				return tList.get(0);
			throw new DAOException("The criteria: '"+criteria+"' retrieves more than one row");
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	// CUD...
	public T create(T c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException();
			}
			this.getHibernateTemplate().save(c);
			
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
		return c;
	}

	// CUD...
	public Long createSubs(T c) throws DAOException {
			try {
				if (c == null) {
					throw new DAOException();
				}
				return (Long) this.getHibernateTemplate().save(c);
			} catch (DataAccessException e) {
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
				throw new DAOException(e);
			} catch (RuntimeException e) {
				log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
				throw new DAOException(e);
			}
		}
	
	// CUD...
		public Long createVenta(T c) throws DAOException {
				try {
					if (c == null) {
						throw new DAOException();
					}
					return (Long) this.getHibernateTemplate().save(c);
				} catch (DataAccessException e) {
					log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
					throw new DAOException(e);
				} catch (RuntimeException e) {
					log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
					throw new DAOException(e);
				}
	}
		
	public T save(T c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException("The entity instance has null value");
			}
			this.getHibernateTemplate().saveOrUpdate(c);
		
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
		return c;

	}

	public void save(Concepto c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException("The entity instance has null value");
			}
//			if(c instanceof Client){
//				if (((Client)c).getId() != null && this.getHibernateTemplate().get(Client.class, ((Client)c).getId()) != null){
//					this.getHibernateTemplate().flush();
//					this.getHibernateTemplate().merge(c);
//				}else
//					this.getHibernateTemplate().saveOrUpdate(c);
//			}else
				this.getHibernateTemplate().saveOrUpdate(c);
		
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	public void save(ClaseConListaAlumnos c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException("The entity instance has null value");
			}
//			if(c instanceof Client){
//				if (((Client)c).getId() != null && this.getHibernateTemplate().get(Client.class, ((Client)c).getId()) != null){
//					this.getHibernateTemplate().flush();
//					this.getHibernateTemplate().merge(c);
//				}else
//					this.getHibernateTemplate().saveOrUpdate(c);
//			}else
				this.getHibernateTemplate().saveOrUpdate(c);
		
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}
	
	public void delete(ClaseConListaAlumnos c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException();
			}
			this.getHibernateTemplate().delete(c);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	public T update(T c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException();
			}
			this.getHibernateTemplate().update(c);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
		return c;
	}

	public void delete(T c) throws DAOException {
		try {
			if (c == null) {
				throw new DAOException();
			}
			this.getHibernateTemplate().delete(c);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	public void deleteAll(Collection<T> c) throws DAOException {
		try {
			if (c == null) throw new DAOException(); {
				this.getHibernateTemplate().deleteAll(c);
			}
		} catch (Exception e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	public int update(final String criteria, final Map<String, Object> params) throws DAOException {
		try {
			if (criteria == null || "".equals(criteria)) {
				throw new DAOException("BaseActiveService:update(criteria): null or empty criteria");
			}
			UpdateCallBack updateCallBack = new UpdateCallBack(criteria, params);
			return (Integer) this.getHibernateTemplate().execute(updateCallBack);
		}
		catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	// Se puede mejorar con un truncate? o como hacer para tomar el nombre de la entidad
	public void deleteAll(String entityName) throws DAOException {
		this.getHibernateTemplate().execute(new DeleteAllCallBack(entityName));
	}

	// lazy loaders ...
	public void loadLazyAttribute(Object attribute) throws DAOException {
		try {
			if(attribute!=null){
				this.getHibernateTemplate().loadLazyAttribute(attribute);
			}
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}

	// eviction
	public void evict(Object entity) {
		try {
			this.getHibernateTemplate().evict(entity);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}


	// **** Callbacks
	private class FindCallBack implements HibernateCallback {
		private String criteria;
		private Map<String, Object> params;
		private int maxResults;

		public FindCallBack(String criteria, Map<String, Object> params, int maxResults) {
			this.criteria = criteria;
			this.params = params;
			this.maxResults = maxResults;
		}

		public FindCallBack(String criteria, Map<String, Object> params) {
			this(criteria, params, -1);
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Query q = session.createQuery(this.criteria);
			if (this.maxResults > 0) {
				q.setMaxResults(this.maxResults);
			}
			if (this.params != null) {
				for (String key : this.params.keySet()) {
					Object type = this.params.get(key);
					//log.debug("key: " + key + ", type: " + (type != null ? type.getClass().getName() : "null"));
					q.setParameter(key, type);
				}
			}
			
			return q.list();
		}
	}

	private class UpdateCallBack implements HibernateCallback {
		public String criteria;
		public Map<String, Object> params;

		public UpdateCallBack(String criteria, Map<String, Object> params) {
			this.criteria = criteria;
			this.params = params;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Query q = session.createQuery(this.criteria);
			if (this.params != null) {
				for (String key : this.params.keySet()) {
					Object type = this.params.get(key);
					//log.debug("key: " + key + ", type: " + (type != null ? type.getClass().getName() : "null"));
					q.setParameter(key, type);
				}
			}
			return q.executeUpdate();
		}
	}

	private class DeleteAllCallBack implements HibernateCallback {
		private String entityName;

		public DeleteAllCallBack(String entityName) {
			this.entityName = entityName;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			session.createQuery("delete from " + this.entityName).executeUpdate();
			return null;
		}
	}
}
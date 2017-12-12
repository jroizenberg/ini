package com.institucion.fm.conf.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.institucion.fm.conf.exception.DAOException;

public class SqlExecutor extends TemplateHolder {
	
	private static Log log = LogFactory.getLog(SqlExecutor.class);
	
	/**
	 * Ejecuta un select en SQL con parametros y alias
	 * @param sqlQuery
	 * @param entityAlias
	 * @param params
	 * @param clazz
	 * @return
	 */
	public List<?> findSQL(final String sqlQuery, final String entityAlias, final Map<String, Object> params, final Class<? extends Serializable> clazz) {
		return (List<?>) this.getHibernateTemplate().execute(
				new HibernateCallback()
				{
					public Object doInHibernate(Session session) throws HibernateException
					{
						SQLQuery query = session.createSQLQuery(sqlQuery);
						if (params != null) {
							for (String key : params.keySet()) {
								Object type = params.get(key);
								query.setParameter(key, type);
							}
						}
						List<?> list;
						if (entityAlias != null) {
							list = query.addEntity(entityAlias, clazz).list();
						} else {
							list = query.list();
						}
						return list;
					}
				}
			);
	}

	/**
	 * Ejecuta un select en SQL con alias
	 * @param sqlQuery
	 * @param entityAlias
	 * @param clazz
	 * @return
	 */
	public List<?> findSQL(final String sqlQuery, final String entityAlias, final Class<? extends Serializable> clazz) {
		return (List<?>)this.findSQL(sqlQuery, entityAlias, null, clazz);
	}

	/**
	 * Ejecuta un select en SQL con parametros
	 * @param sqlQuery
	 * @param params
	 * @return
	 */
	public List<?> findSQL(final String sqlQuery, final Map<String, Object> params) {
		return (List<?>)this.findSQL(sqlQuery, null, params, null);
	}

	
	/**
	 * Ejecuta un select en SQL puro
	 * @param sqlQuery
	 * @return
	 */
	public List<?> findSQL(final String sqlQuery)
	{
		return (List<?>) this.getHibernateTemplate().execute(
			new HibernateCallback()
			{
				public Object doInHibernate(Session session) throws HibernateException
				{
					SQLQuery query = session.createSQLQuery(sqlQuery);
					List<?> list = query.list();
					return list;
				}
			}
		);
	}
	
	/**
	 * Ejecuta un query (insert, update o delete) SQL puro
	 * @param sqlSentence
	 * @return
	 */
	public int executeSQL(final String sqlSentence)
	{
		return (Integer) this.getHibernateTemplate().execute(
			new HibernateCallback()
			{
				public Object doInHibernate(Session session) throws HibernateException
				{
					SQLQuery query = session.createSQLQuery(sqlSentence);
					return query.executeUpdate();
				}
			}
		);
	}
	
	private class FindCallBack implements HibernateCallback {
		public String criteria;
		public Map<String, Object> params;
		public FindCallBack(String criteria, Map<String, Object> params) {
			this.criteria = criteria;
			this.params = params;
		}

		public Object doInHibernate(Session session) throws HibernateException {
			Query q = session.createQuery(this.criteria);
			if (this.params != null) {
				for (String key : this.params.keySet()) {
					Object type = this.params.get(key);
					q.setParameter(key, type);
				}
			}
			return q.list();
		}
	}

	/**
	 * Ejecuta un query (select) en HQL sin devolver un objeto predefinido
	 * @param query
	 * @param params
	 * @return
	 * @throws DAOException
	 */
	public List<?> find(final String query, final Map<String, Object> params) throws DAOException {
		try {
			if (query == null || "".equals(query)) {
				throw new DAOException("SqlExecutor:find(criteria): null or empty query");
			}
			FindCallBack findCallBack = new FindCallBack(query, params);
			return this.getHibernateTemplate().executeFind(findCallBack);
		} catch (DataAccessException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		} catch (RuntimeException e) {
			log.error("Mensaje: " + e.getMessage() + "StackTrace: " + e.getStackTrace());
			throw new DAOException(e);
		}
	}	
}

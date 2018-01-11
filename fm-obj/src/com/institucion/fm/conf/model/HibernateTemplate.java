package com.institucion.fm.conf.model;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateTemplate extends org.springframework.orm.hibernate3.HibernateTemplate implements Serializable {
	private static final long serialVersionUID = 1L;
	public HibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public void loadLazyAttribute(Object attr) {
    	this.execute(new LoadLazyAttributeHibernateCallback(null, attr, null));
    }

    public void loadLazyAttribute(Object entity, Object attr) {
    	this.execute(new LoadLazyAttributeHibernateCallback(entity, attr, null));
    }

    public void loadLazyAttributeF(Object attr, Object filter) {
    	this.execute(new LoadLazyAttributeHibernateCallback(null, attr, filter));
    }

	private class LoadLazyAttributeHibernateCallback implements HibernateCallback {
		private Object entity;
		private Object attr;
//		private Object filter;
		
		public LoadLazyAttributeHibernateCallback (Object entity, Object attr, Object filter) {
            if (attr == null) {
            	throw new HibernateException("parameters cannot be null");
            }
			this.entity = entity;
			this.attr = attr;
//			this.filter = filter;
		}

		public Object doInHibernate(Session session) throws HibernateException {
            if (this.attr instanceof PersistentCollection) {
            	PersistentCollection r = (PersistentCollection)this.attr;
//                Model.ActiveFilter.SetupFilters(session, this.filter);
                if (this.entity == null) {
                	session.lock(r.getOwner(), LockMode.READ);
                } else {
                    session.lock(this.entity, LockMode.READ);
                }
                r.forceInitialization();
            } else if (this.attr instanceof HibernateProxy) {
                session.lock(this.attr, LockMode.READ);
            } /*else {
               	throw new HibernateException("the attribute isn't an instance of PersistentCollection or HibernateProxy");
            }*/
			return null;
		}
	}

}

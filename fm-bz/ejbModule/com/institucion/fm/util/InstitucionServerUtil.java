package com.institucion.fm.util;

import java.util.List;

import com.institucion.fm.conf.dao.TemplateHolder;
import com.institucion.fm.security.bz.CallContext;

public class InstitucionServerUtil extends TemplateHolder{

	
	public Long getSalesForceId(CallContext ctx){
		String query="select new java.lang.Long(wfu.id) from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{ctx.getUserID()});
		if (l.size()>0){
			Long userId=(Long)l.get(0);
			return userId;
		}else
			return null;
	}
	
	public Long getSalesForceId(String userName){
		String query="select new java.lang.Long(wfu.id) from com.institucion.fm.wf.model.User wfu left join wfu.user u where u.name=:name";
		List l=getHibernateTemplate().findByNamedParam(query, new String[]{"name"}, new Object[]{userName});
		if (l.size()>0){
			Long userId=(Long)l.get(0);
			return userId;
		}else
			return null;
		
	}
}

package com.institucion.fm.security.dao.spi;

import java.util.List;

import org.apache.commons.logging.*;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.security.dao.PermissionDAO;
import com.institucion.fm.security.model.*;

public class PermissionDAOImpl extends BaseActiveService<Permission> implements PermissionDAO
{
	private static Log log = LogFactory.getLog(PermissionDAOImpl.class);

	public List<Permission> findAll() throws DAOException
	{
		log.debug("findAll");
		return this.findAll(Permission.class);
	}
	@Override
	public Permission save(Permission c) throws DAOException {
		return super.save(c);
	}
	
	public Permission findById(Long id) throws DAOException {
		return super.findById(id, Permission.class);
	}	
	
	public List<String>getAllPermissionTokens(){
		String query="select distinct p.token from com.institucion.fm.security.model.User u left join u.groups g left join g.permissions p";
		return getHibernateTemplate().find(query);
	}
}
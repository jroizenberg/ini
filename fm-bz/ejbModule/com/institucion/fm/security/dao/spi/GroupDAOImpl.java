package com.institucion.fm.security.dao.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.dao.GroupDAO;
import com.institucion.fm.security.model.Group;

public class GroupDAOImpl extends BaseActiveService<Group> implements GroupDAO {

	public Group findByName(String name) throws DAOException
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		Group grp = super.findUnique("from Group where name = :name", params);
		super.loadLazyAttribute(grp.getPermissions());
		return grp;
	}

	public List<Group> findAll() throws DAOException
	{
		//log.debug("findAll");
		return this.findAll(Group.class);
	}

	public Group save(Group c) throws DAOException {
		return super.save(c);
	}

	public void delete(Group c) throws DAOException {
		super.delete(c);
	}

	public void deleteAll(Collection<Group> c) throws DAOException {
		super.deleteAll(c);
	}
	
	public List<Group> findByCriteria(CriteriaClause criteria) throws DAOException {
		return super.find("from Group "+criteria.getCriteria(), criteria.getCriteriaParameters());
	}

	public Group findById(Long id) throws DAOException {
		Group grp = super.findById(id, Group.class);
		super.loadLazyAttribute(grp.getPermissions());
		return grp;
	}
	
	@Override
	public List<Group> getGroupsByUser(Long id) {
		StringBuilder hql = new StringBuilder("select g from Group as g ")
			.append("inner join g.users users where users.id = ")
			.append(id);
		return super.find(hql.toString(), null);
	}
}
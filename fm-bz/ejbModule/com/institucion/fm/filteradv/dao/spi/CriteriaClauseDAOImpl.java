package com.institucion.fm.filteradv.dao.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.dao.CriteriaClauseDAO;
import com.institucion.fm.filteradv.model.CriteriaClause;
import com.institucion.fm.security.model.User;

public class CriteriaClauseDAOImpl extends BaseActiveService<CriteriaClause> implements CriteriaClauseDAO{

	public List<CriteriaClause> findAll() throws DAOException{
		//log.debug("findAll");
		return this.findAll(CriteriaClause.class);
	}

	@Override
	public CriteriaClause save(CriteriaClause c) throws DAOException{
		return super.save(c);
	}

	public CriteriaClause findByName(String name, User user) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("user", user);
		return super.findUnique("from com.institucion.fm.filteradv.model.CriteriaClause where name = :name AND user = :user", params);
	}

	public List<CriteriaClause> findByPage(String page, User user) throws DAOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", page);
		params.put("user", user);

		return super.find("from com.institucion.fm.filteradv.model.CriteriaClause where selectorPage = :page AND user = :user", params);
	}
	
	public List<CriteriaClause> findByPage(String page, String userName) throws DAOException{
		String query="select new com.institucion.fm.filteradv.model.CriteriaClause(c.name) from com.institucion.fm.filteradv.model.CriteriaClause c " +
				"where c.selectorPage=:sp and c.user.name=:name";
		
		return getHibernateTemplate().findByNamedParam(query, new String[]{"sp","name"}, new Object[]{page,userName});
		
	}
}
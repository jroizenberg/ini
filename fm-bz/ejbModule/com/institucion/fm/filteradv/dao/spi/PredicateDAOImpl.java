package com.institucion.fm.filteradv.dao.spi;

import java.util.List;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.filteradv.dao.PredicateDAO;
import com.institucion.fm.filteradv.model.Predicate;

public class PredicateDAOImpl extends BaseActiveService<Predicate> implements PredicateDAO
{

	public List<Predicate> findAll() throws DAOException
	{
		//log.debug("findAll");
		return this.findAll(Predicate.class);
	}

	@Override
	public Predicate save(Predicate c) throws DAOException
	{
		return super.save(c);
	}
}
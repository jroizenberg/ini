package com.institucion.fm.bb.dao;

import java.math.BigInteger;
import java.util.*;

import com.institucion.fm.bb.model.*;
import com.institucion.fm.conf.dao.*;
import com.institucion.fm.conf.exception.DAOException;

public class EmailDAOImpl extends BaseActiveService<Email> implements EmailDAO
{
	private SqlExecutor sql;

	public SqlExecutor getSql() { return this.sql; }
	public void setSql(SqlExecutor sql) { this.sql = sql; }

	public void saveIfNotExists(Email email) throws DAOException
	{
		if (!existEmail(email))
			email= super.save(email);
	}

	@Override
	public void delete(Email email) throws DAOException
	{
		super.delete(email);
	}

	public List<Email> findAll() throws DAOException
	{
		return this.findAll(Email.class);
	}

	public List<Email> findByCriteria(String criteria) throws DAOException
	{
		return this.find(criteria, null);
	}

	public Email findById(Long id) throws DAOException
	{
		return this.findById(id, Email.class);
	}

	public Email getNextPendingEmail() throws DAOException
	{
		throw new java.lang.UnsupportedOperationException();
	}

	private boolean existEmail(Email email)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("from_user", email.getFrom());
		params.put("to_user", email.getTo());
		params.put("subject", email.getSubject());

		@SuppressWarnings("unchecked")
		List<BigInteger> counts = (List<BigInteger>) sql.findSQL(
			"SELECT * FROM email "+
			" WHERE from_user = :from_user AND to_user = :to_user AND subject = :subject", params);

		return counts.size() > 0;
	}
}
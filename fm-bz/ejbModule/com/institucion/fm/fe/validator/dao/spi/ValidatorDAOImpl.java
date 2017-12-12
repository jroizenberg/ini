package com.institucion.fm.fe.validator.dao.spi;

import java.util.List;

import com.institucion.fm.conf.dao.BaseActiveService;
import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.validator.Validator;
import com.institucion.fm.fe.validator.dao.ValidatorDAO;

public class ValidatorDAOImpl extends BaseActiveService<Validator> implements ValidatorDAO
{
	public List<Validator> findAll() throws DAOException
	{
		return findAll(Validator.class);
	}
}
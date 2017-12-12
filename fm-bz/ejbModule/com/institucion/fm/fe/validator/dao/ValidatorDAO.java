package com.institucion.fm.fe.validator.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.fm.fe.validator.Validator;

public interface ValidatorDAO
{
	List<Validator> findAll() throws DAOException;
}
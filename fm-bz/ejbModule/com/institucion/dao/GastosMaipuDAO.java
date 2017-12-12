package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.GastosMaipu;

public interface GastosMaipuDAO{

	public GastosMaipu create(GastosMaipu c) throws DAOException;

	public void delete(GastosMaipu c) throws DAOException;
	public GastosMaipu save(GastosMaipu c) throws DAOException;
	public GastosMaipu findById(Long id) throws DAOException;
	
	public List<GastosMaipu> findAll() throws DAOException;
	public List<GastosMaipu> findAllConJdbc(String filters) throws DAOException;

}
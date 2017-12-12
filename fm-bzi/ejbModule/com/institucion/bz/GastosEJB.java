package com.institucion.bz;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Gastos;
import com.institucion.model.GastosMaipu;

public interface GastosEJB{
	
	public Gastos create(Gastos c) throws DAOException;

	public void delete(Gastos c) throws DAOException;
	public Gastos save(Gastos c) throws DAOException;
	public Gastos findById(Long id) throws DAOException;
	
	public List<Gastos> findAll() throws DAOException;
	public List<Gastos> findAllConJdbc(String filters) throws DAOException;	
	
	
	public GastosMaipu createMaipu(GastosMaipu c) throws DAOException;
	public void deleteMaipu(GastosMaipu c) throws DAOException;
	public GastosMaipu saveMaipu(GastosMaipu c) throws DAOException;
	public GastosMaipu findByIdMaipu(Long id) throws DAOException;

	public List<GastosMaipu> findAllMaipu() throws DAOException;
	public List<GastosMaipu> findAllConJdbcMaipu(String filters) throws DAOException;	

}
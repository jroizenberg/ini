package com.institucion.bz;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.BancoPromocion;

public interface BancoPromocionEJB{
	
	public void create(BancoPromocion c) throws DAOException;
	public void save(BancoPromocion c) throws DAOException;
	public BancoPromocion findById(Long id) throws DAOException;
	
	public List<BancoPromocion> findAll() throws DAOException;
	public List<BancoPromocion> findAllConJdbc(String filters) throws DAOException;

}
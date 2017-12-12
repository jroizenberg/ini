package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.BancoPromocion;

public interface BancoPromocionDAO{

	public BancoPromocion create(BancoPromocion c) throws DAOException;
	public BancoPromocion save(BancoPromocion c) throws DAOException;
	public BancoPromocion findById(Long id) throws DAOException;
	
	public List<BancoPromocion> findAll() throws DAOException;
	public List<BancoPromocion> findAllByBanco(Long idBanco) throws DAOException;
	
}
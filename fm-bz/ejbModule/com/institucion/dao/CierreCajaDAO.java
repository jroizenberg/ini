package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.CierreCaja;
import com.institucion.model.SucursalEnum;

public interface CierreCajaDAO{

	public CierreCaja create(CierreCaja c) throws DAOException;
	public void delete(CierreCaja c) throws DAOException;
	public CierreCaja save(CierreCaja c) throws DAOException;
	public List<CierreCaja> findCierresCaja( SucursalEnum suc);
	public CierreCaja getUltimoCierre( SucursalEnum suc) throws DAOException;
	public CierreCaja findCierreCajaById(Long id) throws DAOException;


}
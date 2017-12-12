package com.institucion.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.VentaProducto;

public interface VentaProductoDAO{

	public VentaProducto create(VentaProducto c) throws DAOException;
	public void delete(VentaProducto c) throws DAOException;
	public VentaProducto save(VentaProducto c) throws DAOException;
	public VentaProducto findById(Long id) throws DAOException;
	
	public List<VentaProducto> findAll() throws DAOException;
	public List<VentaProducto> findAllConJdbcByDates(Connection cnx , Date fechaDesde, Date fechaHasta) throws DAOException;
	public Long createVenta(VentaProducto c) throws DAOException;
}
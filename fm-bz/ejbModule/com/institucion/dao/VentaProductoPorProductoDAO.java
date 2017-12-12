package com.institucion.dao;

import java.util.List;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.VentaProductoPorProducto;

public interface VentaProductoPorProductoDAO{

	public VentaProductoPorProducto create(VentaProductoPorProducto c) throws DAOException;
	public void delete(VentaProductoPorProducto c) throws DAOException;
	public VentaProductoPorProducto save(VentaProductoPorProducto c) throws DAOException;
	public VentaProductoPorProducto findById(Long id) throws DAOException;
	
	public List<VentaProductoPorProducto> findAll() throws DAOException;

}
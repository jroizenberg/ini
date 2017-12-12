package com.institucion.dao;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;

public interface ClienteDAO{

	public Cliente create(Cliente c) throws DAOException;
	public Long createSubs(Cliente c) throws DAOException;

	public void delete(Cliente c) throws DAOException;
	public Cliente save(Cliente c) throws DAOException;
	public Cliente findById(Long id) throws DAOException;
	
	public List<Cliente> findAll() throws DAOException;
	public List<Cliente> findAllConJdbc(String filters) throws DAOException;
	public List<Cliente> findAllConJdbc2(String filters) throws DAOException;
	public List<ClienteView> findAllConJdbcSinHibernate(String filters) throws DAOException;
	public Long getCount(String query);
	public List<Integer> getResponsableAdeuda(String query);
	public Map<Integer, LinkedList<String>> findAllActividadesConJdbc(String filters) throws DAOException;
	public Date getFechaVentaAdeuda(String query);

	public Cliente actualizarEstadosVencidosACliente(Cliente cli);
	public Cliente actualizarEstadosCursosIniACliente(Cliente cli);

	public Cliente actualizarEstadosCumpleaniosACliente(Cliente cli);
	public void loadLazyAttribute(Object attribute) throws DAOException;

}
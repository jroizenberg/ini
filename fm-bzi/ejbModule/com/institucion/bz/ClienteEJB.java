package com.institucion.bz;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.institucion.fm.conf.exception.DAOException;
import com.institucion.model.ActividadYClase;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;
import com.institucion.model.CupoActividad;
import com.institucion.model.Empleado;
import com.institucion.model.Gastos;
import com.institucion.model.ObraSocial;
import com.institucion.model.Subscripcion;
import com.institucion.model.VentaProducto;

public interface ClienteEJB{
	
	public void create(Cliente c) throws DAOException;
	public Long createSubs(Cliente c) throws DAOException;

	public void delete(Cliente c) throws DAOException;
	public void save(Cliente c) throws DAOException;
	public Cliente findById(Long id) throws DAOException;
	
//	public List<Cliente> findAll() throws DAOException;
	public List<Cliente> findAllConJdbc(String filters) throws DAOException;
	public List<Cliente> findAllConJdbc2(String filters) throws DAOException;
	public List<ClienteView> findAllConJdbcSinHibernate(String filters) throws DAOException;

	public Subscripcion actualizarSubscripcion(Subscripcion subs, ActividadYClase actYClase);
	public Subscripcion actualizarSubscripcionQuitarDeClase(Subscripcion subs, ActividadYClase actYClase, int cupo);

	public CupoActividad actualizarFecha(CupoActividad cupoAct);

	public Long getCount(String query);

	public Map<Integer, LinkedList<String>> findAllActividadesConJdbc(String filters) throws DAOException;

	public boolean isAdeudaAlgo(Long clientID);
	public String getResponsableAdeuda(Long clientID);
	public Date getFechaVentaAdeuda(Long clientID);
	public boolean isTieneDisponibles(Long clientID);
	public String  generarCodigosDeBarras(List<Cliente> clientList) throws Exception;
	public Cliente loadLazy(Cliente cli, boolean subs, boolean CupoActividad, boolean pagosPorSubs, boolean ingresoInscripcion);
	public Subscripcion loadLazy(Subscripcion subscripcion,boolean CupoActividad, boolean pagosPorSubs, boolean concepto,
			boolean subsDeClase, boolean clasesSelecList);
	public ObraSocial loadLazy(ObraSocial os);
	public Empleado loadLazy(Empleado os);

	public Clase loadLazy(Clase clase, boolean clientesEnClase, boolean ingresoAClaseSinFechas, boolean claseConListaAlumnosList, boolean claseConListaAlumnosHistoryList);
	public VentaProducto loadLazy(VentaProducto vt, boolean pagos, boolean productos);
	public Gastos loadLazy(Gastos os);

}
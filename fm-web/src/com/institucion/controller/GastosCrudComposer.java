package com.institucion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.bz.GastosEJB;
import com.institucion.desktop.delegated.GastosDelegate;
import com.institucion.desktop.delegated.SucursalDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Actividad;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Empleado;
import com.institucion.model.EmpleadoActividades;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.Gastos;
import com.institucion.model.GastosCrud;
import com.institucion.model.GastosEnum;
import com.institucion.model.GastosMaipu;
import com.institucion.model.GastosMaipuCrud;
import com.institucion.model.GastosMaipuEnum;
import com.institucion.model.GastosSueldos;
import com.institucion.model.GastosSueldosPorActividadList;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Quincena;
import com.institucion.model.SucursalCrud;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class GastosCrudComposer extends CrudComposer  implements GastosDelegate, SucursalDelegate{

	private PanelCrud gastosCrud;
	public final String idGasto = "idGasto";
	private GastosEJB clienteEJB;
	private CursoEJB actividadEJB;
	private CajaEJB cajaEJB;
	private PanelCrud sucCrud;
	private ClienteEJB cliEJB;

	private Gastos gasto;
	private GastosMaipu gastoMaipu;
	private GastosSueldosPorActividadList gastosSueldosPorActividadListGrid;
	private PanelCrud sucMaipu;
	
	public GastosCrudComposer() {
		clienteEJB = BeanFactory.<GastosEJB>getObject("fmEjbGastos");
		actividadEJB= BeanFactory.<CursoEJB>getObject("fmEjbCurso");
		cajaEJB= BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		cliEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	
	}

	private SucursalCrud getSucursalCrud() {
		return (SucursalCrud)(sucCrud.getGridCrud());
	}
	private GastosMaipuCrud getGastosMaipuCrud() {
		return (GastosMaipuCrud)(sucMaipu.getGridCrud());
	}
	
	public void onCreate() {
		
		getSucursalCrud().setDelegate(this);
		getGastosMaipuCrud().setDelegate(this);
		getClienteCrud().setDelegate(this);

		if(Session.getAttribute(idGasto) != null && Session.getAttribute(idGasto) instanceof GastosMaipu){
			getSucursalCrud().getSucursal().setSelectedIndex(1);
			getSucursalCrud().getSucursal().setDisabled(true);
		}else 	if(Session.getAttribute(idGasto) != null && Session.getAttribute(idGasto) instanceof Gastos){
			getSucursalCrud().getSucursal().setSelectedIndex(0);
			getSucursalCrud().getSucursal().setDisabled(true);
		}
		
		if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt() ){
			
			// si es maipu
			GastosMaipu cliente= (GastosMaipu) Session.getAttribute(idGasto);
			gastoMaipu= cliente;
			this.fromModelToView(gastoMaipu);
			getSucursalCrud().setDelegate(this);
			getGastosMaipuCrud().getDinero().setDisabled(false);	
			getGastosMaipuCrud().setDelegate(this);
			Calendar calStart = Calendar.getInstance(); 

			if(gasto == null){
				getGastosMaipuCrud().setSelectedAnio(new Long((calStart.get(Calendar.YEAR))));
			}
			
			getGastosMaipuCrud().setVisible(true);
			getClienteCrud().setVisible(false);
			gastosSueldosPorActividadListGrid.setVisible(false);

		}else 	if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt() ){
			
			Gastos cliente= (Gastos) Session.getAttribute(idGasto);
			gasto= cliente;
			gasto= cliEJB.loadLazy(gasto);

			this.fromModelToView(gasto);
			gastosSueldosPorActividadListGrid.setVisible(false);
			getClienteCrud().getDinero().setDisabled(false);	
			getClienteCrud().setDelegate(this);
			getSucursalCrud().setDelegate(this);
			getClienteCrud().setVisible(true);
			if(gasto != null && gasto.getTipoGasto().toInt() == GastosEnum.SUELDOS.toInt()){
				gastosSueldosPorActividadListGrid.setVisible(true);
				getClienteCrud().getDinero().setDisabled(true);	
				
				getClienteCrud().getAnioL().setValue("Año al que se le Descuenta el Dinero de los Sueldos");
				getClienteCrud().getMesL().setValue("Mes al que se le Descuenta el Dinero de los Sueldos");
				
				if(gasto.getPagaSueldosPorMes() != null && gasto.getPagaSueldosPorMes()){
					getClienteCrud().getFechaDesde().setVisible(false);
					getClienteCrud().getFechaHasta().setVisible(false);
					
					getClienteCrud().getFechaPagoDL().setVisible(false);
					getClienteCrud().getFechaPagoHL().setVisible(false);

				}else{
					getClienteCrud().getFechaDesde().setVisible(true);
					getClienteCrud().getFechaHasta().setVisible(true);
					
					getClienteCrud().getFechaPagoDL().setVisible(true);
					getClienteCrud().getFechaPagoHL().setVisible(true);

					getClienteCrud().getFechaDesde().setValue(gasto.getFechaDesde());
					getClienteCrud().getFechaHasta().setValue(gasto.getFechaHasta());
				}
				
			}else{
				getClienteCrud().getFechaDesde().setVisible(false);
				getClienteCrud().getFechaHasta().setVisible(false);
				
				getClienteCrud().getFechaPagoDL().setVisible(false);
				getClienteCrud().getFechaPagoHL().setVisible(false);

				getClienteCrud().getAnioL().setValue(I18N.getLabel("gastos.anio"));
				getClienteCrud().getMesL().setValue(I18N.getLabel("gastos.mes"));
				
			}
			Calendar calStart = Calendar.getInstance(); 

			if(gasto == null){
				getClienteCrud().setSelectedAnio(new Long((calStart.get(Calendar.YEAR))));
				getClienteCrud().setSelectedMes(new Long((calStart.get(Calendar.MONTH))));
			}
			
			
			getGastosMaipuCrud().setVisible(false);
			getClienteCrud().setVisible(true);
		}else{
			// dejo todo deshabilitado
			getClienteCrud().setVisible(false);
			gastosSueldosPorActividadListGrid.setVisible(false);
			getClienteCrud().getFechaDesde().setVisible(false);
			getClienteCrud().getFechaHasta().setVisible(false);
			
			getClienteCrud().getFechaPagoDL().setVisible(false);
			getClienteCrud().getFechaPagoHL().setVisible(false);

			getClienteCrud().getAnioL().setValue(I18N.getLabel("gastos.anio"));
			getClienteCrud().getMesL().setValue(I18N.getLabel("gastos.mes"));
			getGastosMaipuCrud().setVisible(false);

			
		}
		
		if((gasto == null || (gasto != null && gasto.getId() == null)) && Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					getSucursalCrud().getSucursal().setDisabled(false);
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					getSucursalCrud().getSucursal().setDisabled(false);
				}
			}
		}

		
	}
	
	public void actualizarPanelSueldos(boolean mostrar, Empleado emp){
		gastosSueldosPorActividadListGrid.cleanRows();
		gastosSueldosPorActividadListGrid.clear();
		if(emp != null){
			emp= cliEJB.loadLazy(emp);
		}
		if(mostrar){
		
			gastosSueldosPorActividadListGrid.setVisible(true);
			getClienteCrud().getDinero().setDisabled(true);
			List<Actividad> actList=actividadEJB.findAllActividadParaPagoSueldos();
			
			List<GastosSueldos> lsss=obtenerGast(actList, gasto, emp);
			gastosSueldosPorActividadListGrid.setField(lsss);
		}else{
			gastosSueldosPorActividadListGrid.setVisible(false);
			getClienteCrud().getDinero().setDisabled(false);	
		}
	}
	
	private List<GastosSueldos>  obtenerGast(List<Actividad> actLis, Gastos gasto, Empleado emp){
		
		ArrayList<GastosSueldos>  list= new ArrayList();
		
		if(actLis != null){
			for (Actividad act : actLis) {
				
				if(existInEmpleado(emp, act)){
					GastosSueldos nuevo = new GastosSueldos();
					nuevo.setActividad(act);
					nuevo.setComentario("");
					nuevo.setDinero(0);
					nuevo.setGasto(gasto);
					
					list.add(nuevo);					
				}
			}
		}
		
		return list;
	}
	
	private boolean existInEmpleado(Empleado emp, Actividad act){
		for (EmpleadoActividades iterable_element : emp.getActividades()) {
			if(iterable_element.getActividad().getId().intValue() == act.getId().intValue() 
					&& iterable_element.isDisponible())
				return true;
		}
		
		return false;
	}
	
	
	private void fromModelToView(Gastos cliente) {
		// si lo que obtuve de bd es != null
		if(cliente != null){
			
			if(cliente.getNombreUsuarioGeneroMovimiento() != null){
				getSucursalCrud().getResponsable().setValue(cliente.getNombreUsuarioGeneroMovimiento());				
			}
			
			if(cliente.getTipoGasto() != null){
				getClienteCrud().setSelectedTipoGasto(cliente.getTipoGasto());
			}
	
			if(cliente != null && cliente.getId() != null && cliente.getTipoGasto().toInt() == GastosEnum.SUELDOS.toInt()){
				getClienteCrud().getEmpleadoCB().setVisible(true);
				getClienteCrud().getEmpleado().setVisible(true);
			}
			
			if(cliente.getEmpleado() != null){
				getClienteCrud().setSelectedEmpleado(cliente.getEmpleado());
			}
			if(cliente.getAnio() != null){
				getClienteCrud().setSelectedAnio(cliente.getAnio());
			}
	
			if(cliente.getMes() != null){
				getClienteCrud().setSelectedMes(cliente.getMes());
			}

			if(cliente.getPagaSueldosPorMes() != null){
				getClienteCrud().setSelectedPagoMensual(cliente.getPagaSueldosPorMes());
			}
			
			if(cliente.getPagaSueldosPorMes() != null && !cliente.getPagaSueldosPorMes()){
				
				if(cliente.getFechaDesde() != null){
					getClienteCrud().getFechaDesde().setValue(cliente.getFechaDesde());
				}
				
				if(cliente.getFechaHasta() != null){
					getClienteCrud().getFechaHasta().setValue(cliente.getFechaHasta());
				}
					
			}
			if(cliente.getComentario() != null){
				getClienteCrud().getComentario().setValue(cliente.getComentario());	
			}
		
			if(cliente.getDinero() != null){
				getClienteCrud().getDinero().setValue(cliente.getDinero());
			}
			
			if(cliente.getGastosSueldoList() != null){
				gastosSueldosPorActividadListGrid.setField(new ArrayList(cliente.getGastosSueldoList()));
			}
			
		}		
	}
	
	private void validateCrudMaipu() {
		
		if(getGastosMaipuCrud().getTipoGastoCB().getSelectedItem()  == null)
			throw new WrongValueException(getGastosMaipuCrud().getTipoGastoCB(), I18N.getLabel("error.empty.field"));
		else{
			
			if(((GastosMaipuEnum)getGastosMaipuCrud().getTipoGastoCB().getSelectedItem().getValue()).toInt() == GastosMaipuEnum.SUELDOS.toInt()){
				
				if(getGastosMaipuCrud().getEmpleadoCB().getSelectedItem() == null)
					throw new WrongValueException(getGastosMaipuCrud().getEmpleadoCB(),I18N.getLabel("error.empty.field"));

			}
			
		}
		if(getGastosMaipuCrud().getAnio().getSelectedItem() == null)
			throw new WrongValueException(getGastosMaipuCrud().getAnio(),I18N.getLabel("error.empty.field"));
		
		if(getGastosMaipuCrud().getQuincena().getSelectedItem() == null)
			throw new WrongValueException(getGastosMaipuCrud().getQuincena(),I18N.getLabel("error.empty.field"));	
		
		if(getGastosMaipuCrud().getDinero().getValue()  == null)
			throw new WrongValueException(getGastosMaipuCrud().getDinero(), I18N.getLabel("error.empty.field"));
	}

	public void onSave(Event event) throws Exception {
		try{
			
			if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
					((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt() ){
				
				if (Sessions.getCurrent().getAttribute(idGasto) != null) {
					// es una modificacion
						gastoMaipu = (GastosMaipu) Sessions.getCurrent().getAttribute(idGasto);
						validateCrudMaipu();
						this.fromViewToModel(gastoMaipu);
						
						// obtener gasto original.
//						De la suma original hago el ajuste en la caja, con el ingreso o egreso.
						GastosMaipu gastoOriginal=clienteEJB.findByIdMaipu(gastoMaipu.getId());
						
						if(gastoOriginal.getDinero() == 0){
							if(gastoMaipu.getDinero() > 0){}
							
						}
						
						if(gastoOriginal.getDinero() == gastoMaipu.getDinero()){
							
						}else if(gastoOriginal.getDinero() > gastoMaipu.getDinero()){
						// si el gasto original es mayor al gasto que estoy haciendo ahora: o sea, le baje plata
							Integer dinero =gastoOriginal.getDinero() - gastoMaipu.getDinero();

							// tengo que devolver a la caja esa diferencia.
							CajaMovimiento caja2= new CajaMovimiento();

							String empleado=null;
							if(getGastosMaipuCrud().getEmpleadoCB().getSelectedItem() != null)
								empleado= ((Empleado)getGastosMaipuCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();

							// si no es pago de sueldos, registro solo 1
							String conceptoGastos= "Gasto Maipu: "+ gastoMaipu.getTipoGasto().toString(gastoMaipu.getTipoGasto().toInt()) ;
							if(empleado != null ){
								conceptoGastos= conceptoGastos +" "+ empleado;
							}
								conceptoGastos= conceptoGastos + ". Pago Quincenal "+ gastoMaipu.getQuincena().getNombre() +"/"+gastoMaipu.getAnio()+
										" .Se modifica el gasto para agregar dinero adicional";
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.MAIPU);
								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.MAIPU);

								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								pagoNuevo.setGastoMaipu(gastoMaipu);
								pagoNuevo.setQuincena((Quincena)getGastosMaipuCrud().getQuincena().getSelectedItem().getValue() );
								

								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
								
						
							
						}else{
							// tengo que restarle mas a la caja.
							Integer dinero =gastoMaipu.getDinero() - gastoOriginal.getDinero();
							CajaMovimiento caja2= new CajaMovimiento();

							// si no es pago de sueldos, registro solo 1
							String conceptoGastos= "Gasto Maipu: "+ gastoMaipu.getTipoGasto().toString(gastoMaipu.getTipoGasto().toInt()) ;
							String empleado=null;
							if(getGastosMaipuCrud().getEmpleadoCB().getSelectedItem() != null)
								empleado= ((Empleado)getGastosMaipuCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
							if(empleado != null ){
								conceptoGastos= conceptoGastos +" "+ empleado;
							}

					
								conceptoGastos= conceptoGastos + ". Pago quincenal "+ gastoMaipu.getQuincena().getNombre() +"/"+gastoMaipu.getAnio()+
										" .Se modifica el gasto para agregar dinero adicional";
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.MAIPU);
								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.MAIPU);
								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								pagoNuevo.setGastoMaipu(gastoMaipu);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								pagoNuevo.setQuincena((Quincena)getGastosMaipuCrud().getQuincena().getSelectedItem().getValue() );
								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
						}
						clienteEJB.saveMaipu(gastoMaipu);

						if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
							Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						}else 	if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 
							Sessions.getCurrent().setAttribute("idFromCaja", null);
							super.gotoPage("/institucion/caja-selector.zul");

						}else 	if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorGastos") != null){ 
							Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelectorGastos", null);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");

						}else
							super.gotoPage("/institucion/gastos-selector.zul");
						
						
						
				} else {
					// es nuevo 
					validateCrudMaipu();
					gastoMaipu= new GastosMaipu();
					gastoMaipu=this.fromViewToModel(gastoMaipu);
					
					// Setear el Gasto
//					gasto= setearGastos(gasto);
					gastoMaipu=clienteEJB.createMaipu(gastoMaipu);
					
					// genero movimiento de caja 
					String conceptoGastos= "Gasto Maipu: "+ gastoMaipu.getTipoGasto().toString(gastoMaipu.getTipoGasto().toInt()) ;

					String empleado=null;
					if(getGastosMaipuCrud().getEmpleadoCB().getSelectedItem() != null)
						empleado= ((Empleado)getGastosMaipuCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
					if(empleado != null ){
						conceptoGastos= conceptoGastos +" "+ empleado;
					}

					CajaMovimiento caja2= new CajaMovimiento();

					// si no es pago de sueldos, registro solo 1
					caja2.setConcepto(conceptoGastos);
					caja2.setFecha(new Date());
					caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
					caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
					caja2.setValor(new Double(gastoMaipu.getDinero()));
					caja2.setSucursal(SucursalEnum.MAIPU);

					cajaEJB.save(caja2);
					
					PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
					pagoNuevo.setCantidadDinero(gastoMaipu.getDinero());
					pagoNuevo.setSucursal(SucursalEnum.MAIPU);
					pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
					pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
					pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
					pagoNuevo.setGastoMaipu(gastoMaipu);
					pagoNuevo.setAdicional(0);
					pagoNuevo.setCantCuotas(0);
					pagoNuevo.setQuincena((Quincena)getGastosMaipuCrud().getQuincena().getSelectedItem().getValue() );
					cajaEJB.savePagosPorSubscripcion(pagoNuevo);
				
					if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){
						Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					}else 	if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 
						Sessions.getCurrent().setAttribute("idFromCaja", null);
						super.gotoPage("/institucion/caja-selector.zul");
					}else 	if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorGastos") != null){ 
						Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelectorGastos", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");

					}else{
						super.gotoPage("/institucion/gastos-selector.zul");
					}
				}
				
				
			}else if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
					((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt() ){
				

				if (Sessions.getCurrent().getAttribute(idGasto) != null) {
					// es una modificacion
						gasto = (Gastos) Sessions.getCurrent().getAttribute(idGasto);
						validateCrud();
						this.fromViewToModel(gasto);
						
						// obtener gasto original.
//						De la suma original hago el ajuste en la caja, con el ingreso o egreso.
						Gastos gastoOriginal=clienteEJB.findById(gasto.getId());
						
						if(gastoOriginal.getDinero() == 0){
							if(gasto.getDinero() > 0){}
							
						}
						
						if(gastoOriginal.getDinero() == gasto.getDinero()){
							
						}else if(gastoOriginal.getDinero() > gasto.getDinero()){
						// si el gasto original es mayor al gasto que estoy haciendo ahora: o sea, le baje plata
							Integer dinero =gastoOriginal.getDinero() - gasto.getDinero();

							// tengo que devolver a la caja esa diferencia.
							CajaMovimiento caja2= new CajaMovimiento();

							// si no es pago de sueldos, registro solo 1
							String conceptoGastos= "Gasto: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
							String empleado=null;
							if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
								empleado= ((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
							if(empleado != null ){
								conceptoGastos= conceptoGastos +" "+ empleado;
							}

							if(gasto.getPagaSueldosPorMes()){
								conceptoGastos= conceptoGastos + ". Pago mensual "+ gasto.getMes() +"/"+gasto.getAnio()+
										" .Se modifica el gasto para agregar dinero adicional";
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.CENTRO);
								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.CENTRO);
								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								pagoNuevo.setGasto(gasto);
								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
								
								
							}else{
								
								Calendar ahoraCal= Calendar.getInstance();
								ahoraCal.setTime(gasto.getFechaDesde() );
								int mes=ahoraCal.get(Calendar.MONTH) + 1;

								String fechaNacDesde=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
								
								Calendar ahoraCalHasta= Calendar.getInstance();
								ahoraCalHasta.setTime(gasto.getFechaHasta());
								int mes2=ahoraCalHasta.get(Calendar.MONTH) + 1;
								String fechaNacHasta=ahoraCalHasta.get(Calendar.DATE)+"/"+mes2+"/"+ahoraCalHasta.get(Calendar.YEAR);
								
								conceptoGastos= conceptoGastos +". Pago por fechas "+ fechaNacDesde+ "-"+ fechaNacHasta
										+	" .Se modifica el gasto para agregar dinero adicional";
							
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.CENTRO);

								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.CENTRO);

								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
								pagoNuevo.setGasto(gasto);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
							}
							
							
						}else{
							// tengo que restarle mas a la caja.
							Integer dinero =gasto.getDinero() - gastoOriginal.getDinero();
							CajaMovimiento caja2= new CajaMovimiento();

							// si no es pago de sueldos, registro solo 1
							String conceptoGastos= "Gasto: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
							String empleado=null;
							if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
								empleado= ((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
							if(empleado != null ){
								conceptoGastos= conceptoGastos +" "+ empleado;
							}

							if(gasto.getPagaSueldosPorMes()){
								conceptoGastos= conceptoGastos + ". Pago mensual "+ gasto.getMes() +"/"+gasto.getAnio()+
										" .Se modifica el gasto para agregar dinero adicional";
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.CENTRO);

								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.CENTRO);

								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								pagoNuevo.setGasto(gasto);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
								
							}else{
								
								Calendar ahoraCal= Calendar.getInstance();
								ahoraCal.setTime(gasto.getFechaDesde() );
								int mes=ahoraCal.get(Calendar.MONTH) + 1;

								String fechaNacDesde=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
								
								Calendar ahoraCalHasta= Calendar.getInstance();
								ahoraCalHasta.setTime(gasto.getFechaHasta());
								int mes2=ahoraCalHasta.get(Calendar.MONTH) + 1;
								String fechaNacHasta=ahoraCalHasta.get(Calendar.DATE)+"/"+mes2+"/"+ahoraCalHasta.get(Calendar.YEAR);
								
								conceptoGastos= conceptoGastos +". Pago por fechas "+ fechaNacDesde+ "-"+ fechaNacHasta
										+	" .Se modifica el gasto para agregar dinero adicional";
						
								caja2.setConcepto(conceptoGastos );
								caja2.setFecha(new Date());
								caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
								caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								caja2.setValor(new Double(dinero));
								caja2.setSucursal(SucursalEnum.CENTRO);

								cajaEJB.save(caja2);
								
								PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
								pagoNuevo.setCantidadDinero(dinero);
								pagoNuevo.setSucursal(SucursalEnum.CENTRO);

								pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//								pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//								pagoNuevo.setSaldadaDeuda(true);
								pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
								pagoNuevo.setGasto(gasto);
								pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
								pagoNuevo.setAdicional(0);
								pagoNuevo.setCantCuotas(0);
								cajaEJB.savePagosPorSubscripcion(pagoNuevo);
							}
						
						}
						
						clienteEJB.save(gasto);

						if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
							Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						}else if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 	
							Sessions.getCurrent().setAttribute("idFromCaja", null);
							super.gotoPage("/institucion/caja-selector.zul");
						}else 	if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorGastos") != null){ 
							Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelectorGastos", null);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");

						}else
							super.gotoPage("/institucion/gastos-selector.zul");
						
				} else {
					// es nuevo 
					validateCrud();
					gasto= new Gastos();
					gasto=this.fromViewToModel(gasto);
					
					// Setear el Gasto
					gasto= setearGastos(gasto);
					gasto=clienteEJB.create(gasto);
					
					// genero movimiento de caja 
					String conceptoGastos= "Gasto: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;

					// si es pago de sueldos, sumo eso
					if(gasto.getTipoGasto().toInt() == GastosEnum.SUELDOS.toInt()){
						// si es pago de sueldos, registro un gasto por cada actividad
						conceptoGastos= "Gasto:PAGO DE "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()).toUpperCase() ;
						String empleado=null;
						if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
							empleado= ((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
						if(empleado != null ){
							conceptoGastos= conceptoGastos +" "+ empleado;
						}

						if (gasto.getGastosSueldoList().size() > 0){
							for (GastosSueldos gastosSueldo : gasto.getGastosSueldoList()) {
								
								if(gastosSueldo.getDinero() != null && gastosSueldo.getDinero() > 0){
									if(gasto.getPagaSueldosPorMes()){
										conceptoGastos= conceptoGastos + " "+gastosSueldo.getActividad().getNombre() +
												". Pago mensual "+ gasto.getMes() +"/"+gasto.getAnio();
										
									}else{
										
										Calendar ahoraCal= Calendar.getInstance();
										ahoraCal.setTime(gasto.getFechaDesde() );
										int mes=ahoraCal.get(Calendar.MONTH) + 1;

										String fechaNacDesde=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
										
										Calendar ahoraCalHasta= Calendar.getInstance();
										ahoraCalHasta.setTime(gasto.getFechaHasta());
										int mes2=ahoraCalHasta.get(Calendar.MONTH) + 1;
										String fechaNacHasta=ahoraCalHasta.get(Calendar.DATE)+"/"+mes2+"/"+ahoraCalHasta.get(Calendar.YEAR);
										
										conceptoGastos= conceptoGastos +" "+gastosSueldo.getActividad().getNombre()+
												". Pago por fechas "+ fechaNacDesde+ "-"+ fechaNacHasta;
									}
									
									CajaMovimiento caja2= new CajaMovimiento();

									// si no es pago de sueldos, registro solo 1
									caja2.setConcepto(conceptoGastos);
									caja2.setFecha(new Date());
									caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
									caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
									caja2.setValor(new Double(gastosSueldo.getDinero()));
									caja2.setSucursal(SucursalEnum.CENTRO);

									cajaEJB.save(caja2);
									conceptoGastos= "Gasto:PAGO DE "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()).toUpperCase()  ;
									if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
										empleado= ((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
									if(empleado != null ){
										conceptoGastos= conceptoGastos +" "+ empleado;
									}

									
									PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
									pagoNuevo.setCantidadDinero(gastosSueldo.getDinero());
									pagoNuevo.setSucursal(SucursalEnum.CENTRO);

									pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//									pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//									pagoNuevo.setSaldadaDeuda(true);
									pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
									pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
									pagoNuevo.setGasto(gasto);
									pagoNuevo.setAdicional(0);
									pagoNuevo.setCantCuotas(0);
									cajaEJB.savePagosPorSubscripcion(pagoNuevo);
									
								}
							}
						}
				
					}else{
						CajaMovimiento caja2= new CajaMovimiento();

						// si no es pago de sueldos, registro solo 1
						caja2.setConcepto(conceptoGastos);
						caja2.setFecha(new Date());
						caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
						caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
						caja2.setValor(new Double(gasto.getDinero()));
						caja2.setSucursal(SucursalEnum.CENTRO);

						cajaEJB.save(caja2);
						conceptoGastos= "Gasto: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
						String empleado=null;
						if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
							empleado= ((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue()).getNombre();
						if(empleado != null ){
							conceptoGastos= conceptoGastos +" "+ empleado;
						}

						PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
						pagoNuevo.setCantidadDinero(gasto.getDinero());
						pagoNuevo.setSucursal(SucursalEnum.CENTRO);

						pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
//						pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//						pagoNuevo.setSaldadaDeuda(true);
						pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
						pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
						pagoNuevo.setGasto(gasto);
						pagoNuevo.setAdicional(0);
						pagoNuevo.setCantCuotas(0);
						cajaEJB.savePagosPorSubscripcion(pagoNuevo);
						
					}
				
					if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){
						Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					}else if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 	
						Sessions.getCurrent().setAttribute("idFromCaja", null);
						super.gotoPage("/institucion/caja-selector.zul");
					}else 	if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorGastos") != null){ 
						Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelectorGastos", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");

					}else{
						super.gotoPage("/institucion/gastos-selector.zul");
					}
				}
				
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	private Gastos setearGastos(Gastos gastoToSet){
		if(gastoToSet != null){
			if(gastoToSet.getGastosSueldoList() != null){
				for (GastosSueldos gastosSueldo : gastoToSet.getGastosSueldoList()) {
					gastosSueldo.setGasto(gastoToSet);
				}
			}
		}
		return gastoToSet;
	}
	
	public void onBack(Event event) {
		if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
			Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 	
			Sessions.getCurrent().setAttribute("idFromCaja", null);
			super.gotoPage("/institucion/caja-selector.zul");
		}else 	if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelectorGastos") != null){ 
			Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelectorGastos", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");

		}else	
			super.gotoPage("/institucion/gastos-selector.zul");
	}

	private GastosCrud getClienteCrud() {
		return (GastosCrud) (gastosCrud.getGridCrud());
	}

	private GastosMaipu fromViewToModel(GastosMaipu cliente) {
		
		if(getGastosMaipuCrud().getTipoGastoCB().getSelectedItem() != null)
			cliente.setTipoGasto((GastosMaipuEnum)getGastosMaipuCrud().getTipoGastoCB().getSelectedItem().getValue());

		if(getGastosMaipuCrud().getAnio().getSelectedItem() != null)
			cliente.setAnio((Long)getGastosMaipuCrud().getAnio().getSelectedItem().getValue());		
				
		if(getGastosMaipuCrud().getQuincena().getSelectedItem() != null)
			cliente.setQuincena((Quincena)getGastosMaipuCrud().getQuincena().getSelectedItem().getValue());	
		
		if(getGastosMaipuCrud().getDinero().getValue() != null)
			cliente.setDinero(getGastosMaipuCrud().getDinero().getValue());	
		
		if(getGastosMaipuCrud().getComentario().getValue() != null)
			cliente.setComentario(getGastosMaipuCrud().getComentario().getValue());
		
		if(getGastosMaipuCrud().getEmpleadoCB().getSelectedItem() != null)
			cliente.setEmpleado((Empleado)getGastosMaipuCrud().getEmpleadoCB().getSelectedItem().getValue());

		cliente.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());

		if(cliente.getFecha() == null)
			cliente.setFecha(new Date());
		return cliente;
	}
	

	private Gastos fromViewToModel(Gastos cliente) {
		
		if(getClienteCrud().getTipoGastoCB().getSelectedItem() != null)
			cliente.setTipoGasto((GastosEnum)getClienteCrud().getTipoGastoCB().getSelectedItem().getValue());

		if(getClienteCrud().getAnio().getSelectedItem() != null)
			cliente.setAnio((Long)getClienteCrud().getAnio().getSelectedItem().getValue());		
		
		if(getClienteCrud().getEsPagoMensual().getSelectedItem() != null)
			cliente.setPagaSueldosPorMes((Boolean)getClienteCrud().getEsPagoMensual().getSelectedItem().getValue());		
		
		if(getClienteCrud().getMes().getSelectedItem() != null)
			cliente.setMes((Long)getClienteCrud().getMes().getSelectedItem().getValue());	
		
		if(((GastosEnum)getClienteCrud().getTipoGastoCB().getSelectedItem().getValue()).toInt() == GastosEnum.SUELDOS.toInt()){
			if(getClienteCrud().getEsPagoMensual().getSelectedItem() != null 
					&& !(Boolean)getClienteCrud().getEsPagoMensual().getSelectedItem().getValue()){
				
				if(getClienteCrud().getFechaDesde().getValue() != null)
					cliente.setFechaDesde(getClienteCrud().getFechaDesde().getValue());
				
				if(getClienteCrud().getFechaHasta().getValue() != null)
					cliente.setFechaHasta(getClienteCrud().getFechaHasta().getValue());
			}	
		}
		
		if(getClienteCrud().getDinero().getValue() != null)
			cliente.setDinero(getClienteCrud().getDinero().getValue());	
		
		if(getClienteCrud().getComentario().getValue() != null)
			cliente.setComentario(getClienteCrud().getComentario().getValue());
		
		if(getClienteCrud().getEmpleadoCB().getSelectedItem() != null)
			cliente.setEmpleado((Empleado)getClienteCrud().getEmpleadoCB().getSelectedItem().getValue());

		cliente.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
		
		if(gastosSueldosPorActividadListGrid.isVisible() && gastosSueldosPorActividadListGrid.getListSueldos() != null 
				&& gastosSueldosPorActividadListGrid.getListSueldos().size() >0){
			
			List<GastosSueldos> gastosSueldos =gastosSueldosPorActividadListGrid.getListSueldos();
			cliente.setGastosSueldoList(new HashSet(gastosSueldos));	
			int total=obtenerSueldos(gastosSueldos);
			cliente.setDinero(total);	
		}
		return cliente;
	}
	
	private int obtenerSueldos(List<GastosSueldos> gastosSueldos){
		int dinero=0;
		if(gastosSueldos != null){
			for (GastosSueldos gastosSueldos2 : gastosSueldos) {
				if(gastosSueldos2.getDinero() != null)
					dinero= dinero+ gastosSueldos2.getDinero();
			}
			
		}
		return dinero;		
	}
	
	private void validateCrud() {
					
		if(getClienteCrud().getTipoGastoCB().getSelectedItem()  == null)
			throw new WrongValueException(getClienteCrud().getTipoGastoCB(), I18N.getLabel("error.empty.field"));
	
		if(getClienteCrud().getAnio().getSelectedItem() == null)
			throw new WrongValueException(getClienteCrud().getAnio(),I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getMes().getSelectedItem() == null)
			throw new WrongValueException(getClienteCrud().getMes(),I18N.getLabel("error.empty.field"));	
		
		if(getClienteCrud().getEsPagoMensual().getSelectedItem() == null){
			throw new WrongValueException(getClienteCrud().getEsPagoMensual(),I18N.getLabel("error.empty.field"));
		}
		
		
		if(((GastosEnum)getClienteCrud().getTipoGastoCB().getSelectedItem().getValue()).toInt() == GastosEnum.SUELDOS.toInt()){
		
			if(getClienteCrud().getEmpleadoCB().getSelectedItem() == null)
				throw new WrongValueException(getClienteCrud().getEmpleadoCB(),I18N.getLabel("error.empty.field"));

			if(getClienteCrud().getEsPagoMensual().getSelectedItem() != null &&
					!(Boolean)getClienteCrud().getEsPagoMensual().getSelectedItem().getValue() ){
				
				if(getClienteCrud().getFechaDesde().getValue() == null){
					throw new WrongValueException(getClienteCrud().getFechaDesde(), I18N.getLabel("error.empty.field"));
				}
				
				if(getClienteCrud().getFechaHasta().getValue() == null){
					throw new WrongValueException(getClienteCrud().getFechaHasta(), I18N.getLabel("error.empty.field"));
				}
			}
			
		}else{
			
			if(getClienteCrud().getDinero().getValue()  == null)
				throw new WrongValueException(getClienteCrud().getDinero(), I18N.getLabel("error.empty.field"));
		}
	}

	public Gastos getGasto() {
		return gasto;
	}

	public void setGasto(Gastos gasto) {
		this.gasto = gasto;
	}

	@Override
	public void actualizarPorSucursal(SucursalEnum suc) {
		// TODO Auto-generated method stub
		if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt() ){
			getGastosMaipuCrud().setVisible(true);
			getClienteCrud().setVisible(false);
			gastosSueldosPorActividadListGrid.setVisible(false);
			
			Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTime(new Date());

			
			Integer anio=calendarDate.get(calendarDate.YEAR);
			setSelected(getGastosMaipuCrud().getAnio(), anio);

			
			
			getGastosMaipuCrud().getQuincena().setSelectedItem(null);
			getGastosMaipuCrud().getDinero().setValue(0);
			getGastosMaipuCrud().getComentario().setValue("");
			getGastosMaipuCrud().getTipoGastoCB().setSelectedItem(null);

		}else if(getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum)getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt() ){
			
			getGastosMaipuCrud().setVisible(false);
			getClienteCrud().setVisible(true);
//			gastosSueldosPorActividadListGrid.setVisible(true);
			
			
			Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTime(new Date());

			
			Integer anio=calendarDate.get(calendarDate.YEAR);
			setSelected(getClienteCrud().getAnio(), anio);
//			getClienteCrud().getAnio().setSelectedIndex(anio);
			
			
			Integer messs=calendarDate.get(calendarDate.MONTH);
			setSelected(getClienteCrud().getMes(), messs);
//			getClienteCrud().getMes().setSelectedIndex(messs);
			getClienteCrud().getTipoGastoCB().setSelectedItem(null);
			getClienteCrud().getDinero().setValue(0);
			getClienteCrud().getComentario().setValue("");

			gastosSueldosPorActividadListGrid.cleanRows();
			gastosSueldosPorActividadListGrid.clear();

		}
 	}
	
	private void setSelected(Combobox combo, int val){
		
		if(combo != null && combo.getItems() != null){
			for (Object comboit : combo.getItems()) {
				if(((Long)((Comboitem)comboit).getValue()).intValue() == val){
					combo.setSelectedItem(((Comboitem)comboit));
				}
			} 
			
		}
	}
	
	
	private void fromModelToView(GastosMaipu cliente) {
		// si lo que obtuve de bd es != null
		if(cliente != null){
			
			if(cliente.getNombreUsuarioGeneroMovimiento() != null){
				getSucursalCrud().getResponsable().setValue(cliente.getNombreUsuarioGeneroMovimiento());
				
			}
			
			if(cliente.getTipoGasto() != null){
				getGastosMaipuCrud().setSelectedTipoGasto(cliente.getTipoGasto());
			}
	
			if(cliente.getAnio() != null){
				getGastosMaipuCrud().setSelectedAnio(cliente.getAnio());
			}
	
			if(cliente.getQuincena() != null){
				getGastosMaipuCrud().setSelectedQuincena(cliente.getQuincena());
			}

			if(cliente != null && cliente.getId() != null && cliente.getTipoGasto().toInt() == GastosMaipuEnum.SUELDOS.toInt()){
				getGastosMaipuCrud().getEmpleadoCB().setVisible(true);
				getGastosMaipuCrud().getEmpleado().setVisible(true);
			}

			if(cliente.getComentario() != null){
				getGastosMaipuCrud().getComentario().setValue(cliente.getComentario());	
			}
		
			if(cliente.getDinero() != null){
				getGastosMaipuCrud().getDinero().setValue(cliente.getDinero());
			}		
			
			if(cliente.getEmpleado() != null){
				getGastosMaipuCrud().setSelectedEmpleado(cliente.getEmpleado());
			}

		}		
	}

	@Override
	public void actualizarLitado() {
		// TODO Auto-generated method stub
		
	}

}
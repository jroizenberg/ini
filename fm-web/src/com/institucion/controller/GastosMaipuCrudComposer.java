package com.institucion.controller;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.GastosEJB;
import com.institucion.desktop.delegated.GastosDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Empleado;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.GastosMaipu;
import com.institucion.model.GastosMaipuCrud;
import com.institucion.model.GastosMaipuEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Quincena;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class GastosMaipuCrudComposer extends CrudComposer  implements GastosDelegate{

	private PanelCrud panelCrud;
	public final String idGasto = "idGasto";
	private GastosEJB clienteEJB;
	private CajaEJB cajaEJB;

	private GastosMaipu gasto;
	
	public GastosMaipuCrudComposer() {
		clienteEJB = BeanFactory.<GastosEJB>getObject("fmEjbGastos");
		cajaEJB= BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}

	public void onCreate() {
		GastosMaipu cliente= (GastosMaipu) Session.getAttribute(idGasto);
		gasto= cliente;
		this.fromModelToView(cliente);
		getClienteCrud().getDinero().setDisabled(false);	
		getClienteCrud().setDelegate(this);
		Calendar calStart = Calendar.getInstance(); 

		if(gasto == null){
			getClienteCrud().setSelectedAnio(new Long((calStart.get(Calendar.YEAR))));
		}
	}
	
	private void fromModelToView(GastosMaipu cliente) {
		// si lo que obtuve de bd es != null
		if(cliente != null){
			
			if(cliente.getTipoGasto() != null){
				getClienteCrud().setSelectedTipoGasto(cliente.getTipoGasto());
			}
	
			if(cliente.getAnio() != null){
				getClienteCrud().setSelectedAnio(cliente.getAnio());
			}
	
			if(cliente.getQuincena() != null){
				getClienteCrud().setSelectedQuincena(cliente.getQuincena());
			}

			if(cliente.getComentario() != null){
				getClienteCrud().getComentario().setValue(cliente.getComentario());	
			}
		
			if(cliente.getDinero() != null){
				getClienteCrud().getDinero().setValue(cliente.getDinero());
			}			
		}		
	}
	
	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idGasto) != null) {
				// es una modificacion
					gasto = (GastosMaipu) Sessions.getCurrent().getAttribute(idGasto);
					validateCrud();
					this.fromViewToModel(gasto);
					
					// obtener gasto original.
//					De la suma original hago el ajuste en la caja, con el ingreso o egreso.
					GastosMaipu gastoOriginal=clienteEJB.findByIdMaipu(gasto.getId());
					
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
						String conceptoGastos= "Gasto Maipu: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
						
							conceptoGastos= conceptoGastos + ". Pago Quincenal "+ gasto.getQuincena().getNombre() +"/"+gasto.getAnio()+
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
//							pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//							pagoNuevo.setSaldadaDeuda(true);
							pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
							pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
							pagoNuevo.setAdicional(0);
							pagoNuevo.setCantCuotas(0);
							pagoNuevo.setGastoMaipu(gasto);
							pagoNuevo.setQuincena((Quincena)getClienteCrud().getQuincena().getSelectedItem().getValue() );
							

							cajaEJB.savePagosPorSubscripcion(pagoNuevo);
							
					
						
					}else{
						// tengo que restarle mas a la caja.
						Integer dinero =gasto.getDinero() - gastoOriginal.getDinero();
						CajaMovimiento caja2= new CajaMovimiento();

						// si no es pago de sueldos, registro solo 1
						String conceptoGastos= "Gasto Maipu: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
						
				
							conceptoGastos= conceptoGastos + ". Pago quincenal "+ gasto.getQuincena().getNombre() +"/"+gasto.getAnio()+
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
//							pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
//							pagoNuevo.setSaldadaDeuda(true);
							pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
							pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
							pagoNuevo.setGastoMaipu(gasto);
							pagoNuevo.setAdicional(0);
							pagoNuevo.setCantCuotas(0);
							pagoNuevo.setQuincena((Quincena)getClienteCrud().getQuincena().getSelectedItem().getValue() );
							cajaEJB.savePagosPorSubscripcion(pagoNuevo);
					}
					clienteEJB.saveMaipu(gasto);

					if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
					}else 	if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 
						Sessions.getCurrent().setAttribute("idFromCaja", null);
						super.gotoPage("/institucion/caja-selector.zul");

					}else
						super.gotoPage("/institucion/gastos-maipu-selector.zul");
					
					
					
					
					
			} else {
				// es nuevo 
				validateCrud();
				gasto= new GastosMaipu();
				gasto=this.fromViewToModel(gasto);
				
				// Setear el Gasto
//				gasto= setearGastos(gasto);
				gasto=clienteEJB.createMaipu(gasto);
				
				// genero movimiento de caja 
				String conceptoGastos= "Gasto Maipu: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;

				CajaMovimiento caja2= new CajaMovimiento();

				// si no es pago de sueldos, registro solo 1
				caja2.setConcepto(conceptoGastos);
				caja2.setFecha(new Date());
				caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
				caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
				caja2.setValor(new Double(gasto.getDinero()));
				caja2.setSucursal(SucursalEnum.MAIPU);

				cajaEJB.save(caja2);
				conceptoGastos= "Gasto Maipu: "+ gasto.getTipoGasto().toString(gasto.getTipoGasto().toInt()) ;
				
				PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
				pagoNuevo.setCantidadDinero(gasto.getDinero());
				pagoNuevo.setSucursal(SucursalEnum.MAIPU);
				pagoNuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
				pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
				pagoNuevo.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
				pagoNuevo.setGastoMaipu(gasto);
				pagoNuevo.setAdicional(0);
				pagoNuevo.setCantCuotas(0);
				pagoNuevo.setQuincena((Quincena)getClienteCrud().getQuincena().getSelectedItem().getValue() );
				cajaEJB.savePagosPorSubscripcion(pagoNuevo);
			
				if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){
					Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				}else 	if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 
					Sessions.getCurrent().setAttribute("idFromCaja", null);
					super.gotoPage("/institucion/caja-selector.zul");

				}else{
					super.gotoPage("/institucion/gastos-maipu-selector.zul");
				}
			}
			
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
	
	public void onBack(Event event) {
		if (Sessions.getCurrent().getAttribute("isClienteFromIngresoInscripcion") != null){ 
			Sessions.getCurrent().setAttribute("isClienteFromIngresoInscripcion", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else 	if (Sessions.getCurrent().getAttribute("idFromCaja") != null){ 
			Sessions.getCurrent().setAttribute("idFromCaja", null);
			super.gotoPage("/institucion/caja-selector.zul");

		}else	
			super.gotoPage("/institucion/gastos-maipu-selector.zul");
	}

	private GastosMaipuCrud getClienteCrud() {
		return (GastosMaipuCrud) (panelCrud.getGridCrud());
	}

	private GastosMaipu fromViewToModel(GastosMaipu cliente) {
		
		if(getClienteCrud().getTipoGastoCB().getSelectedItem() != null)
			cliente.setTipoGasto((GastosMaipuEnum)getClienteCrud().getTipoGastoCB().getSelectedItem().getValue());

		if(getClienteCrud().getAnio().getSelectedItem() != null)
			cliente.setAnio((Long)getClienteCrud().getAnio().getSelectedItem().getValue());		
				
		if(getClienteCrud().getQuincena().getSelectedItem() != null)
			cliente.setQuincena((Quincena)getClienteCrud().getQuincena().getSelectedItem().getValue());	
		
		if(getClienteCrud().getDinero().getValue() != null)
			cliente.setDinero(getClienteCrud().getDinero().getValue());	
		
		if(getClienteCrud().getComentario().getValue() != null)
			cliente.setComentario(getClienteCrud().getComentario().getValue());
		
		if(cliente.getFecha() == null)
			cliente.setFecha(new Date());
		return cliente;
	}
	
	private void validateCrud() {
					
		if(getClienteCrud().getTipoGastoCB().getSelectedItem()  == null)
			throw new WrongValueException(getClienteCrud().getTipoGastoCB(), I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getAnio().getSelectedItem() == null)
			throw new WrongValueException(getClienteCrud().getAnio(),I18N.getLabel("error.empty.field"));
		
		if(getClienteCrud().getQuincena().getSelectedItem() == null)
			throw new WrongValueException(getClienteCrud().getQuincena(),I18N.getLabel("error.empty.field"));	
		
		if(getClienteCrud().getDinero().getValue()  == null)
			throw new WrongValueException(getClienteCrud().getDinero(), I18N.getLabel("error.empty.field"));
	}

	public GastosMaipu getGasto() {
		return gasto;
	}

	public void setGasto(GastosMaipu gasto) {
		this.gasto = gasto;
	}

	@Override
	public void actualizarPanelSueldos(boolean mostrar, Empleado emp) {
		// TODO Auto-generated method stub
		
	}
	
}
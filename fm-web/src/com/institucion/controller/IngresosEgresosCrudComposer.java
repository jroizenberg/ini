package com.institucion.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.desktop.delegated.IngresosEgresosDelegate;
import com.institucion.desktop.delegated.SucursalDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.FormasDePagoVentaProdCrud;
import com.institucion.model.IngresosEgresosCrud;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Producto;
import com.institucion.model.Quincena;
import com.institucion.model.SucursalCrud;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class IngresosEgresosCrudComposer extends CrudComposer implements AnularSubscripcionDelegate,  IngresosEgresosDelegate, SucursalDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	// crud formas de pago
	private FormasDePagoVentaProdCrud crudFormasDePago;
//	private PanelCrud  pagar;
	private CajaEJB cajaEJB;
	private PanelCrud sucCrud;

//	private ToolbarCrud toolbar;
	private PanelCrud crud;
	
//	private IngresosEgresosPagarBotonesFilter  getPagarCursosCrud() {
//		return (IngresosEgresosPagarBotonesFilter ) (pagar.getGridCrud());
//	}
	
	private IngresosEgresosCrud getProductoVentaCrud() {
		return (IngresosEgresosCrud)(crud.getGridCrud());
	}
	
	private SucursalCrud getSucursalCrud() {
		return (SucursalCrud)(sucCrud.getGridCrud());
	}
	
	
	public IngresosEgresosCrudComposer() {
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}
 	
	public void onCreate() {
		crudFormasDePago.setSubsAnulardelegate(this);
//		getPagarCursosCrud().setSubsAnulardelegate(this);
		getProductoVentaCrud().setDelegate(this);
		getProductoVentaCrud().getSaldo().setValue(0);
		 getSucursalCrud().setDelegate(this);
		 
		getSucursalCrud().getResponsable().setVisible(false);
		getSucursalCrud().getResponsableL().setVisible(false);
		fillListBoxProducts(null);
		
		if( getSucursalCrud().getSucursal().getSelectedItem() == null){
			crudFormasDePago.setVisible(false);
			getProductoVentaCrud().setVisible(false);
//			toolbar.setEnabledSave(false);
//			getPagarCursosCrud().setVisible(false);	
		}else{
			crudFormasDePago.setVisible(true);
			getProductoVentaCrud().setVisible(true);
//			toolbar.setEnabledSave(true);
//			getPagarCursosCrud().setVisible(true);	
		}
//		toolbar.setEnabledSave(true);
		
		if( getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt() ){
			getProductoVentaCrud().getQuincena().setVisible(true);
			getProductoVentaCrud().getQuincenalabel().setVisible(true);
		}else if( getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt() ){
			getProductoVentaCrud().getQuincena().setVisible(false);
			getProductoVentaCrud().getQuincenalabel().setVisible(false);
			getProductoVentaCrud().getQuincena().setSelectedItem(null);				
		}
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
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
	
	@Override
	public void pagarCurso() {
		int debePagar= getProductoVentaCrud().getSaldo().getValue();
		
		int pagosRealizados=0;
		int adicionales=0;
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else{
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
				}		
			}
		}
			
		if(pagosRealizados < debePagar ){
			// error el pago debe de ser exacto. 
			MessageBox.info(I18N.getLabel("curso.error.seleccion.select.suma.del.pago.del.venta"), I18N.getLabel("selector.actionwithoutitem.title"));
			return ;
			
		}else{
			int apagar=pagosRealizados + adicionales;
			Object[] params3 =  { "$"+formateador.format(apagar) };	

			if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.ingreso.egreso",params3),
				"Confirmando Pago")){
			
				try {
					onSave(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
					
	private void fillListBoxProducts(List <Producto> listProds) {
		Set<PagosPorSubscripcion> result = new HashSet<PagosPorSubscripcion>();
		if (listProds != null) {
			int importeFinal= getProductoVentaCrud().getSaldo().getValue();

//			for (PagosPorSubscripcion dpp : subscripcion.getPagosPorSubscripcionList()) {
//				result.add(dpp);
//			}
			
//			if(subscripcion.getPagosPorSubscripcionList() != null && subscripcion.getPagosPorSubscripcionList().size() ==0){
				// actualizo el rojo y el verde de la plata, no se hizo ningun pago aca.
				
				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
				crudFormasDePago.getValorSumadoPesos().setValue("0");
//			}
			crudFormasDePago.fillListBox(result);
			
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				int sumaTotPagos=0;
				int sumaAdicionalTarjeta=0;
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}
		   				
						Integer.toString(adicionalparte);
						pagoPorSubscripcion.setAdicional(adicionalparte);
						sumaTotPagos= sumaTotPagos+ pagoPorSubscripcion.getCantidadDinero()+ adicionalparte;
						sumaAdicionalTarjeta= sumaAdicionalTarjeta+ adicionalparte;
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}
		   				
						Integer.toString(adicionalparte);
						pagoPorSubscripcion.setAdicional(adicionalparte);
						sumaTotPagos= sumaTotPagos+ pagoPorSubscripcion.getCantidadDinero()+ adicionalparte;
						sumaAdicionalTarjeta= sumaAdicionalTarjeta+ adicionalparte;
					}else{
						sumaTotPagos = sumaTotPagos +  pagoPorSubscripcion.getCantidadDinero();
						
					}		
				}	
				int faltante=importeFinal -sumaTotPagos+ sumaAdicionalTarjeta;

				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(faltante));
				crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(sumaTotPagos));
				
			}
		}else{
			int importeFinal= getProductoVentaCrud().getSaldo().getValue();
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
			crudFormasDePago.getValorSumadoPesos().setValue("0");
			
		}		
	}
	
	public void onSave(Event event) throws Exception {
		try{
			
			
			int debePagar= getProductoVentaCrud().getSaldo().getValue();
			
			int pagosRealizados=0;
			int adicionales=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else{
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					}		
				}
			}
				
			if(pagosRealizados < debePagar ){
				// error el pago debe de ser exacto. 
				MessageBox.info(I18N.getLabel("curso.error.seleccion.select.suma.del.pago.del.venta"), I18N.getLabel("selector.actionwithoutitem.title"));
				return ;
				
			}else{
				int apagar=pagosRealizados + adicionales;
				Object[] params3 =  { "$"+formateador.format(apagar) };	

				if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.ingreso.egreso",params3),
					"Confirmando Pago")){
				
					try {
						
						if(!validateCrud()){
							return;
						}

						// Guardo el movimiento en la caja ahora
						CajaMovimiento caja= new CajaMovimiento();
						
						List<PagosPorSubscripcion> listaPagos=fromViewToModel((TipoMovimientoCajaEnum)getProductoVentaCrud().getTipoMovimiento().getSelectedItem().getValue());

						
						String comentarioPagos= obtenerFormasDePagoPagos(listaPagos);
						if(getProductoVentaCrud().getComentario().getValue() != null){			
							caja.setConcepto("Movimiento de caja desde Ingresos/Egresos: "+getProductoVentaCrud().getComentario().getValue() +". "+ comentarioPagos);		
						
						}else
							caja.setConcepto("Movimiento de caja desde Ingresos/Egresos. "+ comentarioPagos);
						caja.setFecha(new Date());
						caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
						caja.setTipoMovimiento((TipoMovimientoCajaEnum)getProductoVentaCrud().getTipoMovimiento().getSelectedItem().getValue());
						caja.setValor(Double.valueOf(crudFormasDePago.getValorSumadoPesos().getValue()));
						caja.setSucursal((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue());
						cajaEJB.save(caja);
									
//						ver si se puede grabar con hibernate asi nomas las formasDePago solas, las PagosPorSubscripcion que se registraron aca.
						if(listaPagos != null){
							cajaEJB.savePagosPorSubscripcionAll(listaPagos);
						}
						
						if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelector") != null){ 
							Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelector", null);
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						}else
							super.gotoPage("/institucion/caja-selector.zul");						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
						
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}

	
	private SucursalEnum getSucursal(List<PagosPorSubscripcion> list){
		SucursalEnum suc= SucursalEnum.CENTRO;
		if(list != null && list.size() > 0){
			for (PagosPorSubscripcion iterable_element : list) {
				return iterable_element.getSucursal();
			}
		}
		suc= SucursalEnum.CENTRO;
		return suc;
	}
	
	public String obtenerFormasDePagoPagos(List<PagosPorSubscripcion> listaPagos){
		
		String comentarioFormasDePago="";

			int conTarjeta=0;
			int conTarjetaAdicionales= 0;
			int cuotasTarjeta=0;
			
			int conDebito=0;
			int conEfectivo=0;
				
				for (PagosPorSubscripcion pagoPorSubscripcion: 	listaPagos) {
					
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						conTarjeta= conTarjeta+ pagoPorSubscripcion.getCantidadDinero();
						conTarjetaAdicionales= conTarjetaAdicionales+ pagoPorSubscripcion.getAdicional();
						
						if(pagoPorSubscripcion.getCantCuotas() >  cuotasTarjeta)
							cuotasTarjeta= pagoPorSubscripcion.getCantCuotas();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						conTarjeta= conTarjeta+ pagoPorSubscripcion.getCantidadDinero();
						conTarjetaAdicionales= conTarjetaAdicionales+ pagoPorSubscripcion.getAdicional();
						
						if(pagoPorSubscripcion.getCantCuotas() >  cuotasTarjeta)
							cuotasTarjeta= pagoPorSubscripcion.getCantCuotas();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.DEBITO.toInt()){
						conDebito= conDebito+ pagoPorSubscripcion.getCantidadDinero();
						
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()){
						conEfectivo= conEfectivo+ pagoPorSubscripcion.getCantidadDinero();
						
					}				
				}					
			if(conEfectivo > 0){
				comentarioFormasDePago="Se abono en Efectivo: $"+ formateador.format(conEfectivo )+"- ";
				
			}
			if(conDebito > 0){
				comentarioFormasDePago="Debito: $"+ formateador.format(conDebito)+"- ";
				
			}	
			if(conTarjeta > 0){
				comentarioFormasDePago="Tarjeta: $"+ formateador.format(conTarjeta)+ ", Adicional Tarjeta: $"+formateador.format(conTarjetaAdicionales )+ ", Cuotas: "+ cuotasTarjeta ;
				
			}
		return comentarioFormasDePago;

	} 
	
	public void onBack(Event event) {
		if (Sessions.getCurrent().getAttribute("idFromIgresoInscripcionSelector") != null){ 
			Sessions.getCurrent().setAttribute("idFromIgresoInscripcionSelector", null);
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
		}else
			super.gotoPage("/institucion/caja-selector.zul");
	}

	private List <PagosPorSubscripcion> fromViewToModel(TipoMovimientoCajaEnum tipoMov) {
		List <PagosPorSubscripcion> pagosNuevos = new ArrayList<PagosPorSubscripcion>();

		// tengo que saber primero cuanto debo 
		int deuda= getProductoVentaCrud().getSaldo().getValue();

		// obtengo la plata de la deuda, de todos los pagos que se hicieron
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
				
				pagoPorSubscripcion.setSucursal((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue());
				if(pagoPorSubscripcion.getSucursal().toInt() == SucursalEnum.MAIPU.toInt()){
					pagoPorSubscripcion.setQuincena((Quincena)getProductoVentaCrud().getQuincena().getSelectedItem().getValue());	
				}

				if(pagoPorSubscripcion.getCantidadDinero() > deuda){
					// quiere decir que saco ya todo el dinero de este pago.
					PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
					pagoNuevo.setEsCopago(false);
					pagoNuevo.setCantidadDinero(deuda);
					pagoNuevo.setIdTipoDePago(pagoPorSubscripcion.getIdTipoDePago());
					pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
					pagoNuevo.setSaldadaDeuda(true);
					pagoNuevo.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
					pagoNuevo.setTipoMovimiento(tipoMov);

					pagoNuevo.setSucursal((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue());
					if(pagoNuevo.getSucursal().toInt() == SucursalEnum.MAIPU.toInt()){
						pagoNuevo.setQuincena((Quincena)getProductoVentaCrud().getQuincena().getSelectedItem().getValue());	
					}

					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 15 )/100;
		   				}
		   				
						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoNuevo.setTipoMovimiento(tipoMov);

						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						
						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}
		   				
						pagoPorSubscripcion.setAdicional(adicional);

						pagoPorSubscripcion.setTipoMovimiento(tipoMov);

					}else 	if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 20 )/100;
		   				}
		   				
						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoNuevo.setTipoMovimiento(tipoMov);

						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						
						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}
		   				
						pagoPorSubscripcion.setAdicional(adicional);

						pagoPorSubscripcion.setTipoMovimiento(tipoMov);

					}
					pagosNuevos.add(pagoNuevo);
					break;
				}else if(pagoPorSubscripcion.getCantidadDinero() == deuda){
					pagoPorSubscripcion.setTipoMovimiento(tipoMov);
					pagoPorSubscripcion.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());
					pagoPorSubscripcion.setEsCopago(false);

					pagosNuevos.add(pagoPorSubscripcion);

					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
					break;
				}else{
					// quiere decir que saco el dinero de el pago anterior + un nuevo pago.
					pagoPorSubscripcion.setEsCopago(false);
					pagoPorSubscripcion.setTipoMovimiento(tipoMov);
					pagoPorSubscripcion.setIdUsuarioGeneroMovimientoCaja(Session.getUsernameID().intValue());

					pagosNuevos.add(pagoPorSubscripcion);
					deuda = deuda-pagoPorSubscripcion.getCantidadDinero(); // - pagoPorSubscripcion.getAdicional();
					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
				}
			}
		}
		return pagosNuevos;

		}	

	private boolean validateCrud() {
		boolean result=true;
		
		if(getProductoVentaCrud().getTipoMovimiento().getSelectedItem() == null){
			MessageBox.info("Debe seleccionar un valor en el campo Tipo Movimiento", I18N.getLabel("selector.actionwithoutitem.title"));
			return  false;
		}
//			throw new WrongValueException(getProductoVentaCrud().getTipoMovimiento(), I18N.getLabel("error.empty.field"));

		if( getSucursalCrud().getSucursal().getSelectedItem() == null){
			
			MessageBox.info("Debe seleccionar un valor en el campo Sucursal", I18N.getLabel("selector.actionwithoutitem.title"));
			return  false;
		}
//			throw new WrongValueException(getProductoVentaCrud().getSucursal(), I18N.getLabel("error.empty.field"));

		if( getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt()){
			if(getProductoVentaCrud().getQuincena().getSelectedItem() == null){
//				result= false;
				MessageBox.info("Debe seleccionar un valor en el campo Quincena", I18N.getLabel("selector.actionwithoutitem.title"));
				return  false;
			}
//				throw new WrongValueException(getProductoVentaCrud().getQuincena(), I18N.getLabel("error.empty.field"));
		}
			
		if(getProductoVentaCrud().getSaldo().getValue() == null){
//			result= false;
			MessageBox.info("Debe ingresar un Saldo", I18N.getLabel("selector.actionwithoutitem.title"));
			return  false;
		}
//			throw new WrongValueException(getProductoVentaCrud().getSaldo(), I18N.getLabel("error.empty.field"));
		if(getProductoVentaCrud().getSaldo().getValue() != null && 
				getProductoVentaCrud().getSaldo().getValue() <= 0){
//			result= false;
			MessageBox.info("El saldo debe ser  mayor a 0", I18N.getLabel("selector.actionwithoutitem.title"));
			return  false;
		}
//			throw new WrongValueException(getProductoVentaCrud().getSaldo(), I18N.getLabel("error.empty.field"));
		
		
		// tiene que tener pagos en la lista de pagos
		if(crudFormasDePago.getProducts() == null 
				||  (crudFormasDePago.getProducts() != null && 
						crudFormasDePago.getProducts().size() <= 0)){
//			result= false;
			MessageBox.info("Debe ingresar la forma de pago", I18N.getLabel("selector.actionwithoutitem.title"));
			return  false;
		}
//			throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field"));
//		}
		
		// la lista de pagos debe de ser igual a la deuda.
		if(crudFormasDePago.getProducts() != null && 
				crudFormasDePago.getProducts().size() > 0){
		
			int deudaAPagarSeleccionada= getProductoVentaCrud().getSaldo().getValue();

			int pagosRealizados=0;
			int adicionales=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else{
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					}		
				}
			}
			if(deudaAPagarSeleccionada != pagosRealizados){
//				result= false;
				MessageBox.info(I18N.getLabel("error.empty.field.pago.igual.ingresoEgreso"), I18N.getLabel("selector.actionwithoutitem.title"));
				return  false;
			}
//				throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field.pago.igual.ingresoEgreso"));
		}
		
		return result;
	}

	@Override
	public int getValorCurso() {
		if(getProductoVentaCrud().getSaldo().getValue() != null)
			return getProductoVentaCrud().getSaldo().getValue();
		return 0;
	}

	@Override
	public boolean seSeleccionoElCurso() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actualizarLista() {

		crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(getValorCurso()));
		crudFormasDePago.getValorSumadoPesos().setValue("0");
		
		crudFormasDePago.cleanCrud();
		crudFormasDePago.getGridList().removeAll();
	}
	
	
	@Override
	public void actualizarPorSucursal(SucursalEnum suc) {
		// TODO Auto-generated method stub
		
		if( getSucursalCrud().getSucursal().getSelectedItem() == null){
			crudFormasDePago.setVisible(false);
			getProductoVentaCrud().setVisible(false);	
//			getPagarCursosCrud().setVisible(false);	
//			toolbar.setEnabledSave(false);
		}else{
			crudFormasDePago.setVisible(true);
			getProductoVentaCrud().setVisible(true);
//			toolbar.setEnabledSave(true);
//			getPagarCursosCrud().setVisible(true);	
		}

		if( getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.MAIPU.toInt() ){
			getProductoVentaCrud().getQuincena().setVisible(true);
			getProductoVentaCrud().getQuincenalabel().setVisible(true);
		}else if( getSucursalCrud().getSucursal().getSelectedItem() != null && 
				((SucursalEnum) getSucursalCrud().getSucursal().getSelectedItem().getValue()).toInt() == SucursalEnum.CENTRO.toInt() ){
			getProductoVentaCrud().getQuincena().setVisible(false);
			getProductoVentaCrud().getQuincenalabel().setVisible(false);
		}
		
		getProductoVentaCrud().getSaldo().setValue(0);
		fillListBoxProducts(null);
		getProductoVentaCrud().getTipoMovimiento().setSelectedItem(null);
		getProductoVentaCrud().getComentario().setValue(null);
		getProductoVentaCrud().getQuincena().setSelectedItem(null);				

		
 	}

	@Override
	public void actualizarLitado() {
		// TODO Auto-generated method stub
		
	}
		
}

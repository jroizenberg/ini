package com.institucion.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.AnularSubscripcionDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Cliente;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.FormasDePagoCrud;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Quincena;
import com.institucion.model.SaldarDeudaCrud;
import com.institucion.model.Subscripcion;
import com.institucion.model.SubscripcionDaDPanel;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class SaldarDeudaCrudComposer extends CrudComposer implements AnularSubscripcionDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ClienteEJB clienteEJB;
	private InscripcionEJB subscripcionEJB;

	// crud formas de pago
	private FormasDePagoCrud crudFormasDePago;
		
	// crud del NOMBRE
	private PanelCrud crud;
	private Cliente cliente;
//	private PanelCrud  pagar;

	private SubscripcionDaDPanel leftPanel;
	private SubscripcionDaDPanel rightPanel;
	List<Subscripcion> perms = null;
	private CajaEJB cajaEJB;

	
//	private SubscripcionPagarBotonesFilter getPagarCursosCrud() {
//		return (SubscripcionPagarBotonesFilter) (pagar.getGridCrud());
//	}
	
	public SaldarDeudaCrudComposer() {
		subscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");

	}

	private SaldarDeudaCrud getSaldarDeudaCrud() {
		return (SaldarDeudaCrud)(crud.getGridCrud());
	}
 		
	
	public void onAbonar(Event event) throws Exception {
		pagarCurso();		
	}
	
	@Override
	public void pagarCurso() {
		int debePagar =0; 
				
		if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() != null)
			debePagar =Integer.valueOf(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
		else
			return ;
		
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
			MessageBox.info(I18N.getLabel("curso.error.seleccion.select.suma.del.pago"), I18N.getLabel("selector.actionwithoutitem.title"));
			return ;
			
		}else{
			int apagar=pagosRealizados + adicionales;
			Object[] params3 =  {cliente.getApellido().toUpperCase() +" "+ cliente.getNombre().toUpperCase(),  "$"+formateador.format(apagar)};	

			if (MessageBox.question(I18N.getLabel("selector.seguro.de.abonar.saldar",params3),
				I18N.getLabel("closeup.selector.abonar.y.guardar.title"))){
			
				try {
					onSave(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
			
	public void onCreate() {
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		setCliente(cliente);	

		cliente=clienteEJB.loadLazy(cliente, true, true, true, false);

		getSaldarDeudaCrud().getCliente().setValue(cliente.getApellido().toUpperCase() + " "+ cliente.getNombre().toUpperCase());
		
		boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cliente.getId());

		if(tieneDeudas)
			getSaldarDeudaCrud().getEstado().setValue(" Con deuda ");
		
		getSaldarDeudaCrud().getDeuda().setValue(String.valueOf(getDeuda(cliente)));
		getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf("0"));

		crudFormasDePago.setSubsAnulardelegate(this);
//		getPagarCursosCrud().setSubsAnulardelegate(this);
		fillListBoxProducts(null);
		
		// en este , tengo que poner las deudas de este lado
		//pasarle aca solamente las subscripciones con las que tiene deuda
		List<Subscripcion> subs=obtenerSubscripcionesConDeuda(cliente.getSubscripcionesList());
		if(subs != null)
			this.leftPanel.setSet(new HashSet(subs));

	}

	
	public boolean seSeleccionoElCurso(){
		if(rightPanel.getList() != null && rightPanel.getList().size() >0)
			return true;
		else 
			return false;
	}

	private List<Subscripcion> obtenerSubscripcionesConDeuda(Set <Subscripcion> setSubs){
		List <Subscripcion> subs= null;
		if(setSubs != null){
			
			for (Subscripcion subscripcion : setSubs) {
				if(subscripcion.getCupoActividadList() != null ){
					for (CupoActividad iterable_element : subscripcion.getCupoActividadList()) {
						if(iterable_element.getEstado() != null &&(
								iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
								|| iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()
								|| iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt())){
								if(subs == null)
									subs= new ArrayList<Subscripcion>();
								
									subs.add(subscripcion);
						}				
					}
				}
			}	
		}
		return subs;
	}
	
	private int obtenerDeuda(Subscripcion subs){
		int fantantePago=0;
		
		
		if(subs.getCupoActividadList() != null ){
			for (CupoActividad iterable_element : subs.getCupoActividadList()) {
				if(iterable_element.getEstado() != null && 
						iterable_element.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
						((iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
							||(iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
							||(iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
					int pagosRealizados=0;
					for (PagosPorSubscripcion pagoPorSubscripcion: subs.getPagosPorSubscripcionList()) {
						if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
							pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
							pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
						}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
							pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
							pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
						}else{
							pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						}		
					}
					
					if(pagosRealizados < subs.getPrecioTOTALadicionalTarjeta() ){
						fantantePago =subs.getPrecioTOTALadicionalTarjeta() - pagosRealizados;
					}
					break;
				}			
				
			}
		}
		return fantantePago;
	}
	
	public void onMoveRight(Event event) throws Exception {
		List<Subscripcion> perms = leftPanel.getSelectedAndRemoveThem();
		rightPanel.addList(perms);
		
		int sumaAAbonar=0;
		for (Subscripcion subscripcion : perms) {
			sumaAAbonar=sumaAAbonar+obtenerDeuda(subscripcion);
		}
		
		if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() == null){
			getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(sumaAAbonar));

		}else{
			if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() != null
					&& getSaldarDeudaCrud().getDeudaaSaldar().getValue().equalsIgnoreCase("0")){
				getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(sumaAAbonar));
				
			}else{
				int deudaASaldar=Integer.parseInt(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
				int deudaTotalASaldar=sumaAAbonar+ deudaASaldar;
				getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(deudaTotalASaldar));
			}
		}
		
		int debeTOT=Integer.parseInt(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
		if(debeTOT == 0){
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(0));
			crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(0));
			crudFormasDePago.getGridList().removeAll();

		}else{
			crudFormasDePago.getGridList().removeAll();
			crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(0));
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(debeTOT));
		}

	}

	public void onMoveAllRight(Event event) throws Exception {
		leftPanel.selectAllItems();		
		this.onMoveRight(event);
	}

	public void onMoveLeft(Event event) throws Exception {
		List<Subscripcion> perms = rightPanel.getSelectedAndRemoveThem();
		leftPanel.addList(perms);
		
		int sumaAAbonar=0;
		for (Subscripcion subscripcion : rightPanel.getList()) {
			sumaAAbonar=sumaAAbonar+obtenerDeuda(subscripcion);
		}
		
		if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() == null){
			getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(sumaAAbonar));

		}else{
			if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() != null
					&& getSaldarDeudaCrud().getDeudaaSaldar().getValue().equalsIgnoreCase("0")){
				getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(sumaAAbonar));
				
			}else{

				getSaldarDeudaCrud().getDeudaaSaldar().setValue(String.valueOf(sumaAAbonar));
			}
		}
		
		int debeTOT=Integer.parseInt(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
		if(debeTOT == 0){
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(0));
			crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(0));
			crudFormasDePago.getGridList().removeAll();

		}else{
			crudFormasDePago.getGridList().removeAll();
			crudFormasDePago.getValorSumadoPesos().setValue(String.valueOf(0));
			crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(debeTOT));
		}
	}
	
	public void onMoveAllLeft(Event event) throws Exception {
		rightPanel.selectAllItems();
		this.onMoveLeft(event);
	}
	
	private int getDeuda(Cliente cli){
		int cantDeuda= 0;
		if(cli.getSubscripcionesList() != null){
			for (Subscripcion subs : cli.getSubscripcionesList()) {
				
				if(subs.getCupoActividadList() != null ){
					for (CupoActividad iterable_element : subs.getCupoActividadList()) {
						if(iterable_element.getEstado() != null && 
								iterable_element.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
								((iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
									||iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()
									||(iterable_element.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
							int pagosRealizados=0;
							for (PagosPorSubscripcion pagoPorSubscripcion: subs.getPagosPorSubscripcionList()) {
								if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
									pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
									pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
								}else 	if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
									pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
									pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getAdicional();
								}else{
									pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
								}		
							}
							
							if(pagosRealizados < subs.getPrecioTOTALadicionalTarjeta() ){
								cantDeuda =cantDeuda + (subs.getPrecioTOTALadicionalTarjeta() - pagosRealizados);
								
							}
							break;
						}				
						
					}
				}
			}
		}
		return cantDeuda;		
	}
	
	
	private void fillListBoxProducts(Subscripcion subscripcion) {
		Set<PagosPorSubscripcion> result = new HashSet<PagosPorSubscripcion>();
		if (subscripcion != null) {
			//float importeFinal= obtenerImporteFinalDeLaLista(null);
			int importeFinal= 0;
			for (PagosPorSubscripcion dpp : subscripcion.getPagosPorSubscripcionList()) {
				result.add(dpp);
			}
			
			if(subscripcion.getPagosPorSubscripcionList() != null && subscripcion.getPagosPorSubscripcionList().size() ==0){
				// actualizo el rojo y el verde de la plata, no se hizo ningun pago aca.
				
				crudFormasDePago.getValorFaltantePesos().setValue(String.valueOf(importeFinal));
				crudFormasDePago.getValorSumadoPesos().setValue("0");
			}
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
		}		
	}
	
	public void onSave(Event event) throws Exception {
		try{
			
			if(!validateCrud()){
				MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
				return;
			}

			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					pagoPorSubscripcion.setSaldadaDeuda(true);
				}
			}		
			
			
			if(rightPanel.getList() != null){

				for (Subscripcion subs : rightPanel.getList()) {
					int deuda=obtenerDeuda(subs);

					this.fromViewToModel(subs);

					subs.setIdUsuarioSaldaSubscripcion(Session.getUsernameID().intValue());
					subs.setFechaYHoraSaldaSubscripcion(new Date());
					
					if(subs.getCupoActividadList() != null ){
						for (CupoActividad iterable_element : subs.getCupoActividadList()) {
							
							if(iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
									|| iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
								
								if(iterable_element.getCupos() > 0)
									iterable_element.setEstado(CupoActividadEstadoEnum.C_CUPOS);
								else
									iterable_element.setEstado(CupoActividadEstadoEnum.S_CUPOS);
					
							}else if(iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()){
								iterable_element.setEstado(CupoActividadEstadoEnum.VENCIDA);
							}				
						}
					}
			
					subs=clienteEJB.actualizarSubscripcion(subs, null);
					if(subs.getCliente().getSubscripcionesList() != null){
						for (Subscripcion subs2 : subs.getCliente().getSubscripcionesList()) {
							if(subs2.getId().intValue() == subs.getId().intValue()){
								if(subs2.getCupoActividadList() != null ){
									for (CupoActividad iterable_element2 : subs2.getCupoActividadList()) {
							
										if(subs.getCupoActividadList() != null ){
											for (CupoActividad iterable_element : subs.getCupoActividadList()) {
												if(iterable_element2.getCurso().getId().intValue() == iterable_element.getCurso().getId().intValue()){
													iterable_element2.setEstado(iterable_element.getEstado());

												}
												
											}
										}
									}
								}
							}
						}
					}
					
					subs.setCliente(subs.getCliente());
					setCliente(subs.getCliente());
					
					if(subs.getPagosPorSubscripcionList() != null){
						for (PagosPorSubscripcion subscripcionDeClases : subs.getPagosPorSubscripcionList() ) {
							subscripcionDeClases.setSucursal(getSucursal(subs));	
							
							// falta actualizar la quincena
							
							if(subscripcionDeClases.getSucursal() != null  &&
									subscripcionDeClases.getSucursal().toInt() == SucursalEnum.MAIPU.toInt()){
								Quincena quin=obtenerQuincenaDeConceptos(subs);
								if(quin != null){
									subscripcionDeClases.setQuincena(quin);	
								}
							}
						}
					}
					// se guarda en la BD
					subscripcionEJB.save(subs);
					
					// Guardo el movimiento en la caja ahora
					CajaMovimiento caja= new CajaMovimiento();
					
					String cursos=obtenerCursosContratados(subs);

					// si tiene matricula sumar al concepto matricula
					caja.setConcepto("Deuda Saldada: Inscripcion Cursos "+cursos);
					caja.setFecha(subs.getFechaYHoraSaldaSubscripcion());
					caja.setIdUsuarioGeneroMovimiento(subs.getIdUsuarioSaldaSubscripcion());
					caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
					caja.setValor(Double.valueOf(deuda)  );
					caja.setSucursal(getSucursal(subs));
					
					caja.setCliente(subs.getCliente());
					caja.setIdSubscripcion(subs.getId().intValue());

					cajaEJB.save(caja);
				}
				
				List<Subscripcion> listSubs=subscripcionEJB.findAllSubscripcionesByClient(getCliente().getId());
				if(listSubs != null)
					getCliente().setSubscripcionesList(new HashSet(listSubs));
				
				clienteEJB.save(getCliente());	
				if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
					Sessions.getCurrent().setAttribute("isFromCumples", null);

					super.gotoPage("/institucion/cumples-selector.zul");
				}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
					Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
					super.gotoPage("/institucion/deuda-cliente-selector.zul");
				}else{
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
				}
			}
		
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
	
	private Quincena obtenerQuincenaDeConceptos(Subscripcion subs){
		if(subs != null && subs.getConceptoList() != null){
			
			for (Concepto iterable_element : subs.getConceptoList()) {
				if(iterable_element.getQuincena() != null)
					return iterable_element.getQuincena(); 
			}
		}
	return null;	
	}
	
	private SucursalEnum getSucursal(Subscripcion subs){
		SucursalEnum suc= null;
		boolean esDeMaipu= false;
		if(subs != null && subs.getConceptoList() != null && subs.getConceptoList().size() > 0){
			
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getCurso() != null && iterable_element.getCurso().getNombre() != null  
						&& (iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-TRANSPORTE")  
								|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO-CANTINA")
								|| iterable_element.getCurso().getNombre().equalsIgnoreCase("INI VERANO"))){
					esDeMaipu= true;
				}
			}		
		}
		if(esDeMaipu)
			suc= SucursalEnum.MAIPU;
		else
			suc= SucursalEnum.CENTRO;
		return suc;
	}
	
	private String obtenerCursosContratados(Subscripcion subscripcion){
		String cursos="";
		if(subscripcion.getConceptoList() != null){
			for (Concepto concep : subscripcion.getConceptoList()) {
				if(concep.getCurso() != null){
					cursos= cursos+ concep.getCurso().getNombre().toUpperCase();
				}
			}
		}
		return cursos;
	}
	
	public void onBack(Event event) {
		 

		if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
			Sessions.getCurrent().setAttribute("isFromCumples", null);
			super.gotoPage("/institucion/cumples-selector.zul");
		}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
			Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
			super.gotoPage("/institucion/deuda-cliente-selector.zul");
		}else{
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");	
		}
	}

	private Subscripcion fromViewToModel(Subscripcion subs) {
		
		// tengo que saber primero cuanto debo 
		int deuda=obtenerDeuda(subs);
		
		// obtengo la plata de la deuda, de todos los pagos que se hicieron
		if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
			
			List <PagosPorSubscripcion> pagosNuevos = new ArrayList<PagosPorSubscripcion>();
			
			for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
				
				if(pagoPorSubscripcion.getCantidadDinero() > deuda){
					// quiere decir que saco ya todo el dinero de este pago.
					PagosPorSubscripcion pagoNuevo= new PagosPorSubscripcion();
					pagoNuevo.setEsCopago(false);
					pagoNuevo.setCantidadDinero(deuda);
					pagoNuevo.setIdTipoDePago(pagoPorSubscripcion.getIdTipoDePago());
					pagoNuevo.setSubscripcion(pagoPorSubscripcion.getSubscripcion());
					pagoNuevo.setSaldadaDeuda(true);
					pagoNuevo.setPorcInteres(pagoPorSubscripcion.getPorcInteres());
					pagoNuevo.setSucursal(getSucursal(subs));
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 15 )/100;
		   				}
		   				
						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						pagoPorSubscripcion.setEsCopago(false);
						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 15 )/100;
		   				}
		   				pagoPorSubscripcion.setAdicional(adicional);
					}else 					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						
						int adicionalparte=0;
		   				if(pagoNuevo.getPorcInteres() != null){
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * pagoNuevo.getPorcInteres() )/100;
		   				}else{
		   					adicionalparte=(int)(pagoNuevo.getCantidadDinero() * 20 )/100;
		   				}
		   				
						pagoNuevo.setAdicional(adicionalparte);
						pagoNuevo.setCantCuotas(pagoPorSubscripcion.getCantCuotas());
						pagoPorSubscripcion.setCantidadDinero(pagoPorSubscripcion.getCantidadDinero()- deuda);
						pagoPorSubscripcion.setEsCopago(false);

						int adicional=0;
		   				if(pagoPorSubscripcion.getPorcInteres() != null){
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * pagoPorSubscripcion.getPorcInteres() )/100;
		   				}else{
		   					adicional=(int)(pagoPorSubscripcion.getCantidadDinero() * 20 )/100;
		   				}
		   				pagoPorSubscripcion.setAdicional(adicional);
					}

					pagosNuevos.add(pagoNuevo);
					break;
				}else if(pagoPorSubscripcion.getCantidadDinero() == deuda){
					pagosNuevos.add(pagoPorSubscripcion);
					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
					break;
				}else{
					// quiere decir que saco el dinero de el pago anterior + un nuevo pago.
					pagosNuevos.add(pagoPorSubscripcion);
					deuda = deuda-pagoPorSubscripcion.getCantidadDinero(); // - pagoPorSubscripcion.getAdicional();
					crudFormasDePago.getProducts().remove(pagoPorSubscripcion);
				}
			}
		
			if(pagosNuevos != null && pagosNuevos.size() >0){
				if(subs.getPagosPorSubscripcionList() == null)
					subs.setPagosPorSubscripcionList(new HashSet(pagosNuevos));
				else{
					subs.getPagosPorSubscripcionList().addAll(new HashSet(pagosNuevos));
				}	
			}
			int adic=obtenerAdicionales(subs);
			subs.setValorAdicionaltarjeta(adic);
			subs.setPrecioTOTALadicionalTarjeta(subs.getPrecioCursosMatricula() +adic );
		}		
		return subs;
	}
	
	private int obtenerAdicionales(Subscripcion subs){
		int adic=0;
		if(subs.getPagosPorSubscripcionList() != null){
			for (PagosPorSubscripcion pagoPorSubscripcion : subs.getPagosPorSubscripcionList()) {
				
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					adic= adic+ pagoPorSubscripcion.getAdicional();
				}else 				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					adic= adic+ pagoPorSubscripcion.getAdicional();
				}		
			}
			
		}
		return adic;	
	}

	private boolean validateCrud() {
		
		// tiene que haber seleccionado alguna deuda a pagar.
		if(rightPanel == null || 
				(rightPanel != null && rightPanel.getList() != null && rightPanel.getList().size() == 0))
			throw new WrongValueException(rightPanel, I18N.getLabel("error.empty.field"));

		// la deuda a pagar tiene que ser > a 0
		if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() == null 
				|| (getSaldarDeudaCrud().getDeudaaSaldar().getValue() != null && 
						getSaldarDeudaCrud().getDeudaaSaldar().getValue().equalsIgnoreCase("0")))
			throw new WrongValueException(getSaldarDeudaCrud().getDeudaaSaldar(), I18N.getLabel("error.empty.field.deuda.a.pagar"));
				
		// tiene que tener pagos en la lista de pagos
		if(crudFormasDePago.getProducts() == null 
				||  (crudFormasDePago.getProducts() != null && 
						crudFormasDePago.getProducts().size() <= 0)){
			throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field"));
		}
		
		// la lista de pagos debe de ser igual a la deuda.
		if(crudFormasDePago.getProducts() != null && 
				crudFormasDePago.getProducts().size() > 0){
			// 
			int deudaAPagarSeleccionada=Integer.parseInt(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
			int pagosRealizados=0;
			int adicionales=0;
			if(crudFormasDePago.getProducts() != null && crudFormasDePago.getProducts().size() > 0){
				for (PagosPorSubscripcion pagoPorSubscripcion: crudFormasDePago.getProducts()) {
					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else 					if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
						adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
					}else{
						pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					}		
				}
			}
			if(deudaAPagarSeleccionada != pagosRealizados)
				throw new WrongValueException(crudFormasDePago, I18N.getLabel("error.empty.field.pago.igual"));
		}
		
		return true;
	}


	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int getValorCurso() {
		if(getSaldarDeudaCrud().getDeudaaSaldar().getValue() != null){
			return Integer.valueOf(getSaldarDeudaCrud().getDeudaaSaldar().getValue());
		}
		return 0;
	}
}

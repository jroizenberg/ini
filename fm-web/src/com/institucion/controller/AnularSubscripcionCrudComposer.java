package com.institucion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listitem;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClaseEJB;
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
import com.institucion.model.ActividadYClase;
import com.institucion.model.AnularSubscripcionCrud;
import com.institucion.model.AnularSubscripcionCrud5;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Clase;
import com.institucion.model.Cliente;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.CursoListSeleccionados;
import com.institucion.model.FormasDePagoCrud;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
import com.institucion.model.Inscripcion;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Subscripcion;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoDescuentosEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class AnularSubscripcionCrudComposer extends CrudComposer implements AnularSubscripcionDelegate{

	public final String idSubs = "idSubscripcion";
	private ClienteEJB clienteEJB;
	private InscripcionEJB subscripcionEJB;
	
	// List de los cursos pre-seleccionados
	private CursoListSeleccionados cursoseleccionadoListGrid;
	// crud formas de pago
	private FormasDePagoCrud crudFormasDePago;
		
	// crud del NOMBRE
	private PanelCrud crud;
	private PanelCrud crud2;
	private Subscripcion subscripcion;
	private Cliente cliente;
	private CajaEJB cajaEJB;
	private ClaseEJB claseEJB;

	public AnularSubscripcionCrudComposer() {
		subscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");

	}

	private AnularSubscripcionCrud getSubscripcionCrud() {
		return (AnularSubscripcionCrud)(crud.getGridCrud());
	}
  
	private AnularSubscripcionCrud5 getSubscripcionCrud2() {
		return (AnularSubscripcionCrud5)(crud2.getGridCrud());
	}
		
	public void onAbonar(Event event) throws Exception {
		pagarCurso();		
	}
	
	public void onCreate() {
		Subscripcion clase= (Subscripcion) Session.getAttribute(idSubs);
		setSubscripcion(clase);	
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		setCliente(cliente);	

		getSubscripcionCrud().getCliente().setValue(cliente.getApellido().toUpperCase() + " "+ cliente.getNombre().toUpperCase());
		
//		if(clase.getEstado() != null)
//			getSubscripcionCrud().getEstado().setValue(clase.getEstado().toString());
//				
		getSubscripcionCrud2().setDelegate(this);
		
		this.fromModelToView();	
	}
	
	private void fromModelToView() {
		if(subscripcion != null){
			subscripcion=clienteEJB.loadLazy(subscripcion, true, true, true, true, true);
			crudFormasDePago.getGridCrud().setVisible(false);
			// dehsbilitar combo aplica descuento  dehsbilitar boton abonar y pagar
			crudFormasDePago.setSubsAnulardelegate(this);
			crudFormasDePago.getGridList().setDisabled(true);
			crudFormasDePago.getPagoRapido().setDisabled(true);
			crudFormasDePago.setButtonsVisible(false);
			// llenar aca todos los 
			fillListBoxConceptos(subscripcion);
			fillListBoxProducts(subscripcion);
			
			String totalAbonado=getSubscripcionCrud().getTotalAbonado().getValue();
			if(totalAbonado != null && !totalAbonado.equalsIgnoreCase("")){
				Integer totalAbonadoa=Integer.parseInt(totalAbonado);
				getSubscripcionCrud2().getTipoDevoluciones().setValue(new Double(totalAbonadoa));
			}else{
				getSubscripcionCrud2().getTipoDevoluciones().setValue(new Double(0));
			}
		}			
	}
	
	private void fillListBoxConceptos(Subscripcion subscripcion) {
		if (subscripcion != null) {
		
			for (Concepto dpp : subscripcion.getConceptoList()) {
				cursoseleccionadoListGrid.setList(dpp, false);
			}
		}
	}
	
	private void fillListBoxProducts(Subscripcion subscripcion) {
		Set<PagosPorSubscripcion> result = new HashSet<PagosPorSubscripcion>();
		if (subscripcion != null) {
			int importeFinal=obtenerImporteFinalDeLaLista(null);

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
					}else 	if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
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
				getSubscripcionCrud().getTotalAbonado().setValue(String.valueOf(sumaTotPagos));
				getSubscripcionCrud().getDeuda().setValue(String.valueOf(faltante));
				crudFormasDePago.getProductCrudGrid().setVisible(false);			
			}else{
				getSubscripcionCrud().getTotalAbonado().setValue(String.valueOf(0));
				
				getSubscripcionCrud().getDeuda().setValue(String.valueOf(importeFinal));
				
			}
		}		
	}
	
	public int obtenerImporteFinalDeLaLista(Concepto concepto){
		int importeTotal= 0;
		Iterator<Listitem> itCursos =cursoseleccionadoListGrid.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Concepto cursoSeleccionado=(Concepto)cursosDeLaLista.getValue();
			
			importeTotal=cursoSeleccionado.getImporteConDescuento()+importeTotal;
		}
		if(concepto != null)
			importeTotal= importeTotal + concepto.getImporteConDescuento();
	
		return importeTotal;			
	}
	
	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idSubs) != null) {
				// es una modificacion
				
				this.fromViewToModel(subscripcion);
				if(!validateCrud()){
					MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				subscripcion.setAnulaDevuelveDinero(true);
				if(subscripcion.isAnulaDevuelveDinero()!= null &&  subscripcion.isAnulaDevuelveDinero()){
					subscripcion=clienteEJB.loadLazy(subscripcion, true, false, false, false, false);
					if(subscripcion.getCupoActividadList() != null){
						for (CupoActividad iterable_element : subscripcion.getCupoActividadList()) {
							iterable_element.setEstado(CupoActividadEstadoEnum.ANULADA);
						}
					}					
				}	
				
				boolean tieneMatricula=tieneMatriculaAbonada(subscripcion);
				if(tieneMatricula){
					if (MessageBox.question(I18N.getLabel("subscripcion.list.title.anular.conmatricula"), 
							I18N.getLabel("subscripcion.list.title.anular.title"))){
					
					}else{
						MessageBox.info(I18N.getLabel("curso.error.seleccion.select.volverAtras"), I18N.getLabel("selector.actionwithoutitem.title"));
						if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
							Sessions.getCurrent().setAttribute("isFromCumples", null);
							super.gotoPage("/institucion/cumples-selector.zul");
							
						}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
							Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
							super.gotoPage("/institucion/deuda-cliente-selector.zul");
						}else	
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						return ;
					}
					
				}else if(!tieneMatricula){
					if (MessageBox.question(I18N.getLabel("subscripcion.list.title.anular.sinmatricula"), 
							I18N.getLabel("subscripcion.list.title.anular.title"))){
						
					}else{
						MessageBox.info(I18N.getLabel("curso.error.seleccion.select.volverAtras"), I18N.getLabel("selector.actionwithoutitem.title"));
						if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
							Sessions.getCurrent().setAttribute("isFromCumples", null);
							super.gotoPage("/institucion/cumples-selector.zul");
							
						}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
							Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
							super.gotoPage("/institucion/deuda-cliente-selector.zul");
						}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
							Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
							super.gotoPage("/institucion/deuda-cliente-selector.zul");
						}else	
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						return ;
					}
				}
				
				Inscripcion matriculaInscripcion= null;
				if(tieneMatricula){
					subscripcion.setCliente(clienteEJB.loadLazy(subscripcion.getCliente(), false, false, false, true));
					// Si se anula la inscripcion de matricula, la deberia eliminar , para que aparezca en la proxima.
					if(subscripcion.getCliente().getInscripcionesList() != null){
						 Set<Inscripcion> nueva= new HashSet<Inscripcion>();
						for (Inscripcion inst : subscripcion.getCliente().getInscripcionesList()) {
							if(inst.getFecha().getYear() == subscripcion.getFechaIngresoSubscripcion().getYear()){
								matriculaInscripcion= inst;
							}else{
								nueva.add(inst);
							}	
						}
						
						subscripcion.getCliente().getInscripcionesList().clear();
						subscripcion.getCliente().setInscripcionesList(nueva);
					}
				}
				subscripcion.setIdUsuarioAnuloSubscripcion(Session.getUsernameID().intValue());
				subscripcion.setFechaYHoraAnulacion(new Date());
				
				if(subscripcion.getPagosPorSubscripcionList() != null){
					for (PagosPorSubscripcion subscripcionDeClases : subscripcion.getPagosPorSubscripcionList() ) {
						subscripcionDeClases.setSucursal(getSucursal(subscripcion));	
					}
				}
				subscripcion=clienteEJB.actualizarSubscripcion(subscripcion, null);
				
				// si estuvo relacionada la obra social, se debe anular tb de la caja la obra social.
				if(subscripcion.getConceptoList() != null){
					for (Concepto concep : subscripcion.getConceptoList()) {
						if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
						
							CajaMovimiento caja2= new CajaMovimiento();
							String cursos2=concep.getCurso().getNombre().toUpperCase();
							// si tiene matricula sumar al concepto matricula
							caja2.setConcepto("Anulacion de Obra Social "+ concep.getObraSocial().getNombre().toUpperCase() + " : "+cursos2);
							
							caja2.setFecha(subscripcion.getFechaYHoraCreacion());
							caja2.setIdUsuarioGeneroMovimiento(subscripcion.getIdUsuarioCreoSubscripcion());
							caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
							double valor=concep.getCantSesiones() * concep.getPrecioPorClaseOSesionPagaObraSocial();
							caja2.setValor(valor);
							
							caja2.setSucursal(getSucursal(subscripcion));
							
							caja2.setCliente(subscripcion.getCliente());
							caja2.setIdSubscripcion(subscripcion.getId().intValue());

							cajaEJB.save(caja2);						
						}
					}
				}
				
				subscripcion.setCliente(subscripcion.getCliente());
				setCliente(subscripcion.getCliente());
				
				// se guarda en la BD
				subscripcionEJB.save(subscripcion);
				
				// Guardo el movimiento en la caja ahora
				CajaMovimiento caja= new CajaMovimiento();
				
				String cursos=obtenerCursosContratados(subscripcion);
				// si tiene matricula sumar al concepto matricula
				if(tieneMatricula)
					caja.setConcepto("Anulacion: Matricula + Inscripcion Cursos: "+cursos+ ". "+subscripcion.getAnulaComentario());
				else
					caja.setConcepto("Anulacion: Inscripcion Cursos: "+cursos+". "+subscripcion.getAnulaComentario());
				
				caja.setFecha(subscripcion.getFechaYHoraAnulacion());
				caja.setIdUsuarioGeneroMovimiento(subscripcion.getIdUsuarioAnuloSubscripcion());
				caja.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
				caja.setValor(new Double(subscripcion.getAnulaValorDevuelvo()));
				caja.setSucursal(getSucursal(subscripcion));
				caja.setCliente(subscripcion.getCliente());
				caja.setIdSubscripcion(subscripcion.getId().intValue());
				cajaEJB.save(caja);
	
				List<Subscripcion> listSubs=subscripcionEJB.findAllSubscripcionesByClient(subscripcion.getCliente().getId());
				if(listSubs != null)
					getCliente().setSubscripcionesList(new HashSet(listSubs));
				
				clienteEJB.save(getCliente());
				
				List<Clase> listaClase=claseEJB.findAll();
				List<Clase>  nuevoListado= new ArrayList();
				// obtengo la actividad 
				if(subscripcion.getConceptoList() != null){
					for (Concepto concept: subscripcion.getConceptoList()) {
						if(concept.getCurso() != null){
							if(concept.getCurso().getActividadYClaseList() != null){
								for (ActividadYClase actyClase : concept.getCurso().getActividadYClaseList()) {
									if(actyClase  != null){
										for (Clase clase2 : listaClase) {
											if(isActividadDeClase(actyClase.getActiv().getId().intValue(), clase2)){
//												si la clase no existe
												if(!claseExiste(nuevoListado, clase2))
													nuevoListado.add(clase2);
											}	
										}
									}
								}
							}
						}
					}
				}
				

				//Elimina al cliente de la clase
				for (Clase clase2 : nuevoListado) {
					HashSet list= new HashSet();
					boolean encontroCliEnClase= false;
					clase2=clienteEJB.loadLazy(clase2, true, true, false, false);
					for (Cliente clies: clase2.getClienesEnClase()) {
						if(clies.getId().intValue() == subscripcion.getCliente().getId().intValue()){
							encontroCliEnClase= true;
						}else
							list.add(clies);
					}
					clase2.setClienesEnClase(list);
					
					HashSet<IngresoAClasesSinFechasAlumnos> listIngresos= new HashSet();
					HashSet<IngresoAClasesSinFechasAlumnos> listIngresosaEliminar= new HashSet();

					if(clase2.getIngresoAClaseSinFechas() != null){
						for (IngresoAClasesSinFechasAlumnos ingresos: clase2.getIngresoAClaseSinFechas()) {
							if(ingresos.getSubscripcion().getId().intValue() ==  subscripcion.getId().intValue()
									&& ingresos.getCliente().getId().intValue() ==  subscripcion.getCliente().getId().intValue()){
								encontroCliEnClase= true;
								listIngresosaEliminar.add(ingresos);
							}else
								listIngresos.add(ingresos);
						}			
					}
					
					if(encontroCliEnClase){
						for (IngresoAClasesSinFechasAlumnos object : listIngresosaEliminar) {
							claseEJB.delete(object);	
						}
					}
					clase2.setIngresoAClaseSinFechas(listIngresos);
					
					if(encontroCliEnClase)
						claseEJB.save(clase2);
				}
				
				
				if(tieneMatricula && matriculaInscripcion != null){
					subscripcionEJB.delete(matriculaInscripcion);
				}

				Object[] params2 = {getCliente().getNombre().toUpperCase() + " "+ getCliente().getApellido().toUpperCase(), subscripcion.getAnulaValorDevuelvo() };

				MessageBox.info(I18N.getLabel("curso.anular.exitoso", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
				
				if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
					Sessions.getCurrent().setAttribute("isFromCumples", null);
					super.gotoPage("/institucion/cumples-selector.zul");
				}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
					Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
					super.gotoPage("/institucion/deuda-cliente-selector.zul");
				}else	
					super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
			} 
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
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
	
	private boolean claseExiste(List<Clase>listaClase,Clase clase2){
		if(listaClase != null ){
			for (Clase clase : listaClase) {
				if(clase.getId().intValue() == clase2.getId().intValue())
					return true;
			}
		}
		return false;
	}

	
	private boolean isActividadDeClase(int actividadID, Clase clase){
		if(clase != null){
			
			if(clase.getActividad().getId().intValue() == actividadID)
				return true;
		}
		return false;		
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
	
	private boolean tieneMatriculaAbonada(Subscripcion subs){
		boolean tieneMatricula=false;
		
		if(subs != null && subs.getConceptoList() != null){
			for (Concepto concept: subs.getConceptoList() ) {
				if(concept.getCurso() == null)
					return true;
			}		
		}
		return tieneMatricula;
	}
	
	public void onBack(Event event) {
		if(com.institucion.fm.conf.Session.getAttribute("isFromCumples") != null){
			Sessions.getCurrent().setAttribute("isFromCumples", null);
			super.gotoPage("/institucion/cumples-selector.zul");
		}else if(com.institucion.fm.conf.Session.getAttribute("isSubsFromDeudasClientes") != null){	
			Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", null);
			super.gotoPage("/institucion/deuda-cliente-selector.zul");
		}else
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	}

	private Subscripcion fromViewToModel(Subscripcion subs) {
		
		if(getSubscripcionCrud2().getComentarioCb().getValue() != null)
			subs.setAnulaComentario((String)getSubscripcionCrud2().getComentarioCb().getValue());

		if(getSubscripcionCrud2().getTipoDevoluciones().getValue() != null){
				Double aa=(Double)getSubscripcionCrud2().getTipoDevoluciones().getValue();
				subs.setAnulaValorDevuelvo(aa.intValue());
		}
		return subs;
	}
	
	private boolean validateCrud() {
		
			if(getSubscripcionCrud2().getTipoDevoluciones().getValue() == null)
				throw new WrongValueException(getSubscripcionCrud2().getTipoDevoluciones(), I18N.getLabel("error.empty.field"));
			
			if(getSubscripcionCrud2().getTipoDevoluciones().getValue() != null){
				// verificar que sea menor a lo que se pago realmente
				Double dd=(Double)getSubscripcionCrud2().getTipoDevoluciones().getValue();
				Integer floADevolver=dd.intValue();
				String totalAbonado=getSubscripcionCrud().getTotalAbonado().getValue();
				if(totalAbonado != null){
					Integer totalAbonadoa=Integer.parseInt(totalAbonado);
					if(floADevolver != null && totalAbonadoa != null ){
						if(floADevolver > totalAbonadoa)
							throw new WrongValueException(getSubscripcionCrud2().getTipoDevoluciones(), I18N.getLabel("error.empty.field.dinero.abonado"));						
					}
				}
			}
//		}
		return true;
	}
			
	public Subscripcion getSubscripcion() {
		return subscripcion;
	}

	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public int getValorCurso() {
		return obtenerImporteFinalDeLaLista(null);
	}

	@Override
	public boolean seSeleccionoElCurso() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pagarCurso() {
		// TODO Auto-generated method stub
		
	}
}

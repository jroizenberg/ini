package com.institucion.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
import com.institucion.model.ActividadYClase;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Cliente;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.CursoListSeleccionados;
import com.institucion.model.PosponerSubscripcionCrud;
import com.institucion.model.PosponerSubscripcionCrud5;
import com.institucion.model.Subscripcion;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class PosponerSubscripcionCrudComposer extends CrudComposer implements AnularSubscripcionDelegate{

	public final String idSubs = "idSubscripcion";
	private ClienteEJB clienteEJB;
	private InscripcionEJB subscripcionEJB;
	
	// List de los cursos pre-seleccionados
	private CursoListSeleccionados cursoseleccionadoListGrid;
	
	// crud del NOMBRE
	private PanelCrud crud;
	private PanelCrud crud2;
	private Subscripcion subscripcion;
	private Cliente cliente;
	private CajaEJB cajaEJB;

	public PosponerSubscripcionCrudComposer() {
		subscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");

	}

	private PosponerSubscripcionCrud getSubscripcionCrud() {
		return (PosponerSubscripcionCrud)(crud.getGridCrud());
	}
  
	private PosponerSubscripcionCrud5 getSubscripcionCrud2() {
		return (PosponerSubscripcionCrud5)(crud2.getGridCrud());
	}
		
	public void onCreate() {
		Subscripcion clase= (Subscripcion) Session.getAttribute(idSubs);
		setSubscripcion(clase);	
		Cliente cliente= (Cliente) Session.getAttribute("idCliente");
		setCliente(cliente);	

		getSubscripcionCrud().getCliente().setValue(cliente.getApellido().toUpperCase() + " "+ cliente.getNombre().toUpperCase());

		getSubscripcionCrud2().setDelegate(this);
		
		this.fromModelToView();	
	}
	
	private void fromModelToView() {
		if(subscripcion != null){		
			fillListBoxConceptos(subscripcion);
			
		}			
	}
	
	private void fillListBoxConceptos(Subscripcion subscripcion) {
		if (subscripcion != null) {
			ActividadYClase actYClase= (ActividadYClase)com.institucion.fm.conf.Session.getAttribute("actYClasePosponer");

			for (Concepto dpp : subscripcion.getConceptoList()) {
				if(dpp.getCurso() != null && dpp.getCurso().getId() != null   
						&& dpp.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
					cursoseleccionadoListGrid.setList(dpp, false);
			}
		}
	}
	

	public void onSave(Event event) throws Exception {
		try{
			Date  fechaFinPospo=new Date();
			ActividadYClase actYClase= (ActividadYClase)com.institucion.fm.conf.Session.getAttribute("actYClasePosponer");
			
			if (Sessions.getCurrent().getAttribute(idSubs) != null) {
				// es una modificacion
				if(!validateCrud()){
					MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				
				this.fromViewToModel(subscripcion, actYClase);
				
				//cuando se pospone 1 mes, se pospone 1 mes a partir de la fecha de hoy, NO de la fecha de la inscirpcion.
				Integer mesesPospone=(Integer)getSubscripcionCrud2().getComboMesesPospone().getSelectedItem().getValue();
				if(mesesPospone != null){
					
					Calendar ahoraCal= null;
					String fehchaIngreso= null;
					// si es mayor a 20 es por que esta posponiendo semanaas 
					if(mesesPospone > 20){
						int aa= mesesPospone -20;
						ahoraCal= Calendar.getInstance();
						ahoraCal.setTime(fechaFinPospo);
						ahoraCal.add(Calendar.WEEK_OF_MONTH , aa);
//						ahoraCal.get(Calendar.DATE)+"/"+ahoraCal.get(Calendar.MONTH)+"/"+ahoraCal.get(Calendar.YEAR);
						int mes=ahoraCal.get(Calendar.MONTH)+ 1;
						fehchaIngreso=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
						
						
					}else{
						ahoraCal= Calendar.getInstance();
						ahoraCal.setTime(fechaFinPospo);
						ahoraCal.add(Calendar.MONTH, mesesPospone); 
						int mes=ahoraCal.get(Calendar.MONTH)+ 1;
						fehchaIngreso=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					}
					

					Object[] params2 = {actYClase.getCurso().getNombre() , fehchaIngreso };
					
					if (MessageBox.question(I18N.getLabel("subscripcion.list.title.posponer.nuevo.vencimiento", params2), 
							I18N.getLabel("subscripcion.list.title.posponer.title"))){
						
						boolean pagaAdicional= false;
						double valor=0;
						
						// se
						CupoActividad cupo= null;
						if(subscripcion.getCupoActividadList() != null ){
							for (CupoActividad iterable_element : subscripcion.getCupoActividadList()) {
								if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
									cupo= iterable_element;
								
							}
						}
						
						
						
						if(cupo.getPosponeComentario2() != null){
							// significa que es la 2 vez que pospone
							if(cupo.getPosponePagaAdicional2() != null){
								pagaAdicional= cupo.getPosponePagaAdicional2();
								valor= cupo.getPosponeAdicional2();	
							}
							
						}else{
							if(cupo.getPosponePagaAdicional() != null){
								pagaAdicional= cupo.getPosponePagaAdicional();
								valor= cupo.getPosponeAdicional();
		
							}
						}
						
				
						if(pagaAdicional && valor > 0){
							
							Object[] params = {subscripcion.getCliente().getNombre().toUpperCase() +" "+ subscripcion.getCliente().getApellido().toUpperCase(), valor};
	
							if (MessageBox.question(I18N.getLabel("subscripcion.list.title.posponer.abonar.adicional", params), 
									I18N.getLabel("subscripcion.list.title.posponer.title"))){
							
								if(cupo.getEstado() != null 
										&& cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() ){
										
										if(cupo.getCupos() > 0)
											cupo.setEstado(CupoActividadEstadoEnum.C_CUPOS);
										else
											cupo.setEstado(CupoActividadEstadoEnum.S_CUPOS);
										
//										Verificar si SIEMPRE le pongo con CUPOS, que pasa cuando la subs no tiene cupos.???
										
										
									}else if (cupo.getEstado() != null &&
											cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt() ){
										
										if(cupo.getCupos() > 0)
											cupo.setEstado(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);
										else
											cupo.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
										
									}						
									actualizarFechaVencimiento(subscripcion, ahoraCal.getTime(), actYClase );
									subscripcion=clienteEJB.actualizarSubscripcion(subscripcion, actYClase);
									
									if(subscripcion.getCliente().getSubscripcionesList() != null){
										for (Subscripcion subs2 : subscripcion.getCliente().getSubscripcionesList()) {
											if(subs2.getId().intValue() == subscripcion.getId().intValue()){
												
												if(subs2.getCupoActividadList() != null ){
													for (CupoActividad iterable_element : subs2.getCupoActividadList()) {
														
														if(subscripcion.getCupoActividadList() != null ){
															for (CupoActividad cupos : subscripcion.getCupoActividadList()) {
																if(cupos.getCurso().getId().intValue() == iterable_element.getCurso().getId().intValue())	
																	iterable_element.setEstado(cupos.getEstado());
															}	
														}		
													}	
												}		
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
									caja.setConcepto("Pospone Inscripcion- Cursos: "+cursos + ". ");
									caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
									caja.setValor(valor);
									caja.setSucursal(getSucursal(subscripcion));
									caja.setCliente(subscripcion.getCliente());
									caja.setIdSubscripcion(subscripcion.getId().intValue());

									if(cupo.getIdUsuarioPosponeSubscripcion() != null){
										caja.setFecha(cupo.getPosponefechaYHora());
										caja.setIdUsuarioGeneroMovimiento(cupo.getIdUsuarioPosponeSubscripcion());
									}else{
										caja.setFecha(cupo.getPosponefechaYHora2());
										caja.setIdUsuarioGeneroMovimiento(cupo.getIdUsuarioPosponeSubscripcion2());
									} 
									
									cajaEJB.save(caja);
									
									List<Subscripcion> listSubs=subscripcionEJB.findAllSubscripcionesByClient(subscripcion.getCliente().getId());
									if(listSubs != null)
										getCliente().setSubscripcionesList(new HashSet(listSubs));
									
									clienteEJB.save(getCliente());
										
									super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
								
							}else{
								super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
								return ;
							}
							
						}else{

							if(cupo.getEstado() != null 
									&& cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() ){
									
									if(cupo.getCupos() > 0)
										cupo.setEstado(CupoActividadEstadoEnum.C_CUPOS);
									else
										cupo.setEstado(CupoActividadEstadoEnum.S_CUPOS);
									
//									Verificar si SIEMPRE le pongo con CUPOS, que pasa cuando la subs no tiene cupos.???
									
									
								}else if (cupo.getEstado() != null &&
										cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt() ){
									
									if(cupo.getCupos() > 0)
										cupo.setEstado(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS);
									else
										cupo.setEstado(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS);
								}						
								actualizarFechaVencimiento(subscripcion, ahoraCal.getTime(), actYClase);
								subscripcion=clienteEJB.actualizarSubscripcion(subscripcion, actYClase);
								subscripcion.setCliente(subscripcion.getCliente());
								setCliente(subscripcion.getCliente());
								
								// se guarda en la BD
								subscripcionEJB.save(subscripcion);
								
								List<Subscripcion> listSubs=subscripcionEJB.findAllSubscripcionesByClient(subscripcion.getCliente().getId());
								if(listSubs != null)
									getCliente().setSubscripcionesList(new HashSet(listSubs));
								
								clienteEJB.save(getCliente());
									
								super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
							
						}
	
					}else{
						super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
						
					}
				}
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
	
	private Subscripcion actualizarFechaVencimiento(Subscripcion subs, Date nuevaFechaVencimiento, ActividadYClase actYClase){
	
		// Ver que pasa cuando una subscricion tiene mas de un curso
		if(subs != null && subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(actYClase.getCurso().getId().intValue() == cupoAct.getCurso().getId().intValue() )
					cupoAct.setFechaFin(nuevaFechaVencimiento);
			}
		}
		return subs;
	}

	public void onBack(Event event) {
		super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	}

	private Subscripcion fromViewToModel(Subscripcion subs, ActividadYClase actYClase) {

		
		// se
		CupoActividad cupo= null;
		if(subscripcion.getCupoActividadList() != null ){
			for (CupoActividad iterable_element : subscripcion.getCupoActividadList()) {
				if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
					cupo= iterable_element;
				
			}
		}
		
		
		if(cupo.getIdUsuarioPosponeSubscripcion() == null){
			cupo.setIdUsuarioPosponeSubscripcion(Session.getUsernameID().intValue());
			cupo.setPosponefechaYHora(new Date());

//			cupo.setPosponePagaAdicional(true);

			
			if(getSubscripcionCrud2().getComboMesesPospone().getSelectedItem() != null)
				cupo.setPosponeMeses((Integer)getSubscripcionCrud2().getComboMesesPospone().getSelectedItem().getValue());
			
			if(getSubscripcionCrud2().getComentarioCb().getValue() != null)
				cupo.setPosponeComentario((String)getSubscripcionCrud2().getComentarioCb().getValue());
						
		}else{
			cupo.setIdUsuarioPosponeSubscripcion2(Session.getUsernameID().intValue());
			cupo.setPosponefechaYHora2(new Date());
	
//			cupo.setPosponePagaAdicional2(true);
			if(getSubscripcionCrud2().getComboMesesPospone().getSelectedItem() != null)
				cupo.setPosponeMeses2((Integer)getSubscripcionCrud2().getComboMesesPospone().getSelectedItem().getValue());

			if(getSubscripcionCrud2().getComentarioCb().getValue() != null)
				cupo.setPosponeComentario2((String)getSubscripcionCrud2().getComentarioCb().getValue());
			
		}
		
	return subs;
	}
	
	private boolean validateCrud() {
		
		if(getSubscripcionCrud2().getComboMesesPospone().getSelectedItem() == null)
			throw new WrongValueException(getSubscripcionCrud2().getComboMesesPospone(), I18N.getLabel("error.empty.field"));
			
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
		return 0;
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

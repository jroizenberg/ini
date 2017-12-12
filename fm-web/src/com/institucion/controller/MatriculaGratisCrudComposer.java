package com.institucion.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Cliente;
import com.institucion.model.Inscripcion;
import com.institucion.model.MatriculaGratisCrud;
import com.institucion.model.MatriculaGratisList;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoMovimientoCajaEnum;

public class MatriculaGratisCrudComposer extends CrudComposer {

	private PanelCrud panelCrud;
	public final String idCliente = "idCliente";
	private Cliente cliente;
	private InscripcionEJB inscripcionEJB;
	private MatriculaGratisList matriculaspanelListGrid;
	private CajaEJB cajaEJB;
	private ClienteEJB clienteEJB;
	
	public MatriculaGratisCrudComposer() {
		inscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		clienteEJB= BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
	}

	public void onCreate() {
		Cliente cliente= (Cliente) Session.getAttribute(idCliente);
		setCliente(cliente);
		this.fromModelToView(cliente);
		if(cliente != null && cliente.getId() != null){
			List<Inscripcion> listaIns=inscripcionEJB.findByIdClienteAndAnio(cliente.getId());
			matriculaspanelListGrid.setList(listaIns);	
		}else{
			matriculaspanelListGrid.setList(null);
		}
	}
	
	private void fromModelToView(Cliente cliente) {
		// si lo que obtuve de bd es != null
		if(cliente != null){
			String nombre= null;
			
			if(cliente.getApellido() != null)
				nombre= cliente.getApellido();
				
			if(cliente.getNombre() != null){
				if(nombre== null)
					nombre= cliente.getNombre();
				else
					nombre= nombre + " " + cliente.getNombre();
			}
			getClienteCrud().getNombreCliente().setValue(nombre.toUpperCase());
		}		
	}
	
	public void onDelete(Event event) throws Exception {
		int val=hasSelectedOneItemSinMensaje(matriculaspanelListGrid);
		if(val==0){
			if (MessageBox.question("¿ Esta seguro que desea eliminar la Matricula Gratuita del cliente ? ",
					"Eliminando Matricula Gratuita")){
		 		Inscripcion ins=(Inscripcion) matriculaspanelListGrid.getSelectedItem().getValue();

				eliminarMatricula(ins);
				
				MessageBox.info("Se elimino Correctamente la Matrícula Gratuita", "Matricula Gratuita Eliminada");
				
				cliente=clienteEJB.findById(cliente.getId());
				Session.setAttribute(idCliente, cliente) ;

				if(cliente != null && cliente.getId() != null){
					List<Inscripcion> listaIns=inscripcionEJB.findByIdClienteAndAnio(cliente.getId());
					matriculaspanelListGrid.setList(listaIns);	
				}else{
					matriculaspanelListGrid.setList(null);
				}
			}	
		}
	}
	
	protected int hasSelectedOneItemSinMensaje(GridList gridlist) {
	 	if (gridlist.getSelectedItem() != null && gridlist.getSelectedItems().size() == 1) {
	 		Inscripcion ins=(Inscripcion) gridlist.getSelectedItem().getValue();
	 		if(ins.getMatriculaGratis() != null && ins.getMatriculaGratis()){
	 			return 0;
	 		}else{
	 			MessageBox.info("Se permiten eliminar ÚNICAMENTE matriculas gratuitas", I18N.getLabel("selector.actionwithoutitem.title"));
				return 1;	
	 		}
		}else{
			MessageBox.info("Debe seleccionar la matricula gratuita que quiere eliminar", I18N.getLabel("selector.actionwithoutitem.title"));
			return 2;	
		}
	 	
	}
	
	public void onSave(Event event) throws Exception {
		try{
			
			if (Sessions.getCurrent().getAttribute(idCliente) != null) {
				// es una modificacion
					cliente = (Cliente) Sessions.getCurrent().getAttribute(idCliente);

					validateCrud();

					if (MessageBox.question("¿ Esta seguro que desea generar una Matricula Gratuita ? El cliente NO ABONARA NADA ",
							"Generando Matricula Gratuita")){
						
						guardarMatricula();
						
						MessageBox.info("Se guardo Correctamente la Matrícula Gratuita", "Matricula Gratuita Guardada");
											
						Boolean bo=(Boolean)com.institucion.fm.conf.Session.getAttribute("idClienteFromSubs");
						if(bo != null && bo){
							super.gotoPage("/institucion/subscripcion-crud.zul");
						}else{
							super.gotoPage("/institucion/ingresoInscripcion-selector.zul");	
						}
						com.institucion.fm.conf.Session.setAttribute("idClienteFromSubs", false);
					}
			} 
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public boolean eliminarMatricula(Inscripcion insAEliminar){
	try{
			if(insAEliminar != null){
				cliente.getInscripcionesList().remove(insAEliminar);
				inscripcionEJB.delete(insAEliminar);
				
				// Guardo el movimiento en la caja ahora
				CajaMovimiento caja= new CajaMovimiento();
				caja.setConcepto("Nueva: Matricula Gratuita Eliminada");
				caja.setFecha(new Date());
				caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
				caja.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
				caja.setValor(new Double(0));
				caja.setSucursal(SucursalEnum.CENTRO);
				caja.setCliente(cliente);

				cajaEJB.save(caja);
				return true;
			}
			
		}catch(Exception e){
			return false;
		}
	return true;
	}
	
	public boolean guardarMatricula(){

		try{
			
			Inscripcion insc= new Inscripcion();
			insc.setCliente(cliente);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(getClienteCrud().getFechaDesde().getValue());
			
	        insc.setFecha(getClienteCrud().getFechaDesde().getValue());
	        
			cal.add(Calendar.YEAR, 1);    //Adding 1 year to current date
			insc.setFechaVencimiento(cal.getTime());
			insc.setMatriculaGratis(true);
			insc.setSaldoAbonado(0);
			inscripcionEJB.save(insc);
			
			
			// Guardo el movimiento en la caja ahora
			CajaMovimiento caja= new CajaMovimiento();
			caja.setConcepto("Nueva: Matricula Gratuita ");
			caja.setFecha(new Date());
			caja.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
			caja.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
			caja.setValor(new Double(0));
			caja.setSucursal(SucursalEnum.CENTRO);
			caja.setCliente(cliente);

			cajaEJB.save(caja);
			
			Cliente cli=clienteEJB.findById(cliente.getId());
			com.institucion.fm.conf.Session.setAttribute("idCliente", cli);

			return true;
			
			
		}catch(Exception e){
			return false;
		}
	}
	
	public void onBack(Event event) {
		Boolean bo=(Boolean)com.institucion.fm.conf.Session.getAttribute("idClienteFromSubs");
		if(bo != null && bo){
			super.gotoPage("/institucion/subscripcion-crud.zul");
		}else{
			super.gotoPage("/institucion/ingresoInscripcion-selector.zul");	
		}
		com.institucion.fm.conf.Session.setAttribute("idClienteFromSubs", false);
		
//		super.gotoPage("/institucion/ingresoInscripcion-selector.zul");
	}

	private MatriculaGratisCrud getClienteCrud() {
		return (MatriculaGratisCrud) (panelCrud.getGridCrud());
	}


	private void validateCrud() {
	
		if(getClienteCrud().getFechaDesde().getValue() == null)
			throw new WrongValueException(getClienteCrud().getFechaDesde(), I18N.getLabel("error.empty.field"));

		
		// la fecha debe ser anterior a la fecha de hoy
		if(getClienteCrud().getFechaDesde().getValue() == null || 
				(getClienteCrud().getFechaDesde().getValue() != null && getClienteCrud().getFechaDesde().getValue().after(new Date() )))
			throw new WrongValueException(getClienteCrud().getFechaDesde(), " La fecha debe ser anterior a la fecha de hoy");
		
	
		
		// Se debe verificar que la fecha de pago ya este vencida.
		Date date= new Date();
		Date fechaPago=getClienteCrud().getFechaDesde().getValue();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fechaPago);
		cal.add(Calendar.YEAR, 1);    //Adding 1 year to current date
				
		if (cal.getTime().after(date)){
			
		}else{
			throw new WrongValueException(getClienteCrud().getFechaDesde(), " Se pueden crear solamente Matriculas que estarián vigentes. Según la fecha ingresada, esta estaría vencida.");
		}
		
		Calendar ca2l = Calendar.getInstance();
		ca2l.setTime(fechaPago);
		ca2l.add(Calendar.YEAR, 1);    //Adding 1 year to current date
				
		// se tiene que validar que para la fecha de pago de inscripcion no este en ningun periodo
		boolean encontroMatriculasVigentes= false;
		List<Inscripcion> listaIns=inscripcionEJB.findByIdClienteAndAnio(cliente.getId());
		if(listaIns != null ){
			
			for (Inscripcion inscripcion : listaIns) {
				if(inscripcion.getFechaVencimiento().after(ca2l.getTime())){
					encontroMatriculasVigentes= true;
				}
			}
			if(encontroMatriculasVigentes){
				throw new WrongValueException(getClienteCrud().getFechaDesde(), " Se encontraron matriculas vigentes para la fecha de Pago ingresada.");
			}
		}
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}

package com.institucion.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Window;

import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.desktop.view.ToolbarFiltersDeudas2;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.ActividadYClase;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.DeudaClientFilter;
import com.institucion.model.DeudaClienteList;
import com.institucion.model.DeudaClienteSubscripcionFilter;
import com.institucion.model.DeudaClienteSubscripcionList;
import com.institucion.model.DeudaTotalCrud;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Subscripcion;
import com.institucion.model.VencimientoCursoEnum;

public class DeudaClienteSelectorComposer extends SelectorFEComposer implements CursosDelegate, ClienteDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private DeudaClienteList clientepanelListGrid;
	private DeudaClienteSubscripcionList subscripcionListGrid;
//	private ToolbarFiltersDeudas2 toolbar2;

	private PanelFilter filter;
	private PanelFilter filter2;
	
	private List<Cliente> clienteList;

	private ClienteEJB clienteEJB;
//	private ClaseEJB claseEJB;
//	private CursoEJB cursoEJB;
	private InscripcionEJB inscripcionEJB;
//	private CajaEJB cajaEJB;
//	private Window win = null;
	private PanelCrud totales2Crud; 

	/** The _page size. */
//	private final int _pageSize = 6;
	
	private Window win = null;

//	/** The user paging. */
//	private Paging userPaging;
	
//	private Long totalSize=0L;
	
//	private String filtrosAnt=null;
	
	
	private ToolbarFiltersDeudas2 toolbar2;

	
	/** The id. */
	String id = null;
	
	/** The order. */
	boolean order = false;
	
	public DeudaClienteSelectorComposer(){
		super();
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
//		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		inscripcionEJB= BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
//		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
//		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
	}
	
	private DeudaTotalCrud getDeudasTotales2CrudCrud() {
		return (DeudaTotalCrud) (totales2Crud.getGridCrud());
	}

	
	@Override
	public void sortEvent(Event evt) {
		final Listheader lh = (Listheader) evt.getTarget();
		final String sortDirection = lh.getSortDirection();
		if (sortDirection != null && !"".equals(sortDirection))
			order = sortDirection.equals("ascending")?false:true;
		id = (String)lh.getAttribute("id");
//		int pgno = userPaging.getActivePage();
//		int ofs = pgno * _pageSize;    	
		getData( null, id, order, evt);
	}
	
	private ClienteEJB getClientService() {
		if (this.clienteEJB == null) {
			this.clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		}
		return this.clienteEJB;
	}
	
//	@Override
//	public void doAfterCompose(Component comp) throws Exception {
//
//		super.doAfterCompose(comp);
//		userPaging.setPageSize(_pageSize);
//		totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
//		userPaging.setTotalSize(totalSize.intValue());
//		userPaging.setDetailed(true);
//		userPaging.addEventListener("onPaging", new EventListener() {
//			public void onEvent(Event event) {
//				int pgno = userPaging.getActivePage();
//				int ofs = pgno * _pageSize;    	
//				getData(ofs, _pageSize, null, id, order, event);
//			}
//		});
//	}
	
	private void  getData( String filters, String id, boolean order, Event evt){
		super.setSelectedEntity(clientepanelListGrid);
		// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		if(filters != null)
			clienteList =clienteEJB.findAllConJdbc(filters);
		else
			clienteList =clienteEJB.findAllConJdbc(getClientFilter().getFilters(id,order  ));	
		List<Cliente> listNueva= null;
		if(clienteList != null){
			listNueva=eliminarRepetidosCliente(clienteList, listNueva);
		}
		clienteList= listNueva;
		Cliente cliSeleccionadoAnteriormente=(Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente");
		clientepanelListGrid.setList(clienteList, cliSeleccionadoAnteriormente);
		subscripcionListGrid.setList(null, null, null);
		super.getSelectedEntity();
	}
	
	private double  getDeudaTotalData( ){
		
//		System.out.println("llamo a los find dentro de getData TOTALES");
		String filters = getClientFilter().getFiltersDeudasTotales();
		clienteList =clienteEJB.findAllConJdbc(filters);
		List<Cliente> listNueva= null;
		if(clienteList != null){
			listNueva=eliminarRepetidosCliente(clienteList, listNueva);
		}
		clienteList= listNueva;

		double total= 0;
		if(clienteList != null){
			Iterator<Cliente> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				Cliente cliente = itPharmacy.next();
				float cant=getDeuda(cliente);
				total= total+ cant;
			}
		}
		return total;
	}
	
	private int getDeuda(Cliente cli){
		int cantDeuda= 0;
		cli=clienteEJB.loadLazy(cli, true, true, true, false);
		if(cli.getSubscripcionesList() != null){
			for (Subscripcion subs : cli.getSubscripcionesList()) {
				
				
				if(subs.getCupoActividadList() != null){
					for (CupoActividad cupoAct: subs.getCupoActividadList()) {
						if(cupoAct.getEstado() != null 
								&& cupoAct.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
								((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
									||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
									||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
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
	public void clear(Event event){
		getClientFilter().clear();
	}
	
	public void onCreate() {
		
		com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelector", null);
		com.institucion.fm.conf.Session.setAttribute("isClienteFromIngresoInscripcion", null);
		com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", null);
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("¡Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		clientepanelListGrid.setDelegate(this);
		clientepanelListGrid.setActionComposerDelegate(this);
		clientepanelListGrid.setActionComposerDelegate(this);

		if(Session.getDesktopPanel().getMessage() == null){
			Session.getDesktopPanel().setMessage("menu");
		}
		
		String query="  select count(*) from users u    "+
					 	" inner join user_group ug on (ug.user_id= u.id)  "+
						" inner join group_permission gp on (gp.group_id = ug.group_id)  "+
						" where u.name='"+Session.getUsername()+"'  " +
						" and gp.permission_id = ( select id from permission where name='tx.toolbar2.view.chart')  ";
		Long cant=clienteEJB.getCount(query);
		if(cant.intValue() > 0){
//			toolbar2.setVisibleVencerElCursoButton(true);
			toolbar2.setVisibleSaldaCursoPorErrorElCursoButton(true);
		}else{
//			toolbar2.setVisibleVencerElCursoButton(false);
			toolbar2.setVisibleSaldaCursoPorErrorElCursoButton(false);
		}
		
		boolean VinoDelMenu=Session.getDesktopPanel().getMessage().equals("menu");
		if(VinoDelMenu){
			Sessions.getCurrent().removeAttribute("idCliente");
			Sessions.getCurrent().removeAttribute("pagno");
			Sessions.getCurrent().removeAttribute("col");
			Sessions.getCurrent().removeAttribute("order");
			Sessions.getCurrent().removeAttribute("total");
			Sessions.getCurrent().removeAttribute("filters");

			Sessions.getCurrent().removeAttribute("idClienteFromSubs");
			Sessions.getCurrent().removeAttribute("isClienteFromIngresoInscripcion");
			Sessions.getCurrent().removeAttribute("idClase");
			Sessions.getCurrent().removeAttribute("isClaseFromIngresoInscripcion");
			Sessions.getCurrent().removeAttribute("idSubs");
			Sessions.getCurrent().removeAttribute("isSubsFromDeudasClientes");
			Sessions.getCurrent().removeAttribute("max");
			Sessions.getCurrent().removeAttribute("actYClase");
			Sessions.getCurrent().removeAttribute("isSubsFromDeudasClientes");
			Sessions.getCurrent().removeAttribute("idSubscripcion");
			Sessions.getCurrent().removeAttribute("idCliente");
			Sessions.getCurrent().removeAttribute("idSubscripcion");
			Sessions.getCurrent().removeAttribute("isSubsFromDeudasClientes");
			Sessions.getCurrent().removeAttribute("fechaFinPosponer");
			Sessions.getCurrent().removeAttribute("actYClasePosponer");
			Sessions.getCurrent().removeAttribute("fechaFinPosponer");
			Sessions.getCurrent().removeAttribute("actYClasePosponer");
			Sessions.getCurrent().removeAttribute("idactYClase");
			Sessions.getCurrent().removeAttribute("idSubscripcion");
			Sessions.getCurrent().removeAttribute("subs");
			Sessions.getCurrent().removeAttribute("idSubs");
			Sessions.getCurrent().removeAttribute("isClienteFromIngresoInscripcion");
			Sessions.getCurrent().removeAttribute("vieneDeCrearCliente");
			clear(null);
		}
		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu

		if(!VinoDelMenu){

//			if(Sessions.getCurrent().getAttribute("pagno") != null)
//				userPaging.setActivePage((Integer)Sessions.getCurrent().getAttribute("pagno"));
			
			if(Sessions.getCurrent().getAttribute("col") != null)
				id=(String)Sessions.getCurrent().getAttribute("col");
			
			if(Sessions.getCurrent().getAttribute("order") != null)
				order=(Boolean)Sessions.getCurrent().getAttribute("order");
				
//			if(Sessions.getCurrent().getAttribute("total") != null)
//				totalSize=((Integer)Sessions.getCurrent().getAttribute("total")).longValue();

			
//			if(Sessions.getCurrent().getAttribute("filters") != null)
//				filtrosAnt=(String)Sessions.getCurrent().getAttribute("filters");
			
		}else{
			Sessions.getCurrent().setAttribute("pagno", null);
			Sessions.getCurrent().setAttribute("filters", null);
			Sessions.getCurrent().setAttribute("total",  null);
			Sessions.getCurrent().setAttribute("col",  null);
			Sessions.getCurrent().setAttribute("order",  null);
		}

		onFind(VinoDelMenu, false, null);
		
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		getIngresoInscripcionFilter().setDelegate(this);
		filter2.getInnerPanel().setOpen(true);
		filter2.setOpen(true);
		getClientFilter().setActionComposerDelegate(this);
		refreshEvents();
	}
	
	private void refreshEvents(){
		
		getClientFilter().getNombre().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	
		
		getClientFilter().getApellido().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	
		
	}
	
	public void onMatriculaGratis(Event event) throws Exception {
//		com.institucion.fm.conf.Session.setAttribute("idClienteFromSubs", false);
//
//		if (hasSelectedOneItemSinMensaje(clientepanelListGrid)){
//			com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
//			
//			guardarTemasPaginacion();
//			super.saveHistory("filter");
//			super.gotoPage("/institucion/matricula-gratis-crud.zul");
//		
//		}else{
//			MessageBox.validation(I18N.getLabel("subscripcion.seleccionar.cliente"), I18N.getLabel("selector.actionwithoutitem.title"));
//			return;	
//		}
	}

	public void onVerCumplesContratados(Event event) throws Exception {
//		super.saveHistory("filter");
//		super.gotoPage("/institucion/cumples-selector.zul");
	}
	
//	/* CREAR NUEVO CLIENTE */
	public void onInsert(Event event) throws Exception {

//		com.institucion.fm.conf.Session.setAttribute("idCliente", null);
//		com.institucion.fm.conf.Session.setAttribute("isClienteFromIngresoInscripcion", true);
//
//		eliminarTemasPaginacion();
//		super.saveHistory("filter");
//		super.gotoPage("/institucion/clientes-crud.zul");
	}

	/* CREAR NUEVA CLASE */
	public void onInsertClass(Event event) throws Exception {

//		com.institucion.fm.conf.Session.setAttribute("idClase", null);
//		com.institucion.fm.conf.Session.setAttribute("isClaseFromIngresoInscripcion", true);
//		com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
//		guardarTemasPaginacion();
//		super.saveHistory("filter");
//
//		super.gotoPage("/institucion/clase-crud.zul");
	}

	/* CREAR NUEVA SUBSCRIPCION */
	public void onInsertSubs(Event event) throws Exception {

//		if (hasSelectedOneItemSinMensaje(clientepanelListGrid)){
//			
//			com.institucion.fm.conf.Session.setAttribute("idSubs", null);
//			com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", true);
//			com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
//			
//			Sessions.getCurrent().setAttribute("max", 0);
//			
//			guardarTemasPaginacion();
//
//			super.saveHistory("filter");
//			super.gotoPage("/institucion/subscripcion-crud.zul");
//		
//		}else{
//			MessageBox.validation(I18N.getLabel("subscripcion.seleccionar.cliente"), I18N.getLabel("selector.actionwithoutitem.title"));
//			return;	
//		}
	}
	
	public void buscarSubscripcionesDesdeFiltro(){
//		getClientFilter().getCodBarras().setFocus(true);

		onFind2(true);
	}
	

	public void onSaldarDeudaSubs(Event event) throws Exception {

		if (hasSelectedOneItem(clientepanelListGrid)){
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
			if(cli != null ){
				
				boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cli.getId());
				
				if(!tieneDeudas){
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.saldar.solo.deuda"), 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;
				}else{
					com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
					com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", true);
					
//					guardarTemasPaginacion();
					super.saveHistory("filter");
					
					super.gotoPage("/institucion/subscripcion-saldar-deuda-crud.zul");
				}
			}
		}

	}
	
	private String getCursosAanular(Subscripcion subsSeleccionada){
		String cursos= null;
		if(subsSeleccionada.getCupoActividadList() != null){
			
			for (CupoActividad cupo : subsSeleccionada.getCupoActividadList()) {
				if(cursos == null)
					cursos= cupo.getCurso().getNombre().toUpperCase();
				else
					cursos= cursos + ", "+ cupo.getCurso().getNombre().toUpperCase();
				
			}
		}
		return cursos;
	}
	
	/*
	 * Anular Subscripcion
	 */
	public void onAnularSubs(Event event) throws Exception {
		
		if (hasSelectedOneItem(clientepanelListGrid)){
			if(subscripcionListGrid.getSelectedItem() == null){
				MessageBox.validation("Debe seleccionar una subscripcion", 
						I18N.getLabel("selector.actionwithoutitem.title"));
				return ;
			}
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
			Subscripcion subsSeleccionada= null;
			subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();	

			if(subsSeleccionada != null && subsSeleccionada.getFechaYHoraAnulacion() != null){
				MessageBox.validation("La subscripcion ya se encuentra anulada", 
						I18N.getLabel("selector.actionwithoutitem.title"));
				return ;

			}else{
				com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
				com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
					
				if(subsSeleccionada != null){
					int cantCursosEnSubscripcion=0;
					if(subsSeleccionada.getCupoActividadList() != null  ){
						cantCursosEnSubscripcion=subsSeleccionada.getCupoActividadList().size();
					}
					
					
					if(cantCursosEnSubscripcion > 1){
						//  Avisar que se Anularan todas los cursos contratados en esa misma subscripcion
						Object[] params7 = {getCursosAanular(subsSeleccionada) };
						
						if (MessageBox.question((I18N.getLabel("curso.pregunta.de.la.subscripcion", params7)), 
								I18N.getLabel("subscripcion.list.title.anular.title"))){
					
							if(subsSeleccionada.getCupoActividadList() != null ){
								for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
									if(iterable_element.getEstado() != null 
											&&(iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt() )){
										
										MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.errors"), 
												I18N.getLabel("selector.actionwithoutitem.title"));
										return ;
									}					
								}
							}
						}
					}
				
					if(subsSeleccionada.getCupoActividadList() != null){
						for (CupoActividad cupoAct : subsSeleccionada.getCupoActividadList()) {
							
							// Validar que : si la inscripcion fue Pospuesta por lo menos 1 vez, esta no se puede anular.
							if(cupoAct.getPosponefechaYHora() != null ||  cupoAct.getPosponefechaYHora2() != null){
								
								MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.errors.pospuesta"), 
										I18N.getLabel("selector.actionwithoutitem.title"));
								return ;
							}					
						}
					}
		
		
					if(subsSeleccionada != null){
						if(subsSeleccionada.getCupoActividadList() != null ){
							for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
								if(iterable_element.getEstado() != null 
										&&(iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt() 
											|| iterable_element.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt())){
									MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.asignar.errors.solo.nuevasoconcupos"), 
											I18N.getLabel("selector.actionwithoutitem.title"));
									return ;
									
								}
							}
						}
					}
					
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(subsSeleccionada.getFechaIngresoSubscripcion() );
					int mes=ahoraCal.get(Calendar.MONTH)+ 1;
					String fehchaIngreso=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					Object[] params5 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase(), fehchaIngreso };
					
					if (MessageBox.question(I18N.getLabel("subscripcion.list.title.anular", params5), I18N.getLabel("subscripcion.list.title.anular.title"))){
						
						com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
						com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
						com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", true);
						
//						guardarTemasPaginacion();
						super.saveHistory("filter");

						super.gotoPage("/institucion/subscripcion-anular-crud.zul");
						
					}
					
					
				}else{
					return ;
				}
			}

		}else{
			MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.debe.seleccionar.una.inscricion"), 
					I18N.getLabel("selector.actionwithoutitem.title"));
			return ;
			
		}
	}

	
	/* POSPONER VENCIMiENTO */
	public void onPosponerSubs(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("fechaFinPosponer", null);
		com.institucion.fm.conf.Session.setAttribute("actYClasePosponer", null);

		if (hasSelectedOneItem(clientepanelListGrid)){
			if (hasSelectedOneItem(subscripcionListGrid)){
					
				Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
				Subscripcion subsSeleccionada= null;
				subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();	
				ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");

				if(subsSeleccionada == null){
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.debe.seleccionar.una.inscricion.posponer"), 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;
				}
				com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
				com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
				
				CupoActividad cupo= null;
				if(subsSeleccionada.getCupoActividadList() != null ){
					for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
						if(iterable_element.getCurso() != null && actYClase.getCurso() != null 
								&& 	iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
							cupo= iterable_element;
					}
				}
				
				// Estos se pueden posponer
				if(subsSeleccionada != null){
					
					if(cupo.getFechaComienzo() == null){
						
						MessageBox.validation("No se puede posponer una subscripción que aun no comenzaron.", 
								I18N.getLabel("selector.actionwithoutitem.title"));
						return ;

					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripción anulada anteriormente", 
									I18N.getLabel("selector.actionwithoutitem.title"));
							return ;
		
					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripción sin cupos", 
									I18N.getLabel("selector.actionwithoutitem.title"));
							return ;						

					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripción sin cupos", 
									I18N.getLabel("selector.actionwithoutitem.title"));
							return ;						
					}

				}
				
				//actYClase
				if(cupo.getCupos() <=0){
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.posponer.sin.cupos"), 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				
				//hacer una validacion de que se pueden solamente posponer hasta 2 veces una inscripcion
				if(cupo.getIdUsuarioPosponeSubscripcion() != null &&
						cupo.getIdUsuarioPosponeSubscripcion2() != null ){
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.posponer.solo.2.veces"), 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				
				Date fechaFin=cupo.getFechaFin();
				
				Calendar ahoraCal= Calendar.getInstance();
				ahoraCal.setTime(fechaFin );
				int mes=ahoraCal.get(Calendar.MONTH)+ 1;
				String fehchaIngreso=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				
				Object[] params2 = {cupo.getCurso().getNombre(), cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase(), fehchaIngreso };
					
				if (MessageBox.question(I18N.getLabel("subscripcion.list.title.posponer", params2), I18N.getLabel("subscripcion.list.title.posponer.title"))){
					
					com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
					com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
					com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", true);
					
//					guardarTemasPaginacion();
					super.saveHistory("filter");
					com.institucion.fm.conf.Session.setAttribute("fechaFinPosponer", fechaFin);
					com.institucion.fm.conf.Session.setAttribute("actYClasePosponer", actYClase);

					
					super.gotoPage("/institucion/subscripcion-posponer-crud.zul");
					
				}
			}
		}
	}	
	
	private void actualizarListasPantallaCodigoDeBarras(){
		
		if(clientepanelListGrid.getSelectedItem() != null){
			//actualizar el estado del cliente
			clientepanelListGrid.actualizoClienteLuegoDePasarCodigoDeBarras();

			//actualizar la subscripcion en la que estoy trabajando
			subscripcionListGrid.actualizoSubscripcionLuegoDePasarCodigoDeBarras();
		}
	}
	
	
//	private CupoActividad obtenerElCupoActividadDeLaSubs(Subscripcion subsSeleccionada, Curso curso){
//		
//		if(subsSeleccionada.getCupoActividadList() != null){
//			for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
//				
//				if(iterable_element.getCurso() != null && curso != null &&
//						iterable_element.getCurso().getId().intValue() == curso.getId().intValue() )
//					return iterable_element;
//			}
//		}
//		return null;
//	}
	
	
	public void onSaldarElCurso(Event event) throws Exception{
			
		if (hasSelectedOneItem(subscripcionListGrid)){
			Concepto c=(Concepto)subscripcionListGrid.getSelectedItem().getAttribute("concepto");
			Subscripcion s=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
			s=clienteEJB.loadLazy(s, true, false, false, false, false);
			boolean encontro=false;
			if(c != null){
				if(s.getCupoActividadList() != null){
					for (CupoActividad cupoAct : s.getCupoActividadList()) {
							if(cupoAct.getEstado().equals(CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS)  
									|| cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS)
											|| cupoAct.getEstado().equals(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA)){
								encontro=true;
							
								if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
									cupoAct.setEstado(CupoActividadEstadoEnum.C_CUPOS);
									
								}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt() ){
									cupoAct.setEstado(CupoActividadEstadoEnum.S_CUPOS);
								
								}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt() ){
									cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA);
								}
								
							}
						}
				}
			}
			if(encontro){
				inscripcionEJB.save(s);
				MessageBox.info("Se le quito la deuda de dicha subscripcion al cliente de forma exitosa", I18N.getLabel("selector.actionwithoutitem.title.realizada"));

				// actualizo pantalla 
				actualizarListasPantallaCodigoDeBarras();
			}else{
				MessageBox.info("No se encontro ninguna deuda en la subscripcion seleccionada", "Accion cancelada");
			}
		}	}

	
	public void onUpdateSubscripciones(Event event) throws Exception{

		if (hasSelectedOneItem(subscripcionListGrid)){
			
			if(subscripcionListGrid.getSelectedItem().getValue() == null){
				MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.debe.seleccionar.una.inscricion.visualizar"), 
						I18N.getLabel("selector.actionwithoutitem.title"));
				return ;
				
			}
			if(subscripcionListGrid.getSelectedItem().getValue() instanceof Subscripcion){
				Sessions.getCurrent().setAttribute("idSubs", subscripcionListGrid.getSelectedItem().getValue());

			}else{
				Subscripcion subs2=(Subscripcion)subscripcionListGrid.getSelectedItem().getAttribute("subs");
				Sessions.getCurrent().setAttribute("idSubs", subs2);
			}
			Sessions.getCurrent().setAttribute("idCliente", clientepanelListGrid.getSelectedItem().getValue());
			Sessions.getCurrent().setAttribute("isSubsFromDeudasClientes", true);
			
//			guardarTemasPaginacion();

			super.saveHistory("filter");
			super.gotoPage("/institucion/subscripcion-crud.zul");		
		}
	}
	
	// es el update del cliente
	public void onUpdate(Event event) throws Exception{
	}
	
//	public void guardarTemasPaginacion(){
//		int pa = userPaging.getActivePage();
//		savePage(pa);
//		Sessions.getCurrent().setAttribute("pagno", userPaging.getActivePage());
//		Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
//		Sessions.getCurrent().setAttribute("total", totalSize.intValue());
//		Sessions.getCurrent().setAttribute("col", id);
//		Sessions.getCurrent().setAttribute("order", order);
//	}

//	public void eliminarTemasPaginacion(){
//		savePage(0);
//		Sessions.getCurrent().setAttribute("pagno", null);
//		Sessions.getCurrent().setAttribute("filters", null);
//		Sessions.getCurrent().setAttribute("total", null);
//		Sessions.getCurrent().setAttribute("col", null);
//		Sessions.getCurrent().setAttribute("order", null);
//	}

	public void onDelete(Event event) {
		
	}

	public void onVerSubs(Event event) throws Exception {
		this.onUpdateSubscripciones(null);
		
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		this.onUpdate(null);
	}

	public void onDoubleClickSubscripcionesEvt(Event event) throws Exception {
		this.onUpdateSubscripciones(null);
	}

	public void onClickClasesEvt(Event event) throws Exception {
	}
	
	public void onClickSubscripcionesArribaEvt(Event event) throws Exception {
	}
	
	
	public Map<String, List<CupoActividad>> tieneCursosdisponiblesDeDistintosTipos(List<Subscripcion> listSubs){
		Map<String, List<CupoActividad>> mapcursos= new HashMap<String, List<CupoActividad>> ();
		if(listSubs != null){
			for (Subscripcion subscripcion : listSubs) {
				subscripcion=clienteEJB.loadLazy(subscripcion, true, false, false, false, false);
				
				if(subscripcion.getCupoActividadList() != null){
					for (CupoActividad cupoAct : subscripcion.getCupoActividadList()) {
						
						if(cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_CUPOS)  
								|| cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS)){
							
							if(cupoAct.getCurso() != null && cupoAct.getCurso().getId() != null){
								String nuevo= null;
								if(cupoAct.getCurso().getNombre().indexOf(" ") == -1)
									nuevo= cupoAct.getCurso().getNombre();
								else
									nuevo=cupoAct.getCurso().getNombre().substring(0, cupoAct.getCurso().getNombre().indexOf(" "));

//								String nuevo=cupoAct.getCurso().getNombre().substring(0, cupoAct.getCurso().getNombre().indexOf(" "));
								if(mapcursos.containsKey(nuevo)){
									mapcursos.get(nuevo).add(cupoAct);
								}else{
									List aa= new ArrayList();
									aa.add(cupoAct);
									mapcursos.put(nuevo, aa);
								}
							}
						}
					}
				}
				
			}
		}

		return mapcursos;
	}

	public CupoActividad tieneCursosdisponiblesDelMismoTipo(List<Subscripcion> listSubs, Map<String, List<CupoActividad>> mapcursos){
	    CupoActividad cupoMenor= null;
	    
	    if(mapcursos == null){
	    	mapcursos= new HashMap<String, List<CupoActividad>> ();
			if(listSubs != null){
				for (Subscripcion subscripcion : listSubs) {
					subscripcion=clienteEJB.loadLazy(subscripcion, true, false, false, false, false);
					
					if(subscripcion.getCupoActividadList() != null){
						for (CupoActividad cupoAct : subscripcion.getCupoActividadList()) {
							
							if(cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_CUPOS)  
									|| cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS)){
								
								if(cupoAct.getCurso() != null && cupoAct.getCurso().getId() != null){
									String nuevo= null;
									if(cupoAct.getCurso().getNombre().indexOf(" ") == -1)
										nuevo= cupoAct.getCurso().getNombre();
									else
										nuevo=cupoAct.getCurso().getNombre().substring(0, cupoAct.getCurso().getNombre().indexOf(" "));
									if(mapcursos.containsKey(nuevo)){
										mapcursos.get(nuevo).add(cupoAct);
									}else{
										List aa= new ArrayList();
										aa.add(cupoAct);
										mapcursos.put(nuevo, aa);
									}

								}
							}
						}
					}
					
				}
			}
	    	
	    }
		
		if(mapcursos != null && mapcursos.size() ==1){
			// tiene 1 solo curso, ahora valido que si tiene cursos del mismo tipo obtener la mas vieja
			for (Map.Entry<String, List<CupoActividad>> entry : mapcursos.entrySet()){

			    List<CupoActividad> list =entry.getValue();
			    for (CupoActividad cupoActividad : list) {
					if(cupoActividad.getEstado().equals(CupoActividadEstadoEnum.C_CUPOS)
							||cupoActividad.getEstado().equals(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS)){
						if(cupoMenor == null)
							cupoMenor= cupoActividad;
						else{
							// valido que el cupoMenor sea menor al que ahora
							if(cupoActividad.getId().intValue() < cupoMenor.getId().intValue()){
								cupoMenor= cupoActividad;
							}
						}
					}
				}
			}
		}
		return cupoMenor;
	}
	
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		if(!isFromCodigoBarras){
			onFind(evt);
		}
	}
	
	

	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		getIngresoInscripcionFilter().blanquearEstado();
//		savePage(0);
//		Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
//		Sessions.getCurrent().setAttribute("total", totalSize.intValue());
		
		this.onFind(false, true, evt);
	}
	
	public void onClickEvt(Event evt){

		subscripcionListGrid.setVisible(true);
		toolbar2.setVisible(true);
		Cliente cli= null;
		
		if(clientepanelListGrid.getItems() != null && clientepanelListGrid.getItems().size()== 1 ){
			clientepanelListGrid.setSelectedIndex(0);
		}
		
		if(clientepanelListGrid.getSelectedItem() != null ){
			// busco las subscripciones del cliente
			cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
		}
		subscripcionListGrid.getItems().clear();

		if(cli != null){
			List<Subscripcion> listSubs =inscripcionEJB.findAllConJdbc(getIngresoInscripcionFilter().getFilters(cli.getId()));	
			System.out.println("llamo a los find dentro de onClickEvt");
			subscripcionListGrid.setList(listSubs, null, null);

		}

	}
		
	private DeudaClientFilter getClientFilter() {
		return (DeudaClientFilter)(filter.getGridFilter());
	}
	
	private DeudaClienteSubscripcionFilter getIngresoInscripcionFilter() {
		return (DeudaClienteSubscripcionFilter)(filter2.getGridFilter());
	}


	public void onFind(boolean isFromMenu, boolean esDelEvent, Event evt) {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		clienteList= new ArrayList<Cliente>();
		clientepanelListGrid.removeAll();
		
		if(esDelEvent){
			// no debe tener en cuenta lo que vino de session
			String filters = getClientFilter().getFilters(id,order  );
//			totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
//			userPaging.setTotalSize(totalSize.intValue());
			this.getData( filters, id, order, evt);
			
			// actualizo la Deuda TOTAL
			double sumaCantidadDineroTotal = getDeudaTotalData();
			getDeudasTotales2CrudCrud().agregarCampos(sumaCantidadDineroTotal);

//			userPaging.setActivePage(super.loadPage());
//			super.savePage(0);
//			Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
//			Sessions.getCurrent().setAttribute("total", totalSize.intValue());

		}else{
			if(isFromMenu){
				// no debe tener en cuenta lo que vino de session
				String filters = getClientFilter().getFilters( id,order  );
//				totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
//				userPaging.setTotalSize(totalSize.intValue());
				this.getData( filters, id, order, evt);
				
				// actualizo la Deuda TOTAL
				double sumaCantidadDineroTotal = getDeudaTotalData();
				getDeudasTotales2CrudCrud().agregarCampos(sumaCantidadDineroTotal);
				
//				userPaging.setActivePage(super.loadPage());
//				super.savePage(0);
			}else{
//				if(filtrosAnt != null){
//					this.getData( filtrosAnt, id, order, evt);
//					
//					// actualizo la Deuda TOTAL
//					double sumaCantidadDineroTotal = getDeudaTotalData();
//					getDeudasTotales2CrudCrud().agregarCampos(sumaCantidadDineroTotal);
//
////					userPaging.setActivePage(super.loadPage());
////					userPaging.setTotalSize(totalSize.intValue());
//
//				}else{
					// no debe tener en cuenta lo que vino de session
					String filters = getClientFilter().getFilters( id,order  );
//					totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
//					userPaging.setTotalSize(totalSize.intValue());
					this.getData( filters, id, order, evt);
					
					// actualizo la Deuda TOTAL
					double sumaCantidadDineroTotal = getDeudaTotalData();
					getDeudasTotales2CrudCrud().agregarCampos(sumaCantidadDineroTotal);

//					userPaging.setActivePage(super.loadPage());
//					super.savePage(0);
//				}
			}			
		}
	
		super.saveHistory("gridFilter");
		subscripcionListGrid.removeAll();
		toolbar2.setVisible(true);
		subscripcionListGrid.getItems().clear();
		onClickEvt(evt);
	}
	
	public void onFind2(boolean vieneDelFiltroDeEstadosSubs){
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		List<Subscripcion> subsList= new ArrayList<Subscripcion>();
		if(clientepanelListGrid != null && clientepanelListGrid.getSelectedItem() != null){
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
			if(cli != null){
				Subscripcion subsSelecccionada= (Subscripcion)com.institucion.fm.conf.Session.getAttribute("idSubscripcion");
				ActividadYClase actYclase = (ActividadYClase)com.institucion.fm.conf.Session.getAttribute("idactYClase");
				subsList =inscripcionEJB.findAllConJdbc(getIngresoInscripcionFilter().getFilters(cli.getId()));	
				subscripcionListGrid.setList(subsList, subsSelecccionada, actYclase);
			}
		}	
	}
	
	public void onClearFilter2(){
		getIngresoInscripcionFilter().clear();
		this.onFind2(false);	
	}
	
	public void onClearFilter(Event evt){	
		getClientFilter().clear();
		getIngresoInscripcionFilter().clear();
//		eliminarTemasPaginacion();
//		userPaging.setActivePage(0);
		id=null;
		order=false;
//		totalSize=null;
//		filtrosAnt=null;

		this.onFind(false, false, null);	
	}

	@Override
	public void actualizarTipoCurso() {
		
	}


	@Override
	public void muestraLasSubscripciones() {
		onClickEvt(null);		
	}


	@Override
	public void setearClasesCantidades(boolean esLibre, boolean clear) {		
	}


	@Override
	public void setearPrimeros10DiasDeClasesCantidades(VencimientoCursoEnum vencimientoEnum, boolean clear) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public VencimientoCursoEnum getTipoCursoEnum() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setearClasesCantidadesSemanales( boolean clear) {
		// TODO Auto-generated method stub
		
	}
	
	
	private List eliminarRepetidosCliente(List<Cliente> list, List<Cliente> listNuevo){
		listNuevo= new ArrayList<Cliente>();	
			
		for (Cliente clienteView : list) {
			if(!existClient(listNuevo, clienteView)){
				listNuevo.add(clienteView);
			}
		}
		return listNuevo;
	}
	
	private boolean existClient(List<Cliente> listNuevo, Cliente cv){
		
		for (Cliente clienteView : listNuevo) {
			if(clienteView.getId().intValue() ==  cv.getId().intValue())
				return true;
		}
		return false;
	}
	
	private List eliminarRepetidos(List<ClienteView> list, List<ClienteView> listNuevo){
		listNuevo= new ArrayList<ClienteView>();	
		
		if(getClientFilter().getFechaVentaD().getValue() != null
				&& getClientFilter().getFechaVentaH().getValue() != null){
			Calendar calDesde= Calendar.getInstance();
			calDesde.setTime(getClientFilter().getFechaVentaD().getValue());
			
			calDesde.set(Calendar.HOUR, 00);
			calDesde.set(Calendar.HOUR_OF_DAY, 00);
			calDesde.set(Calendar.MINUTE, 00);
			calDesde.set(Calendar.SECOND, 00);
			calDesde.set(Calendar.MILLISECOND, 00);
			
			Calendar calHasta= Calendar.getInstance();
			calHasta.setTime(getClientFilter().getFechaVentaH().getValue());
			
			calHasta.set(Calendar.HOUR, 00);
			calHasta.set(Calendar.HOUR_OF_DAY, 00);
			calHasta.set(Calendar.MINUTE, 00);
			calHasta.set(Calendar.SECOND, 00);
			calHasta.set(Calendar.MILLISECOND, 00);		
			for (ClienteView clienteView : list) {
				Calendar cal= Calendar.getInstance();
				cal.setTime(clienteView.getFechaVenta());
				
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
				cal.set(Calendar.MILLISECOND, 00);
			
				if ( ((cal.getTime().after(calDesde.getTime()) || cal.getTime().equals(calDesde.getTime())) 
				&&	(cal.getTime().before(calHasta.getTime()) || cal.getTime().equals(calHasta.getTime())))){
					if(!exist(listNuevo, clienteView)){
						listNuevo.add(clienteView);
					}
				}
			}
		}else if(getClientFilter().getFechaVentaD().getValue() != null){
			Calendar calDesde= Calendar.getInstance();
			calDesde.setTime(getClientFilter().getFechaVentaD().getValue());
			
			calDesde.set(Calendar.HOUR, 00);
			calDesde.set(Calendar.HOUR_OF_DAY, 00);
			calDesde.set(Calendar.MINUTE, 00);
			calDesde.set(Calendar.SECOND, 00);
			calDesde.set(Calendar.MILLISECOND, 00);
	
			for (ClienteView clienteView : list) {
				Calendar cal= Calendar.getInstance();
				cal.setTime(clienteView.getFechaVenta());
				
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
				cal.set(Calendar.MILLISECOND, 00);
			
				if ( (cal.getTime().after(calDesde.getTime()) || cal.getTime().equals(calDesde.getTime()))){
					if(!exist(listNuevo, clienteView)){
						listNuevo.add(clienteView);
					}
				}
			}
		}else if(getClientFilter().getFechaVentaH().getValue() != null){
			Calendar calHasta= Calendar.getInstance();
			calHasta.setTime(getClientFilter().getFechaVentaH().getValue());
			
			calHasta.set(Calendar.HOUR, 00);
			calHasta.set(Calendar.HOUR_OF_DAY, 00);
			calHasta.set(Calendar.MINUTE, 00);
			calHasta.set(Calendar.SECOND, 00);
			calHasta.set(Calendar.MILLISECOND, 00);		
			for (ClienteView clienteView : list) {
				Calendar cal= Calendar.getInstance();
				cal.setTime(clienteView.getFechaVenta());
				
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
				cal.set(Calendar.MILLISECOND, 00);
			
				if ((cal.getTime().before(calHasta.getTime()) || cal.getTime().equals(calHasta.getTime()))){
					if(!exist(listNuevo, clienteView)){
						listNuevo.add(clienteView);
					}
				}
			}
		}else{
			for (ClienteView clienteView : list) {
				if(!exist(listNuevo, clienteView)){
					listNuevo.add(clienteView);
				}
			}
		}
		return listNuevo;
	}
	private boolean exist(List<ClienteView> listNuevo, ClienteView cv){
		
		for (ClienteView clienteView : listNuevo) {
			if(clienteView.getId().intValue() ==  cv.getId().intValue())
				return true;
		}
		return false;
	}
	public void onExportClientExcel(Event event) throws Exception {

		List<ClienteView> clienteExportList= null;
		String filters = getClientFilter().getFiltersForExportingExcelClientes(id,order  );
		clienteExportList =clienteEJB.findAllConJdbcSinHibernate(filters);
		List<ClienteView> listNueva= null;

		if(clienteExportList != null){
			listNueva=eliminarRepetidos(clienteExportList, listNueva);
		}
		
		if(listNueva == null || (listNueva != null && listNueva.size() == 0)){
			MessageBox.validation("No se encontraron resultados a guardar en excel", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}else{
			
			String[] sheetNames = {"Clientes Deudores" }; 
			Iframe iframeReporte = new Iframe();
			
			iframeReporte.setWidth("0%");
	        iframeReporte.setHeight("0%");
			iframeReporte.setId("reporte");
			
			JRDistributionPlanSummaryXls jasperReport = new JRDistributionPlanSummaryXls();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
							
				Map<Integer, List<String>> mapFields= new HashMap <Integer, List<String>>(); 
				List<String>fieldNames =new ArrayList <String>();
				fieldNames.add("Apellido");
				fieldNames.add("Nombre");
				fieldNames.add("Telefono");
				fieldNames.add("Celular");
				fieldNames.add("Edad");
				fieldNames.add("Deuda");
				fieldNames.add("Responsable");
				fieldNames.add("Fecha Venta");
				fieldNames.add("Dias deuda");
				
				mapFields.put(1, fieldNames);
								
				jasperReport.setFieldNamesList(mapFields);
				
				Map<Integer, List<RowModel>> map2 = new HashMap <Integer, List<RowModel>>(); 

				map2.put(1, obtenerParaExcel(listNueva));
			    jasperReport.setDsList(map2);
				
				Calendar ahoraCal= Calendar.getInstance();
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR);

				try {
					jasperReport.exportXlsReportRestructuring(os, sheetNames, "Deudores al_"+fechaNac);
				} catch (JRException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ByteArrayInputStream mediais = new ByteArrayInputStream(os.toByteArray());
								
				AMedia amedia = new AMedia("Deudores al_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

				if (win == null) {
					win = (Window) (Executions.getCurrent()).createComponents("institucion/report-popup.zul", super.self, null);	
				}
				iframeReporte = (Iframe) win.getFellow("reporte");
				iframeReporte.setContent(amedia);
				try {
					win.doEmbedded();
					win.setVisible(false);
				} catch (SuspendNotAllowedException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	private List<com.institucion.controller.RowModel>  obtenerParaExcel (List<ClienteView> clienteExportList){
		 List<com.institucion.controller.RowModel>  rowModel= new ArrayList();
		double total= 0;
					
		for (ClienteView iterable_element : clienteExportList) {
			com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
			
			row.set("Apellido", iterable_element.getApellido());
			row.set("Nombre", iterable_element.getNombre());
			
			if(iterable_element.getTelefonoFijo() != null)
				row.set("Telefono", iterable_element.getTelefonoFijo());
			else
				row.set("Telefono",  " ");
			

			if(iterable_element.getCelular() != null)
				row.set("Celular", iterable_element.getCelular());
			else
				row.set("Celular",  " ");

			if(iterable_element.getFechaNacimiento() != null){
				row.set("Edad", String.valueOf(EdadViewHelper.calcularEdad(iterable_element.getFechaNacimiento(), new Date())));
			}else
				row.set("Edad",  " ");


			float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(iterable_element.getId()));
			row.set("Deuda", " $"+formateador.format(cant ) );

			total= total+ cant;

			if( iterable_element.getResponsable() != null)
				row.set("Responsable", iterable_element.getResponsable());
			else
				row.set("Responsable", " ");
			
			if( iterable_element.getFechaVenta() != null){
				Calendar ahoraCal= Calendar.getInstance();
				ahoraCal.setTime(iterable_element.getFechaVenta());
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				
				row.set("Fecha Venta", fechaNac);
				
				Date fechaFinal=new Date();
				int dias=(int) ((fechaFinal.getTime()-iterable_element.getFechaVenta().getTime())/86400000);
//				row.appendChild(new Listcell(String.valueOf(dias) +" días"));	

				row.set("Dias deuda",  String.valueOf(dias) +" días");
				
			}else{
				row.set("Fecha Venta",  " ");
				row.set("Dias deuda",  " ");
			}
			rowModel.add(row);			
		}
		
		com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
		row.set("Apellido",  " ");
		row.set("Nombre",  " ");
		row.set("Telefono",  " ");
		row.set("Celular",  " ");
		row.set("Edad",  " ");
		row.set("Responsable", " ");
		row.set("Fecha Venta",  " ");
		row.set("Dias deuda",  " ");
		rowModel.add(row);			

		row= new com.institucion.controller.RowModel();
		row.set("Apellido",  " ");
		row.set("Nombre",  " ");
		row.set("Telefono",  " ");
		row.set("Celular",  " ");
		row.set("Edad",  " ");
		row.set("Responsable", " ");
		row.set("Fecha Venta",  " ");
		row.set("Dias deuda",  " ");
		rowModel.add(row);			

		row= new com.institucion.controller.RowModel();
		row.set("Apellido",  " ");
		row.set("Nombre",  " ");
		row.set("Telefono",  " DEUDA TOTAL: ");
		row.set("Celular",  String.valueOf(total));
		row.set("Edad",  " ");
		row.set("Responsable", " ");
		row.set("Fecha Venta",  " ");
		row.set("Dias deuda",  " ");
		rowModel.add(row);			

		
		
		return rowModel;
	}
		

	private int getDeuda(List<Subscripcion> listSubscripcion){
		int cantDeuda= 0;
		if(listSubscripcion != null){
			for (Subscripcion subs : listSubscripcion) {
				
				subs=clienteEJB.loadLazy(subs, true, true, false, false, false);
				
				
				if(subs.getCupoActividadList() != null){
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
					for (CupoActividad cupoAct: subs.getCupoActividadList()) {
						if(cupoAct.getEstado() != null 
								&& cupoAct.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
								((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
									||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
									||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
							
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
	public void venderNuevaSubs(Event evt, int idCliente){
//		com.institucion.fm.conf.Session.setAttribute("idSubs", null);
//		com.institucion.fm.conf.Session.setAttribute("isSubsFromDeudasClientes", true);
//		Cliente aaaa=clienteEJB.findById(new Long(idCliente));
//		com.institucion.fm.conf.Session.setAttribute("idCliente", aaaa);
//		Sessions.getCurrent().setAttribute("max", 0);
//		guardarTemasPaginacion();
//
//		super.saveHistory("filter");
//		super.gotoPage("/institucion/subscripcion-crud.zul");	
	}


	@Override
	public void actualizarPaneldePrecioProducto() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckFiltersClasesDoing(boolean check,
			boolean esCheckTodosLosDias) {
		// TODO Auto-generated method stub
		
	}	
		
		
}

package com.institucion.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClaseEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.CursoEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.ClaseIngresoDelegate;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.desktop.view.ToolbarFilters2;
import com.institucion.desktop.view.ToolbarFiltersClases;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.Actividad;
import com.institucion.model.ActividadYClase;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.Clase;
import com.institucion.model.ClaseList;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteView;
import com.institucion.model.Concepto;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.Curso;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.IngresoAClasesSinFechasAlumnos;
import com.institucion.model.IngresoInscripcionClaseFilter;
import com.institucion.model.IngresoInscripcionClientFilter;
import com.institucion.model.IngresoInscripcionClienteList;
import com.institucion.model.IngresoInscripcionFilter;
import com.institucion.model.IngresoInscripcionSubscripcionList;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.SubsYCurso;
import com.institucion.model.Subscripcion;
import com.institucion.model.SubscripcionDeClases;
import com.institucion.model.SubscripcionDeClasesPorActividad;
import com.institucion.model.SucursalEnum;
import com.institucion.model.TipoDescuentosEnum;
import com.institucion.model.TipoMovimientoCajaEnum;
import com.institucion.model.VencimientoCursoEnum;

public class IngresoInscripcionSelectorComposer extends SelectorFEComposer implements CursosDelegate, ClienteDelegate, ClaseIngresoDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private IngresoInscripcionClienteList clientepanelListGrid;
	private ClaseList clasesListGrid;
	private IngresoInscripcionSubscripcionList subscripcionListGrid;
	private ToolbarFilters2 toolbar2;
	private ToolbarFiltersClases toolbar3;
	

	private PanelFilter filter;
	private PanelFilter filter2;
	private PanelFilter filter3;
	
	private List<Cliente> clienteList;

	private ClienteEJB clienteEJB;
	private ClaseEJB claseEJB;
	private CursoEJB cursoEJB;
	private InscripcionEJB inscripcionEJB;
	private CajaEJB cajaEJB;
	private Window win = null;

	/** The _page size. */
	private final int _pageSize = 6;
	
	/** The user paging. */
	private Paging userPaging;
	
	private Long totalSize=0L;
	
	private String filtrosAnt=null;
	
	/** The id. */
	String id = null;
	
	/** The order. */
	boolean order = false;
	
	public IngresoInscripcionSelectorComposer(){
		super();
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
		inscripcionEJB= BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");

	}

	public void onIngresosEgresos(Event evt) {
		com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelector", true);
		super.saveHistory();
		super.gotoPage("/institucion/ingresosEgresos-crud.zul");		
	}

	
	public void onGastosCentro(Event evt){
		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorGastos", true);
		super.saveHistory();
		super.gotoPage("/institucion/gastos-crud.zul");
	}

	public void onVenderProd(Event evt){
		com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelectorVenta", true);
		super.saveHistory();
		super.gotoPage("/institucion/productos-venta-selector.zul");
	}
	
	
	@Override
	public void sortEvent(Event evt) {
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}

		final Listheader lh = (Listheader) evt.getTarget();
		final String sortDirection = lh.getSortDirection();
		if (sortDirection != null && !"".equals(sortDirection))
			order = sortDirection.equals("ascending")?false:true;
		id = (String)lh.getAttribute("id");
		int pgno = userPaging.getActivePage();
		int ofs = pgno * _pageSize;    	
		getData(ofs, _pageSize, null, id, order, evt);
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}

	}
	
	private ClienteEJB getClientService() {
		if (this.clienteEJB == null) {
			this.clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		}
		return this.clienteEJB;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
		userPaging.setPageSize(_pageSize);
		totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
		userPaging.setTotalSize(totalSize.intValue());
		userPaging.setDetailed(true);
		userPaging.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event event) {
				if(event != null && event instanceof InputEvent && 
						((InputEvent)event).getTarget() != null && 
						((InputEvent)event).getTarget() instanceof Textbox){
					
				}else{
					getClientFilter().getCodBarras().setFocus(true);	
				}

				int pgno = userPaging.getActivePage();
				int ofs = pgno * _pageSize;    	
				getData(ofs, _pageSize, null, id, order, event);
				if(event != null && event instanceof InputEvent && 
						((InputEvent)event).getTarget() != null && 
						((InputEvent)event).getTarget() instanceof Textbox){
					
				}else{
					getClientFilter().getCodBarras().setFocus(true);	
				}

			}
		});
	}
	
	private void  getData( Integer firstResult, Integer MaxResult, String filters, String id, boolean order, Event evt){
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}
		super.setSelectedEntity(clientepanelListGrid);
		// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		if(filters != null)
			clienteList =clienteEJB.findAllConJdbc(filters);
		else
			clienteList =clienteEJB.findAllConJdbc(getClientFilter().getFilters(firstResult, MaxResult,id,order  ));	
		
		Cliente cliSeleccionadoAnteriormente=(Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente");

		clientepanelListGrid.setList(clienteList, cliSeleccionadoAnteriormente);
		
		subscripcionListGrid.setList(null, null, null, null);
		clasesListGrid.setList(null, null);
		super.getSelectedEntity();
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}

	}
	
	public void clear(Event event){
		getClientFilter().clear();
		if(event != null && event instanceof InputEvent && 
				((InputEvent)event).getTarget() != null && 
				((InputEvent)event).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}


//		getFilter().clear();

	}
	public void onCreate() {
		
		com.institucion.fm.conf.Session.setAttribute("idFromIgresoInscripcionSelector", null);
		com.institucion.fm.conf.Session.setAttribute("isClienteFromIngresoInscripcion", null);
		clasesListGrid.setDelegate(this);
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("�Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		clientepanelListGrid.setDelegate(this);
		clientepanelListGrid.setActionComposerDelegate(this);
		clientepanelListGrid.setActionComposerDelegate(this);
		
		
		toolbar2.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
		toolbar3.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
		filter2.getInnerPanel().addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
		filter2.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
		filter3.getInnerPanel().addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
		filter3.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	

		userPaging.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				getClientFilter().getCodBarras().setFocus(true);
			}
		});	
//		clear();
		getIngresoInscripcionClaseFilter().setDelegate(this);
		if(Session.getDesktopPanel().getMessage() == null){
			Session.getDesktopPanel().setMessage("menu");
		}
		
		String query="  select count(*) from users u    "+
					 	" inner join user_group ug on (ug.user_id= u.id)  "+
						" inner join group_permission gp on (gp.group_id = ug.group_id)  "+
						" where u.name='"+Session.getUsername()+"'  " +
						" and gp.permission_id = ( select id from permission where name='tx.toolbar2.create.occasionalaction')  ";
		Long cant=clienteEJB.getCount(query);
		if(cant.intValue() > 0){
			toolbar2.setVisibleVencerElCursoButton(true);
		}else{
			toolbar2.setVisibleVencerElCursoButton(false);
		}

		String query2="  select count(*) from users u    "+
			 	" inner join user_group ug on (ug.user_id= u.id)  "+
				" inner join group_permission gp on (gp.group_id = ug.group_id)  "+
				" where u.name='"+Session.getUsername()+"'  " +
				" and gp.permission_id = ( select id from permission where name='tx.toolbar2.view.chart')  ";
		Long cant2=clienteEJB.getCount(query2);
		if(cant2.intValue() > 0){
			toolbar2.setVisibleSaldaCursoPorErrorElCursoButton(true);
		}else{
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
			Sessions.getCurrent().removeAttribute("isSubsFromIngresoInscripcion");
			Sessions.getCurrent().removeAttribute("max");
			Sessions.getCurrent().removeAttribute("actYClase");
			Sessions.getCurrent().removeAttribute("isSubsFromIngresoInscripcion");
			Sessions.getCurrent().removeAttribute("idSubscripcion");
			Sessions.getCurrent().removeAttribute("idCliente");
			Sessions.getCurrent().removeAttribute("idSubscripcion");
			Sessions.getCurrent().removeAttribute("isSubsFromIngresoInscripcion");
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
			getClientFilter().getCodBarras().setValue(null);
			clear(null);
		}
		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu

		if(!VinoDelMenu){

			if(Sessions.getCurrent().getAttribute("pagno") != null)
				userPaging.setActivePage((Integer)Sessions.getCurrent().getAttribute("pagno"));
			
			if(Sessions.getCurrent().getAttribute("col") != null)
				id=(String)Sessions.getCurrent().getAttribute("col");
			
			if(Sessions.getCurrent().getAttribute("order") != null)
				order=(Boolean)Sessions.getCurrent().getAttribute("order");
				
			if(Sessions.getCurrent().getAttribute("total") != null)
				totalSize=((Integer)Sessions.getCurrent().getAttribute("total")).longValue();

			
			if(Sessions.getCurrent().getAttribute("filters") != null)
				filtrosAnt=(String)Sessions.getCurrent().getAttribute("filters");
			
		}else{
			Sessions.getCurrent().setAttribute("pagno", null);
			Sessions.getCurrent().setAttribute("filters", null);
			Sessions.getCurrent().setAttribute("total",  null);
			Sessions.getCurrent().setAttribute("col",  null);
			Sessions.getCurrent().setAttribute("order",  null);
		}
		getClientFilter().getCodBarras().setFocus(true);

		onFind(VinoDelMenu, false, null);
		
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		filter3.getInnerPanel().setOpen(true);
		filter3.setOpen(true);
		
		if(clasesListGrid.getItems() != null && clasesListGrid.getItems().size() >0){
			clasesListGrid.setVisible(true);
			toolbar3.setVisible(true);
			filter3.setVisible(true);
		}else{
			clasesListGrid.setVisible(false);
			toolbar3.setVisible(false);
			filter3.setVisible(false);
		}
		getIngresoInscripcionFilter().setDelegate(this);
		filter2.getInnerPanel().setOpen(true);
		filter2.setOpen(true);
		getClientFilter().setActionComposerDelegate(this);
		refreshEvents();
		getClientFilter().getCodBarras().setFocus(true);
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
		
//		getClientFilter().getDni().addEventListener(Events.ON_OK, new EventListener() {
//			public void onEvent(Event evt){
//				onFind(evt);
//			}
//		});	
	}
	
	public void onMatriculaGratis(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("idClienteFromSubs", false);
		getClientFilter().getCodBarras().setFocus(true);

		if (hasSelectedOneItemSinMensaje(clientepanelListGrid)){
			com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
			
			guardarTemasPaginacion();
			super.saveHistory("filter");
			super.gotoPage("/institucion/matricula-gratis-crud.zul");
		
		}else{
			MessageBox.validation(I18N.getLabel("subscripcion.seleccionar.cliente"), I18N.getLabel("selector.actionwithoutitem.title"));
			return;	
		}
	}

	public void onVerCumplesContratados(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);
		super.saveHistory("filter");
		super.gotoPage("/institucion/cumples-selector.zul");
	}
	
//	/* CREAR NUEVO CLIENTE */
	public void onInsert(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		com.institucion.fm.conf.Session.setAttribute("idCliente", null);
		com.institucion.fm.conf.Session.setAttribute("isClienteFromIngresoInscripcion", true);

		eliminarTemasPaginacion();
		super.saveHistory("filter");
		super.gotoPage("/institucion/clientes-crud.zul");
	}

	/* CREAR NUEVA CLASE */
	public void onInsertClass(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		com.institucion.fm.conf.Session.setAttribute("idClase", null);
		com.institucion.fm.conf.Session.setAttribute("isClaseFromIngresoInscripcion", true);
		com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
		guardarTemasPaginacion();
		super.saveHistory("filter");

		super.gotoPage("/institucion/clase-crud.zul");
	}

	/* CREAR NUEVA SUBSCRIPCION */
	public void onInsertSubs(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		if (hasSelectedOneItemSinMensaje(clientepanelListGrid)){
			float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(((Cliente)clientepanelListGrid.getSelectedItem().getValue()).getId()), true);

			if(cant > 0){
				MessageBox.validation("Para realizar nueva venta, se debe saldar la DEUDA ANTERIOR", "Accion cancelada");
				return;	
			}
			
			com.institucion.fm.conf.Session.setAttribute("idSubs", null);
			com.institucion.fm.conf.Session.setAttribute("isSubsFromIngresoInscripcion", true);
			com.institucion.fm.conf.Session.setAttribute("idCliente", (Cliente)clientepanelListGrid.getSelectedItem().getValue());
			
			Sessions.getCurrent().setAttribute("max", 0);
			
			guardarTemasPaginacion();

			super.saveHistory("filter");
			super.gotoPage("/institucion/subscripcion-crud.zul");
		
		}else{
			MessageBox.validation(I18N.getLabel("subscripcion.seleccionar.cliente"), I18N.getLabel("selector.actionwithoutitem.title"));
			return;	
		}
	}
	
	public void buscarSubscripcionesDesdeFiltro(){
//		getClientFilter().getCodBarras().setFocus(true);

		onFind2(true);
	}
	
	/*
  	Hacer alguna funcionalidad que permita sacar de la clase, y/o devolver cupo.
		- Sacarlo del estado 'En Clase'
		- Tener en cuenta si se habia pasado ya a Sin cupos el curso, de volver a actualizar la subscripcion( estado y cupo), actualizar el estado del cliente.	
		- si ya se le habia tomado lista, sacarlo de la lista de presentes
		- si se le saca de la lista, luego tambien actualizar la cantidad de presentes.
		- Tener en cuenta tambien de mostrar como Posibles errores de Control  en 
			-Ver donde se registran por cliente todas las 

	 */
	public void onQuitarDeClase (Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		if (hasSelectedOneItem(clientepanelListGrid) && hasSelectedOneItem(clasesListGrid)){
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();

			if(cli != null ){
				
				Cliente cli2=clienteEJB.findById(cli.getId());
				cli2=clienteEJB.loadLazy(cli2, true, true, true, false);
				cli.setIsEnClase(cli2.getIsEnClase());
				if((cli2.getIsEnClase() != null && cli2.getIsEnClase() <= 0) || cli2.getIsEnClase() == null){
					MessageBox.validation(I18N.getLabel("curso.error.seleccion.select.error.select.saldar.solo.deuda.inscri.niguna"), 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;	
					
				}else{
					Clase clase=(Clase)clasesListGrid.getSelectedItem().getValue();
					clase=clienteEJB.loadLazy(clase, true, true, false, false);
					if (clase != null) {

						Object[] params2 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , clase.getNombre() };
						if (MessageBox.question(I18N.getLabel("subscripcion.list.title.devolver.cupos", params2), 
								I18N.getLabel("subscripcion.list.title.devolver.cupo.title"))){
							
							// sumarle el cupo de nuevo al cliente
							Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();	
//							subs=inscripcionEJB.findSubscripcionById(subs.getId());
							ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");
							if(subs.getCupoActividadList() != null){
								
								
//								Antes de seguir el proceso de quitar de la clase:
//									- verificar que la clase que se quiere quitar de la subscripcion sea de la misma subscripcion a la que se ingreso, 
//										ya que se pudo ahber ingresado en una subscripcion pero otro curso.
								
								boolean seleccionoLaSubsConClientesEnClase=false;
								if(clase.getIngresoAClaseSinFechas() != null){
									for (IngresoAClasesSinFechasAlumnos object : clase.getIngresoAClaseSinFechas()) {
										if(object.getCliente().getId().intValue() == cli.getId().intValue()){
											if(object.getSubscripcion().getId().intValue() == subs.getId().intValue()
													&& object.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue()){
												seleccionoLaSubsConClientesEnClase= true;
											}
										}
									}
								}		
								
								if(!seleccionoLaSubsConClientesEnClase){
									MessageBox.validation("Selecciono una subscripcion que no contiene clientes en clase. Para quitar un cliente, seleccione la subscripcion correcta", 
											I18N.getLabel("selector.actionwithoutitem.title"));
									return ;	
								}

								for (CupoActividad cupoAct : subs.getCupoActividadList()) {
									if(cupoAct.getCurso() != null && actYClase.getCurso() != null &&
											cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
										if(cupoAct.getActividad().getId().equals(actYClase.getActiv().getId())){
											if(cupoAct.getCupos() == 99){
												
												// sacarlo de la lista de enClase de la Clase. private Set<Cliente> clienesEnClase;
												HashSet list= new HashSet();
												clase=clienteEJB.loadLazy(clase, true, true, false, false);

												for (Cliente clies: clase.getClienesEnClase()) {
													if(clies.getId().intValue() == cli.getId().intValue()){
													}else
														list.add(clies);
												}
												clase.setClienesEnClase(list);
												
												
												HashSet listIngresos= new HashSet();
												if(clase.getIngresoAClaseSinFechas() != null){
													for (IngresoAClasesSinFechasAlumnos ingresos: clase.getIngresoAClaseSinFechas()) {
														if(ingresos.getClase().getId().intValue() ==  clase.getId().intValue()
																&& ingresos.getCliente().getId().intValue() ==  subs.getCliente().getId().intValue()
																&& ingresos.getSubscripcion().getId().intValue() ==  subs.getId().intValue()
																&& ingresos.getCurso().getId().intValue() ==  cupoAct.getCurso().getId().intValue()){
														}else
															listIngresos.add(ingresos);
													}			
												}
												clase.setIngresoAClaseSinFechas(listIngresos);
												
												claseEJB.save(clase);
												onFind(false, false, null);
											}else{
												subs=clienteEJB.loadLazy(subs, false, false, true, false, false);

												Integer cantidad=getConceptoObraSocialYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());

												// si es obra social y pago un adicional preguntar si se devuelve el valor de la clase
												// si se devuelve
													// eliminarle el pago de Pagos de la subscripcion
													// 	hacer movimiento en caja de devolucion
													// Eliminar el concepto tambien
												
												if(cantidad != null && cantidad.intValue() > 0){
													Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
													
													// si tiene mas de un cupo utilizado
													 //y la obra social estaba configurada para una unica vez
														//  no devolver.
														// sino SI devolver
													if(tieneMasDeUnCupoGastado(subs, actYClase) ){
														concepttt.setObraSocial(clienteEJB.loadLazy(concepttt.getObraSocial()));
														if(pagaCopagoSegunActividad(concepttt.getObraSocial().getPreciosActividadesObraSocial(), actYClase.getActiv())){
															
														}else{
															
															// es por que paga todas las veces, tonces hay que devolver
															if (MessageBox.question("� Desea devolver tambien el dinero del COPAGO de $"+formateador.format(cantidad) +"?"
																	,I18N.getLabel("subscripcion.list.title.anular.title"))){
																
																PagosPorSubscripcion pagoToDelete= null;
																// Elimino el PagoPorSubscripcion
																for (PagosPorSubscripcion pagos : subs.getPagosPorSubscripcionList()) {
																	
																	if(pagos.getCantidadDinero().intValue() == cantidad &&
																			pagos.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()
																			&& pagos.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
																		
																		pagoToDelete= pagos;
																		
																		break;
																	}
																}
																if(pagoToDelete != null)
																	subs.getPagosPorSubscripcionList().remove(pagoToDelete);
																
																Concepto concepToDelete= null;
																// Elimino el Concepto
																for (Concepto conc : subs.getConceptoList()) {
																	
																	if(conc.getActividadDelConcepto() != null 
																			&&  conc.getActividadDelConcepto().getId().intValue() == actYClase.getActiv().getId().intValue()
																			&& conc.getCurso() != null && actYClase.getCurso() != null 
																			&& conc.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue()
																			&& conc.getCantSesiones() ==1
																			&& conc.getConcepto() != null && conc.getConcepto().contains("Copago")){
																		
																			concepToDelete = conc;
																			break;
																	}
																}
																
																if(concepToDelete != null)
																	subs.getConceptoList().remove(concepToDelete);
																
																
																if(subs.getPrecioCursosMatricula() != null)
																	subs.setPrecioCursosMatricula(subs.getPrecioCursosMatricula().intValue() - cantidad);
																else
																	subs.setPrecioCursosMatricula(0);
																
																if(subs.getPrecioTOTALadicionalTarjeta()!= null)
																	subs.setPrecioTOTALadicionalTarjeta(subs.getPrecioTOTALadicionalTarjeta().intValue() - cantidad);
																else
																	subs.setPrecioTOTALadicionalTarjeta(0);
															
																
																
																// Genero Movimiento caja Egreso
																// Se genera el movimiento en Caja
																CajaMovimiento caja2= new CajaMovimiento();
																caja2.setConcepto("Se devolvio dinero del copago por Quitar al cliente de clase " 
																		 + actYClase.getActiv().getNombre() + ".Valor $"+formateador.format(cantidad) + 
																		 " Cliente: " + subs.getCliente().getApellido().toUpperCase() + " " +subs.getCliente().getNombre().toUpperCase());
																
																caja2.setFecha(new Date());
																caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
																caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
																caja2.setValor(new Double(cantidad));
																caja2.setSucursal(getSucursal(subs));
																caja2.setCliente(subs.getCliente());
																caja2.setIdSubscripcion(subs.getId().intValue());

																cajaEJB.save(caja2);	
															}						
															
														}														
													}else{
														if (MessageBox.question("� Desea devolver tambien el dinero del COPAGO de $"+formateador.format(cantidad)  +"?"
																,I18N.getLabel("subscripcion.list.title.anular.title"))){
															
															PagosPorSubscripcion pagoToDelete= null;
															// Elimino el PagoPorSubscripcion
															for (PagosPorSubscripcion pagos : subs.getPagosPorSubscripcionList()) {
																
																if(pagos.getCantidadDinero().intValue() == cantidad &&
																		pagos.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.EFECTIVO.toInt()
																		&& pagos.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
																	
																	pagoToDelete= pagos;
																	
																	break;
																}
															}
															if(pagoToDelete != null)
																subs.getPagosPorSubscripcionList().remove(pagoToDelete);
															
															Concepto concepToDelete= null;
															// Elimino el Concepto
															for (Concepto conc : subs.getConceptoList()) {
																
																if(conc.getActividadDelConcepto() != null 
																		&&  conc.getActividadDelConcepto().getId().intValue() == actYClase.getActiv().getId().intValue()
																		&& conc.getCurso() != null && actYClase.getCurso() != null 
																		&& conc.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue()
																		&& conc.getCantSesiones() ==1
																		&& conc.getConcepto() != null && conc.getConcepto().contains("Copago")){
																	
																		concepToDelete = conc;
																		break;
																}
															}
															
															if(concepToDelete != null)
																subs.getConceptoList().remove(concepToDelete);
															
															
															if(subs.getPrecioCursosMatricula() != null)
																subs.setPrecioCursosMatricula(subs.getPrecioCursosMatricula().intValue() - cantidad);
															else
																subs.setPrecioCursosMatricula(0);
															
															if(subs.getPrecioTOTALadicionalTarjeta()!= null)
																subs.setPrecioTOTALadicionalTarjeta(subs.getPrecioTOTALadicionalTarjeta().intValue() - cantidad);
															else
																subs.setPrecioTOTALadicionalTarjeta(0);
														
															
															
															// Genero Movimiento caja Egreso
															// Se genera el movimiento en Caja
															CajaMovimiento caja2= new CajaMovimiento();
															caja2.setConcepto("Se devolvio dinero del copago por Quitar al cliente de clase " 
																	 + actYClase.getActiv().getNombre() + ".Valor $"+ formateador.format(cantidad)  + 
																	 " Cliente: " + subs.getCliente().getApellido().toUpperCase() + " " +subs.getCliente().getNombre().toUpperCase());
															
															caja2.setFecha(new Date());
															caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
															caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
															caja2.setValor(new Double(cantidad));
															caja2.setSucursal(getSucursal(subs));

															caja2.setCliente(subs.getCliente());
															caja2.setIdSubscripcion(subs.getId().intValue());

															cajaEJB.save(caja2);	
														}						
														
													}
												}
												int cuposTotales= cupoAct.getCupos() +1;
												cupoAct.setCupos(cuposTotales);
												
												// actualizar el estado de la subscripcion considerando que se le sumo 1 nuevo cupo
												// ver si este metodo funciona bien , cuando el cupo estaba en 0, y ya estaba SIn cupo, o esta adeudado sin cupo o con deuda y cupos
												subs=clienteEJB.actualizarSubscripcionQuitarDeClase(subs, actYClase, cuposTotales);
												inscripcionEJB.save(subs);
							
												cli= subs.getCliente();

												// ver ahora que por ahi el cleinte esta en estado ADEUDA o SIN CUPO, si ahora se actualiza a COn CUPO
												subs.setCliente(cli);
												cli =  subs.getCliente();
												
												List<Subscripcion> listSubs=inscripcionEJB.findAllSubscripcionesByClient(cli.getId());
												if(listSubs != null)
													cli.setSubscripcionesList(new HashSet(listSubs));
												
												Cliente cliToDelete= null;
												clase=clienteEJB.loadLazy(clase, true, true, false, false);

												// sacarlo de la lista de enClase de la Clase. private Set<Cliente> clienesEnClase;
												for (Cliente clies: clase.getClienesEnClase()) {
													if(clies.getId().intValue() == cli.getId().intValue())
														cliToDelete = clies;
												}
												if(cliToDelete != null)
													clase.getClienesEnClase().remove(cliToDelete);
												
												IngresoAClasesSinFechasAlumnos ingresoAClasesSinFechasAlumnosToDelete= null;

												// sacarlo de la lista de enClase de la Clase. private Set<Cliente> clienesEnClase;
												if(clase.getIngresoAClaseSinFechas() != null){
													for (IngresoAClasesSinFechasAlumnos clies: clase.getIngresoAClaseSinFechas()) {
														if(clies.getClase().getId().intValue() ==  clase.getId().intValue()
																&& clies.getCliente().getId().intValue() ==  subs.getCliente().getId().intValue()
																&& clies.getSubscripcion().getId().intValue() ==  subs.getId().intValue()
																&& clies.getCurso().getId().intValue() ==  cupoAct.getCurso().getId().intValue())
															ingresoAClasesSinFechasAlumnosToDelete = clies;
													}
												}
												
												if(ingresoAClasesSinFechasAlumnosToDelete != null)
													clase.getIngresoAClaseSinFechas().remove(ingresoAClasesSinFechasAlumnosToDelete);
												
												
												// actualizar el estado del cliente
												clienteEJB.save(cli);
												claseEJB.save(clase);

												// crear algun nuevo objeto hbm donde se guarden las devoluciones de cupos asociadas al cliente y registrar cada una
												CajaMovimiento caja2= new CajaMovimiento();
												caja2.setConcepto("Se Devolvio Cupo al cliente al retirarlo de la clase/sesion: "+clase.getNombre());
												caja2.setFecha(new Date());
												caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
												caja2.setTipoMovimiento(TipoMovimientoCajaEnum.EGRESO);
												caja2.setValor(new Double(0));
												caja2.setSucursal(getSucursal(subs));

												caja2.setCliente(subs.getCliente());
												caja2.setIdSubscripcion(subs.getId().intValue());

												cajaEJB.save(caja2);						
												
												onFind(false, false, null);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		getClientFilter().getCodBarras().setFocus(true);

	}
	
	private String imprimirVentaDiaHoy(Cliente clienteDeBD){
		Boolean bool=cursoEJB.findImprimible();
		String cosasAImprimir=null;

		if(bool != null && bool.booleanValue()){
			if(clienteDeBD.getSubscripcionesList() != null){
				for (Subscripcion subscripcion : clienteDeBD.getSubscripcionesList() ) {
					subscripcion=clienteEJB.loadLazy(subscripcion, true, true, true, true, true);

					Calendar startC = Calendar.getInstance();
					startC.setTime(new Date());
					startC.set(Calendar.HOUR_OF_DAY, 00);
					startC.set(Calendar.MINUTE, 00);
					startC.set(Calendar.SECOND, 00);
					startC.set(Calendar.MILLISECOND, 00);

					Calendar start2 = Calendar.getInstance();
					start2.setTime(subscripcion.getFechaYHoraCreacion());
					start2.set(Calendar.HOUR_OF_DAY, 00);
					start2.set(Calendar.MINUTE, 00);
					start2.set(Calendar.SECOND, 00);
					start2.set(Calendar.MILLISECOND, 00);

					// si son iguales, es una vente auq se hizo hoy, entonces puedo imprimir comprobante
					if(startC.getTime().compareTo(start2.getTime())== 0){
						
						float pagosRealizados=0;
						for (PagosPorSubscripcion pagoPorSubscripcion: subscripcion.getPagosPorSubscripcionList()) {
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
						int precioDelCurso=0;
						double pagosRealizadosTotales= pagosRealizados;
						
						int precioMatricula=0;
						if(subscripcion.getConceptoList() != null){
							for (Concepto concep : subscripcion.getConceptoList()) {
								if(concep.getCurso() == null){
									precioMatricula=concep.getImporteConDescuento();
								}
							}
						}
						
						if(subscripcion.getConceptoList() != null){
							for (Concepto concep : subscripcion.getConceptoList()) {
								boolean esObraSocial=false;
								if(concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
									esObraSocial= true;
								}

								if(concep.getCurso() != null && concep.getCurso().isPagaSubscripcion()){
									if(precioMatricula >0){
										precioDelCurso=concep.getImporteConDescuento();
										precioDelCurso= precioDelCurso+ precioMatricula;
										precioMatricula=0;
									}else{
										precioDelCurso=concep.getImporteConDescuento();
									}
								}else if(concep.getCurso() != null && !concep.getCurso().isPagaSubscripcion()){
									precioDelCurso=concep.getImporteConDescuento();
								}
								
								if(concep.getCurso() != null){
									// tengo que pagar precioDelCurso
									double nuevoVal=0;
									if(pagosRealizadosTotales > precioDelCurso){
										nuevoVal=precioDelCurso;
									}else if(pagosRealizadosTotales <= precioDelCurso){
										nuevoVal=pagosRealizadosTotales;
									}
									if(pagosRealizadosTotales > 0)
										pagosRealizadosTotales= pagosRealizadosTotales-nuevoVal;

//									encontroAlgoPaImprimir= true;
									String ss=Imprimir.imprimirComprobante("Comprob de Actividad", subscripcion.getCliente(), 
											concep.getActividadDelConcepto().getNombre(), esObraSocial, String.valueOf(nuevoVal), null,
											concep.getActividadDelConcepto().isImprimeComprobante(), concep.getCurso().getIdTipoCurso(), 0, false);
									if(cosasAImprimir == null){
										cosasAImprimir= ss;	
									}else{
										cosasAImprimir= cosasAImprimir+"<br/>" + ss;	
									}
								}
							}
						}
					}						
				}
			}
		}
		return cosasAImprimir;
	}
	
	
	private String imprimirIngresoEnVentaDiaHoy(Cliente clienteDeBD){
//		boolean encontroAlgoPaImprimir=false;
		Boolean bool=cursoEJB.findImprimible();
		String cosasAImprimir=null;
		if(bool != null && bool.booleanValue()){
			if(clienteDeBD.getSubscripcionesList() != null){
				for (Subscripcion subscripcion : clienteDeBD.getSubscripcionesList() ) {
					subscripcion=clienteEJB.loadLazy(subscripcion, true, true, true, true, true);

//					Calendar startC = Calendar.getInstance();
//					startC.setTime(new Date());
//					startC.set(Calendar.HOUR_OF_DAY, 00);
//					startC.set(Calendar.MINUTE, 00);
//					startC.set(Calendar.SECOND, 00);
//					startC.set(Calendar.MILLISECOND, 00);
//
//					Calendar start2 = Calendar.getInstance();
//					start2.setTime(subscripcion.getFechaYHoraCreacion());
//					start2.set(Calendar.HOUR_OF_DAY, 00);
//					start2.set(Calendar.MINUTE, 00);
//					start2.set(Calendar.SECOND, 00);
//					start2.set(Calendar.MILLISECOND, 00);

					// si son iguales, es una vente auq se hizo hoy, entonces puedo imprimir comprobante
//					if(startC.getTime().compareTo(start2.getTime())== 0){
						
						List<Clase> clases2 =claseEJB.findAllConJdbc
								("select id from clase where id  in ( select idclase from cliente_clase where idcliente= "+clienteDeBD.getId()+" )");
						if(clases2 != null){
							for (Clase claseDeBD : clases2) {
								claseDeBD=clienteEJB.loadLazy(claseDeBD, true, true, false, false);
								
								for (CupoActividad cupoAct : subscripcion.getCupoActividadList()) {
									if(cupoAct.getCurso() != null 
											&& cupoAct.getActividad().getId().intValue() == claseDeBD.getActividad().getId()){
											
										boolean esObraSocial=false;

										if(subscripcion.getConceptoList() != null){
											for (Concepto concep : subscripcion.getConceptoList()) {
												if(concep.getCurso() != null &&
														concep.getCurso().getId().intValue() ==  cupoAct.getCurso().getId().intValue() &&
														concep.getActividadDelConcepto().getId().intValue() == cupoAct.getActividad().getId().intValue() &&
														concep.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
													esObraSocial= true;
												}
											}
										}
										if(esObraSocial){
											
											cosasAImprimir=	Imprimir.imprimirComprobante("Comprob para el Profesional", clienteDeBD, 
													cupoAct.getActividad().getNombre(), true, String.valueOf(0), String.valueOf(0),
													cupoAct.getActividad().isImprimeComprobante(), cupoAct.getCurso().getIdTipoCurso(), cupoAct.getCupos(), false);
										}else{
											cosasAImprimir=		Imprimir.imprimirComprobante("Comprob para el Profesional", clienteDeBD, 
													cupoAct.getActividad().getNombre(), false, String.valueOf(0), String.valueOf(0), 
													cupoAct.getActividad().isImprimeComprobante(), cupoAct.getCurso().getIdTipoCurso(), cupoAct.getCupos(), false);
										}
									}
								}
							}
						}
//					}						
				}
			}	
		}
		return cosasAImprimir;
	}

	public void onReimprimirSubs(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		if (hasSelectedOneItem(clientepanelListGrid)){
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
			if(cli != null ){
				Cliente clienteDeBD=clienteEJB.findById(cli.getId());
				clienteDeBD=clienteEJB.loadLazy(clienteDeBD, true, true, true, true);
				// Imprime venta del dia de hoy
				String imprimir=null;
				String cosasAImprimir=imprimirVentaDiaHoy(clienteDeBD);

				
				// imprime ingresos a Clase del dia de hoy
				String cosasAImprimir2=imprimirIngresoEnVentaDiaHoy(clienteDeBD);
				
				if(cosasAImprimir != null)
					imprimir= cosasAImprimir;
				
				if(cosasAImprimir2 != null){
					if(imprimir != null){
						imprimir= imprimir+"<br/>" + cosasAImprimir2;
					}else{
						imprimir=  cosasAImprimir2;
					}
				}

				if(imprimir != null){
					Imprimir.imprimirComprobante(imprimir);
				}
				if(cosasAImprimir == null && cosasAImprimir2 == null){
					MessageBox.validation("Para el cliente seleccionado no se le encontro nada con fecha de hoy para Re-imprimir.", 
							I18N.getLabel("selector.actionwithoutitem.title"));
					return ;

				}
			}
		}
		// ver si tiene subscripciones nuevas con fecha de hoy
		getClientFilter().getCodBarras().setFocus(true);

	}
	

	public void onSaldarDeudaSubs(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

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
					com.institucion.fm.conf.Session.setAttribute("isSubsFromIngresoInscripcion", true);
					
					guardarTemasPaginacion();
					super.saveHistory("filter");
					
					super.gotoPage("/institucion/subscripcion-saldar-deuda-crud.zul");
				}
			}
		}
		getClientFilter().getCodBarras().setFocus(true);

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
		getClientFilter().getCodBarras().setFocus(true);
		
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
						com.institucion.fm.conf.Session.setAttribute("isSubsFromIngresoInscripcion", true);
						
						guardarTemasPaginacion();
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
		getClientFilter().getCodBarras().setFocus(true);

	}

	
	/* POSPONER VENCIMiENTO */
	public void onPosponerSubs(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("fechaFinPosponer", null);
		com.institucion.fm.conf.Session.setAttribute("actYClasePosponer", null);
		getClientFilter().getCodBarras().setFocus(true);

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
						
						MessageBox.validation("No se puede posponer una subscripci�n que aun no comenzaron.", 
								I18N.getLabel("selector.actionwithoutitem.title"));
						return ;

					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripci�n anulada anteriormente", 
									I18N.getLabel("selector.actionwithoutitem.title"));
							return ;
		
					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripci�n sin cupos", 
									I18N.getLabel("selector.actionwithoutitem.title"));
							return ;						

					}else if(cupo.getEstado() != null 
							&&((cupo.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt() ))){
				
							MessageBox.validation("No se puede posponer una subscripci�n sin cupos", 
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
					com.institucion.fm.conf.Session.setAttribute("isSubsFromIngresoInscripcion", true);
					
					guardarTemasPaginacion();
					super.saveHistory("filter");
					com.institucion.fm.conf.Session.setAttribute("fechaFinPosponer", fechaFin);
					com.institucion.fm.conf.Session.setAttribute("actYClasePosponer", actYClase);

					
					super.gotoPage("/institucion/subscripcion-posponer-crud.zul");
					
				}
			}
		}
		getClientFilter().getCodBarras().setFocus(true);

	}	
	

	private boolean clienteExisteYaEnClase(Cliente cliente , Set <Cliente> clases){
		
		if(clases != null){
			for (Cliente cliente2 : clases) {
				
				if(cliente2.getId().equals(cliente.getId()))
					return true;
				
			}
			
		}
	return false;	
	}
	
	/* ASIGNAR CLASE */
	public void onAsignClass(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);
		boolean isFromCodigoBarrasBuscar= false;
		String nombreFinalCliente= null;
		if(event != null && event.getName().equalsIgnoreCase("buscar"))
			isFromCodigoBarrasBuscar= true;
		
		if(event != null && event.getName().equalsIgnoreCase("onRightClick")){
			((Listitem)event.getTarget()).setSelected(true);
		}
			
		if (hasSelectedOneItem(clientepanelListGrid)){
			if (hasSelectedOneItem(subscripcionListGrid)){
				if (hasSelectedOneItem(clasesListGrid)){
					
					
					//verificar si tengo otra subscripcion al mismo curso, que tenga un vencimiento anterior y que le queden cupos .
					Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
					ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");
					
					com.institucion.fm.conf.Session.setAttribute("idCliente", cli);
					
					
					boolean result= false;
					if(isFromCodigoBarrasBuscar){
						String clasxes= null;
						
						if(cli 	!= null && cli.getApellido() != null){
							nombreFinalCliente=cli.getApellido() ;
						}
						if(cli 	!= null && cli.getNombre() != null){
							nombreFinalCliente=nombreFinalCliente + ", "+ cli.getNombre() ;
						}
						
						if(clasesListGrid.getSelectedItem() != null){
							clasxes= ((Clase)clasesListGrid.getSelectedItem().getValue()).getNombre();
						}
						String preguntas= "� Desea ingresar a "+nombreFinalCliente+ " a clase ?";
						if(clasxes != null){
							preguntas=  "� Desea ingresar a "+nombreFinalCliente+ " a la clase "+clasxes +" ?";;
						}
						if (MessageBox.question(preguntas, "Ingreso clientes a clase/Tratamientos")){
							result= true;
						}
					}
					
					if((isFromCodigoBarrasBuscar && result) || !isFromCodigoBarrasBuscar ){
						
						// SI EL CURSO ERA LIBRE, ENTONCES NO SE DEBE HACER NADa
						if(actYClase.getCurso() != null && 
							(actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt()
							||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt()
									||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt()
									||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt())){
							
							
							Subscripcion subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
							subsSeleccionada=clienteEJB.loadLazy(subsSeleccionada, true, true, true, true, true);

							CupoActividad cupo= null;
							if(subsSeleccionada.getCupoActividadList() != null ){
								for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
									if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
										cupo= iterable_element;
									
								}
							}
							
							if(cupo != null && 
									(cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() 
									 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
									 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt())){
								MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.con.subs.estado.inco"), I18N.getLabel("selector.actionwithoutitem.title"));
								return;		
							} 
							
							com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
					
							Clase clase =(Clase)clasesListGrid.getSelectedItem().getValue();
							Clase claseDeBD=claseEJB.findById(clase.getId());
							claseDeBD=clienteEJB.loadLazy(claseDeBD, true, true, false, false);

							
							if(clienteExisteYaEnClase(subsSeleccionada.getCliente(), claseDeBD.getClienesEnClase())){
								MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.ya.asignado"), I18N.getLabel("selector.actionwithoutitem.title"));
								return;		
							}
							
							
							subsSeleccionada=clienteEJB.actualizarSubscripcion(subsSeleccionada, actYClase);
							Cliente cll=subsSeleccionada.getCliente();
							cll=clienteEJB.loadLazy(cll, true, true, true,true);

							if(cll.getSubscripcionesList() != null){
								for (Subscripcion subs : cll.getSubscripcionesList()) {
									if(subs.getId().intValue() == subsSeleccionada.getId().intValue()){
										
										if(subs.getCupoActividadList() != null ){
											for (CupoActividad iterable_element : subs.getCupoActividadList()) {
												
												if(subsSeleccionada.getCupoActividadList() != null ){
													for (CupoActividad iterable_elementSeleccionada : subs.getCupoActividadList()) {
														if(iterable_elementSeleccionada.getCurso().getId().intValue() == iterable_element.getCurso().getId().intValue()){
															iterable_element.setEstado(iterable_elementSeleccionada.getEstado());
														}
													}
												}
											}
										}
									}
								}
							}
							subsSeleccionada.setCliente(cll);
							cli= subsSeleccionada.getCliente();
							
							// se guarda en la BD
							inscripcionEJB.save(subsSeleccionada);
							
							List<Subscripcion> listSubs=inscripcionEJB.findAllSubscripcionesByClient(cli.getId());
							if(listSubs != null)
								cli.setSubscripcionesList(new HashSet(listSubs));
							
							if(claseDeBD.getClienesEnClase() != null){
//								Error el cliente ya estaba en clase
								claseDeBD.getClienesEnClase().add(cli);
							}else{
								claseDeBD.setClienesEnClase(new HashSet());
								claseDeBD.getClienesEnClase().add(cli);
							}
							agregarClienteAClasesSinFechas(cli, claseDeBD, cupo.getCurso(), subsSeleccionada);
							claseEJB.save(claseDeBD);
							
//							boolean isObraSocial= false;
//							Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//							if(concepttt != null)
//								isObraSocial= true;
//							Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//									actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), null);

							clienteEJB.save(cli);
							Object[] params2 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase()};
							MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
							
							com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
							
							if(isFromCodigoBarrasBuscar){
								// si viene del codigo de barras entonces solo limpio las clases
								actualizarListasPantallaCodigoDeBarras();
							}else
								actualizarListasPantalla();
							return;		
												
						}else{
							
							Clase clase =(Clase)clasesListGrid.getSelectedItem().getValue();
							Clase claseDeBD=claseEJB.findById(clase.getId());
							
							claseDeBD=clienteEJB.loadLazy(claseDeBD, true, true, false, false);
							if(clienteExisteYaEnClase(cli, claseDeBD.getClienesEnClase())){
								MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.ya.asignado"), I18N.getLabel("selector.actionwithoutitem.title"));
								return;		
							}
							
							Object[] params2 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase()};
							
							//si NO es del codigo de barras
							if(!isFromCodigoBarrasBuscar){
								Subscripcion subs =verificarSiElClienteTieneSubsDelMismoCursoActivasConCupos();

								if(subs != null){
									
									if (MessageBox.question(I18N.getLabel("subscripcion.list.title.wantcontratar"), I18N.getLabel("subscripcion.list.title"))){
										// el usuario desea que se descuente del curso viejo
										
										// verifico por estados de que NO se pueda ingresar a clase cuando la subs esta vencida 
										//VENCIDA_CON_DEUDA(1), VENCIDA(5), ANULADA(6), S_CUPOS(8)
										CupoActividad cupo= subs.getCupoActividadParaCalcular();

										if(cupo != null && 
												(cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() 
												 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
												 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt())){
											MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.con.subs.estado.inco"), I18N.getLabel("selector.actionwithoutitem.title"));
											return;		
										} 
										actYClase = (ActividadYClase)new ArrayList(cupo.getCurso().getActividadYClaseList()).get(0);
										subs= clienteEJB.loadLazy(subs, true, false, true, false, false);
										
										// se obtiene la actividadYClase de la subscripcion vieja
										Integer cantidad=getConceptoObraSocialYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
										
										if(cantidad != null && cantidad.intValue() >0 ){
											// quiere decir que es por obra social
											// si viene de la obra Social, se debe descontar abonar adicional
											if(tieneCupos(subs, actYClase)){
												
												// si no se abono el copago todavia
												if(!pagoElCopago(subs, actYClase)){
													Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };

													if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
															I18N.getLabel("cliente.abonar.adicional.title"))){
														
														if(descontarCupo(subs, actYClase, cantidad, true, true)){
															com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subs);
															com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
															
//															boolean isObraSocial= false;
//															Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
//															if(concepttt != null)
//																isObraSocial= true;
//															Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																	actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), null);

															
															MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
															if(isFromCodigoBarrasBuscar){
																actualizarListasPantallaCodigoDeBarras();
															}else
																actualizarListasPantalla();
															return;										
														}
													}else{
														MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
																I18N.getLabel("selector.actionwithoutitem.title"));
														return;	
													}
																							
												}else{
													Concepto con=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
													// si se pago el copago, ver si es una actividad que se tiene que pagar siempre
													con.setObraSocial(clienteEJB.loadLazy(con.getObraSocial()));

													if(pagaCopagoSegunActividad(con.getObraSocial().getPreciosActividadesObraSocial(), actYClase.getActiv())){
														if(descontarCupo(subs, actYClase, cantidad, true, false)){
															com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subs);
															com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
															
//															boolean isObraSocial= false;
//															Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
//															if(concepttt != null)
//																isObraSocial= true;
//															Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																	actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

															
															MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
															if(isFromCodigoBarrasBuscar){
																// si viene del codigo de barras entonces solo limpio las clases
																actualizarListasPantallaCodigoDeBarras();
															}else
																actualizarListasPantalla();
															return;										
														}		
														
													}else{
														Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };

														if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
																I18N.getLabel("cliente.abonar.adicional.title"))){
															
															if(descontarCupo(subs, actYClase, cantidad, true, true)){
																com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subs);
																com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//																boolean isObraSocial= false;
//																Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
//																if(concepttt != null)
//																	isObraSocial= true;
//																Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																		actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

																
																MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
																if(isFromCodigoBarrasBuscar){
																	// si viene del codigo de barras entonces solo limpio las clases
																	actualizarListasPantallaCodigoDeBarras();
																}else
																	actualizarListasPantalla();
																return;										
															}
														}else{
															MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
																	I18N.getLabel("selector.actionwithoutitem.title"));
															return;	
															
														}
													}
												}

											}else{
												MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
												return;										
											}
										
										}else{
												// quiere decir que no es por obra social
												if(descontarCupo(subs, actYClase, 0, false, false)){
													com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subs);
													com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
			
//													boolean isObraSocial= false;
//													Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
//													if(concepttt != null)
//														isObraSocial= true;
//													Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//															actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(0));

													
													MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
													if(isFromCodigoBarrasBuscar){
														// si viene del codigo de barras entonces solo limpio las clases
														actualizarListasPantallaCodigoDeBarras();
													}else
														actualizarListasPantalla();
													return;										
												
												}else{
													MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
													return;										
												}
										}
										
									}else{
										
										Subscripcion subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();

										// el usuario desea que se descuente del curso viejo

										Integer cantidad=getConceptoObraSocialYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
										
										if(cantidad != null && cantidad.intValue() >0 ){
											// quiere decir que es por obra social
											// si viene de la obra Social, se debe descontar abonar adicional
											if(tieneCupos(subsSeleccionada, actYClase)){
												
												// si no se abono el copago todavia
												if(!pagoElCopago(subsSeleccionada, actYClase)){
													Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };

													if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
															I18N.getLabel("cliente.abonar.adicional.title"))){
														
														
														if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, true)){
															com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
															com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//															boolean isObraSocial= false;
//															Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//															if(concepttt != null)
//																isObraSocial= true;
//															Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																	actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

															
															MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
															if(isFromCodigoBarrasBuscar){
																// si viene del codigo de barras entonces solo limpio las clases
																actualizarListasPantallaCodigoDeBarras();
															}else
																actualizarListasPantalla();
															return;										
														}
														
														
													}else{
														MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
																I18N.getLabel("selector.actionwithoutitem.title"));
														return;	
														
													}
												
												}else{
													Concepto con=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
													// si se pago el copago, ver si es una actividad que se tiene que pagar siempre
													con.setObraSocial(clienteEJB.loadLazy(con.getObraSocial()));
													if(pagaCopagoSegunActividad(con.getObraSocial().getPreciosActividadesObraSocial(), actYClase.getActiv())){
														if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, false)){
															com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
															com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//															boolean isObraSocial= false;
//															Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//															if(concepttt != null)
//																isObraSocial= true;
//															Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																	actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

															MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
															if(isFromCodigoBarrasBuscar){
																// si viene del codigo de barras entonces solo limpio las clases
																actualizarListasPantallaCodigoDeBarras();
															}else
																actualizarListasPantalla();
															return;										
														}		
														
													}else{
														Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };
					
														if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
																I18N.getLabel("cliente.abonar.adicional.title"))){
															
															if(descontarCupo(subs, actYClase, cantidad, true, true)){
																com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subs);
																com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
																
//																boolean isObraSocial= false;
//																Concepto concepttt=getConceptoObraSociallYPrecio(subs, actYClase.getActiv(), actYClase.getCurso());
//																if(concepttt != null)
//																	isObraSocial= true;
//																Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																		actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

																
																MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
																if(isFromCodigoBarrasBuscar){
																	// si viene del codigo de barras entonces solo limpio las clases
																	actualizarListasPantallaCodigoDeBarras();
																}else
																	actualizarListasPantalla();
																return;										
															}
														}else{
															MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
																	I18N.getLabel("selector.actionwithoutitem.title"));
															return;	
															
														}
													}
												}
										
											}else{
												MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
												return;										
											}
										
										}else{
												// quiere decir que no es por obra social
												if(descontarCupo(subsSeleccionada, actYClase, 0, false, false)){
													com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
													com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
			
//													boolean isObraSocial= false;
//													Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//													if(concepttt != null)
//														isObraSocial= true;
//													Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//															actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(0));

													
													MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
													if(isFromCodigoBarrasBuscar){
														// si viene del codigo de barras entonces solo limpio las clases
														actualizarListasPantallaCodigoDeBarras();
													}else
														actualizarListasPantalla();
													return;										
												
												}else{
													MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
													return;										
												}
										}
									}
									
								}else{
									Subscripcion subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();							
									// verifico por estados de que NO se pueda ingresar a clase cuando la subs esta vencida 

									CupoActividad cupo= null;
									if(subsSeleccionada.getCupoActividadList() != null ){
										for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
											if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
												cupo= iterable_element;
											
										}
									}
									
									if(cupo != null && 
											(cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() 
											 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
											 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt())){
										MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.con.subs.estado.inco"), I18N.getLabel("selector.actionwithoutitem.title"));
										return;		
									} 
									
									Integer cantidad=getConceptoObraSocialYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
									
									if(cantidad != null && cantidad.intValue() >0 ){
										// quiere decir que es por obra social
										// si viene de la obra Social, se debe descontar abonar adicional
										if(tieneCupos(subsSeleccionada, actYClase)){
											
											// si no se abono el copago todavia
											if(!pagoElCopago(subsSeleccionada, actYClase)){
												Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };

												if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
														I18N.getLabel("cliente.abonar.adicional.title"))){
													
													if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, true)){
														com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
														com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//														boolean isObraSocial= false;
//														Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//														if(concepttt != null)
//															isObraSocial= true;
//														Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

														
														MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
														if(isFromCodigoBarrasBuscar){
															// si viene del codigo de barras entonces solo limpio las clases
															actualizarListasPantallaCodigoDeBarras();
														}else
															actualizarListasPantalla();
														return;										
													}
													
													
												}else{
													MessageBox.validation("Error: No se puede ingresar a clase/Sesion al cliente por FALTA de pago de la Sesion", 
															I18N.getLabel("selector.actionwithoutitem.title"));
													return;	
													
												}
											
											}else{
												Concepto con=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
												// si se pago el copago, ver si es una actividad que se tiene que pagar una unica vez
												con.setObraSocial(clienteEJB.loadLazy(con.getObraSocial()));
												if(pagaCopagoSegunActividad(con.getObraSocial().getPreciosActividadesObraSocial(), actYClase.getActiv())){
													if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, false)){
														com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
														com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
														
//														boolean isObraSocial= false;
//														Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//														if(concepttt != null)
//															isObraSocial= true;
//														Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

														MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
														if(isFromCodigoBarrasBuscar){
															// si viene del codigo de barras entonces solo limpio las clases
															actualizarListasPantallaCodigoDeBarras();
														}else
															actualizarListasPantalla();
														return;										
													}		
													
												}else{
													Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };
				
													if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
															I18N.getLabel("cliente.abonar.adicional.title"))){
														
														if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, true)){
															com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
															com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
															
//															boolean isObraSocial= false;
//															Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//															if(concepttt != null)
//																isObraSocial= true;
//															Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																	actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

															MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
															if(isFromCodigoBarrasBuscar){
																// si viene del codigo de barras entonces solo limpio las clases
																actualizarListasPantallaCodigoDeBarras();
															}else
																actualizarListasPantalla();
															return;										
														}
													}else{
														MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
																I18N.getLabel("selector.actionwithoutitem.title"));
														return;	
														
													}
												}
											}
											
										}else{
											MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
											return;										
										}
									
									}else{
											// quiere decir que no es por obra social
											if(descontarCupo(subsSeleccionada, actYClase, 0, false, false)){
												com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
												com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//												boolean isObraSocial= false;
//												Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//												if(concepttt != null)
//													isObraSocial= true;
//												Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//														actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(0));

												
												MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
												if(isFromCodigoBarrasBuscar){
													// si viene del codigo de barras entonces solo limpio las clases
													actualizarListasPantallaCodigoDeBarras();
												}else
													actualizarListasPantalla();
												return;										
											
											}else{
												MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
												return;										
											}
									}
								}
								
							}else{
								// si es por codigo de barras

								Subscripcion subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();							
								// verifico por estados de que NO se pueda ingresar a clase cuando la subs esta vencida 

								CupoActividad cupo= null;
								if(subsSeleccionada.getCupoActividadList() != null ){
									for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
										if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
											cupo= iterable_element;
										
									}
								}
								
								if(cupo != null && 
										(cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA.toInt() 
										 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()
										 || cupo.getEstado().toInt() == CupoActividadEstadoEnum.ANULADA.toInt())){
									MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.con.subs.estado.inco"), I18N.getLabel("selector.actionwithoutitem.title"));
									return;		
								} 
								
								
							
								
								Integer cantidad=getConceptoObraSocialYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
								
								if(cantidad != null && cantidad.intValue() >0 ){
									// quiere decir que es por obra social
									// si viene de la obra Social, se debe descontar abonar adicional
									if(tieneCupos(subsSeleccionada, actYClase)){
										
										// si no se abono el copago todavia
										if(!pagoElCopago(subsSeleccionada, actYClase)){
											Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };

											if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
													I18N.getLabel("cliente.abonar.adicional.title"))){
												
												if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, true)){
													com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
													com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//													boolean isObraSocial= false;
//													Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//													if(concepttt != null)
//														isObraSocial= true;
//													Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//															actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

													
													MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
													if(isFromCodigoBarrasBuscar){
														// si viene del codigo de barras entonces solo limpio las clases
														actualizarListasPantallaCodigoDeBarras();
													}else
														actualizarListasPantalla();
													return;										
												}
												
												
											}else{
												MessageBox.validation("Error: No se puede ingresar a clase/Sesion al cliente por FALTA de pago de la Sesion", 
														I18N.getLabel("selector.actionwithoutitem.title"));
												return;	
												
											}
										
										}else{
											Concepto con=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
											// si se pago el copago, ver si es una actividad que se tiene que pagar una unica vez
											con.setObraSocial(clienteEJB.loadLazy(con.getObraSocial()));
											if(pagaCopagoSegunActividad(con.getObraSocial().getPreciosActividadesObraSocial(), actYClase.getActiv())){
												if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, false)){
													com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
													com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
													
//													boolean isObraSocial= false;
//													Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//													if(concepttt != null)
//														isObraSocial= true;
//													Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//															actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

													
													MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
													if(isFromCodigoBarrasBuscar){
														// si viene del codigo de barras entonces solo limpio las clases
														actualizarListasPantallaCodigoDeBarras();
													}else
														actualizarListasPantalla();
													return;										
												}		
												
											}else{
												Object[] params3 = {cli.getNombre().toUpperCase() + " "+ cli.getApellido().toUpperCase() , cantidad };
			
												if (MessageBox.question(I18N.getLabel("cliente.abonar.adicional", params3), 
														I18N.getLabel("cliente.abonar.adicional.title"))){
													
													if(descontarCupo(subsSeleccionada, actYClase, cantidad, true, true)){
														com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
														com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);
														
//														boolean isObraSocial= false;
//														Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//														if(concepttt != null)
//															isObraSocial= true;
//														Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//																actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(cantidad));

														
														MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
														if(isFromCodigoBarrasBuscar){
															// si viene del codigo de barras entonces solo limpio las clases
															actualizarListasPantallaCodigoDeBarras();
														}else
															actualizarListasPantalla();
														return;										
													}
												}else{
													MessageBox.validation("Error: No se puede ingresar a clase/sesion al cliente por FALTA de pago de la Sesion", 
															I18N.getLabel("selector.actionwithoutitem.title"));
													return;	
													
												}
											}
										}
										
									}else{
										MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
										return;										
									}
								
								}else{
										// quiere decir que no es por obra social
										if(descontarCupo(subsSeleccionada, actYClase, 0, false, false)){
											com.institucion.fm.conf.Session.setAttribute("idSubscripcion", subsSeleccionada);
											com.institucion.fm.conf.Session.setAttribute("idactYClase", actYClase);

//											boolean isObraSocial= false;
//											Concepto concepttt=getConceptoObraSociallYPrecio(subsSeleccionada, actYClase.getActiv(), actYClase.getCurso());
//											if(concepttt != null)
//												isObraSocial= true;
//											Imprimir.imprimirComprobante("Comprob de Ingreso", cli, 
//													actYClase.getActiv().getNombre(), isObraSocial, String.valueOf(0), String.valueOf(0));

											
											MessageBox.info(I18N.getLabel("subscripcion.ingreso.a.clase.cliente", params2), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
											if(isFromCodigoBarrasBuscar){
												// si viene del codigo de barras entonces solo limpio las clases
												actualizarListasPantallaCodigoDeBarras();
											}else
												actualizarListasPantalla();
											return;										
										
										}else{
											MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.error.sin.cupo", params2), I18N.getLabel("selector.actionwithoutitem.title"));
											return;										
										}
								}
								
								
							}
						}						
						
					}
				}
			}						
		}
		getClientFilter().getCodBarras().setFocus(true);

	}
	
	
	private void agregarClienteAClasesSinFechas(Cliente cli, Clase clase, Curso curso, Subscripcion subs){
		
		if(cli != null && clase != null){
			if(clase.getIngresoAClaseSinFechas() == null){
				clase.setIngresoAClaseSinFechas(new HashSet());
				clase.getIngresoAClaseSinFechas().add(crearIngresoAClase(cli, clase, curso, subs));
			}else{
				if(!existeClienteEnClaseSinFechas(cli, clase)){
					clase.getIngresoAClaseSinFechas().add(crearIngresoAClase(cli, clase, curso, subs));
				}
			}
		}
	}

	private boolean existeClienteEnClaseSinFechas(Cliente cli, Clase clase){
		
		if(cli != null && clase != null && clase.getIngresoAClaseSinFechas() != null){
			
			for (IngresoAClasesSinFechasAlumnos iterable_element : clase.getIngresoAClaseSinFechas()) {
				
				if(iterable_element.getCliente().getId().intValue() == cli.getId().intValue() &&
						iterable_element.getClase().getId().intValue() == clase.getId().intValue() )
					return true;
			}
		}
		return false;
	}
	
	private IngresoAClasesSinFechasAlumnos crearIngresoAClase(Cliente cli, Clase clase, Curso curso, Subscripcion subs){
		IngresoAClasesSinFechasAlumnos ingresoaClae= new IngresoAClasesSinFechasAlumnos();
		ingresoaClae.setFecha(new Date());
		ingresoaClae.setCliente(cli);
		ingresoaClae.setClase(clase);
		ingresoaClae.setCurso(curso);
		ingresoaClae.setSubscripcion(subs);

		return ingresoaClae;
	}
	
	private Subscripcion generarPagoYMovimientoCaja(Subscripcion subs, Actividad act, Integer cantidadDinero, Curso curso){
	
		if(cantidadDinero == null)
			cantidadDinero = 0;
		
		if(subs.getPagosPorSubscripcionList() == null)
			subs.setPagosPorSubscripcionList(new HashSet());

		// Se genera el Pago Adicional
		PagosPorSubscripcion nuevo= new PagosPorSubscripcion();
		nuevo.setEsCopago(true);
		nuevo.setCantidadDinero(cantidadDinero);
		nuevo.setSubscripcion(subs);
		nuevo.setIdTipoDePago(FormasDePagoSubscripcionEnum.EFECTIVO);
		nuevo.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
		nuevo.setSucursal(getSucursal(subs));

		subs.getPagosPorSubscripcionList().add(nuevo);
		
		if(subs.getPrecioCursosMatricula() != null)
			subs.setPrecioCursosMatricula(subs.getPrecioCursosMatricula().intValue() + cantidadDinero);
		else
			subs.setPrecioCursosMatricula(cantidadDinero);
		
		if(subs.getPrecioTOTALadicionalTarjeta()!= null)
			subs.setPrecioTOTALadicionalTarjeta(subs.getPrecioTOTALadicionalTarjeta().intValue() + cantidadDinero);
		else
			subs.setPrecioTOTALadicionalTarjeta(cantidadDinero);
	
		Concepto con= new Concepto();
		con.setConcepto("Copago ");
		con.setImporteOriginal(cantidadDinero);
		con.setImporteConDescuento(cantidadDinero);
		con.setActividadDelConcepto(act);
		con.setCantSesiones(1);
//		con.setCantClase(1);
		con.setTipoDescuento(TipoDescuentosEnum.NO);
		con.setCurso(curso);
//		con.setSubscripcion(subs);
		con.setComentarioDescuento("Copago realizado de 1 Sesion. Actividad: " + act.getNombre());
		subs.getConceptoList().add(con);
		
		// Se genera el movimiento en Caja
		CajaMovimiento caja2= new CajaMovimiento();
		caja2.setConcepto("Copago Sesion " +act.getNombre() + ".Valor $"+ formateador.format(cantidadDinero) + 
				" Cliente: " + subs.getCliente().getApellido().toUpperCase() + " " +subs.getCliente().getNombre().toUpperCase());
		
		caja2.setFecha(new Date());
		caja2.setIdUsuarioGeneroMovimiento(Session.getUsernameID().intValue());
		caja2.setTipoMovimiento(TipoMovimientoCajaEnum.INGRESO);
		caja2.setValor(new Double(cantidadDinero));
		caja2.setSucursal(getSucursal(subs));

		caja2.setCliente(subs.getCliente());
		caja2.setIdSubscripcion(subs.getId().intValue());
		caja2.setSucursal(getSucursal(subs));
		cajaEJB.save(caja2);	
		
		return subs;
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
	
	private Integer getConceptoObraSocialYPrecio(Subscripcion subs, Actividad act, Curso curso){
		
		if(subs.getConceptoList() != null){
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getConcepto() != null && !iterable_element.getConcepto().contains("Copago")){

					if(iterable_element.getCurso() != null && curso != null &&
							iterable_element.getCurso().getId().intValue() == curso.getId().intValue() ){
						
						if(iterable_element.getActividadDelConcepto().getId().intValue() == act.getId().intValue()){
							
							if(iterable_element.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
								if(iterable_element.getPrecioPorClaseOSesionPagaCliente() != null 
										&&  iterable_element.getPrecioPorClaseOSesionPagaCliente() > 0){
									
									return iterable_element.getPrecioPorClaseOSesionPagaCliente();
								}else{
									return null;
								}
								
								
							}else{
								return null;
							} 
						}	
					}
				}

			}
		}
		return null;
	}
	
	private Concepto getConceptoObraSociallYPrecio(Subscripcion subs, Actividad act, Curso curso){
		
		if(subs.getConceptoList() != null){
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getConcepto() != null && !iterable_element.getConcepto().contains("Copago")){

					if(iterable_element.getCurso() != null && curso != null &&
							iterable_element.getCurso().getId().intValue() == curso.getId().intValue() ){
						
						if(iterable_element.getActividadDelConcepto().getId().intValue() == act.getId().intValue()){
							
							if(iterable_element.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
								if(iterable_element.getPrecioPorClaseOSesionPagaCliente() != null 
										&&  iterable_element.getPrecioPorClaseOSesionPagaCliente() > 0){
									
									return iterable_element;
								}else{
									return null;
								}
									
							}
						}	
					}
				}

			}
		}
		return null;
	}
	
	
	private boolean tieneMasDeUnCupoGastado(Subscripcion subs, ActividadYClase actYClase ){
		
		if(subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(cupoAct.getCurso() != null && actYClase.getCurso() != null &&
						cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
					if(cupoAct.getActividad().getId().equals(actYClase.getActiv().getId())){
					
						int cuposDisponibles= cupoAct.getCupos();
						int cuposTotales=actYClase.getCantClases();
						if((cuposDisponibles == cuposTotales) || (cuposDisponibles == cuposTotales -1)){
							return false;
						}						
					}
				}
			}
		}
	return true;
	}
	
	private void actualizarListasPantalla(){
		getClientFilter().getCodBarras().setFocus(true);
		onFind(false, false, null);
		getClientFilter().getCodBarras().setFocus(true);

	}
	
	private void actualizarListasPantallaCodigoDeBarras(){
		getClientFilter().getCodBarras().setFocus(true);
		
		// si viene del codigo de barras entonces solo limpio las clases
		clasesListGrid.removeAll();

		if(clientepanelListGrid.getSelectedItem() != null){
			//actualizar el estado del cliente
			clientepanelListGrid.actualizoClienteLuegoDePasarCodigoDeBarras();

			//actualizar la subscripcion en la que estoy trabajando
			subscripcionListGrid.actualizoSubscripcionLuegoDePasarCodigoDeBarras();
		}
		getClientFilter().getCodBarras().setFocus(true);
	}
	
	private boolean tieneCupos(Subscripcion subs, ActividadYClase actYClase ){
		
		subs=clienteEJB.loadLazy(subs, true, false, false, false, false);

		if(subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(cupoAct.getCurso() != null && actYClase.getCurso() != null &&
						cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
					if(cupoAct.getActividad().getId().equals(actYClase.getActiv().getId())){
						
						Clase clase =(Clase)clasesListGrid.getSelectedItem().getValue();
						Clase claseDeBD=claseEJB.findById(clase.getId());
						
						claseDeBD=clienteEJB.loadLazy(claseDeBD, true, false, false, false);
						if(clienteExisteYaEnClase(subs.getCliente(), claseDeBD.getClienesEnClase())){
							MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.ya.asignado"), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
							return false;		
						}
						
						if(cupoAct.getCupos() > 0){
							return true;
							
						}
					}
				}
			}
		}
	return false;
	}
	
	private boolean pagaCopagoSegunActividad( Set<ObraSocialesPrecio>  listPrecios, Actividad act){
		
		if(listPrecios != null){
			for (ObraSocialesPrecio obraSocialesPrecio : listPrecios) {
				
				if(obraSocialesPrecio.getActividad().getId().intValue() == act.getId().intValue()){
					if(obraSocialesPrecio.getSePagaUnaUnicaVez() != null)
						return obraSocialesPrecio.getSePagaUnaUnicaVez();
					else
						return false;
				}
			}
		}
		return false;
	}

	
//	terminar de revisar lo del copago.
//	ver lo de la anulacion y devolucion de la guita
	
	private boolean pagoElCopago(Subscripcion subs, ActividadYClase actYClase ){
		
		if(subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(cupoAct.getCurso() != null && actYClase.getCurso() != null &&
						cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
					if(cupoAct.getActividad().getId().equals(actYClase.getActiv().getId())){
					
						if(cupoAct.getPagoElCopago() != null && cupoAct.getPagoElCopago().booleanValue()){
							return true;
							
						}
					}
				}
			}
		}
	return false;
	}
	
	
	private boolean descontarCupo(Subscripcion subs, ActividadYClase actYClase, Integer cantidad, boolean esDeObraSocial, boolean descontarDinero ){
		
		Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();

		subs=clienteEJB.loadLazy(subs, true, false, false, false, false);
		if(subs.getCupoActividadList() != null){
			
			for (CupoActividad cupoAct : subs.getCupoActividadList()) {
				if(cupoAct.getCurso() != null && actYClase.getCurso() != null &&
						cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
					if(cupoAct.getCurso().getId().equals(actYClase.getCurso().getId())){
						if(cupoAct.getCupos() > 0){
							Clase clase =(Clase)clasesListGrid.getSelectedItem().getValue();
							Clase claseDeBD=claseEJB.findById(clase.getId());
							claseDeBD=clienteEJB.loadLazy(claseDeBD, true, true, false, false);

							if(clienteExisteYaEnClase(subs.getCliente(), claseDeBD.getClienesEnClase())){
								MessageBox.validation(I18N.getLabel("subscripcion.ingreso.a.clase.cliente.ya.asignado"), I18N.getLabel("selector.actionwithoutitem.title.realizada"));
								return false;		
							}
							
							cupoAct.setCupos(cupoAct.getCupos() -1);
							cupoAct.setPagoElCopago(true);		
							subs=clienteEJB.actualizarSubscripcion(subs , actYClase);
							
							if(descontarDinero){
								if(esDeObraSocial)
									subs= generarPagoYMovimientoCaja(subs, actYClase.getActiv(), cantidad, cupoAct.getCurso());
							}

							inscripcionEJB.save(subs);	
								
							cli= subs.getCliente();
							if(cli.getSubscripcionesList() != null){
								cli=clienteEJB.loadLazy(cli, true, true, false, false);

								for (Subscripcion subs2 : cli.getSubscripcionesList()) {
									if(subs2.getId().intValue() == subs.getId().intValue()){
										
										if(subs2.getCupoActividadList() != null ){
											for (CupoActividad iterable_element : subs2.getCupoActividadList()) {
										
												if(subs.getCupoActividadList() != null ){
													for (CupoActividad iterable_elementsubs : subs.getCupoActividadList()) {
														if(iterable_elementsubs.getCurso().getId().intValue() == iterable_element.getCurso().getId().intValue())
															iterable_element.setEstado(iterable_elementsubs.getEstado());		
													}
												}
															
											}
										}
										
									
									}
								}
							}
							
							subs.setCliente(cli);
							cli = subs.getCliente();
							
							List<Subscripcion> listSubs=inscripcionEJB.findAllSubscripcionesByClient(cli.getId());
							if(listSubs != null)
								cli.setSubscripcionesList(new HashSet(listSubs));
							
							if(claseDeBD.getClienesEnClase() != null)
								claseDeBD.getClienesEnClase().add(cli);
							else{
								claseDeBD.setClienesEnClase(new HashSet());
								claseDeBD.getClienesEnClase().add(cli);
							}
							agregarClienteAClasesSinFechas(cli, claseDeBD, cupoAct.getCurso(), subs);

							clienteEJB.save(cli);
							
							//Error el cliente ya estaba en clase
						
							claseEJB.save(claseDeBD);
				
							Boolean bool=cursoEJB.findImprimible();

							if(bool != null && bool.booleanValue()){
								if(descontarDinero){
									if(esDeObraSocial){
										
										Imprimir.imprimirComprobante("Comprob para el Profesional", cli, 
												actYClase.getActiv().getNombre(), true, String.valueOf(0), String.valueOf(cantidad),
												actYClase.getActiv().isImprimeComprobante(), actYClase.getCurso().getIdTipoCurso(), cupoAct.getCupos(), true);
									}else{
										Imprimir.imprimirComprobante("Comprob para el Profesional", cli, 
												actYClase.getActiv().getNombre(), false, String.valueOf(cantidad), String.valueOf(0), 
												actYClase.getActiv().isImprimeComprobante(), actYClase.getCurso().getIdTipoCurso(), cupoAct.getCupos(), true);
									}
								}else{
									if(esDeObraSocial){
										Imprimir.imprimirComprobante("Comprob para el Profesional", cli, 
												actYClase.getActiv().getNombre(), true, String.valueOf(0), String.valueOf(0), 
												actYClase.getActiv().isImprimeComprobante(), actYClase.getCurso().getIdTipoCurso(), cupoAct.getCupos(), true);
									}else{
										Imprimir.imprimirComprobante("Comprob para el Profesional", cli, 
												actYClase.getActiv().getNombre(), false, String.valueOf(0), String.valueOf(0), 
												actYClase.getActiv().isImprimeComprobante(), actYClase.getCurso().getIdTipoCurso(), cupoAct.getCupos(), true);
									}
								}
							}
							return true;		
						}
					}
				}				
			}
		}	
		return false;		
	} 
	
	private CupoActividad obtenerElCupoActividadDeLaSubs(Subscripcion subsSeleccionada, Curso curso){
		
		if(subsSeleccionada.getCupoActividadList() != null){
			for (CupoActividad iterable_element : subsSeleccionada.getCupoActividadList()) {
				
				if(iterable_element.getCurso() != null && curso != null &&
						iterable_element.getCurso().getId().intValue() == curso.getId().intValue() )
					return iterable_element;
			}
		}
		return null;
	}
	
	
	private Subscripcion verificarSiElClienteTieneSubsDelMismoCursoActivasConCupos(){
		Cliente cliSeleccionado=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
		Subscripcion subsSeleccionada=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
		ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");
		
		CupoActividad cupoActMinima  = obtenerElCupoActividadDeLaSubs(subsSeleccionada, actYClase.getCurso());
		Subscripcion subsMinima=subsSeleccionada;
			
		if(cliSeleccionado.getSubscripcionesList() != null){
			cliSeleccionado= clienteEJB.loadLazy(cliSeleccionado, true, true, false, false);
			
			for (Subscripcion subs : cliSeleccionado.getSubscripcionesList()) {
				if(!subs.getId().equals(subsSeleccionada.getId())){
					
						for (CupoActividad cupoAct : subs.getCupoActividadList()) {
							if(cupoActMinima.getCurso() != null && cupoAct.getCurso() != null ){
								
								String cupoActMinimaPrimerString= null;
								String cupoPrimerString= null;
								if(cupoActMinima.getCurso().getNombre().indexOf(" ") == -1)
									cupoActMinimaPrimerString= cupoActMinima.getCurso().getNombre();
								else
									cupoActMinimaPrimerString=cupoActMinima.getCurso().getNombre().substring(0, cupoActMinima.getCurso().getNombre().indexOf(" "));

								if(cupoAct.getCurso().getNombre().indexOf(" ") == -1)
									cupoPrimerString= cupoAct.getCurso().getNombre();
								else
									cupoPrimerString=cupoAct.getCurso().getNombre().substring(0, cupoAct.getCurso().getNombre().indexOf(" "));

//								String cupoActMinimaPrimerString=cupoActMinima.getCurso().getNombre().substring(0, cupoActMinima.getCurso().getNombre().indexOf(" "));
//								String cupoPrimerString=cupoAct.getCurso().getNombre().substring(0, cupoAct.getCurso().getNombre().indexOf(" "));
								
								if(cupoActMinimaPrimerString != null && cupoPrimerString != null &&
										cupoActMinimaPrimerString.equalsIgnoreCase(cupoPrimerString)){
									if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt() || 
											cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
								
										
										// si existe otro concepto con id menor al concepto de la subscripcion seleccionada, entonces  
										if(cupoAct.getId().intValue() < cupoActMinima.getId().intValue()){
											cupoActMinima= cupoAct;
											subsMinima= subs;
										}
									}	
								}
							}
						}
					}
				}
			}
		if(subsMinima.getId().intValue() == subsSeleccionada.getId().intValue())
			return null;
		
		subsMinima.setCupoActividadParaCalcular(cupoActMinima);
	return subsMinima;
	}
	
	
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

	public void onVencerElCurso(Event event) throws Exception{
		
		if (hasSelectedOneItem(subscripcionListGrid)){
			
			Concepto c=(Concepto)subscripcionListGrid.getSelectedItem().getAttribute("concepto");
			Subscripcion s=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
			s=clienteEJB.loadLazy(s, true, false, false, false, false);
			boolean encontro=false;
			if(c != null){
				if(s.getCupoActividadList() != null){
					for (CupoActividad cupoAct : s.getCupoActividadList()) {
						if(cupoAct.getCurso().getId().intValue() == c.getCurso().getId().intValue() ){
							
							if(cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_CUPOS)  
									|| cupoAct.getEstado().equals(CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS)){
								encontro=true;
								if(cupoAct.getFechaComienzo() == null){
									Calendar calendar = Calendar.getInstance();
								    calendar.add(Calendar.DAY_OF_YEAR, -2);  // numero de d�as a a�adir, o restar en caso de d�as<0
									cupoAct.setFechaComienzo(calendar.getTime());
									
									Calendar calendarf = Calendar.getInstance();
//								    calendarf.add(Calendar.DAY_OF_YEAR, -1);  // numero de d�as a a�adir, o restar en caso de d�as<0

									cupoAct.setFechaFin(calendarf.getTime());
								}else{
									Calendar calendarf = Calendar.getInstance();
//								    calendarf.add(Calendar.DAY_OF_YEAR, -1);  // numero de d�as a a�adir, o restar en caso de d�as<0

									cupoAct.setFechaFin(calendarf.getTime());
								}

								if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
									cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA_CON_DEUDA);
									
								}else if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt() ){
									cupoAct.setEstado(CupoActividadEstadoEnum.VENCIDA);
								}
							}else{
								MessageBox.validation("Se permite pasar a vencido solo subscripciones con cupos disponibles", 
										I18N.getLabel("selector.actionwithoutitem.title"));
								return ;

							}
						}
					}
				}
			}
			if(encontro){
				inscripcionEJB.save(s);
				
				MessageBox.info("El curso se paso a Vencido de forma exitosa", I18N.getLabel("selector.actionwithoutitem.title.realizada"));

				// actualizo pantalla 
				actualizarListasPantallaCodigoDeBarras();
			}
		}
	}
	
	public void onUpdateSubscripciones(Event event) throws Exception{
		getClientFilter().getCodBarras().setFocus(true);

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
			Sessions.getCurrent().setAttribute("isSubsFromIngresoInscripcion", true);
			
			guardarTemasPaginacion();
			
			super.saveHistory("filter");
			super.gotoPage("/institucion/subscripcion-crud.zul");		
		}
	}
	
	// es el update del cliente
	public void onUpdate(Event event) throws Exception{
		getClientFilter().getCodBarras().setFocus(true);

		if (hasSelectedOneItem(clientepanelListGrid)){
			com.institucion.fm.conf.Session.setAttribute("idCliente", clientepanelListGrid.getSelectedItem().getValue());
			com.institucion.fm.conf.Session.setAttribute("isClienteFromIngresoInscripcion", true);
			
			guardarTemasPaginacion();
			
			super.saveHistory("filter");
			super.gotoPage("/institucion/clientes-crud.zul");
		}
	}
	
	public void guardarTemasPaginacion(){
		int pa = userPaging.getActivePage();
		savePage(pa);
		Sessions.getCurrent().setAttribute("pagno", userPaging.getActivePage());
		Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
		Sessions.getCurrent().setAttribute("total", totalSize.intValue());
		Sessions.getCurrent().setAttribute("col", id);
		Sessions.getCurrent().setAttribute("order", order);
	}

	public void eliminarTemasPaginacion(){
		savePage(0);
		Sessions.getCurrent().setAttribute("pagno", null);
		Sessions.getCurrent().setAttribute("filters", null);
		Sessions.getCurrent().setAttribute("total", null);
		Sessions.getCurrent().setAttribute("col", null);
		Sessions.getCurrent().setAttribute("order", null);
	}

	public void onDelete(Event event) {
		
		/*
		 *  PASAN A ESTADO ELIMINADO LOS CLIENTES, de ese estado no se los puede volver, y se pone la fecha y la descripcion y quien lo elimino.
		 *  
		 * 
		 */
	}

	public void onVerSubs(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);
		this.onUpdateSubscripciones(null);
		
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);
		this.onUpdate(null);
	}

	public void onDoubleClickSubscripcionesEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
		getClientFilter().getCodBarras().setFocus(true);

		this.onUpdateSubscripciones(null);
	}

	public void onClickClasesEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
//		getClientFilter().getCodBarras().setFocus(true);
	}
	
	public void onClickSubscripcionesArribaEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
		getClientFilter().getCodBarras().setFocus(true);
		clasesListGrid.setList(null, null);
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

							/* DEJO LA PARTE DE ARRIBA SOLAMENTE PARA QUE VALIDE POR ID, HAY QUE PROBAR A VER COMO SE COMPORTA, 
							 * LO QUE SI HAY QUE HACER ANTES LIMPIEZA DE TODAS LAS SUBS QUE FIGURAN CON CUPOS PERO QUE YA NO TIENEN PK QUEDARON MAL
							 * 
							if(cupoActividad.getFechaComienzo() == null && cupoMenor.getFechaComienzo() == null){
								// significa que los dos cupos son nullos, o sea que es lo mismo trabajar con 1 q con el otro
								if(cupoActividad.getId().intValue() < cupoMenor.getId().intValue()){
									cupoMenor= cupoActividad;
								}
							
							}else if(cupoActividad.getFechaComienzo() != null &&  cupoMenor.getFechaComienzo() != null){
								// si la fecha de comienzo del nuevo curso esta despues que el menor, 
								if(cupoActividad.getFechaComienzo().after(cupoMenor.getFechaComienzo())){
//									cupoMenor= cupoActividad;
								}else{
									cupoMenor= cupoActividad;
								}
							}else if(cupoActividad.getFechaComienzo() != null &&  cupoMenor.getFechaComienzo() == null){
								cupoMenor= cupoActividad;
							
							}
							*/
						}
					}
				}
			}
		}
		return cupoMenor;
	}
	
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		if(isFromCodigoBarras)
			getClientFilter().getCodBarras().setFocus(true);
		
		if(!isFromCodigoBarras){
			onFind(evt);
			
		}else{
			// buscar el cliente por ID.
			onFind(evt);
			if(clientepanelListGrid.getItems() != null && clientepanelListGrid.getItems().size() == 1){
			// Si el cliente EXISTE: 
				//. filtrar el resultado del cliente y dejarlo seleccionado.
				clientepanelListGrid.setSelectedIndex(0);
				Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue(); 
				
				// verifico el estado de las subscripciones, si tiene disponibles etc.
				subscripcionListGrid.setVisible(true);
				toolbar2.setVisible(true);
				subscripcionListGrid.getItems().clear();
				List<Subscripcion> listSubs =inscripcionEJB.findAllConJdbc(getIngresoInscripcionFilter().getFiltersConSubsActivasOConCuposDisponibles(cli.getId()));	
				if(listSubs != null && listSubs.size() >0){
					
					// si 1 o mas de 1 subscripcion esta activa y tiene cursos de distintos tipos con cupos disponibles:
					// avisar por pantalla, que se debe continuar a mano.
					Map<String, List<CupoActividad>> map=tieneCursosdisponiblesDeDistintosTipos(listSubs);
					
					if(map != null && map.size() >1){
						MessageBox.validation("�Error! El cliente posee varias subscripciones activas, debe continuar el ingreso MANUALMENTE", I18N.getLabel("selector.actionwithoutitem.title"));
//						subscripcionListGrid.setList(null, null, null, null);
						List<SubsYCurso> clasesParaComparar4 =cursoEJB.findAllConJdbcSubsYCurso(getIngresoInscripcionClaseFilter().getFiltersSubscripcionesActualByCliente(cli.getId()));
						subscripcionListGrid.setList(listSubs, null, null, clasesParaComparar4);

						
						getClientFilter().getCodBarras().setFocus(true);
						return;
					}
					
					// si 1 o mas de 1 subscripcion esta activa y si tiene cursos del mismo tipo con cupos disponibles o solo 1 curso disponible:
					// agarro de la subscripcion mas vieja, la que no estaba inicializada o mas vieja.
					// la dejo seleccionada la subscripcion
					subscripcionListGrid.setList(null, null, null, null);
					CupoActividad cupo =tieneCursosdisponiblesDelMismoTipo(listSubs, map);
					if(cupo != null){
						List<SubsYCurso> clasesParaComparar4 =cursoEJB.findAllConJdbcSubsYCurso(getIngresoInscripcionClaseFilter().getFiltersSubscripcionesActualByCliente(cli.getId()));
						
						ActividadYClase actYClase=null;
						if(cupo.getCurso().getActividadYClaseList() != null){
							for (ActividadYClase acttt : cupo.getCurso().getActividadYClaseList()) {
								actYClase= acttt;
								break;
							}
						}
						
						if(listSubs != null)
							subscripcionListGrid.setList(listSubs, cupo.getSubscripcion(), actYClase, clasesParaComparar4);
						
						clasesListGrid.setList(null, null);
						
						
						// AHORA CARGO LAS CLASES
						clasesListGrid.setVisible(true);
						toolbar3.setVisible(true);
						filter3.setVisible(true);

						Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
						clasesListGrid.removeAll();
						if(actYClase != null && (actYClase.getCurso() != null && 
								actYClase.getActiv().isUsaCarnet() 
								&&  (actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
										||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
								||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
										||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()))){

							subs=clienteEJB.loadLazy(subs, false, false, false, true, true);

							// se debe consultar a la base de datos Segun los dias seteados en la subscripcion
							Long hora=obtenerHorarioClase(actYClase.getCurso().getId(), actYClase.getActiv().getId(), subs);
							String horaString=null;
							if(hora== null || (hora!= null && hora.intValue() == -1)){
								// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
								List<Clase> clases2 =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
								if(!isFromCodigoBarras)
									clases2 =adaptarClases(clases2);

								List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//								agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//								Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
								
								if(clases2 != null && clases2.size() > 0){
									if(clases2 != null)
										clasesListGrid.setList(clases2, clasesParaComparar);
								}else{
									// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
									List<Clase> clases2A =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
									if(!isFromCodigoBarras)
										clases2A =adaptarClases(clases2A);
//									Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//									agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
									if(clases2A != null)
										clasesListGrid.setList(clases2A, clasesParaComparar);
									
								}
//								return ;

							}else
								horaString= String.valueOf(hora);
								
							List<Clase> clases2 =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFilters(actYClase.getActiv().getId(), -1,  
										horaString, subs.getId(), actYClase.getCurso().getId()));	
							if(!isFromCodigoBarras)
								clases2 =adaptarClases(clases2);
							
							List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//							agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//							Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
							if(clases2 != null && clases2.size() > 0){
								if(clases2 != null)
									clasesListGrid.setList(clases2, clasesParaComparar);
							}else{
								// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
								List<Clase> clasesA =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
								if(!isFromCodigoBarras)
									clasesA =adaptarClases(clasesA);
//								Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//								agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
								if(clasesA != null)
									clasesListGrid.setList(clasesA, clasesParaComparar);
								
							}
						}else{
							// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
							List<Clase> clases2 =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
							if(!isFromCodigoBarras)
								clases2 =adaptarClases(clases2);
//							Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
							List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//							agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
							if(clases2 != null)
								clasesListGrid.setList(clases2, clasesParaComparar);
							
						}		

						// si el cliente ya existe en alguna clase, aviso por pantalla
						if(clasesParaComparar4 != null && clasesParaComparar4.size() >0){
							MessageBox.validation("�Error! El cliente se encuentra en una clase actualmente", I18N.getLabel("selector.actionwithoutitem.title"));
							getClientFilter().getCodBarras().setFocus(true);
							return;

						}

						List<Clase> clasesPosiblesDeIngreso= null;
						// si tiene una sola clase lo agrego directamente ahi
						if(clasesListGrid.getItems() != null && clasesListGrid.getItems().size() ==1 ){
							clasesListGrid.setSelectedIndex(0);
							if(clasesPosiblesDeIngreso == null)
								clasesPosiblesDeIngreso = new ArrayList();
							clasesPosiblesDeIngreso.add((Clase)clasesListGrid.getSelectedItem().getValue());

							// asigna automaticamente a la clase que empezo(hora de inicio) hasta hace media hora
						}else if(clasesListGrid.getItems() != null && clasesListGrid.getItems().size() >0 ){
							Calendar cal30Antes= Calendar.getInstance();
							cal30Antes.setTime(new Date());
							
							cal30Antes.set(Calendar.YEAR, 1970);
							cal30Antes.set(Calendar.MONTH, 0);
							cal30Antes.set(Calendar.DAY_OF_MONTH, 01);
							cal30Antes.set(Calendar.MINUTE, cal30Antes.get(Calendar.MINUTE)- 40);
							
							Calendar cal30Despeus= Calendar.getInstance();
							cal30Despeus.setTime(new Date());
							
							cal30Despeus.set(Calendar.YEAR, 1970);
							cal30Despeus.set(Calendar.MONTH, 0);
							cal30Despeus.set(Calendar.DAY_OF_MONTH, 01);
							cal30Despeus.set(Calendar.MINUTE, cal30Despeus.get(Calendar.MINUTE)+ 40);
							
							Iterator ite= clasesListGrid.getItems().iterator();
							while(ite.hasNext()){
								Listitem item=(Listitem)ite.next();
								Clase clas=(Clase)item.getValue();
								if(clas != null && !clas.getEsClaseSinHora()){
										
									if(cal30Antes.getTime().before(clas.getHoraDesde())
											&& cal30Despeus.getTime().after(clas.getHoraDesde())){
										if(clasesPosiblesDeIngreso == null)
											clasesPosiblesDeIngreso = new ArrayList();
										clasesPosiblesDeIngreso.add(clas);
									}
								}
							}							
						}else{
							MessageBox.validation("�Error! Seleccione la clase MANUALMENTE ", I18N.getLabel("selector.actionwithoutitem.title"));
							getClientFilter().getCodBarras().setFocus(true);
							return;
						}
						if(clasesPosiblesDeIngreso != null && clasesPosiblesDeIngreso.size() ==1){
							try {
								Clase clasee=(Clase)clasesPosiblesDeIngreso.get(0);
								// preeleccionar el item
								clasesListGrid.setSelectedItem(clasee);
								Event e= new Event("buscar", self);
								onAsignClass(e);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}else{
							MessageBox.validation("�Error! Seleccione la clase MANUALMENTE ", I18N.getLabel("selector.actionwithoutitem.title"));
							getClientFilter().getCodBarras().setFocus(true);
							return;
						}
							
					}else{
					// supuestamente no deberia pasar nunca por aca
						// 
						if(listSubs != null){
							List<SubsYCurso> clasesParaComparar4 =cursoEJB.findAllConJdbcSubsYCurso(getIngresoInscripcionClaseFilter().getFiltersSubscripcionesActualByCliente(cli.getId()));
							subscripcionListGrid.setList(listSubs, null, null, clasesParaComparar4);
						}
					}
					
				}else{
					// Si NO tiene subscripciones activas con cupos disponibles, o estan vencidas, avisar por pantalla, No tiene subscripciones activas
					MessageBox.validation("�Error! El cliente no tiene subscripciones vigentes con cupos disponibles", I18N.getLabel("selector.actionwithoutitem.title"));
					getClientFilter().getCodBarras().setFocus(true);
					return;

				}
				
			}else{
				//Si el cliente NO existe: Avisar por pantalla que no existe, que hay un error en el codigo de barras.
				MessageBox.validation("�Error! Codigo de barras o cliente inexistente", I18N.getLabel("selector.actionwithoutitem.title"));
				getClientFilter().getCodBarras().setFocus(true);
				return;
		
			} 
		}	
		if(isFromCodigoBarras)
			getClientFilter().getCodBarras().setFocus(true);
	}
	
	// Si el cliente EXISTE: 
				//. filtrar el resultado del cliente y dejarlo seleccionado.
	
			// Si NO tiene subscripciones activas con cupos disponibles, o estan vencidas, avisar por pantalla, No tiene subscripciones activas
	
			// Si TIENE subscripciones activas(no vencidas) y con cupos disponibles, 

				// si 1 o mas de 1 subscripcion esta activa y tiene cursos de distintos tipos con cupos disponibles:
										// avisar por pantalla, que se debe continuar a mano.
	
				// si 1 o mas de 1 subscripcion esta activa y si tiene cursos del mismo tipo con cupos disponibles o solo 1 curso disponible:
										// agarro de la subscripcion mas vieja, la que no estaba inicializada o mas vieja.
										// la dejo seleccionada la subscripcion
	
				
						// 	si no tiene clases disponibles (30 min antes y 30 min despues):
								// Avisar por pantalla que no hay clases disponibles, favor de seleccionarlas a mano.
	 

						// Si TIENE clases disponibles para ese horario en el que esta ingresando ( 30 min antes o 30 min despues)
								// Pregunto por pantalla si desea ingresar al cliente tal a la clase tal ?
										// si si, realizo el ingreso 
										// si no, dejo todo seleccionado, pero no hago nada.

	public void onFind(Event evt) {
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}
		
		Session.setAttribute("pag", false);
		getIngresoInscripcionFilter().blanquearEstado();

//		int pa = userPaging.getActivePage();
		savePage(0);
		Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
		Sessions.getCurrent().setAttribute("total", totalSize.intValue());
		
		this.onFind(false, true, evt);
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}

	}
	
	public void onClickEvt(Event evt){
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}		// dejo vacio el combo de estado de subscri
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
//		else if (com.institucion.fm.conf.Session.getAttribute("idCliente") != null){
//			cli=(Cliente)com.institucion.fm.conf.Session.getAttribute("idCliente")	;
//		}
			
		subscripcionListGrid.getItems().clear();
		clasesListGrid.setList(null, null);

		if(cli != null){
			List<Subscripcion> listSubs =inscripcionEJB.findAllConJdbc(getIngresoInscripcionFilter().getFilters(cli.getId()));	
			List<SubsYCurso> clasesParaComparar =cursoEJB.findAllConJdbcSubsYCurso(getIngresoInscripcionClaseFilter().getFiltersSubscripcionesActualByCliente(cli.getId()));
					
			CupoActividad cupo =tieneCursosdisponiblesDelMismoTipo(listSubs, null);
			if(cupo != null){
				
				ActividadYClase actYClase=null;
				if(cupo.getCurso().getActividadYClaseList() != null){
					for (ActividadYClase acttt : cupo.getCurso().getActividadYClaseList()) {
						actYClase= acttt;
						break;
					}
				}
				
				if(listSubs != null)
					subscripcionListGrid.setList(listSubs, cupo.getSubscripcion(), actYClase, clasesParaComparar);
				
				clasesListGrid.setList(null, null);
				
				
				// AHORA CARGO LAS CLASES
				clasesListGrid.setVisible(true);
				toolbar3.setVisible(true);
				filter3.setVisible(true);

				Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
				
				subs=clienteEJB.loadLazy(subs, false, false, false, true, true);
				clasesListGrid.removeAll();
				if(actYClase != null && (actYClase.getCurso() != null && 
						actYClase.getActiv().isUsaCarnet() 
						&&  (actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
								||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
						||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
								||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()))){

					subs=clienteEJB.loadLazy(subs, false, false, false, true, true);

					// se debe consultar a la base de datos Segun los dias seteados en la subscripcion
					Long hora=obtenerHorarioClase(actYClase.getCurso().getId(), actYClase.getActiv().getId(), subs);
					String horaString=null;
					if(hora== null || (hora!= null && hora.intValue() == -1)){
						// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
						List<Clase> clases2 =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
						clases2 =adaptarClases(clases2);

						List<Clase> clasesParaComparar22 =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//						agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//						Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
						
						if(clases2 != null && clases2.size() > 0){
							if(clases2 != null)
								clasesListGrid.setList(clases2, clasesParaComparar22);
						}else{
							// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
							List<Clase> clases2A =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
							clases2A =adaptarClases(clases2A);
//							Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//							agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
							if(clases2A != null)
								clasesListGrid.setList(clases2A, clasesParaComparar22);
							
						}
//						return ;

					}else
						horaString= String.valueOf(hora);
						
					List<Clase> clases2 =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFilters(actYClase.getActiv().getId(), -1,  
								horaString, subs.getId(), actYClase.getCurso().getId()));	
					clases2 =adaptarClases(clases2);
					
					List<Clase> clasesParaComparar22 =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//					agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//					Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
					if(clases2 != null && clases2.size() > 0){
						if(clases2 != null)
							clasesListGrid.setList(clases2, clasesParaComparar22);
					}else{
						// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
						List<Clase> clasesA =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
						clasesA =adaptarClases(clasesA);
//						Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//						agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
						if(clasesA != null)
							clasesListGrid.setList(clasesA, clasesParaComparar22);
						
					}
				}else{
					// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
					List<Clase> clases2 =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
					clases2 =adaptarClases(clases2);
//					Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
					List<Clase> clasesParaComparar22 =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//					agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
					if(clases2 != null)
						clasesListGrid.setList(clases2, clasesParaComparar22);
					
				}	
				
				// si el cliente ya existe en alguna clase, aviso por pantalla
				if(clasesParaComparar != null && clasesParaComparar.size() >0){
//					MessageBox.validation("�Error! El cliente se encuentra en una clase actualmente", I18N.getLabel("selector.actionwithoutitem.title"));
					if(evt != null && evt instanceof InputEvent && 
							((InputEvent)evt).getTarget() != null && 
							((InputEvent)evt).getTarget() instanceof Textbox){
						
					}else{
						getClientFilter().getCodBarras().setFocus(true);	
					}
					return;

				}

				List<Clase> clasesPosiblesDeIngreso= null;
				// si tiene una sola clase lo agrego directamente ahi
				if(clasesListGrid.getItems() != null && clasesListGrid.getItems().size() ==1 ){
					clasesListGrid.setSelectedIndex(0);
					if(clasesPosiblesDeIngreso == null)
						clasesPosiblesDeIngreso = new ArrayList();
					clasesPosiblesDeIngreso.add((Clase)clasesListGrid.getSelectedItem().getValue());

					// asigna automaticamente a la clase que empezo(hora de inicio) hasta hace media hora
				}else if(clasesListGrid.getItems() != null && clasesListGrid.getItems().size() >0 ){
					Calendar cal30Antes= Calendar.getInstance();
					cal30Antes.setTime(new Date());
					
					cal30Antes.set(Calendar.YEAR, 1970);
					cal30Antes.set(Calendar.MONTH, 0);
					cal30Antes.set(Calendar.DAY_OF_MONTH, 01);
					cal30Antes.set(Calendar.MINUTE, cal30Antes.get(Calendar.MINUTE)- 40);
					
					Calendar cal30Despeus= Calendar.getInstance();
					cal30Despeus.setTime(new Date());
					
					cal30Despeus.set(Calendar.YEAR, 1970);
					cal30Despeus.set(Calendar.MONTH, 0);
					cal30Despeus.set(Calendar.DAY_OF_MONTH, 01);
					cal30Despeus.set(Calendar.MINUTE, cal30Despeus.get(Calendar.MINUTE)+ 40);
					
					Iterator ite= clasesListGrid.getItems().iterator();
					while(ite.hasNext()){
						Listitem item=(Listitem)ite.next();
						Clase clas=(Clase)item.getValue();
						if(clas != null && !clas.getEsClaseSinHora()){
								
							if(cal30Antes.getTime().before(clas.getHoraDesde())
									&& cal30Despeus.getTime().after(clas.getHoraDesde())){
								if(clasesPosiblesDeIngreso == null)
									clasesPosiblesDeIngreso = new ArrayList();
								clasesPosiblesDeIngreso.add(clas);
							}
						}
					}							
				}else{
//					MessageBox.validation("�Error! Seleccione la clase MANUALMENTE ", I18N.getLabel("selector.actionwithoutitem.title"));
					if(evt != null && evt instanceof InputEvent && 
							((InputEvent)evt).getTarget() != null && 
							((InputEvent)evt).getTarget() instanceof Textbox){
						
					}else{
						getClientFilter().getCodBarras().setFocus(true);	
					}
					return;
				}
				
//				probar si esto quedo bien, ver si funciona con clases que son con horas y clases sin horas
				if(clasesPosiblesDeIngreso != null && clasesPosiblesDeIngreso.size() ==1){
					try {
						Clase clasee=(Clase)clasesPosiblesDeIngreso.get(0);
						// preeleccionar el item
						clasesListGrid.setSelectedItem(clasee);
//						Event e= new Event("buscar", self);
//						onAsignClass(e);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
//					MessageBox.validation("�Error! Seleccione la clase MANUALMENTE ", I18N.getLabel("selector.actionwithoutitem.title"));
					if(evt != null && evt instanceof InputEvent && 
							((InputEvent)evt).getTarget() != null && 
							((InputEvent)evt).getTarget() instanceof Textbox){
						
					}else{
						getClientFilter().getCodBarras().setFocus(true);	
					}
					return;
				}
				
			}else{
				if(listSubs != null)
					subscripcionListGrid.setList(listSubs, null, null, clasesParaComparar);
				clasesListGrid.setList(null, null);
			}			
		}

		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}
	}
	
	
	public void onCheckFiltersClasesDoing(boolean check, boolean esCheckboxTodosLosDias){
//		getClientFilter().getCodBarras().setFocus(true);

		if(esCheckboxTodosLosDias){
			if(check){
				// obtengo las clases del historial
				if(subscripcionListGrid.getSelectedItem() != null){
					clasesListGrid.setVisible(true);
					toolbar3.setVisible(true);
					filter3.setVisible(true);
					ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");
					
					if(actYClase != null && (actYClase.getCurso() != null && actYClase.getActiv().isUsaCarnet())){
						
						List<Clase> clases =claseEJB.findAllByActividad(actYClase.getActiv().getId(), 8);

						Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();

						List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(
								getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId()));
						
//						ver aca que es para comparar
						
						if(clases != null)
							clasesListGrid.setList(clases, clasesParaComparar);
						return ;
					}
				}
			}else{
				// obtengo las clases normalmente
				onClickSubscripcionesEvt(null);
			}	
			
		}else{
			if(check){
				// obtengo las clases del historial
				if(subscripcionListGrid.getSelectedItem() != null){
					clasesListGrid.setVisible(true);
					toolbar3.setVisible(true);
					filter3.setVisible(true);
					ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");
					
					if(actYClase != null && (actYClase.getCurso() != null && 
							actYClase.getActiv().isUsaCarnet())){
						
						List<Clase> clases =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
//						List<Clase> clases2 =adaptarClases(clases);

						Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();

						List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(
								getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId()));

						if(clases != null)
							clasesListGrid.setList(clases, clasesParaComparar);
						return ;
						
					}
				}
			}else{
				// obtengo las clases normalmente
				onClickSubscripcionesEvt(null);
			}	
		}
//		getClientFilter().getCodBarras().setFocus(true);

	}
	
	private List<Clase> adaptarClases(	List<Clase> listClases){
		List<Clase> clases= new ArrayList();
		if(listClases != null){
			Date horaActual= new Date();	
			
			for (Clase clase : listClases) {
					
				if(clase.getEsClaseSinHora()){
					// debe controlar que se procese todo UNICAMENTE cuando pasaron 3 horas de ingresada la 1 persona a clase
						clases.add(clase);	
				}else{
					// si la hora actual es > a 2 horas de la hora de finalizacion Proceso
					if(horaActual.getHours() >= (clase.getHoraHasta().getHours() +1)){
						
					}else{
						clases.add(clase);	
					}			
				}	
			}
		}
		return clases;
	}

	
	public void onClickSubscripcionesEvt(Event evt){
		
		boolean esMaipu=false;
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				if( ((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada")).equals(SucursalEnum.MAIPU)){
					esMaipu= true;
				}
			}
		}

		if(esMaipu){
//			getClientFilter().getCodBarras().setFocus(true);	

		}else{
			getIngresoInscripcionClaseFilter().getCheck().setChecked(false);
			getIngresoInscripcionClaseFilter().getCheckTodosLosDias().setChecked(false);
//			getClientFilter().getCodBarras().setFocus(true);

			if(subscripcionListGrid.getSelectedItem() != null){
				// busco las clases de la subscripcion cliente
				clasesListGrid.setVisible(true);
				toolbar3.setVisible(true);
				filter3.setVisible(true);

				Subscripcion subs=(Subscripcion)subscripcionListGrid.getSelectedItem().getValue();
				ActividadYClase actYClase=(ActividadYClase)subscripcionListGrid.getSelectedItem().getAttribute("actYClase");

				subs=clienteEJB.loadLazy(subs, false, false, false, true, true);

				CupoActividad cupo= null;
				if(subs != null && subs.getCupoActividadList() != null ){
					for (CupoActividad iterable_element : subs.getCupoActividadList()) {
						if(iterable_element.getCurso().getId().intValue() == actYClase.getCurso().getId().intValue())
							cupo= iterable_element;
					}
				}
				
				if(subs != null && 
						((cupo.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt())
							|| cupo.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()) ){
										
								if(actYClase != null && (actYClase.getCurso() != null && 
										actYClase.getActiv().isUsaCarnet() 
										&&  (actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LA_SEMANA.toInt()
												||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_ANo.toInt()
										||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_AL_MES.toInt()
												||  actYClase.getCurso().getVencimiento().toInt() == VencimientoCursoEnum.SEMANAL_VENCE_A_LOS_3_MES.toInt()))){

									subs=clienteEJB.loadLazy(subs, false, false, false, true, true);

									// se debe consultar a la base de datos Segun los dias seteados en la subscripcion
									Long hora=obtenerHorarioClase(actYClase.getCurso().getId(), actYClase.getActiv().getId(), subs);
									String horaString=null;
									if(hora== null || (hora!= null && hora.intValue() == -1)){
										// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
										List<Clase> clases =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
										List<Clase> clases2 =adaptarClases(clases);

										List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//										agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//										Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
										
										if(clases2 != null && clases2.size() > 0){
											if(clases2 != null)
												clasesListGrid.setList(clases2, clasesParaComparar);
										}else{
											// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
											List<Clase> clases2A =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
//											List<Clase> clases2A =adaptarClases(clasesA);
//											Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//											agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
											if(clases2A != null)
												clasesListGrid.setList(clases2A, clasesParaComparar);
											
										}
										return ;

									}else
										horaString= String.valueOf(hora);
										
									List<Clase> clases =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFilters(actYClase.getActiv().getId(), -1,  
												horaString, subs.getId(), actYClase.getCurso().getId()));	
									List<Clase> clases2 =adaptarClases(clases);
									
									List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//									agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
//									Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
									if(clases2 != null && clases2.size() > 0){
										if(clases2 != null)
											clasesListGrid.setList(clases2, clasesParaComparar);
									}else{
										// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
										List<Clase> clasesA =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
//										List<Clase> clases2A =adaptarClases(clasesA);
//										Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
//										agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
										if(clasesA != null)
											clasesListGrid.setList(clasesA, clasesParaComparar);
										
									}
								}else{
									// se debe consultar a la base de datos solo segun el dia de hoy y id de actividad.
									List<Clase> clases =claseEJB.findAllByActividad(actYClase.getActiv().getId(), -1);
									List<Clase> clases2 =adaptarClases(clases);
//									Si el cliente esta en alguna de las clases que devolvio el sistema, lo pinto
									List<Clase> clasesParaComparar =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().getFiltersClasesActualByClienteAndCurso(subs.getCliente().getId(), actYClase.getCurso().getId(), subs.getId() ));
//									agregar aca validacion segun curso, pintar solo las clases del curso ingresoaclasessinfechasalumnos
									if(clases2 != null)
										clasesListGrid.setList(clases2, clasesParaComparar);
									
								}									
										
					}else if(subs != null &&  
							(cupo.getEstado().toInt() == CupoActividadEstadoEnum.S_CUPOS.toInt() 
							|| cupo.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())){
							
						// busco solamente la clase en la que esta ahora .
						Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();

						if(cli != null){
//							aca se debe agregar consulta tambien por id de actividad
//							Luego tengo que pintar en verde el nombre de la clase
							List<Clase> clases =claseEJB.findAllConJdbc(getIngresoInscripcionClaseFilter().
									getFiltersClasesActualByClienteAndCurso(cli.getId(), actYClase.getCurso().getId(), subs.getId() )); 
					
							if(clases != null)
								clasesListGrid.setList(clases, clases);						
						}
					}
			}
//			getClientFilter().getCodBarras().setFocus(true);	
		}
	}
	
	private Long obtenerHorarioClase(Long idCurso, Long idActividad, Subscripcion subs){
		Long hora=(long) -1;
		if(subs != null){
			Calendar cal = Calendar.getInstance();
			int day=cal.getTime().getDay();
			
			for (SubscripcionDeClases subsDeClase : subs.getSubscripcionDeClases()) {
				if(subsDeClase.getCurso() != null &&  
						subsDeClase.getCurso().getId().intValue() == idCurso.intValue()){
					
					if(subsDeClase.getClaseSeleccionadasList() != null){
						for (SubscripcionDeClasesPorActividad porAct : subsDeClase.getClaseSeleccionadasList()) {
							if(porAct.getActividad().getId().intValue()   == idActividad.intValue()){
								
								if(day == 0){ // dom
									if(porAct.isDomingo() && porAct.getClaseDomingo() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseDomingo());	
									}							
								}
								
								if(day == 1){ // lunes
									if(porAct.isLunes() && porAct.getClaseLunes() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseLunes());	
									}
								}
								if(day == 2){ // martes
									if(porAct.isMartes() && porAct.getClaseMartes() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseMartes());	
									}
								}
								if(day == 3){ // miercoles
									if(porAct.isMiercoles() && porAct.getClaseMiercoles() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseMiercoles());	
									}
								}
								if(day == 4){ // jueves
									if(porAct.isJueves() && porAct.getClaseJueves() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseJueves());	
									}
								}
								if(day == 5){ // viernes
									if(porAct.isViernes() && porAct.getClaseViernes() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseViernes());	
									}
								}
								if(day == 6){ // sabado
									if(porAct.isSabado() && porAct.getClaseSabado() != null){
										hora=obtenerHoraDeUnaClase(porAct.getClaseSabado());	
									}
								}	
							}
							
						}	
					}
				}	
			}	
		}
		return hora;
	}
	
	
	private Long obtenerHoraDeUnaClase(Clase clase){
		Long hora= null;
	
		if(clase.getHoraDesde() != null)
			hora =  new Long(clase.getHoraDesde().getHours());
		
		return hora;
	}
	
	private IngresoInscripcionClientFilter getClientFilter() {
		return (IngresoInscripcionClientFilter)(filter.getGridFilter());
	}
	
	private IngresoInscripcionFilter getIngresoInscripcionFilter() {
		return (IngresoInscripcionFilter)(filter2.getGridFilter());
	}

	private IngresoInscripcionClaseFilter getIngresoInscripcionClaseFilter() {
		return (IngresoInscripcionClaseFilter)(filter3.getGridFilter());
	}

	public void onFind(boolean isFromMenu, boolean esDelEvent, Event evt) {
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}

		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		clienteList= new ArrayList<Cliente>();
		clientepanelListGrid.removeAll();
		
		Cliente cliNuevoCreado=(Cliente)Sessions.getCurrent().getAttribute("vieneDeCrearCliente");
		if(cliNuevoCreado != null){
			// vino de crear un cliente.
			 clear(evt);
			 
			 if(cliNuevoCreado.getApellido() != null)
				 getClientFilter().getApellido().setValue(cliNuevoCreado.getApellido());
			 if(cliNuevoCreado.getNombre() != null)
				 getClientFilter().getNombre().setValue(cliNuevoCreado.getNombre());			 
		}
		Sessions.getCurrent().removeAttribute("vieneDeCrearCliente");
		
		if(esDelEvent){
			// no debe tener en cuenta lo que vino de session
			String filters = getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  );
			totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
			userPaging.setTotalSize(totalSize.intValue());
			this.getData( super.loadPage() * _pageSize, userPaging.getPageSize(),filters, id, order, evt);
			userPaging.setActivePage(super.loadPage());
			super.savePage(0);
			Sessions.getCurrent().setAttribute("filters", getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  ));
			Sessions.getCurrent().setAttribute("total", totalSize.intValue());

		}else{
			if(isFromMenu){
				// no debe tener en cuenta lo que vino de session
				String filters = getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  );
				totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
				userPaging.setTotalSize(totalSize.intValue());
				this.getData( super.loadPage() * _pageSize, userPaging.getPageSize(),filters, id, order, evt);
				userPaging.setActivePage(super.loadPage());
				super.savePage(0);
			}else{
				if(filtrosAnt != null){
					this.getData( super.loadPage() * _pageSize, userPaging.getPageSize(),filtrosAnt, id, order, evt);
					userPaging.setActivePage(super.loadPage());
					userPaging.setTotalSize(totalSize.intValue());

				}else{
					// no debe tener en cuenta lo que vino de session
					String filters = getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  );
					totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
					userPaging.setTotalSize(totalSize.intValue());
					this.getData( super.loadPage() * _pageSize, userPaging.getPageSize(),filters, id, order, evt);
					userPaging.setActivePage(super.loadPage());
					super.savePage(0);
				}
			}			
		}
	
		super.saveHistory("gridFilter");
		subscripcionListGrid.removeAll();
		clasesListGrid.removeAll();
//		onFind2();
		

		// si es del codigo de barras
		 if(!getClientFilter().getCodBarras().getValue().trim().equalsIgnoreCase("") ){
			subscripcionListGrid.setVisible(true);
			toolbar2.setVisible(true);
			subscripcionListGrid.getItems().clear();
			clasesListGrid.setList(null, null);
			 
		 }else{
			subscripcionListGrid.getItems().clear();
			clasesListGrid.setList(null, null);
			onClickEvt(evt);
		 }
		log.info("Fin Creando listado de farmacia en la version modificada");
		if(evt != null && evt instanceof InputEvent && 
				((InputEvent)evt).getTarget() != null && 
				((InputEvent)evt).getTarget() instanceof Textbox){
			
		}else{
			getClientFilter().getCodBarras().setFocus(true);	
		}
	}
	
	public void onFind2(boolean vieneDelFiltroDeEstadosSubs){
		if(!vieneDelFiltroDeEstadosSubs)
			getClientFilter().getCodBarras().setFocus(true);

		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		List<Subscripcion> subsList= new ArrayList<Subscripcion>();
		if(clientepanelListGrid != null && clientepanelListGrid.getSelectedItem() != null){
			Cliente cli=(Cliente)clientepanelListGrid.getSelectedItem().getValue();
			if(cli != null){
				Subscripcion subsSelecccionada= (Subscripcion)com.institucion.fm.conf.Session.getAttribute("idSubscripcion");
				ActividadYClase actYclase = (ActividadYClase)com.institucion.fm.conf.Session.getAttribute("idactYClase");
				subsList =inscripcionEJB.findAllConJdbc(getIngresoInscripcionFilter().getFilters(cli.getId()));	
				
				List<SubsYCurso> clasesParaComparar =cursoEJB.findAllConJdbcSubsYCurso(getIngresoInscripcionClaseFilter().getFiltersSubscripcionesActualByCliente(cli.getId()));

				subscripcionListGrid.setList(subsList, subsSelecccionada, actYclase, clasesParaComparar);
				
				if(subscripcionListGrid.getSelectedItem() != null){
					onCheckFiltersClasesDoing(getIngresoInscripcionClaseFilter().getCheck().isChecked(), false);
				}
			}
		}	
		if(!vieneDelFiltroDeEstadosSubs)
			getClientFilter().getCodBarras().setFocus(true);

	}
	
	public void onClearFilter2(){
		getClientFilter().getCodBarras().setFocus(true);

		getIngresoInscripcionFilter().clear();
		
		this.onFind2(false);	
		getClientFilter().getCodBarras().setFocus(true);

	}
	
	public void onClearFilter(Event evt){	
		getClientFilter().getCodBarras().setFocus(true);

		getClientFilter().clear();
		getIngresoInscripcionFilter().clear();
		getIngresoInscripcionClaseFilter().clear();
		eliminarTemasPaginacion();
		userPaging.setActivePage(0);
		id=null;
		order=false;
		totalSize=null;
		filtrosAnt=null;
		getClientFilter().getCodBarras().setValue(null);

		this.onFind(false, false, null);	
		getClientFilter().getCodBarras().setFocus(true);

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
	
	
	public void onExportClientExcel(Event event) throws Exception {
		getClientFilter().getCodBarras().setFocus(true);

		List<ClienteView> clienteExportList= null;
		String filters = getClientFilter().getFiltersForExportingExcelClientes( 0, -10,id,order  );
		clienteExportList =clienteEJB.findAllConJdbcSinHibernate(filters);
		
//		 Map<Integer,LinkedList<String>> actividadesList =clienteEJB.findAllActividadesConJdbc(" and (ca.cupos > 0 )  and ca.estado in (3, 10 )  ");
//		 Map<Integer,LinkedList<String>> actividadesList2 =clienteEJB.findAllActividadesConJdbc(" and ca.estado != 6 ");

		if(clienteExportList == null || (clienteExportList != null && clienteExportList.size() == 0)){
			MessageBox.validation("No se encontraron resultados a guardar en excel", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}else{
			
			String[] sheetNames = {"Base datos Clientes" }; //, "Clientes-Activ-c_cupos", "Clentes-Activ-Historico"};
			   

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
				fieldNames.add("Dni");
				fieldNames.add("Fecha Nacimiento");
				fieldNames.add("Edad");
				fieldNames.add("Domicilio");
				fieldNames.add("Celular");
				fieldNames.add("Telefono");
				fieldNames.add("Facebook");
				fieldNames.add("Mail");
				fieldNames.add("Estado");
				fieldNames.add("Deuda");
//				fieldNames.add("Codigo de Barra");

				mapFields.put(1, fieldNames);
								
				jasperReport.setFieldNamesList(mapFields);
				
				Map<Integer, List<RowModel>> map2 = new HashMap <Integer, List<RowModel>>(); 

				map2.put(1, obtenerParaExcel(clienteExportList));
//				map2.put(2, obtenerParaExcelActividades(actividadesList));
//				map2.put(3, obtenerParaExcelActividades(actividadesList2));
			    jasperReport.setDsList(map2);
				
				Calendar ahoraCal= Calendar.getInstance();
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR);

				try {
					jasperReport.exportXlsReportRestructuring(os, sheetNames, "Clientes_"+fechaNac);
				} catch (JRException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ByteArrayInputStream mediais = new ByteArrayInputStream(os.toByteArray());
								
				AMedia amedia = new AMedia("Base datos Clientes_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

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
		getClientFilter().getCodBarras().setFocus(true);

	}
	
	
	
	private List<com.institucion.controller.RowModel>  obtenerParaExcel (List<ClienteView> clienteExportList){
		 List<com.institucion.controller.RowModel>  rowModel= new ArrayList();
		for (ClienteView iterable_element : clienteExportList) {
			com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
			
			row.set("Apellido", iterable_element.getApellido());
			row.set("Nombre", iterable_element.getNombre());
			
			if( iterable_element.getDni() != null)
				row.set("Dni", iterable_element.getDni());
			else
				row.set("Dni", " ");
			
			if( iterable_element.getFechaNacimiento() != null){
				Calendar ahoraCal= Calendar.getInstance();
				ahoraCal.setTime(iterable_element.getFechaNacimiento());
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				
				row.set("Fecha Nacimiento", fechaNac);
			}else
				row.set("Fecha Nacimiento",  " ");
			

			if(iterable_element.getFechaNacimiento() != null){
				row.set("Edad", String.valueOf(EdadViewHelper.calcularEdad(iterable_element.getFechaNacimiento(), new Date())));
			}else
				row.set("Edad",  " ");
			
			
			if(iterable_element.getDomicilio() != null)
				row.set("Domicilio", iterable_element.getDomicilio());
			else
				row.set("Domicilio",  " ");
			
			if(iterable_element.getCelular() != null)
				row.set("Celular", iterable_element.getCelular());
			else
				row.set("Celular",  " ");
			
			
			if(iterable_element.getTelefonoFijo() != null)
				row.set("Telefono", iterable_element.getTelefonoFijo());
			else
				row.set("Telefono",  " ");
			
			
			if(iterable_element.getFacebook() != null)
				row.set("Facebook", iterable_element.getFacebook());
			else
				row.set("Facebook",  " ");
			
			
			if(iterable_element.getMail() != null)
				row.set("Mail", iterable_element.getMail());
			else
				row.set("Mail",  " ");
			
			boolean tieneDeudas=clienteEJB.isAdeudaAlgo(iterable_element.getId());
			boolean tieneDisponibles=clienteEJB.isTieneDisponibles(iterable_element.getId());


			if(tieneDeudas){
				String coment =  "";
			
				if(tieneDisponibles){
					coment = "Adeuda con Cupos";
				}else{
					coment = "Adeuda sin Cupos";
				} 
				row.set("Estado", coment);
			}else {
		
				String coment =  "";
				
				if(tieneDisponibles){
					coment = "Con Cupos";
				}else{
					coment = "Sin Cupos";
				} 
				row.set("Estado", coment);			
			}
			
			if(tieneDeudas){
				float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(iterable_element.getId()), false);
				
				row.set("Deuda", " $"+formateador.format(cant ) );
			}else
				row.set("Deuda", " $"+formateador.format(0) );
		
			rowModel.add(row);			
		}
		return rowModel;
	}
		
	private int getDeuda(List<Subscripcion> listSubscripcion, boolean validaFecha){
		int cantDeuda= 0;
		if(listSubscripcion != null){
			for (Subscripcion subs : listSubscripcion) {
				
				subs=clienteEJB.loadLazy(subs, true, true, false, false, false);
				
				Date fechaIni=subs.getFechaYHoraCreacion();
				Date fechaFinal=new Date();
				int dias=(int) ((fechaFinal.getTime()-fechaIni.getTime())/86400000);

				if((!validaFecha) ||  (validaFecha && dias >= 10)){
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
		}
		return cantDeuda;		
	}

		
	public void venderNuevaSubs(Event evt, int idCliente){
		getClientFilter().getCodBarras().setFocus(true);
		
		
		com.institucion.fm.conf.Session.setAttribute("idSubs", null);
		com.institucion.fm.conf.Session.setAttribute("isSubsFromIngresoInscripcion", true);
		Cliente aaaa=clienteEJB.findById(new Long(idCliente));
		com.institucion.fm.conf.Session.setAttribute("idCliente", aaaa);
		
		float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(aaaa.getId()), true);

		if(cant > 0){
			MessageBox.validation("Para realizar nueva venta, se debe saldar la DEUDA ANTERIOR", "Accion cancelada");
			return;	
		}
		Sessions.getCurrent().setAttribute("max", 0);
		guardarTemasPaginacion();

		super.saveHistory("filter");
		super.gotoPage("/institucion/subscripcion-crud.zul");	
	}

	@Override
	public void ingresarAClase(Event evt, int idClase) throws Exception {
		onAsignClass(evt);
		
	}

	@Override
	public void actualizarPaneldePrecioProducto() {
		// TODO Auto-generated method stub
		
	}	
		
		
}

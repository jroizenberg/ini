package com.institucion.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Window;

import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.ClientFilter;
import com.institucion.model.Cliente;
import com.institucion.model.ClienteList;
import com.institucion.model.ClienteView;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Subscripcion;

public class ClientSelectorComposer extends SelectorFEComposer  implements ClienteDelegate{
	
	private ClienteList clientepanelListGrid;
	private List<Cliente> pharmacyList;
	private PanelFilter filter;

	private ClienteEJB clienteEJB;
	private InscripcionEJB inscripcionEJB;

	private Window win = null;
	DecimalFormat formateador = new DecimalFormat("###,###");

	
	/** The _page size. */
	private final int _pageSize = 12;
	
	/** The user paging. */
	private Paging userPaging;
	
	private Long totalSize=0L;
	
	/** The id. */
	String id = null;
	
	/** The order. */
	boolean order = false;
	
	public ClientSelectorComposer(){
		super();
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		inscripcionEJB= BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");

	}

	
	public void buscar(Event evt, boolean isFromCodigoBarras) {
		onFind(evt);
	}
	
	private ClientFilter getClientFilter() {
		return (ClientFilter)(filter.getGridFilter());
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

				int pgno = userPaging.getActivePage();
				int ofs = pgno * _pageSize;    	
				getData(ofs, _pageSize, null, id, order);
				
			}
		});
	}
	
	private void  getData( Integer firstResult, Integer MaxResult, String filters, String id, boolean order){
		super.setSelectedEntity(clientepanelListGrid);
		// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
		pharmacyList =clienteEJB.findAllConJdbc(getClientFilter().getFilters(firstResult, MaxResult,id,order  ));	
		clientepanelListGrid.setList(pharmacyList);
		super.getSelectedEntity();
	}
	
	public void clear(){
		getClientFilter().clear();
	}
	
	
	public void onCreate() {
		clientepanelListGrid.setActionComposerDelegate(this);
		getClientFilter().getApellido().setFocus(true);

		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
			setCallFromMenu(false);			
		}
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();
		getClientFilter().setActionComposerDelegate(this);
		onFind();
		
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
	
	public void onUpdate(Event event) throws Exception{
		if (hasSelectedOneItem(clientepanelListGrid)){
			Sessions.getCurrent().setAttribute("idCliente", clientepanelListGrid.getSelectedItem().getValue());		
			Sessions.getCurrent().setAttribute("position", clientepanelListGrid);
			Sessions.getCurrent().setAttribute("key", pharmacyList);
			Sessions.getCurrent().setAttribute("max", pharmacyList.size());
			super.saveHistory("filter");
			super.gotoPage("/institucion/clientes-crud.zul");		
		}
	}

	public void onDelete(Event event) {
		// No se eliminan clientes
		
		/*
		 *  PASAN A ESTADO ELIMINADO LOS CLIENTES, de ese estado no se los puede volver, y se pone la fecha y la descripcion y quien lo elimino.
		 *  
		 * 
		 */

	}

	public void onInsert(Event event) throws Exception {
		com.institucion.fm.conf.Session.setAttribute("idCliente", null);
		Sessions.getCurrent().setAttribute("max", 0);
		super.saveHistory("filter");
		super.gotoPage("/institucion/clientes-crud.zul");
	}

	public void onDoubleClickEvt(Event event) throws Exception {
		// Pregunta si tiene permisos para la operacion
//		if (this.getManager().havePermission(PermissionTxManager.TX_PHARMACY_MODIFY)) {
			this.onUpdate(null);
//		}
	}

	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind();
	}
	
	public void onFind() {
		Logger log=Logger.getLogger(this.getClass());
		log.info("Creando listado de farmacia en la version modificada");
		pharmacyList= new ArrayList<Cliente>();
		clientepanelListGrid.removeAll();
		
		String filters = getClientFilter().getFilters( super.loadPage() * _pageSize, userPaging.getPageSize(),id,order  );
		totalSize= getClientService().getCount(getClientFilter().getFiltersCount());
		userPaging.setTotalSize(totalSize.intValue());
		
		this.getData( super.loadPage() * _pageSize, userPaging.getPageSize(),filters, id, order);
		super.saveHistory("filter");
		userPaging.setActivePage(super.loadPage());
		super.savePage(0);

		log.info("Fin Creando listado de farmacia en la version modificada");
	}
	
	
	@Override
	public void sortEvent(Event event) {
		final Listheader lh = (Listheader) event.getTarget();
		final String sortDirection = lh.getSortDirection();
		if (sortDirection != null && !"".equals(sortDirection))
			order = sortDirection.equals("ascending")?false:true;
		id = (String)lh.getAttribute("id");
		int pgno = userPaging.getActivePage();
		int ofs = pgno * _pageSize;    	
		getData(ofs, _pageSize, null, id, order);
	}
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();
		if (isCallFromMenu()){
			getClientFilter().clear();
		}else{
			this.onFind();
		}		
	}


	public void onExportClientTODOExcel(Event event) throws Exception {

//		List<Cliente> clienteExportList= null;
//		String filters = getClientFilter().getFilters( 0, -10,id,order  );
//		clienteExportList =clienteEJB.findAllConJdbc(filters);
		
		 Map<Integer,LinkedList<String>> actividadesList =clienteEJB.findAllActividadesConJdbc(" and (ca.cupos > 0 )  and ca.estado in (3, 10 )  ");
		 Map<Integer,LinkedList<String>> actividadesList2 =clienteEJB.findAllActividadesConJdbc(" and ca.estado != 6 ");

//		if(clienteExportList == null || (clienteExportList != null && clienteExportList.size() == 0)){
//			MessageBox.validation("No se encontraron resultados a guardar en excel", I18N.getLabel("selector.actionwithoutitem.title"));
//			return;
//		}else{
			
			String[] sheetNames = { "Clientes-Activ-c_cupos", "Clentes-Activ-Historico"};
			   

			Iframe iframeReporte = new Iframe();
			
			iframeReporte.setWidth("0%");
	        iframeReporte.setHeight("0%");
			iframeReporte.setId("reporte");
			
			JRDistributionPlanSummaryXls jasperReport = new JRDistributionPlanSummaryXls();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
							
				Map<Integer, List<String>> mapFields= new HashMap <Integer, List<String>>(); 
								
				List<String>fieldNames2 =new ArrayList <String>();
				fieldNames2.add("Actividad");
				fieldNames2.add("Curso");
				fieldNames2.add("Apellido");
				fieldNames2.add("Nombre");
				fieldNames2.add("DNI");
				fieldNames2.add("Fecha contratacion curso");
				fieldNames2.add("Fecha comienzo");
				fieldNames2.add("Fecha fin");
				fieldNames2.add("estado");
				fieldNames2.add("cupos Disp");
							
				for (int i = 0; i <= sheetNames.length -1; i++) {
					int ind= i+1;
					mapFields.put(ind, fieldNames2);
				}

				jasperReport.setFieldNamesList(mapFields);
				
				Map<Integer, List<RowModel>> map2 = new HashMap <Integer, List<RowModel>>(); 

				map2.put(1, obtenerParaExcelActividades(actividadesList));
				map2.put(2, obtenerParaExcelActividades(actividadesList2));
			    jasperReport.setDsList(map2);
				
				Calendar ahoraCal= Calendar.getInstance();
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR);

				try {
					jasperReport.exportXlsReportRestructuring(os, sheetNames, "BaseDatosVentasYDisponibles_"+fechaNac);
				} catch (JRException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ByteArrayInputStream mediais = new ByteArrayInputStream(os.toByteArray());
								
				AMedia amedia = new AMedia("BaseDatosVentasYDisponibles_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

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
//		}

	}
	
	private List<com.institucion.controller.RowModel>  obtenerParaExcelActividades ( Map<Integer,LinkedList<String>> actividadesList ){
		 List<com.institucion.controller.RowModel>  rowModel= new ArrayList();
		 
		for (Map.Entry<Integer, LinkedList<String>> entry : actividadesList.entrySet()){

		    LinkedList<String> list =entry.getValue();
		    if(list != null){
		    	int i =0;
				com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();

			    for (String st : list) {
			    	
			    	if(st == null)
			    		st= " ";
			    	
			    	if(i==0)
						row.set("Actividad", st);
			    	if(i==1)
						row.set("Curso", st);
			    	if(i==2)
						row.set("Apellido", st);
			    	if(i==3)
						row.set("Nombre", st);
			    	if(i==4){
						row.set("DNI", st);
			    	}
			    	if(i==5){
			    		if(st != null && !st.equalsIgnoreCase(" ")){
							row.set("Fecha contratacion curso", st);

			    		}else
			    			row.set("Fecha contratacion curso", " ");
			    	}
			    	if(i==6){
			    		if(st != null && !st.equalsIgnoreCase(" ")){
							row.set("Fecha comienzo", st);
			    		}else
			    			row.set("Fecha comienzo", " ");
			    	}
			    	if(i==7){
			    		if(st != null && !st.equalsIgnoreCase(" ")){
							row.set("Fecha fin", st);
			    		}else
			    			row.set("Fecha fin", " ");
			    	}
			    	if(i==8){
			    		if(st != null && !st.equalsIgnoreCase(" ")){
							row.set("estado", st);
			    		}else{
							row.set("estado", " ");
			    		}
			    	}
			    	if(i==9)
						row.set("cupos Disp", st);
					
			    	i++;
				}
			    rowModel.add(row);
		    }
		}
		return rowModel;
	}

	
	public void onExportClientExcel(Event event) throws Exception {
		List<ClienteView> clienteExportList= null;
		String filters = getClientFilter().getFiltersForExportingExcelClientes();
		clienteExportList =clienteEJB.findAllConJdbcSinHibernate(filters);
		
		if(clienteExportList == null || (clienteExportList != null && clienteExportList.size() == 0)){
			MessageBox.info("No se encontraron resultados a guardar en excel", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}else{
			
			
			Iframe iframeReporte = new Iframe();
				iframeReporte.setWidth("0%");
		        iframeReporte.setHeight("0%");
				iframeReporte.setId("reporte");
				JRDistributionPlanSummaryXls jasperReport = new JRDistributionPlanSummaryXls();
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				
				List<String>fieldNames=new ArrayList();
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

				List<com.institucion.controller.RowModel>rowModel=obtenerParaExcel(clienteExportList);
								
				jasperReport.setFieldNames(fieldNames);
				jasperReport.setDataSourceTable(rowModel);
				try {
					jasperReport.exportXlsReport(os);
				} catch (JRException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				ByteArrayInputStream mediais = new ByteArrayInputStream(os.toByteArray());
				
				Calendar ahoraCal= Calendar.getInstance();
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR);
				
				AMedia amedia = new AMedia("Clientes_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

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
				float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(iterable_element.getId()));
				
				row.set("Deuda", " $"+formateador.format(cant ) );
			}else
				row.set("Deuda", " $"+formateador.format(0) );

		
			rowModel.add(row);			
		}
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
	
//		private boolean tieneInscripcionesVigentes(Cliente cli){
//		
//		// esto tiene dos conceptos:
//				// puede ser que sea por clases o libre
//				// si es por clase, esta bien validar por cupos.
//				// si es libre, tiene cupos.
//		
//		subs=clienteEJB.loadLazy(cli, true, true, true);
//
//		if(cli.getSubscripcionesList() != null){
//			for (Subscripcion iterable_element : cli.getSubscripcionesList()) {
//				
//				if(iterable_element.getCupoActividadList() != null){
//					for (CupoActividad cupoAct: iterable_element.getCupoActividadList()) {
//						
//						if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt()
//								||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt()){
//							
//								// si para estas subscripciones TIENE CUPO
//							if(iterable_element.getCupoActividadList() != null && iterable_element.getCupoActividadList().size() >0){
//								
//								for (CupoActividad cupo : iterable_element.getCupoActividadList()) {
//									
//									VencimientoCursoEnum vencimientoEnum= (VencimientoCursoEnum) cupo.getCurso().getVencimiento() ;
//									if(vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_QUINCENA.toInt() || 
//											vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LA_SEMANA.toInt() ||
//											vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_A_LOS_3_MES.toInt() ||
//											vencimientoEnum.toInt() == VencimientoCursoEnum.LIBRE_VENCE_AL_MES.toInt()){
//										
//										return true;
//									}else{
//										// es por clase
//										if(cupo.getCupos() > 0 )
//											return true;
//									}
//								}
//								
//							}
//						}		
//						
//					}
//					
//				}
//			}
//		}
//		return false;		
//	}


		@Override
		public void venderNuevaSubs(Event evt, int idCliente) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void actualizarPaneldePrecioProducto() {
			// TODO Auto-generated method stub
			
		}
}
package com.institucion.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import org.zkoss.zul.Window;

import com.institucion.bz.CursoEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.ActividadYClase;
import com.institucion.model.Curso;
import com.institucion.model.CursoList;
import com.institucion.model.CursosFilter;

public class CursosSelectorComposer extends SelectorFEComposer implements CursosCrudDelegate{
	DecimalFormat formateador = new DecimalFormat("###,###");

	private CursoList cursopanelListGrid;
	private List<Curso> pharmacyList;

	private CursoEJB cursoEJB;
	private PanelFilter filter;
	private Window win = null;

	public CursosSelectorComposer(){
		super();
		cursoEJB = BeanFactory.<CursoEJB>getObject("fmEjbCurso");
	}

	private CursosFilter getFilter() {
		return (CursosFilter)(filter.getGridFilter());
	}
	
	public void clear(){
		getFilter().clear();
		
//		getFilter().clear();

	}
	public void onCreate() {
		 getFilter().setDelegate(this); 
		if(Session.getDesktopPanel().getMessage().equals("menu")){
			clear();	
			setCallFromMenu(false);			
		}
		onFind();		
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		refreshEvents();
	}
	
	private void refreshEvents(){
		
		getFilter().getNombre().addEventListener(Events.ON_OK, new EventListener() {
			public void onEvent(Event evt){
				onFind(evt);
			}
		});	

	}

	public void onUpdate(Event event) throws Exception{
		if (hasSelectedOneItem(cursopanelListGrid)){
			Sessions.getCurrent().setAttribute("idCurso", cursopanelListGrid.getSelectedItem().getValue());		
			Sessions.getCurrent().setAttribute("position", cursopanelListGrid);
			Sessions.getCurrent().setAttribute("key", pharmacyList);
			Sessions.getCurrent().setAttribute("max", pharmacyList.size());
//			super.saveHistory();

			super.saveHistory("filter");

			super.gotoPage("/institucion/cursos-crud.zul");		
		}
		
	}

	public void onInsertClass(Event event) throws Exception{
		super.gotoPage("/institucion/clase-selector.zul");				
	}
	
	
	public void onDelete(Event event) {
	}

	public void onInsert(Event event) throws Exception {
	
		com.institucion.fm.conf.Session.setAttribute("idCurso", null);
		Sessions.getCurrent().setAttribute("max", 0);
		super.saveHistory("filter");
		super.gotoPage("/institucion/cursos-crud.zul");
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
		pharmacyList= new ArrayList<Curso>();
		
		if(getFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
			pharmacyList =cursoEJB.findAllConJdbc(getFilter().getFilters(false));	
			
		}else{
			pharmacyList =cursoEJB.findAll2();	
		}
		
		cursopanelListGrid.setList(pharmacyList, false);
	}

	public void onExportExcel(Event event) throws Exception {
		if(pharmacyList == null || (pharmacyList != null && pharmacyList.size() == 0)){
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
				fieldNames.add("Nombre Curso/Tratamiento");
				fieldNames.add("Paga Matricula");
				fieldNames.add("Cant TOTAL Clases");
				fieldNames.add("Disponible");
				fieldNames.add("Importe");

				List<com.institucion.controller.RowModel>rowModel=obtenerParaExcel();
								
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
				
				AMedia amedia = new AMedia("Cursos_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

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

	
	private List<com.institucion.controller.RowModel>  obtenerParaExcel (){
		 List<com.institucion.controller.RowModel>  rowModel= new ArrayList();
		for (Curso iterable_element : pharmacyList) {
			com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
			
			
			// 	fieldNames.add("Nombre Curso/Tratamiento");
			row.set("Nombre Curso/Tratamiento", iterable_element.getNombre());
			
			//fieldNames.add("Paga Matricula");
			if( iterable_element.isPagaSubscripcion())
				row.set("Paga Matricula", "SI");
			else
				row.set("Paga Matricula", "NO");
			
			row.set("Importe",  "$"+formateador.format(iterable_element.getPrecio()));

			if(iterable_element.getActividadYClaseList() != null){
				ActividadYClase act=iterable_element.getActividadYClaseList().iterator().next();
				if(act.getCantClases() == 99)
					row.set("Cant TOTAL Clases", "LIBRE");
				else
					row.set("Cant TOTAL Clases", String.valueOf(act.getCantClases()));
				
			}else{
				row.set("Cant TOTAL Clases", "");
			}
			
			if( iterable_element.isDisponible())
				row.set("Disponible", "SI");
			else
				row.set("Disponible", "NO");
		
			rowModel.add(row);
			
		}
		return rowModel;
	}
	
	
	public void buscar(){
		
		onFind();
	}

	
	public void onClearFilter(Event evt){
		
		getFilter().clear();
		if (isCallFromMenu()){
			getFilter().clear();
		}else{
			this.onFind();
		}		
	}
}
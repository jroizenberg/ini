//package com.institucion.controller;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//import org.zkoss.zk.ui.Sessions;
//import org.zkoss.zk.ui.event.Event;
//import org.zkoss.zul.Comboitem;
//
//import com.institucion.bz.CajaEJB;
//import com.institucion.desktop.delegated.PulsoDelegate;
//import com.institucion.fm.conf.Session;
//import com.institucion.fm.conf.spring.BeanFactory;
//import com.institucion.fm.desktop.service.I18N;
//import com.institucion.fm.desktop.service.SelectorFEComposer;
//import com.institucion.fm.desktop.view.MessageBox;
//import com.institucion.fm.desktop.view.PanelFilter;
//import com.institucion.fm.desktop.view.PanelReport;
//import com.institucion.model.PulsoMaipuDetalle;
//import com.institucion.model.PulsoMaipuDetalleParte2;
//import com.institucion.model.PulsoMaipuFilter;
//import com.institucion.model.PulsoMaipuResumenDetalladoList;
//
//public class PulsoMaipuSelectorComposer extends SelectorFEComposer implements PulsoDelegate{
//
//	private PanelFilter filter9;
//	private PanelReport report9;
////	private PanelReport report2;
////	private ClaseEJB claseEJB;
//
//	private CajaEJB cajaEJB;
//
//	public PulsoMaipuSelectorComposer(){
//		super();
//		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
////		claseEJB = BeanFactory.<ClaseEJB>getObject("fmEjbClase");
//
//	}
//
//	private PulsoMaipuFilter getClientFilter() {
//		return (PulsoMaipuFilter)(filter9.getGridFilter());
//	}
//
//	private PulsoMaipuResumenDetalladoList getResumenDetallado() {
//		return (PulsoMaipuResumenDetalladoList) report9.getGridReport();
//	}
//
//	public void onCreate() {
//
//		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
//		setCallFromMenu(false);
//		filter9.getInnerPanel().setOpen(true);
//		filter9.setOpen(true);
//		
//		// seleccionar un anio menos que el actual
////		getClientFilter().getAnio().setSelectedIndex(5);
//		seleccionarUnoMenosQueElactual();
//		getClientFilter().setDelegate(this);
//		
//		onFind(true);
//	}
//
//	private void seleccionarUnoMenosQueElactual(){
//		Calendar calendarDate = Calendar.getInstance();  
//		calendarDate.setTime(new Date());  
//		int anio=calendarDate.get(Calendar.YEAR); 
//		
//		if(calendarDate.get(Calendar.MONTH) <= 3){
//			anio= anio -1;
//		}
//
//		Iterator<Comboitem> itCursos =getClientFilter().getAnio().getItems().iterator();
//		while (itCursos.hasNext()) {
//			Comboitem cursosDeLaLista=(Comboitem)itCursos.next();
//			Long cursoSeleccionado=(Long)cursosDeLaLista.getValue();
//			if(cursoSeleccionado.intValue() == anio ){
//				getClientFilter().getAnio().setSelectedItem(cursosDeLaLista);
//			}
//		}
//	}
//	
//	
//	public void buscar(Event evt){
//		onFind(evt);
//	}
//
//	
//	public void onFind(Event evt) {
//		Session.setAttribute("pag", false);
//		this.onFind(false);
//	}
//	
//	public void onFind9(Boolean boo) {
//		// si viene del create
//		if(boo){
//			
//		}else{ 
//			if(getClientFilter().getAnio().getSelectedItem() == null){
//				MessageBox.info(I18N.getLabel("reporte.error.anio"), I18N.getLabel("selector.actionwithoutitem.title"));
//				return;		
//			}
//		}
//		
////		List<Quincena> quincenaList=claseEJB.findAllConActividadTomaListaDelDiaNombreCurso();
//
//		/*****************************************************************************/
//		/* ACA SE ARMA LA LISTA CON EL DETALLE POR MES Y ACTIVIDAD PAGOS, ETC */
//		List<PulsoMaipuDetalle> listaPulsoResumendetalle= cajaEJB.obtenerResumenDetalleDeTotalesJdbcMaipu((Long)getClientFilter().getAnio().getSelectedItem().getValue());
//
//		List<Integer> listaCursosIds=obtenerCursosPorDeTodoElAnio(listaPulsoResumendetalle);
//
//		/* ADAPTAR ACA TODAS LAS ACTIVIDADES PARA QUE TODOS LOS MESES TENGAN TODAS LAS ACTIVIDADES Y ESTEN ORDENADAS IGUAL, POR ID*/
//		listaPulsoResumendetalle= adaptarDatos(listaPulsoResumendetalle,listaCursosIds);
//		
//		// setear las posiciones 
//		listaPulsoResumendetalle= setearPosicionesEnQueSeMuestran(listaPulsoResumendetalle);
//		Sessions.getCurrent().setAttribute("numeroTab", 1);
//		// Este Setea de Enero a Julio
//		if(listaPulsoResumendetalle != null)
//			getResumenDetallado().setField(listaPulsoResumendetalle,listaCursosIds);		
//	
//	}
//	
//	private List<PulsoMaipuDetalle>  setearPosicionesEnQueSeMuestran(List<PulsoMaipuDetalle> pulso){
//		if(pulso != null){
//			
//			Collections.sort(pulso, new Comparator() {
//				public int compare(PulsoMaipuDetalle p1, PulsoMaipuDetalle p2) {
//					return new Integer(p1.getQuincenaId()).compareTo(new Integer(p2.getQuincenaId()));
//				}
//
//				@Override
//				public int compare(Object arg0, Object arg1) {
//					return (new Integer((((PulsoMaipuDetalle)arg0).getQuincenaId())).
//									compareTo(new Integer((((PulsoMaipuDetalle)arg1).getQuincenaId()))));
////					return 0;
//				}
//			});
//		}
//		return pulso;
//	}
//	
//	
//	private List<Integer> obtenerCursosPorDeTodoElAnio(List<PulsoMaipuDetalle> lista){
//		List<Integer> listass= new ArrayList();
//		
//		if(lista != null){
//		for (PulsoMaipuDetalle pulsoClinicaDetalle : lista) {
//			if(pulsoClinicaDetalle.getList() != null){
//				for (PulsoMaipuDetalleParte2 pulsoClinicaDetalleParte2 : pulsoClinicaDetalle.getList()) {
//					
//					if(!existeActividad(pulsoClinicaDetalleParte2.getIdCurso(), listass)){
//						listass.add(pulsoClinicaDetalleParte2.getIdCurso());
//					}
//				}
//			}
//		}
//		}
//		return 	listass;	
//	}
//	private List<PulsoMaipuDetalle> adaptarDatos(List<PulsoMaipuDetalle> lista,List<Integer> listaCursos){
//		if(lista != null){
//			// Ahora, por cada mes, ordeno cada objeto por el mismo criterio, que va a ser idActividad
//			
//			//Luego , recorro el objeto de Integers , voy comparando: si no existe ese idActividad Integuer, se lo creo igual vacio, 
//																	//si existe no hago nada 
//			for (PulsoMaipuDetalle pulso : lista) {
//				
//				// ordenar el objeto pulso.getList segun actividad
//				
//				for (Integer cursoId : listaCursos) {
//					
//					if(!existeIdActividadEnMes(cursoId.intValue(), pulso.getList())){
//						PulsoMaipuDetalleParte2 pulsoClinica2 = new PulsoMaipuDetalleParte2();
//						pulsoClinica2.setIdCurso(cursoId);
////						pulsoClinica2.setCantidadEgresoRRHH(0);
////						pulsoClinica2.setCantidadIngresoObraSocial(new Double(0));
//						pulsoClinica2.setCantidadIngresoEfectivo(new Double(0));
////						pulsoClinica2.setCantidadTotal(new Double(0));
//						if(pulso.getList() == null)
//							pulso.setList(new ArrayList());
//						
//						pulso.getList().add(pulsoClinica2);
//					}
//				}	
//			}
//		}
//		return lista;
//
//	}
//	
//	private boolean existeIdActividadEnMes(int cursoId, ArrayList<PulsoMaipuDetalleParte2> lista){
//		
//		if(lista != null){
//			for (PulsoMaipuDetalleParte2 pulsoClinicaDetalleParte2 : lista) {
//				if(pulsoClinicaDetalleParte2.getIdCurso() == cursoId)
//					return true;
//			}
//			
//		}
//		return false;
//	}
//	
//	private boolean existeActividad(Integer idActividad, List<Integer> listass){
//		if(listass != null){
//			for (Integer integer : listass) {
//				if(integer.intValue() == idActividad.intValue())
//					return true;
//			}
//		}
//		return false;
//	}
//	
//	public void onClearFilter(Event evt){
//		
//		getClientFilter().clear();
//
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(new Date());
//		seleccionarUnoMenosQueElactual();
////		getClientFilter().getAnio().setSelectedIndex(5);
//		
//		if (isCallFromMenu()){
//		}else{
//			this.onFind(true);
//		}		
//	}
//
//}
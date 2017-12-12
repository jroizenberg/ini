package com.institucion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tabbox;

import com.institucion.bz.CajaEJB;
import com.institucion.desktop.delegated.PulsoDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.desktop.view.PanelReport;
import com.institucion.model.PulsoClinica;
import com.institucion.model.PulsoClinicaDetalle;
import com.institucion.model.PulsoClinicaDetalleParte2;
import com.institucion.model.PulsoClinicaFilter;
import com.institucion.model.PulsoClinicaResumenDetalladoList;
import com.institucion.model.PulsoClinicaResumenDetalladoTAB2List;
import com.institucion.model.PulsoClinicaResumenList;
import com.institucion.model.PulsoMaipuDetalle;
import com.institucion.model.PulsoMaipuDetalleParte2;
import com.institucion.model.PulsoMaipuResumenDetalladoList;

public class PulsoClinicaSelectorComposer extends SelectorFEComposer implements PulsoDelegate{

	private PulsoClinicaResumenList cursopanelListGrid;
	private PanelFilter filter;
	private PanelReport report;
	private PanelReport report2;

//	private PanelFilter filter9;
	private PanelReport report9;
	private Panel titMaipu;
	private Tabbox tabpanelPrimero;
	
	private CajaEJB cajaEJB;

	public PulsoClinicaSelectorComposer(){
		super();
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}

	private PulsoClinicaFilter getClientFilter() {
		return (PulsoClinicaFilter)(filter.getGridFilter());
	}

	private PulsoClinicaResumenDetalladoList getResumenDetallado() {
		return (PulsoClinicaResumenDetalladoList) report.getGridReport();
	}

	private PulsoClinicaResumenDetalladoTAB2List getResumenDetallado2() {
		return (PulsoClinicaResumenDetalladoTAB2List) report2.getGridReport();
	}
	
	
//	private PulsoMaipuFilter getFilter9() {
//		return (PulsoMaipuFilter)(filter9.getGridFilter());
//	}

	private PulsoMaipuResumenDetalladoList getResumenDetallado9() {
		return (PulsoMaipuResumenDetalladoList) report9.getGridReport();
	}
	public void onCreate() {

		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
//		filter9.getInnerPanel().setOpen(true);
//		filter9.setOpen(true);

		getClientFilter().getAnio().setSelectedIndex(5);
		getClientFilter().setDelegate(this);
		
		seleccionarUnoMenosQueElactual();
//		getFilter9().setDelegate(this);
		
		onFind(true);
		onFind9(true);
	}

	private void seleccionarUnoMenosQueElactual(){
	
		if(getClientFilter().getAnio().getSelectedItem() != null){
			
			Calendar calendarDate = Calendar.getInstance();  
			calendarDate.setTime(new Date());  
			int mess=calendarDate.get(Calendar.MONTH);
			Long anioQueQueda=(Long)getClientFilter().getAnio().getSelectedItem().getValue();
			if(mess < 4){
				anioQueQueda=anioQueQueda -1;
			}

			
			int ttt=anioQueQueda.intValue();
			ttt= ttt +1;
			titMaipu.setTitle("Temporada: Desde:12/"+anioQueQueda.intValue() + "- Hasta:04/"+ttt);
		}		
	}
	
	public void buscar(Event evt){
		onFind(evt);
		seleccionarUnoMenosQueElactual();
//		tabpanelPrimero.setSelectedIndex(0);
		onFind9(false);
	}

	
	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind(false);
		this.onFind9(false);

	}
	
	public void onFind9(Boolean boo) {
		/* ACA SE ARMA LA LISTA CON EL DETALLE POR MES Y ACTIVIDAD PAGOS, ETC */
		
		// si el mes actual es < que 4, tengo que mandar por parametro año anterior, sino año actual
		
		Calendar calendarDate = Calendar.getInstance();  
		calendarDate.setTime(new Date());  
		int mess=calendarDate.get(Calendar.MONTH);
		Long anioQueQueda=(Long)getClientFilter().getAnio().getSelectedItem().getValue();
		if(mess < 4){
			anioQueQueda=anioQueQueda -1;
		}

		List<PulsoMaipuDetalle> listaPulsoResumendetalle= cajaEJB.obtenerResumenDetalleDeTotalesJdbcMaipu(anioQueQueda);

		List<Integer> listaCursosIds=obtenerCursosPorDeTodoElAnio(listaPulsoResumendetalle);

		/* ADAPTAR ACA TODAS LAS ACTIVIDADES PARA QUE TODOS LOS MESES TENGAN TODAS LAS ACTIVIDADES Y ESTEN ORDENADAS IGUAL, POR ID*/
//		listaPulsoResumendetalle= adaptarDatos9(listaPulsoResumendetalle,listaCursosIds);
		
		// setear las posiciones 
		listaPulsoResumendetalle= setearPosicionesEnQueSeMuestran(listaPulsoResumendetalle);
		Sessions.getCurrent().setAttribute("numeroTab", 1);
		// Este Setea de Enero a Julio
		if(listaPulsoResumendetalle != null)
			getResumenDetallado9().setField(listaPulsoResumendetalle,listaCursosIds);		
	
	}

	private List<PulsoMaipuDetalle>  setearPosicionesEnQueSeMuestran(List<PulsoMaipuDetalle> pulso){
		if(pulso != null){
			
			Collections.sort(pulso, new Comparator() {
				public int compare(PulsoMaipuDetalle p1, PulsoMaipuDetalle p2) {
					return new Integer(p1.getQuincenaId()).compareTo(new Integer(p2.getQuincenaId()));
				}

				@Override
				public int compare(Object arg0, Object arg1) {
					return (new Integer((((PulsoMaipuDetalle)arg0).getQuincenaId())).
									compareTo(new Integer((((PulsoMaipuDetalle)arg1).getQuincenaId()))));
//					return 0;
				}
			});
		}
		return pulso;
	}
	
	private List<PulsoMaipuDetalle> adaptarDatos9(List<PulsoMaipuDetalle> lista,List<Integer> listaCursos){
		if(lista != null){
			// Ahora, por cada mes, ordeno cada objeto por el mismo criterio, que va a ser idActividad
			
			//Luego , recorro el objeto de Integers , voy comparando: si no existe ese idActividad Integuer, se lo creo igual vacio, 
																	//si existe no hago nada 
			for (PulsoMaipuDetalle pulso : lista) {
				
				// ordenar el objeto pulso.getList segun actividad
				
				for (Integer cursoId : listaCursos) {
					
					if(!existeIdCursosEnMes(cursoId.intValue(), pulso.getList())){
						PulsoMaipuDetalleParte2 pulsoClinica2 = new PulsoMaipuDetalleParte2();
						pulsoClinica2.setIdCurso(cursoId);
//						pulsoClinica2.setCantidadEgresoRRHH(0);
//						pulsoClinica2.setCantidadIngresoObraSocial(new Double(0));
						pulsoClinica2.setCantidadIngresoEfectivo(new Double(0));
//						pulsoClinica2.setCantidadTotal(new Double(0));
						if(pulso.getList() == null)
							pulso.setList(new ArrayList());
						
						pulso.getList().add(pulsoClinica2);
					}
				}	
			}
		}
		return lista;

	}
	
	private boolean existeIdCursosEnMes(int cursoId, ArrayList<PulsoMaipuDetalleParte2> lista){
		
		if(lista != null){
			for (PulsoMaipuDetalleParte2 pulsoClinicaDetalleParte2 : lista) {
				if(pulsoClinicaDetalleParte2.getIdCurso() == cursoId)
					return true;
			}
			
		}
		return false;
	}

	private List<Integer> obtenerCursosPorDeTodoElAnio(List<PulsoMaipuDetalle> lista){
		List<Integer> listass= new ArrayList();
		
		if(lista != null){
		for (PulsoMaipuDetalle pulsoClinicaDetalle : lista) {
			if(pulsoClinicaDetalle.getList() != null){
				for (PulsoMaipuDetalleParte2 pulsoClinicaDetalleParte2 : pulsoClinicaDetalle.getList()) {
					
					if(!existeActividad(pulsoClinicaDetalleParte2.getIdCurso(), listass)){
						listass.add(pulsoClinicaDetalleParte2.getIdCurso());
					}
				}
			}
		}
		}
		return 	listass;	
	}
	public void onFind(Boolean boo) {
		// si viene del create
		if(boo){
			
		}else{ 
			if(getClientFilter().getAnio().getSelectedItem() == null){
				tabpanelPrimero.setSelectedIndex(0);	
				MessageBox.info(I18N.getLabel("reporte.error.anio"), I18N.getLabel("selector.actionwithoutitem.title"));
				return;		
			}
		}
		
		/*****************************************************************************/
		/* ACA ARMA EL RESUMEN CON LOS TOTALES */
		List<PulsoClinica> listaPulsoResumen= cajaEJB.obtenerResumenDeTotalesJdbc((Long)getClientFilter().getAnio().getSelectedItem().getValue());
		if(listaPulsoResumen != null)
			cursopanelListGrid.setField(listaPulsoResumen);

		/*****************************************************************************/
		/* ACA SE ARMA LA LISTA CON EL DETALLE POR MES Y ACTIVIDAD PAGOS, ETC */
		List<PulsoClinicaDetalle> listaPulsoResumendetalle= cajaEJB.obtenerResumenDetalleDeTotalesJdbc((Long)getClientFilter().getAnio().getSelectedItem().getValue());
		
		List<Integer> listaActividadesIds=obtenerActividadesPorDeTodoElAnio(listaPulsoResumendetalle);
		Collections.sort(listaActividadesIds);
		/* ADAPTAR ACA TODAS LAS ACTIVIDADES PARA QUE TODOS LOS MESES TENGAN TODAS LAS ACTIVIDADES Y ESTEN ORDENADAS IGUAL, POR ID*/
		listaPulsoResumendetalle= adaptarDatos(listaPulsoResumendetalle,listaActividadesIds);
		
		Sessions.getCurrent().setAttribute("numeroTab", 1);
		// Este Setea de Enero a Julio
		if(listaPulsoResumendetalle != null)
			getResumenDetallado().setField(listaPulsoResumendetalle,listaActividadesIds);

		Sessions.getCurrent().setAttribute("numeroTab", 2);
		// Este Setea de Agos a DIC
		if(listaPulsoResumendetalle != null)
			getResumenDetallado2().setField(listaPulsoResumendetalle,listaActividadesIds);
		
	}
	
	private List<PulsoClinicaDetalle> adaptarDatos(List<PulsoClinicaDetalle> lista,List<Integer> listaActividades){
		if(lista != null){
			// Ahora, por cada mes, ordeno cada objeto por el mismo criterio, que va a ser idActividad
			
			//Luego , recorro el objeto de Integers , voy comparando: si no existe ese idActividad Integuer, se lo creo igual vacio, 
																	//si existe no hago nada 
			for (PulsoClinicaDetalle pulso : lista) {
				
				// ordenar el objeto pulso.getList segun actividad
				
				for (Integer actividadId : listaActividades) {
					
					if(!existeIdActividadEnMes(actividadId, pulso.getList())){
						PulsoClinicaDetalleParte2 pulsoClinica2 = new PulsoClinicaDetalleParte2();
						pulsoClinica2.setIdActividad(actividadId);
						pulsoClinica2.setCantidadEgresoRRHH(0);
						pulsoClinica2.setCantidadIngresoObraSocial(new Double(0));
						pulsoClinica2.setCantidadIngresoEfectivo(new Double(0));
						pulsoClinica2.setCantidadTotal(new Double(0));
						if(pulso.getList() == null)
							pulso.setList(new ArrayList());
						
						pulso.getList().add(pulsoClinica2);
					}
				}	
			}
		}
		return lista;

	}
	
	private boolean existeIdActividadEnMes(int actividadId, ArrayList<PulsoClinicaDetalleParte2> lista){
		
		if(lista != null){
			for (PulsoClinicaDetalleParte2 pulsoClinicaDetalleParte2 : lista) {
				if(pulsoClinicaDetalleParte2.getIdActividad() == actividadId)
					return true;
			}
			
		}
		return false;
	}
	
	private List<Integer> obtenerActividadesPorDeTodoElAnio(List<PulsoClinicaDetalle> lista){
		List<Integer> listass= new ArrayList();
		
		if(lista != null){
		for (PulsoClinicaDetalle pulsoClinicaDetalle : lista) {
			if(pulsoClinicaDetalle.getList() != null){
				for (PulsoClinicaDetalleParte2 pulsoClinicaDetalleParte2 : pulsoClinicaDetalle.getList()) {
					
					if(!existeActividad(pulsoClinicaDetalleParte2.getIdActividad(), listass)){
						listass.add(pulsoClinicaDetalleParte2.getIdActividad());
					}
				}
			}
		}
		}
		return 	listass;	
	}
	
	private boolean existeActividad(Integer idActividad, List<Integer> listass){
		if(listass != null){
			for (Integer integer : listass) {
				if(integer.intValue() == idActividad.intValue())
					return true;
			}
		}
		return false;
	}
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		getClientFilter().getAnio().setSelectedIndex(5);
		seleccionarUnoMenosQueElactual();
		if (isCallFromMenu()){
		}else{
			this.onFind(true);
		}		
	}

}
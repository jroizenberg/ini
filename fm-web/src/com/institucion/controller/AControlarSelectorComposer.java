package com.institucion.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import com.institucion.bz.CajaEJB;
import com.institucion.desktop.delegated.CursosCrudDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.model.AControlarFilter;
import com.institucion.model.AControlarList;
import com.institucion.model.CajaMovimientoView;

public class AControlarSelectorComposer extends SelectorFEComposer implements CursosCrudDelegate{
	
	private AControlarList clientepanelListGrid;
	private  List<CajaMovimientoView> pharmacyList;
	private PanelFilter filter;

	private CajaEJB cajaEJB;

	public AControlarSelectorComposer(){
		super();
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
	}

	private AControlarFilter getClientFilter() {
		return (AControlarFilter)(filter.getGridFilter());
	}
	
	public void onCreate() {
		getClientFilter().setDelegate(this);
		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		getClientFilter().getFechaDesde().setValue(cal.getTime());
		getClientFilter().getFechaHasta().setValue(cal.getTime());

		onFind(true);
	}

	
	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind(false);
	}
		
	public void onFind(Boolean boo) {
		
		if(boo){
			
		}else{
			// valido que se hayan ingresado FechaDesde , FechaHasta 
			if(getClientFilter().getFechaDesde().getValue() == null 
					|| getClientFilter().getFechaHasta().getValue() == null){
				MessageBox.info(I18N.getLabel("caja.error.no.ingreso.fechas"), I18N.getLabel("selector.actionwithoutitem.title"));
				return;		
			}
		}
		pharmacyList= new ArrayList<CajaMovimientoView>();
		pharmacyList =cajaEJB.findAllConJdbcAControlar(getClientFilter().getFechaDesde().getValue() , getClientFilter().getFechaHasta().getValue());
		clientepanelListGrid.setList(pharmacyList);
	}
		
//	Comparator<CajaMovimiento> comparator = new Comparator<CajaMovimiento>() {
//	    public int compare( CajaMovimiento a, CajaMovimiento b ) {
//	    	   return b.getFecha().compareTo(a.getFecha());
//	    }
//	};
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		getClientFilter().getFechaDesde().setValue(cal.getTime());
		getClientFilter().getFechaHasta().setValue(cal.getTime());

		if (isCallFromMenu()){
		}else{
			this.onFind(true);
		}		
	}

	@Override
	public void buscar() {
		onFind(false);
	}

}
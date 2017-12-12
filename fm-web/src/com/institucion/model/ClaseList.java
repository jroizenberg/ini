package com.institucion.model;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.desktop.delegated.ClaseIngresoDelegate;
import com.institucion.desktop.helper.BooleanViewHelper;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class ClaseList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClaseIngresoDelegate delegate;
	
	public ClaseList() {
		super();
		super.addHeader(new Listheader("Nombre")).setWidth("35%");;
		super.addHeader(new Listheader(I18N.getLabel("clase.iniciaFin"))).setWidth("8%");
		super.addHeader(new Listheader(I18N.getLabel("curso.cantAlumnos"))).setWidth("14%");
		super.addHeader(new Listheader("Cliente en Clase/Sesion")).setWidth("10%");
		super.addHeader(new Listheader("Disponible")).setWidth("8%");
		this.setMultiple(false);
		this.setPageSize(8);
	}

	
	private boolean laClaseDelClienteEsQueElClienteEstaEnEsaClase(Clase clase, List<Clase> claseParaCompararList){
		
		if(claseParaCompararList != null){
			
			for (Clase clase2 : claseParaCompararList) {
				
				if(clase.getId().intValue() == clase2.getId().intValue())
					return true;
			}
			
		}
		
		return false;
	}
	
	
	// si esta la clase en claseParaCompararList la pinto en verde
	public void setSelectedItem(Clase selectedClass) {

		Iterator ite= getItems().iterator();
		while(ite.hasNext()){
			Listitem item=(Listitem)ite.next();
			Clase clas=(Clase)item.getValue();
			if(clas != null && clas.getId() != null && selectedClass != null &&
					selectedClass.getId() != null && clas.getId().equals(selectedClass.getId())){
				item.setSelected(true);
				break;
			}
		}
	}
	
	// si esta la clase en claseParaCompararList la pinto en verde
	public void setList(List<Clase> claseList, List<Clase> claseParaCompararList) {
		super.removeAll();
		if(claseList != null){
			Iterator<Clase> itPharmacy = claseList.iterator();
			while (itPharmacy.hasNext()) {
				Clase clase = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(clase);
				row.setAttribute("prodID", String.valueOf(clase.getId()));

				row.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){
					public void onEvent(Event evt){
						String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
						try {
							delegate.ingresarAClase(evt, Integer.parseInt(prodID));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});


				boolean hayQuePintarClase=laClaseDelClienteEsQueElClienteEstaEnEsaClase(clase, claseParaCompararList);

				
				Listcell companyCell2 = new Listcell(clase.getNombre());

//				if(hayQuePintarClase){
//					companyCell2.setStyle("color:#0B610B !important; font-weight:bold; ");
//				}
				row.appendChild(companyCell2);
				
				if(clase.getEsClaseSinHora() != null && clase.getEsClaseSinHora().booleanValue()){
					
					Listcell companyCell3 = new Listcell("Sin Horario Fijo");

//					if(hayQuePintarClase){
//						companyCell3.setStyle("color:#0B610B !important; font-weight:bold; ");
//					}
					row.appendChild(companyCell3);
					
				}else{
					String horaDesde= null;
					if(clase.getHoraDesde().getMinutes() < 10){ 
						horaDesde= clase.getHoraDesde().getHours() +":0"+clase.getHoraDesde().getMinutes() ;	
					}else{
						horaDesde= clase.getHoraDesde().getHours() +":"+clase.getHoraDesde().getMinutes();
					}
					
					String horaHasta= null;
					if(clase.getHoraHasta().getMinutes() < 10){ 
						horaHasta= clase.getHoraHasta().getHours() +":0"+clase.getHoraHasta().getMinutes();	
					}else{
						horaHasta= clase.getHoraHasta().getHours() +":"+clase.getHoraHasta().getMinutes();
					}
					
					
					Listcell companyCell3 = new Listcell(horaDesde +" - "+ horaHasta);

//					if(hayQuePintarClase){
//						companyCell3.setStyle("color:#0B610B !important; font-weight:bold; ");
//					}
					row.appendChild(companyCell3);			
				}
				
				if(clase.getCantAlumnos() != null){
					
					Listcell companyCell5 = new Listcell(String.valueOf(clase.getCantAlumnos()));

//					if(hayQuePintarClase){
//						companyCell5.setStyle("color:#0B610B !important; font-weight:bold; ");
//					}
					row.appendChild(companyCell5);
					
//					row.appendChild(new Listcell(String.valueOf(clase.getCantAlumnos())));
				}else
					row.appendChild(new Listcell(""));		

				
				Listcell vencsubscell222= null ;
				if(hayQuePintarClase){
					vencsubscell222 = new Listcell("Si");
					vencsubscell222.setStyle("color:#FFB24C !important; font-weight:bold; font-size: 8pt;");
				}else{
					vencsubscell222 = new Listcell("No");
				}
				row.appendChild(vencsubscell222);
				
				
				Listcell pagasubscell4 = new Listcell(BooleanViewHelper.getBooleanString(clase.isDisponible()));

				if(!clase.isDisponible()){
					// pintar en rojo y negrita
					pagasubscell4.setStyle("color:#FF0000 !important; font-weight:bold; ");
				}else{
					// pintar en verde y negrita
					pagasubscell4.setStyle("color:#0B610B !important; font-weight:bold; ");
				}
				row.appendChild(pagasubscell4);
//				super.addRow(row, "onDoubleClickSubscripcionesEvt");
				super.addRow(row, "onClickClasesEvt");

				Component nullComponent = null;
				row.addForward("onClick",nullComponent,"onClickClasesEvt");

				
				super.addRow(row);
			}
		}
	}


	public ClaseIngresoDelegate getDelegate() {
		return delegate;
	}


	public void setDelegate(ClaseIngresoDelegate delegate) {
		this.delegate = delegate;
	}	
}
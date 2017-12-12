package com.institucion.fm.desktop.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.model.ClienteListaEncontradoEnPileta;
import com.institucion.model.EmpleadoActividades;
import com.institucion.model.ObraSocialesPrecio;
import com.institucion.model.Producto;

public abstract class PanelRoutingCrud<T> extends GridReport {

	private static final long serialVersionUID = 1L;
	public static final String ID = "panelRoutingCrud";


//	private DistributionPlanListDelegate delegateDistributionPlanList;
	private boolean checkbox;
	private boolean uievent;
	private boolean disable;
	private Checkbox routingCK;
	private int line;
	
	
	protected abstract boolean setPanelTitle();
	protected abstract void doEvent(Event evt);
	protected abstract void setCheckbox();
	
	protected abstract void setUIEvent();

	public PanelRoutingCrud() {
		super();
		line = 0;
		this.makeTitle();
	}
	
	
	protected void makeTitle(){
		this.addAuxhead();
		if (this.setPanelTitle()){
			this.addRow();
			this.addAuxheader(I18N.getLabel("panel.routing.crud.routing"), 5, 1);
			this.addColumn(I18N.getLabel("panel.routing.crud.one"));
			this.addColumn(I18N.getLabel("panel.routing.crud.two"));
			this.addColumn(I18N.getLabel("panel.routing.crud.three"));
			this.addColumn(I18N.getLabel("panel.routing.crud.four"));
			this.addColumn(I18N.getLabel("panel.routing.crud.five"));
		}
		else{
//			this.addAuxheader(I18N.getLabel("distributionPlan.list.distributionQuantity"), 1, 1);
			this.addColumn(I18N.getLabel(""));
		}
		this.addRow();
		
		
	}

	protected void makeRouting(T routing) {
		if (isCheckbox()) {
			this.makeCheckBoxFields(routing, null);
		} else {
			this.makeIntBoxFields((T) routing, null);
		}
	}
	
	public boolean isCheckbox() {
		return checkbox;
	}
	
	public boolean isUievent() {
		return uievent;
	}
	
	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public void setUievent(boolean uievent) {
		this.uievent = uievent;
	}
		
	public boolean isDisable() {
		return disable;
	}
	
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	
	private void makeCheckBoxFields(T routing, String onlyView){
//WEEKONE			
		line = line + 1;
		routingCK = new Checkbox();
		if (isUievent()) {
			routingCK.addEventListener(Events.ON_CHECK, new EventListener() {
				public void onEvent(Event evt) {
					doEvent(evt);
				}
			});
		}

//		if (onlyView != null)
//			routingCK.setId(WEEKONEID + "-" + ((Routing) routing).getContact().getId() + "-"+ onlyView);
//		else
//			routingCK.setId(WEEKONEID + "-"	+ ((Routing) routing).getContact().getId());
//
//		routingCK.setAttribute("routing", routing);
//		routingCK.setAttribute("id", WEEKONEID);
//		routingCK.setChecked((((Routing) routing).getWeekone() != null ? ((Routing) routing).getWeekone(): false));
//		if (this.isDisable()) {
//			routingCK.setDisabled(true);
//		}
		this.addField(routingCK);

//WEEKTWO			
		routingCK = new Checkbox();
		if (isUievent()) {
			routingCK.addEventListener(Events.ON_CHECK, new EventListener() {
				public void onEvent(Event evt) {
					doEvent(evt);
				}
			});
		}
//		if (onlyView != null)
//			routingCK.setId(WEEKTWOID + "-"	+ ((Routing) routing).getContact().getId() + "-"+ onlyView);
//		else
//			routingCK.setId(WEEKTWOID + "-"	+ ((Routing) routing).getContact().getId());
//
//		routingCK.setAttribute("routing", routing);
//		routingCK.setAttribute("id", WEEKTWOID);
//		routingCK.setChecked((((Routing) routing).getWeektwo() != null ? ((Routing) routing).getWeektwo(): false));
//		if (this.isDisable()) {
//			routingCK.setDisabled(true);
//		}
		this.addField(routingCK);
//WEEKTHREE		
		routingCK = new Checkbox();
		if (isUievent()) {
			routingCK.addEventListener(Events.ON_CHECK, new EventListener() {
				public void onEvent(Event evt) {
					doEvent(evt);
				}
			});
		}
//		if (onlyView != null)
//			routingCK.setId(WEEKTHREEID + "-"+ ((Routing) routing).getContact().getId() + "-"+ onlyView);
//		else
//			routingCK.setId(WEEKTHREEID + "-"+ ((Routing) routing).getContact().getId());
//
//		routingCK.setAttribute("id", WEEKTHREEID);
//		routingCK.setAttribute("routing", routing);
//		routingCK.setChecked((((Routing) routing).getWeekthree() != null ? ((Routing) routing).getWeekthree(): false));
//		if (this.isDisable()) {
//			routingCK.setDisabled(true);
//		}
		this.addField(routingCK);
//WEEKFOUR		
		routingCK = new Checkbox();
		if (isUievent()) {
			routingCK.addEventListener(Events.ON_CHECK, new EventListener() {
				public void onEvent(Event evt) {
					doEvent(evt);
				}
			});
		}
//		if (onlyView != null)
//			routingCK.setId(WEEKFOURID + "-"+ ((Routing) routing).getContact().getId() + "-"+ onlyView);
//		else
//			routingCK.setId(WEEKFOURID + "-"+ ((Routing) routing).getContact().getId());
//
//		routingCK.setAttribute("id", WEEKFOURID);
//		routingCK.setAttribute("routing", routing);
//		routingCK.setChecked((((Routing) routing).getWeekfour() != null ? ((Routing) routing).getWeekfour(): false));
//		if (this.isDisable()) {
//			routingCK.setDisabled(true);
//		}
		this.addField(routingCK);
//WEEKFIVE
		routingCK= new Checkbox();
		if (isUievent()){
			routingCK.addEventListener(Events.ON_CHECK, new EventListener()	{
				public void onEvent(Event evt){
					doEvent(evt);
				}
			});
		}
//		if(onlyView!=null)
//			routingCK.setId(WEEKFIVEID+"-"+((Routing) routing).getContact().getId()+"-"+onlyView);
//		else
//			routingCK.setId(WEEKFIVEID+"-"+((Routing) routing).getContact().getId());
//		
//		routingCK.setAttribute("id", WEEKFIVEID);
//		routingCK.setAttribute("routing", routing);
//		routingCK.setChecked((((Routing) routing).getWeekfive()!=null?((Routing) routing).getWeekfive():false));
//		if (this.isDisable()){
//			routingCK.setDisabled(true);	
//		}
		this.addField(routingCK);
	}
	
	
	private void makeIntBoxFields(T distribution, String onlyView) {
//WEEKONE
	
	}
		
	@SuppressWarnings("unchecked")
	public void clear(){
		Rows rows =this.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
			} 
		this.addRow();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<T>getList(){
//		Checkbox check=null;
//		Intbox intb=null;
//		boolean has=false;
		List<T>routings = new ArrayList<T>();
//		for (Object obj: super.getRows().getChildren()) {
//			Row row = (Row)obj;
//			for (Object obj2: row.getChildren()) {
//				Component c = (Component)obj2;
//				if (c instanceof Checkbox || c instanceof Intbox){
//					T routing = (T) c.getAttribute("routing");
//					if (routing !=null){
//						if (!routings.contains(routing)){
//							if (checkbox){
//								has = false;
//								check=(Checkbox)c;
//								((Routing) routing).setWeekone(check.isChecked());
//								has = check.isChecked();
//								
//								check=(Checkbox)getFellow(WEEKTWOID+"-"+((Routing) routing).getContact().getId());
//								((Routing) routing).setWeektwo(check.isChecked());
//								if (!has)
//									has = check.isChecked();
//								
//								check=(Checkbox)getFellow(WEEKTHREEID+"-"+((Routing) routing).getContact().getId());
//								((Routing) routing).setWeekthree(check.isChecked());
//								if (!has)
//									has = check.isChecked();
//								
//								check=(Checkbox)getFellow(WEEKFOURID+"-"+((Routing) routing).getContact().getId());
//								((Routing) routing).setWeekfour(check.isChecked());
//								if (!has)
//									has = check.isChecked();
//								
//								check=(Checkbox)getFellow(WEEKFIVEID+"-"+((Routing) routing).getContact().getId());
//								((Routing) routing).setWeekfive(check.isChecked());
//								if (!has)
//									has = check.isChecked();
//								if (has)
//									routings.add(routing);
//							} else {
//								Distribution d = ((Distribution) routing);
//								intb=(Intbox)c;
//								d.setPlannedQuantity(intb.getValue());
////								((Distribution) routing).setWeekQuantity(intb.getValue());
////								intb=(Intbox)getFellow(WEEKTWOID+"-"+((Distribution) routing).getContact().getId());
////								((Distribution)routing).setWeektwoamount(intb.getValue());
////								intb=(Intbox)getFellow(WEEKTHREEID+"-"+((Distribution) routing).getContact().getId());
////								((Distribution)routing).setWeekthreeamount(intb.getValue());
////								intb=(Intbox)getFellow(WEEKFOURID+"-"+((Distribution) routing).getContact().getId());
////								((Distribution)routing).setWeekfouramount(intb.getValue());
////								intb=(Intbox)getFellow(WEEKFIVEID+"-"+((Distribution) routing).getContact().getId());
////								((Distribution)routing).setWeekfiveamount(intb.getValue());
//								routings.add(routing);
//							}
//						}
//							
//					}
//				}
//			}
//			
//		}
		return routings;
	}
	
	public void makeIntBoxFieldsFromRow(T distribution, Row row){
		//WEEKONE
		
	}
	
//	public DistributionPlanListDelegate getDelegateDistributionPlanList() {
//		return delegateDistributionPlanList;
//	}
//
//	public void setDelegateDistributionPlanList(DistributionPlanListDelegate delegate) {
//		this.delegateDistributionPlanList = delegate;
//	}
	

	@SuppressWarnings("unchecked")
	public List<T>getListProducts(){
		Checkbox check=null;
		List<T>routings = new ArrayList<T>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Checkbox || c instanceof Intbox){
					T routing = (T) c.getAttribute("check");
					if (routing !=null){
						check=(Checkbox)c;
						((Producto)routing).setChecked(check.isChecked());
						routings.add(routing);
	
					}
				}
			}
			
		}
		return routings;
	}

	@SuppressWarnings("unchecked")
	public List<T>getListProductsSelected(){
		Checkbox check=null;
		List<T>routings = new ArrayList<T>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Checkbox || c instanceof Intbox){
					T routing = (T) c.getAttribute("check");
					if (routing !=null){
						check=(Checkbox)c;
						if(check.isChecked())
							routings.add(routing);
	
					}
				}
			}
			
		}
		return routings;
	}
	
	@SuppressWarnings("unchecked")
	public List<Producto>getListProductsSelectedConCantidadActualizada(){
		Checkbox check=null;
		List<Producto>routings = new ArrayList<Producto>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			boolean tienecheck= false;
			Producto prod= null;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Checkbox ){ //|| c instanceof Intbox){
					T routing = (T) c.getAttribute("check");
					if (routing !=null){
						check=(Checkbox)c;
						if(check.isChecked()){
							tienecheck= true;
							prod= (Producto)routing;
						}
	
					}
				}
				if (c instanceof Intbox){
					if(tienecheck){
						int cant=((Intbox)c).getValue();
						prod.setCantidad(cant);
						routings.add(prod);	
					}
					tienecheck=false;
					prod= null;
				}
			}	
		}
		return routings;
	}
	
	@SuppressWarnings("unchecked")
	public void actualizarListaDeCantVentaCambiada(String idProdu){
		Checkbox check=null;
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Checkbox ){ //|| c instanceof Intbox){
					T routing = (T) c.getAttribute("check");
					if (routing !=null){
						check=(Checkbox)c;
					}
				}
				if (c instanceof Intbox){
					
					if(check.getId().equalsIgnoreCase(idProdu)){
						int cant=((Intbox)c).getValue();
						if(cant == 0)
							check.setChecked(false);
						else
							check.setChecked(true);	
					}
				}
			}	
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<ObraSocialesPrecio>getListObraSocialesPreciosSelectedConCantidadActualizada(){
		ObraSocialesPrecio actual=null;
		List<ObraSocialesPrecio>routings = new ArrayList<ObraSocialesPrecio>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			boolean pasoPorDisponible= false;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				
				if (c instanceof Label ){ //|| c instanceof Intbox){
					ObraSocialesPrecio routing = (ObraSocialesPrecio) c.getAttribute("valorObjeto");
					if (routing !=null){
						actual= routing;
					}
				}
			
				if (c instanceof Combobox){
					if(!pasoPorDisponible){
						if(((Combobox) c).getSelectedItem() != null){
							actual.setDisponible((Boolean)((Combobox) c).getSelectedItem().getValue());	
						}else{
							actual.setDisponible(false);
						}
						pasoPorDisponible= true;
					}else{
						if(((Combobox) c).getSelectedItem() != null){
							actual.setSePagaUnaUnicaVez((Boolean)((Combobox) c).getSelectedItem().getValue());	
						}else{
							actual.setSePagaUnaUnicaVez(false);
						}
					}
				}
				
				if (c instanceof Intbox){
					Boolean pagacli = (Boolean) c.getAttribute("pagaCliente");
					
					if(pagacli != null && pagacli){
						
						if(((Intbox)c).getValue() == null)
							throw new WrongValueException(c, I18N.getLabel("error.empty.field"));

						int cant=((Intbox)c).getValue();
						actual.setPrecioQuePagaElCliente(cant);
					}

				}
				
				if (c instanceof Doublebox){
					if(((Doublebox)c).getValue() == null)
						throw new WrongValueException(c, I18N.getLabel("error.empty.field"));

					Double cant=((Doublebox)c).getValue();
					
					actual.setPrecioQuePagaLaObraSocial(cant);
					routings.add(actual);	
					pasoPorDisponible= false;
					actual= null;
				}
			}	
		}
		return routings;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoActividades>getListActividadesSueldosActualizada(){
		EmpleadoActividades actual=null;
		List<EmpleadoActividades>routings = new ArrayList<EmpleadoActividades>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				
				if (c instanceof Label ){ //|| c instanceof Intbox){
					EmpleadoActividades routing = (EmpleadoActividades) c.getAttribute("valorObjeto");
					if (routing !=null){
						actual= routing;
					}
				}
			
				if (c instanceof Combobox){
					if(((Combobox) c).getSelectedItem() != null){
						actual.setDisponible((Boolean)((Combobox) c).getSelectedItem().getValue());	
					}else{
						actual.setDisponible(false);
					}
					routings.add(actual);	
					actual= null;

				}
				
//				if (c instanceof Intbox){
//					Boolean pagacli = (Boolean) c.getAttribute("pagaCliente");
//					
//					if(pagacli != null && pagacli){
//						
//						if(((Intbox)c).getValue() == null)
//							throw new WrongValueException(c, I18N.getLabel("error.empty.field"));
//
//						int cant=((Intbox)c).getValue();
//						actual.setPrecioQuePagaElCliente(cant);
//					}
//
//				}
//				
//				if (c instanceof Doublebox){
//					if(((Doublebox)c).getValue() == null)
//						throw new WrongValueException(c, I18N.getLabel("error.empty.field"));
//
//					Double cant=((Doublebox)c).getValue();
//					
//					actual.setPrecioQuePagaLaObraSocial(cant);
//					routings.add(actual);	
//					pasoPorDisponible= false;
//					actual= null;
//				}
			}	
		}
		return routings;
	}
	
	@SuppressWarnings("unchecked")
	public List<ClienteListaEncontradoEnPileta>getListClientesEnClase(){
		List<ClienteListaEncontradoEnPileta>routings = new ArrayList<ClienteListaEncontradoEnPileta>();
		for (Object obj: super.getRows().getChildren()) {
			Row row = (Row)obj;
			for (Object obj2: row.getChildren()) {
				Component c = (Component)obj2;
				if (c instanceof Checkbox ){
					Checkbox cc=(Checkbox)c;
					ClienteListaEncontradoEnPileta routing = (ClienteListaEncontradoEnPileta) c.getAttribute("check");
					if (routing !=null){
						routing.setAsistencia(cc.isChecked());
						routings.add(routing);
					}
				}
			}
			
		}
		return routings;
	}
	
	
}

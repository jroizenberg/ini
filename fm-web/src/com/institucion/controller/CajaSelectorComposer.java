package com.institucion.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.institucion.bz.CajaEJB;
import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.desktop.delegated.CajaDelegate;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.service.SelectorFEComposer;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.fm.desktop.view.PanelFilter;
import com.institucion.fm.security.bz.SecurityAAEJB;
import com.institucion.fm.security.model.User;
import com.institucion.model.Actividad;
import com.institucion.model.BancoPromocion;
import com.institucion.model.CajaFilter;
import com.institucion.model.CajaList;
import com.institucion.model.CajaMovimiento;
import com.institucion.model.CajaTotales2Crud;
import com.institucion.model.CajaTotalesCrud;
import com.institucion.model.CierreCaja;
import com.institucion.model.ClienteView;
import com.institucion.model.CupoActividad;
import com.institucion.model.CupoActividadEstadoEnum;
import com.institucion.model.FormasDePagoSubscripcionEnum;
import com.institucion.model.PagosPorSubscripcion;
import com.institucion.model.Subscripcion;
import com.institucion.model.SucursalEnum;
import com.institucion.model.SumasTotalesMovimientos;
import com.institucion.model.TipoMovimientoCajaEnum;

public class CajaSelectorComposer extends SelectorFEComposer implements CajaDelegate{
	
	private CajaList clientepanelListGrid;
	private List<CajaMovimiento> pharmacyList;
	private PanelFilter filter;

	private PanelCrud totalesCrud; 
	private PanelCrud totales2Crud; 

	private CajaEJB cajaEJB;
	private ClienteEJB clienteEJB;
	private InscripcionEJB inscripcionEJB;

	private SecurityAAEJB securityService;
	DecimalFormat formateador = new DecimalFormat("###,###");
	private Window win = null;

	public CajaSelectorComposer(){
		super();
		cajaEJB = BeanFactory.<CajaEJB>getObject("fmEjbCaja");
		securityService= BeanFactory.<SecurityAAEJB>getObject("fm.ejb.securityAAService");
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
		inscripcionEJB= BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");
	}

	private CajaFilter getClientFilter() {
		return (CajaFilter)(filter.getGridFilter());
	}

	public SecurityAAEJB getSecurityService(){
		return securityService;
	}
	
	private CajaTotalesCrud getCajaTotalesCrudCrud() {
		return (CajaTotalesCrud) (totalesCrud.getGridCrud());
	}
	private CajaTotales2Crud getCajaTotales2CrudCrud() {
		return (CajaTotales2Crud) (totales2Crud.getGridCrud());
	}
	
	public void clear(){
		getClientFilter().clear();
	}
	
	public void onCreate() {
		if(Session.getAttribute("sucursalPrincipalSeleccionada") == null){
			MessageBox.validation("�Debe seleccionar con que sucursal desea operar!", I18N.getLabel("selector.actionwithoutitem.title"));
			Executions.getCurrent().sendRedirect(null);
		}

		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		com.institucion.fm.conf.Session.setAttribute("idFromCaja", null);
		
		setCallFromMenu(Session.getDesktopPanel().getMessage().equals("menu")); // enlazado con DesktopMenu
		setCallFromMenu(false);
		clear();
		filter.getInnerPanel().setOpen(true);
		filter.setOpen(true);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		getClientFilter().getFechaDesde().setValue(cal.getTime());
		getClientFilter().getFechaHasta().setValue(cal.getTime());
		getClientFilter().setActionComposerDelegate(this);
		
		if(!validaPermisos()){
			getClientFilter().getFechaDesde().setDisabled(true);
			getClientFilter().getFechaHasta().setDisabled(true);
			
			// griseo su filtro de usuario
			getClientFilter().getUsuarios().setDisabled(true);
		}
		
		int i= 0;
		Iterator<Comboitem> itCursos =getClientFilter().getUsuarios().getItems().iterator();
		while (itCursos.hasNext()) {
			Comboitem cursosDeLaLista=(Comboitem)itCursos.next();
			if(cursosDeLaLista.getValue() != null && cursosDeLaLista.getValue() instanceof com.institucion.fm.security.model.User){
				if(((com.institucion.fm.security.model.User)(cursosDeLaLista.getValue())).getName().equalsIgnoreCase(Session.getUsername()))
					getClientFilter().getUsuarios().setSelectedIndex(i);
			}
			i=i+1;
		}

		getClientFilter().getCurso().setSelectedIndex(0);
		getClientFilter().getTipoMovimiento().setSelectedIndex(0);
		getClientFilter().getPromocionBanco().setSelectedIndex(0);

		onFind(true);
		
		pongoVisibleCrudObrasSociales(false);
	}

	private boolean validaPermisos(){
		User usuario=getSecurityService().getUserWs(Session.getUsername());
		if(usuario != null && usuario.getGroups() != null){
			for (com.institucion.fm.security.model.Group iterable_element : usuario.getGroups()) {
				if(iterable_element.getName().equalsIgnoreCase("Secretaria"))
					return false;
			}
		}
		return true;
	}
	
	public void onCerrarCaja(Event evt) {
		super.gotoPage("/institucion/cerrarCaja-crud.zul");		
	}
	
	
	public void onIngresosEgresos(Event evt) {
		super.gotoPage("/institucion/ingresosEgresos-crud.zul");		
	}
	
	public void onFind(Event evt) {
		Session.setAttribute("pag", false);
		this.onFind(false);
	}
	
	
	
	public void onGastosCentro(Event evt){
		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		com.institucion.fm.conf.Session.setAttribute("idFromCaja", true);
		super.saveHistory();
		super.gotoPage("/institucion/gastos-crud.zul");
	}

	
	public void onGastosMaipu(Event evt){
		com.institucion.fm.conf.Session.setAttribute("idGasto", null);
		com.institucion.fm.conf.Session.setAttribute("idFromCaja", true);
		super.saveHistory();
		super.gotoPage("/institucion/gastosMaipu-crud.zul");
	}

	
	private String getFilters(){
		
		StringBuilder actionConditions= new StringBuilder("select caja.id  from cajamovimiento caja   ");
		
			if (getClientFilter().getCurso().getSelectedIndex() >= 0 && getClientFilter().getCurso().getSelectedItem().getValue() instanceof Actividad) {
				actionConditions.append("left join subscripcion  subs on (caja.idSubscripcion= subs.id)   ");
				actionConditions.append("left join Concepto  concepto on (concepto.idsubscripcion= subs.id)  ");			
			}else{
				if(getClientFilter().getCurso().getSelectedItem() != null 
						&&((String)getClientFilter().getCurso().getSelectedItem().getValue()).equalsIgnoreCase("Venta de Productos"))
					actionConditions.append("inner join ventaproductos  ventaProds on (caja.idVentaProducto= ventaProds.id)   ");
			}
			
			actionConditions.append("where 1=1  ");
	
			if (getClientFilter().getFechaDesde().getValue() != null && getClientFilter().getFechaHasta().getValue() != null) {
			
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String date1 = format1.format(getClientFilter().getFechaDesde().getValue());    
				String date2 = format1.format(getClientFilter().getFechaHasta().getValue()); 
				
				actionConditions.append(" and to_char(caja.fecha,'YYYY-MM-DD') <='"+ date2+"' ");
				actionConditions.append(" and to_char(caja.fecha,'YYYY-MM-DD') >='"+ date1+"' ");
			}

			if (getClientFilter().getTipoMovimiento().getSelectedIndex() >= 0  ) {
				TipoMovimientoCajaEnum stateType= ((TipoMovimientoCajaEnum)getClientFilter().getTipoMovimiento().getSelectedItem().getValue());
				if(stateType.toInt() != TipoMovimientoCajaEnum.TODOS.toInt())
					actionConditions.append(" and caja.tipoMovimiento= '"+stateType.toInt()+"' ");
			}
			
			if (getClientFilter().getCurso().getSelectedIndex() >= 0) {
				if(getClientFilter().getCurso().getSelectedItem().getValue() instanceof Actividad){
					Actividad stateType= ((Actividad)getClientFilter().getCurso().getSelectedItem().getValue());
					actionConditions.append(" and concepto.idactividad= '"+stateType.getId()+"' ");	
				}else{
					if(((String)getClientFilter().getCurso().getSelectedItem().getValue()).equalsIgnoreCase("Venta de Productos")){
						actionConditions.append(" and caja.idsubscripcion is null and caja.idventaproducto is not null ");	
					}
				}
			}
			
			if (getClientFilter().getUsuarios().getSelectedIndex() >= 0) {
				if(getClientFilter().getUsuarios().getSelectedItem().getValue() instanceof User){
					User stateType= ((User)getClientFilter().getUsuarios().getSelectedItem().getValue());
					actionConditions.append(" and caja.idusuariogeneromovimiento= '"+stateType.getId()+"' ");	
				}
			}
			
			if (getClientFilter().getObtenerMovimientosSoloOS().isChecked()) {
				actionConditions.append(" and (Upper(caja.concepto) like Upper('%A abonar por Obra%')  " +
												"  OR  Upper(caja.concepto) like Upper('%Anulacion de Obra Social%') )");	
			}else{
				actionConditions.append(" and Upper(caja.concepto) not like Upper('%A abonar por Obra%') ");
				actionConditions.append(" and Upper(caja.concepto) not like Upper('%Anulacion de Obra Social%') ");
			}
			
			if (getClientFilter().getPromocionBanco().getSelectedIndex() >= 0) {
				if(getClientFilter().getPromocionBanco().getSelectedItem().getValue() instanceof BancoPromocion){
					
					BancoPromocion stateType= ((BancoPromocion)getClientFilter().getPromocionBanco().getSelectedItem().getValue());
					
					actionConditions.append(" and caja.idsubscripcion is not null " +
							" AND  caja.idsubscripcion in ( Select s.id from subscripcion s " +
																" inner join pagosporsubscripcion p on (s.id= p.idsubscripcion)	" +
																" where 1=1 AND p.idbancopromocion = '"+stateType.getId()+"'  ) ");
				}
			}
			
			if(getClientFilter().getSucursal().getSelectedItem() != null){
				if(getClientFilter().getSucursal().getSelectedItem().getValue() instanceof SucursalEnum ){
					SucursalEnum suc=((SucursalEnum)(getClientFilter().getSucursal().getSelectedItem().getValue()));
					actionConditions.append(" and caja.sucursal= "+ suc.toInt());
				}
//				else if(getClientFilter().getSucursal().getSelectedItem().getValue() instanceof String ){
//					String suc=((String)(getClientFilter().getSucursal().getSelectedItem().getValue()));
//					actionConditions.append(" and caja.sucursal= "+ ((SucursalEnum)getClientFilter().getSucursal().getSelectedItem().getValue()).toInt());
//				}
			}
			
			actionConditions.append(" order by caja.fecha desc  ");

			return actionConditions.toString();
	}
	
	private List<CajaMovimiento>  getListaFiltrada (List <CajaMovimiento>cajaMov){
		List<CajaMovimiento> caja= new ArrayList<CajaMovimiento>();
		
		if(cajaMov != null){
			for (CajaMovimiento cajaMovimiento : cajaMov) {
				if (!existInList(caja, cajaMovimiento.getId()))
					caja.add(cajaMovimiento);
			}
		}
		return caja;
	}
	
	private boolean existInList(List <CajaMovimiento>cajaMov, Long idCaja){
		if(cajaMov != null){
			for (CajaMovimiento cajaMovimiento : cajaMov) {
				if(cajaMovimiento.getId().intValue() == idCaja.intValue())
					return true;
			}
		}
		return false;	
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
		pharmacyList= new ArrayList<CajaMovimiento>();
		
		SucursalEnum suc= null;
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					suc= SucursalEnum.CENTRO;
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					suc= SucursalEnum.MAIPU;
				}
			}
		}
		
		CierreCaja cierre=cajaEJB.getUltimoCierre(suc);
		
		String user= "admin";
		if(cierre.getId() != null){
			cierre= cajaEJB.findCierreCajaById(cierre.getId());	
			user= cierre.getNombreUsuarioGeneroMovimiento();
		}
		
		if(getClientFilter().validateHaveFilters() || getClientFilter().validateHaveFilters()){
			// hago la consulta jdbc con los filtros, devuelvo IDs, y despues hago la busqueda por ids con hibernate
			pharmacyList =getListaFiltrada(cajaEJB.findAllConJdbc(getFilters()));
			
		}
		
		// si existe un cierre de caja el mismo dia para ese usuario
		
		
		Date fechaYHoraDesde= null;
//		if(cierre != null && cierre.getIdUsuarioGeneroMovimiento().intValue() == Session.getUsernameID().intValue() ){
			// tengo que sumar solamente los que son mayor a la fecha y hora 
			fechaYHoraDesde= cierre.getFecha();
//		}

		clientepanelListGrid.setList(pharmacyList, fechaYHoraDesde);

		Actividad act= null;
		User usuarioSelected= null;
		TipoMovimientoCajaEnum tipo= null;
		boolean tieneSeteadaActividad= false;
		Long idPromocionBanco=null;
		
		if(getClientFilter().getCurso().getSelectedItem() != null 
				&& getClientFilter().getCurso().getSelectedItem().getValue() instanceof Actividad )
			act= (Actividad) getClientFilter().getCurso().getSelectedItem().getValue();
		else{
			if(getClientFilter().getCurso().getSelectedItem() != null && 
					((String)getClientFilter().getCurso().getSelectedItem().getValue()).equalsIgnoreCase("Venta de Productos"))
				tieneSeteadaActividad= true;
		} 
		
		if(getClientFilter().getUsuarios().getSelectedItem() != null && getClientFilter().getUsuarios().getSelectedItem().getValue() instanceof User )
			usuarioSelected= (User) getClientFilter().getUsuarios().getSelectedItem().getValue();
		
		
		if(getClientFilter().getTipoMovimiento().getSelectedItem() != null &&
				((TipoMovimientoCajaEnum)getClientFilter().getTipoMovimiento().getSelectedItem().getValue()).toInt() != TipoMovimientoCajaEnum.TODOS.toInt())
			tipo= (TipoMovimientoCajaEnum)getClientFilter().getTipoMovimiento().getSelectedItem().getValue();
		 
		
		if(getClientFilter().getPromocionBanco().getSelectedItem() != null 
				&& getClientFilter().getPromocionBanco().getSelectedItem().getValue() instanceof BancoPromocion ){
			idPromocionBanco= ((BancoPromocion) getClientFilter().getPromocionBanco().getSelectedItem().getValue()).getId();
		}
		

		SumasTotalesMovimientos sumasTotales= 
				cajaEJB.findAllConJdbc(getClientFilter().getFechaDesde().getValue(), getClientFilter().getFechaHasta().getValue(), act, 
						tieneSeteadaActividad, tipo, usuarioSelected, getClientFilter().getObtenerMovimientosSoloOS().isChecked(), idPromocionBanco, 
						Session.getUsernameID().intValue(), suc);
				
		Calendar calHoy = Calendar.getInstance();
		calHoy.setTime(new Date());
		
		Calendar calFDesde = Calendar.getInstance();
		calFDesde.setTime(getClientFilter().getFechaDesde().getValue());

		Calendar calFHasta = Calendar.getInstance();
		calFHasta.setTime(getClientFilter().getFechaHasta().getValue());

		double sumatot=0;
		
		if( (calHoy.get(Calendar.YEAR) ==  calFDesde.get(Calendar.YEAR)) && (calHoy.get(Calendar.MONTH) ==  calFDesde.get(Calendar.MONTH)) 
				&& (calHoy.get(Calendar.DAY_OF_YEAR) ==  calFDesde.get(Calendar.DAY_OF_YEAR))
			&&  (calHoy.get(Calendar.YEAR) ==  calFHasta.get(Calendar.YEAR)) && (calHoy.get(Calendar.MONTH) ==  calFHasta.get(Calendar.MONTH)) 
			&& (calHoy.get(Calendar.DAY_OF_YEAR) ==  calFHasta.get(Calendar.DAY_OF_YEAR))){
			
			SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String date1 = format1.format(cierre.getFecha());    
		
			// se le pone el valor seteado
			getClientFilter().getUltimoEstadoCajaInicial().setValue("Sucursal: "+ suc.toString() +
					" $"+formateador.format(cierre.getValor())+" (Caja cerrada por: "+ user+ " en fecha: "+date1 +")");

			if(sumasTotales.getEfectivo() != null){
				sumasTotales.setEfectivo(sumasTotales.getEfectivo() + cierre.getValor());
			}else{
				sumasTotales.setEfectivo(new Double(cierre.getValor()));
			}		
//			sumatot= sumatot+ cierre.getValor();
		}else{
			
			getClientFilter().getUltimoEstadoCajaInicial().setValue("Sucursal: "+ suc.toString() +" $"+formateador.format(0));
	
		}
		
		getCajaTotales2CrudCrud().agregarCampos(sumasTotales.getListaObrasSociales());
		
		// actualizar valores 
		if(sumasTotales != null){
			if(sumasTotales.getDebito() != null){
				getCajaTotalesCrudCrud().getDebito().setValue("$"+formateador.format(sumasTotales.getDebitoInt()));
				if(sumasTotales.getDebitoInt() instanceof Integer){
					sumatot=sumatot+ (Integer)sumasTotales.getDebitoInt();	
				}else{
					sumatot=sumatot+(Double)sumasTotales.getDebitoInt();
				}
			}
			if(sumasTotales.getTarjeta() != null){
				getCajaTotalesCrudCrud().getTarjeta().setValue("$"+formateador.format(sumasTotales.getTarjetaInt()));
				if(sumasTotales.getTarjetaInt() instanceof Integer){
					sumatot=sumatot+ (Integer)sumasTotales.getTarjetaInt();	
				}else{
					sumatot=sumatot+(Double)sumasTotales.getTarjetaInt();
				}
			}
			
			if(sumasTotales.getEfectivo() != null){
				if(sumasTotales.getEfectivoInt() instanceof Integer){
					sumatot=sumatot+ (Integer)sumasTotales.getEfectivoInt();	
				}else{
					sumatot=sumatot+(Double)sumasTotales.getEfectivoInt();
				}
				getCajaTotalesCrudCrud().getEfectivo().setValue("$"+formateador.format(sumasTotales.getEfectivoInt()));

			}
			
			Double doo=Math.rint(sumatot*100)/100;
			String dada=String.valueOf(doo);
			
			if(dada != null && dada.endsWith(",0")){
				String sas=dada.substring(0, dada.indexOf(","));
				int  val = Integer.parseInt(sas);
				getCajaTotalesCrudCrud().getTotales().setValue("$"+formateador.format(val));// (("$ "+sas);		
			}else{
				getCajaTotalesCrudCrud().getTotales().setValue("$"+formateador.format(doo));
			}
				
		}else{
			getCajaTotalesCrudCrud().getDebito().setValue("$"+formateador.format(0));
			getCajaTotalesCrudCrud().getTarjeta().setValue("$"+formateador.format(0));
			getCajaTotalesCrudCrud().getEfectivo().setValue("$"+formateador.format(0));
			getCajaTotales2CrudCrud().clear();
			getCajaTotalesCrudCrud().getTotales().setValue("$"+formateador.format(0));	
		}
	}
		
	Comparator<CajaMovimiento> comparator = new Comparator<CajaMovimiento>() {
	    public int compare( CajaMovimiento a, CajaMovimiento b ) {
	    	   return b.getFecha().compareTo(a.getFecha());
	    }
	};
	
	public void onClearFilter(Event evt){
		
		getClientFilter().clear();

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		getClientFilter().getFechaDesde().setValue(cal.getTime());
		getClientFilter().getFechaHasta().setValue(cal.getTime());
		getClientFilter().getCurso().setSelectedIndex(0);
		getClientFilter().getTipoMovimiento().setSelectedIndex(0);
		getClientFilter().getPromocionBanco().setSelectedIndex(0);

		if(!validaPermisos()){
			getClientFilter().getFechaDesde().setDisabled(true);
			getClientFilter().getFechaHasta().setDisabled(true);
		}
		if (isCallFromMenu()){
		}else{
			this.onFind(true);
		}		
	}

	@Override
	public void pongoVisibleCrudObrasSociales(boolean bool) {
		getCajaTotales2CrudCrud().setVisible(bool);
	}
	
	@Override
	public void  onfindFromChangeSucursal(boolean bool){
		onFind(bool);
	}

	
	public void onExportCajaExcel(Event event) throws Exception {
		SucursalEnum suc= null;
		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					suc= SucursalEnum.CENTRO;
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					suc= SucursalEnum.MAIPU;
				}
			}
		}
		CierreCaja cierre=cajaEJB.getUltimoCierre(suc);
		if(cierre.getId() != null){
			cierre= cajaEJB.findCierreCajaById(cierre.getId());	
		}
		
		Date fechaYHoraDesde= null;
		if(cierre != null && cierre.getIdUsuarioGeneroMovimiento().intValue() == Session.getUsernameID().intValue() ){
			// tengo que sumar solamente los que son mayor a la fecha y hora 
			fechaYHoraDesde= cierre.getFecha();
		}
		
		List<CajaMovimiento> clienteExportList =getListaFiltrada(cajaEJB.findAllConJdbc(getFilters()));

		if(clienteExportList == null || (clienteExportList != null && clienteExportList.size() == 0)){
//			MessageBox.validation("No se encontraron resultados a guardar en excel", I18N.getLabel("selector.actionwithoutitem.title"));
			return;
		}else{
		
			Actividad act= null;
			User usuarioSelected= null;
			TipoMovimientoCajaEnum tipo= null;
			boolean tieneSeteadaActividad= false;
			Long idPromocionBanco=null;
			
			if(getClientFilter().getCurso().getSelectedItem() != null 
					&& getClientFilter().getCurso().getSelectedItem().getValue() instanceof Actividad )
				act= (Actividad) getClientFilter().getCurso().getSelectedItem().getValue();
			else{
				if(getClientFilter().getCurso().getSelectedItem() != null && 
						((String)getClientFilter().getCurso().getSelectedItem().getValue()).equalsIgnoreCase("Venta de Productos"))
					tieneSeteadaActividad= true;
			} 
			
			if(getClientFilter().getUsuarios().getSelectedItem() != null && getClientFilter().getUsuarios().getSelectedItem().getValue() instanceof User )
				usuarioSelected= (User) getClientFilter().getUsuarios().getSelectedItem().getValue();
			
			
			if(getClientFilter().getTipoMovimiento().getSelectedItem() != null &&
					((TipoMovimientoCajaEnum)getClientFilter().getTipoMovimiento().getSelectedItem().getValue()).toInt() != TipoMovimientoCajaEnum.TODOS.toInt())
				tipo= (TipoMovimientoCajaEnum)getClientFilter().getTipoMovimiento().getSelectedItem().getValue();
			 
			
			if(getClientFilter().getPromocionBanco().getSelectedItem() != null 
					&& getClientFilter().getPromocionBanco().getSelectedItem().getValue() instanceof BancoPromocion ){
				idPromocionBanco= ((BancoPromocion) getClientFilter().getPromocionBanco().getSelectedItem().getValue()).getId();
			}
			

			SumasTotalesMovimientos sumasTotales= 
					cajaEJB.findAllConJdbc(getClientFilter().getFechaDesde().getValue(), getClientFilter().getFechaHasta().getValue(), act, 
							tieneSeteadaActividad, tipo, usuarioSelected, getClientFilter().getObtenerMovimientosSoloOS().isChecked(), idPromocionBanco, 
							Session.getUsernameID().intValue(), suc);
					
			Calendar calHoy = Calendar.getInstance();
			calHoy.setTime(new Date());
			
			Calendar calFDesde = Calendar.getInstance();
			calFDesde.setTime(getClientFilter().getFechaDesde().getValue());

			Calendar calFHasta = Calendar.getInstance();
			calFHasta.setTime(getClientFilter().getFechaHasta().getValue());

			double sumatot=0;
			
			if( (calHoy.get(Calendar.YEAR) ==  calFDesde.get(Calendar.YEAR)) && (calHoy.get(Calendar.MONTH) ==  calFDesde.get(Calendar.MONTH)) 
					&& (calHoy.get(Calendar.DAY_OF_YEAR) ==  calFDesde.get(Calendar.DAY_OF_YEAR))
				&&  (calHoy.get(Calendar.YEAR) ==  calFHasta.get(Calendar.YEAR)) && (calHoy.get(Calendar.MONTH) ==  calFHasta.get(Calendar.MONTH)) 
				&& (calHoy.get(Calendar.DAY_OF_YEAR) ==  calFHasta.get(Calendar.DAY_OF_YEAR))){

				if(sumasTotales.getEfectivo() != null){
					sumasTotales.setEfectivo(sumasTotales.getEfectivo());
				}else{
					sumasTotales.setEfectivo(new Double(0));
				}		
			}	
			// actualizar valores 
			if(sumasTotales != null){
				if(sumasTotales.getDebito() != null){
					if(sumasTotales.getDebitoInt() instanceof Integer){
						sumatot=sumatot+ (Integer)sumasTotales.getDebitoInt();	
					}else{
						sumatot=sumatot+(Double)sumasTotales.getDebitoInt();
					}
				}
				if(sumasTotales.getTarjeta() != null){
					if(sumasTotales.getTarjetaInt() instanceof Integer){
						sumatot=sumatot+ (Integer)sumasTotales.getTarjetaInt();	
					}else{
						sumatot=sumatot+(Double)sumasTotales.getTarjetaInt();
					}
				}
				
				if(sumasTotales.getEfectivo() != null){
					if(sumasTotales.getEfectivoInt() instanceof Integer){
						sumatot=sumatot+ (Integer)sumasTotales.getEfectivoInt();	
					}else{
						sumatot=sumatot+(Double)sumasTotales.getEfectivoInt();
					}
				}					
			}
			
			String[] sheetNames = {"Caja del dia" }; //, "Clientes-Activ-c_cupos", "Clentes-Activ-Historico"};
		
			Iframe iframeReporte = new Iframe();
			
			iframeReporte.setWidth("0%");
	        iframeReporte.setHeight("0%");
			iframeReporte.setId("reporte");
			
			JRDistributionPlanSummaryXls jasperReport = new JRDistributionPlanSummaryXls();
			ByteArrayOutputStream os = new ByteArrayOutputStream();
						
			Map<Integer, List<String>> mapFields= new HashMap <Integer, List<String>>(); 
			List<String>fieldNames =new ArrayList <String>();
			fieldNames.add("Fecha y Hora");
			fieldNames.add("Resp");
			fieldNames.add("Cliente");
			fieldNames.add("Concepto");
			fieldNames.add("Tipo Movimiento");
			fieldNames.add("Importe abonado");
//			fieldNames.add("Observaciones");

			mapFields.put(1, fieldNames);
							
			jasperReport.setFieldNamesList(mapFields);
			
			Map<Integer, List<RowModel>> map2 = new HashMap <Integer, List<RowModel>>(); 

			map2.put(1, obtenerParaExcel(clienteExportList, fechaYHoraDesde, sumatot, sumasTotales));
		    jasperReport.setDsList(map2);
			
			Calendar ahoraCal= Calendar.getInstance();
			int mes=ahoraCal.get(Calendar.MONTH) + 1;

			String fechaNac=ahoraCal.get(Calendar.DATE)+"-"+mes+"-"+ahoraCal.get(Calendar.YEAR);

			try {
				jasperReport.exportXlsReportRestructuring(os, sheetNames, "Caja del dia_"+fechaNac);
			} catch (JRException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ByteArrayInputStream mediais = new ByteArrayInputStream(os.toByteArray());
							
			AMedia amedia = new AMedia("Caja del dia_"+fechaNac+".xls", "xls", "application/vnd.ms-excel", mediais);

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
	
	private List<com.institucion.controller.RowModel>  obtenerParaExcel (List<CajaMovimiento> clienteExportList, Date fechaYHoraDesde, 
			double sumatot, SumasTotalesMovimientos sumasTotales){
		 List<com.institucion.controller.RowModel>  rowModel= new ArrayList();
		for (CajaMovimiento iterable_element : clienteExportList) {
			
			if(fechaYHoraDesde != null && iterable_element.getFecha().before(fechaYHoraDesde)){
				// no considerar por que es del cierre anterior
				
			}else{
				com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
				
				if(iterable_element.getFecha() != null){
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String date1 = format1.format(iterable_element.getFecha());  
					row.set("Fecha y Hora", date1);
				}else
					row.set("Fecha y Hora", " ");

				if(iterable_element.getIdUsuarioGeneroMovimiento() != null){
					String aa=iterable_element.getNombreUsuarioGeneroMovimiento();
					if(aa.length() >12)
						aa=aa.substring(0, 11);

					row.set("Resp", aa);
				}else
					row.set("Resp", " ");
				
				if(iterable_element.getCliente() != null){
					String aa=iterable_element.getCliente().getApellido().toUpperCase() +" "+ iterable_element.getCliente().getNombre().toUpperCase();
//					if(aa.length() >12)
//						aa=aa.substring(0, 11);
					row.set("Cliente", aa);
				}else
					row.set("Cliente", " ");

				if(iterable_element.getConcepto() != null){
					row.set("Concepto", String.valueOf(iterable_element.getConcepto()));
				}else
					row.set("Concepto", " ");
				
				if(iterable_element.getTipoMovimiento() != null){
					row.set("Tipo Movimiento", String.valueOf(iterable_element.getTipoMovimiento().toString()));
				}else
					row.set("Tipo Movimiento", " ");

				if(iterable_element.getTipoMovimiento() != null){
					String dada=String.valueOf(iterable_element.getValor() );
					if(dada!= null && dada.endsWith(".0")){
						String sas=dada.substring(0, dada.indexOf("."));
						int aaa= Integer.parseInt(sas);
						row.set("Importe abonado", "$"+formateador.format(aaa));
					}else if(dada!= null && dada.endsWith(",0")){
						String sas=dada.substring(0, dada.indexOf(","));
						int aaa= Integer.parseInt(sas);
						row.set("Importe abonado", "$"+formateador.format(aaa));
					}else if(iterable_element.getValor() != null){
						String aaa= String.valueOf(iterable_element.getValor() );
						if(aaa.contains(",")){
							aaa= aaa.replace(",", "");
						}							
						if(aaa.contains(".")){
					        double a= Double.parseDouble(aaa);
					        long nue=Math.round(a);
							row.set("Importe abonado", "$"+formateador.format(nue));
						}else{
							row.set("Importe abonado", "$"+formateador.format(Integer.parseInt(aaa)));

						}
					}else{
						row.set("Importe abonado", "$0");
					}
				}else
					row.set("Importe abonado", " ");
				
//				row.set("Observaciones", "                                                        ");
		
				rowModel.add(row);		
			}
		}
		
		// dejo 2 vacios para agregar el total de efectivo etc
		com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " ");
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
//		row.set("Observaciones", " ");
		rowModel.add(row);		

		com.institucion.controller.RowModel row2= new com.institucion.controller.RowModel();
		row2.set("Fecha y Hora", " ");
		row2.set("Resp", " ");
		row2.set("Cliente", " ");
		row2.set("Concepto", " ");
		row2.set("Tipo Movimiento", " ");
		row2.set("Importe abonado", " ");
//		row2.set("Observaciones", " ");
		rowModel.add(row2);		
		
		// ahora agregar: EFECTIVO, 
		com.institucion.controller.RowModel row3= new com.institucion.controller.RowModel();
		row3.set("Fecha y Hora", " ");
		row3.set("Resp", " ");
		row3.set("Cliente", " EFECTIVO "); 
		row3.set("Concepto", "$"+formateador.format(sumasTotales.getEfectivoInt()));
		row3.set("Tipo Movimiento", " ");
		row3.set("Importe abonado", " ");
//		row3.set("Observaciones", " ");
		rowModel.add(row3);		
		
		com.institucion.controller.RowModel row4= new com.institucion.controller.RowModel();
		row4.set("Fecha y Hora", " ");
		row4.set("Resp", " ");
		row4.set("Cliente", " DEBITO "); 
		row4.set("Concepto", "$"+formateador.format(sumasTotales.getDebitoInt()));
		row4.set("Tipo Movimiento", " ");
		row4.set("Importe abonado", " ");
//		row4.set("Observaciones", " ");
		rowModel.add(row4);		

		com.institucion.controller.RowModel row5= new com.institucion.controller.RowModel();
		row5.set("Fecha y Hora", " ");
		row5.set("Resp", " ");
		row5.set("Cliente", " TARJETA ");
		row5.set("Concepto", "$"+formateador.format(sumasTotales.getTarjetaInt()));
		row5.set("Tipo Movimiento", " ");
		row5.set("Importe abonado", " ");
//		row5.set("Observaciones", " ");
		rowModel.add(row5);		

		// SE IMPRIME EL TOTAL
		Double doo=Math.rint(sumatot*100)/100;
		String dada=String.valueOf(doo);
		com.institucion.controller.RowModel row6= new com.institucion.controller.RowModel();
		row6.set("Fecha y Hora", " ");
		row6.set("Resp", " ");
		row6.set("Cliente", " TOTAL -->  ");
		if(dada != null && dada.endsWith(",0")){
			String sas=dada.substring(0, dada.indexOf(","));
			int  val = Integer.parseInt(sas);
			row6.set("Concepto", "$"+formateador.format(val));
		}else{
			row6.set("Concepto", "$"+formateador.format(doo));
		}
		row6.set("Tipo Movimiento", " ");
		row6.set("Importe abonado", " ");
//		row6.set("Observaciones", " ");
		rowModel.add(row6);		

		
		
		// dejo 2 vacios para agregar el total de efectivo etc
		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " ");
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
//		row.set("Observaciones", " ");
		rowModel.add(row);		

		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " ");
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
//		row.set("Observaciones", " ");
		rowModel.add(row);		

		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " ");
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
//		row.set("Observaciones", " ");
		rowModel.add(row);		

		Calendar ahoraCal= Calendar.getInstance();
		ahoraCal.setTime(getClientFilter().getFechaDesde().getValue());
		int mes=ahoraCal.get(Calendar.MONTH) + 1;

		String fechaD=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

		
		Calendar ahoraCal2= Calendar.getInstance();
		ahoraCal2.setTime(getClientFilter().getFechaHasta().getValue());
		int mes2=ahoraCal2.get(Calendar.MONTH) + 1;

		String fechaH=ahoraCal2.get(Calendar.DATE)+"/"+mes2+"/"+ahoraCal2.get(Calendar.YEAR);

		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", "NUEVAS DEUDAS");
		row.set("Resp", "PERIODO:");
		row.set("Cliente", fechaD+ "-"+ fechaH);
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
//		row.set("Observaciones", " ");
		rowModel.add(row);
		
		String filter=getFiltersAdeudados();
		List<ClienteView> list =clienteEJB.findAllConJdbcSinHibernate(filter);
		
		if(list != null){
			List<ClienteView> listNueva= null;
			listNueva=eliminarRepetidos(list, listNueva);

			obtenerParaExcel(rowModel, listNueva);
		}
		return rowModel;
	}
	
	private List eliminarRepetidos(List<ClienteView> list, List<ClienteView> listNuevo){
		listNuevo= new ArrayList<ClienteView>();	
		
		Calendar calDesde= Calendar.getInstance();
		calDesde.setTime(getClientFilter().getFechaDesde().getValue());
		
		calDesde.set(Calendar.HOUR, 00);
		calDesde.set(Calendar.HOUR_OF_DAY, 00);
		calDesde.set(Calendar.MINUTE, 00);
		calDesde.set(Calendar.SECOND, 00);
		calDesde.set(Calendar.MILLISECOND, 00);
		
		Calendar calHasta= Calendar.getInstance();
		calHasta.setTime(getClientFilter().getFechaHasta().getValue());
		
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
		
			if ( (cal.getTime().after(calDesde.getTime()) || cal.getTime().equals(calDesde.getTime()) 
			&&	(cal.getTime().before(calHasta.getTime()) || cal.getTime().equals(calHasta.getTime())))){
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
	
	private List<com.institucion.controller.RowModel>  obtenerParaExcel (List<com.institucion.controller.RowModel>  rowModel,
			List<ClienteView> clienteExportList){
		double total= 0;
		com.institucion.controller.RowModel row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", "APELLIDO");
		row.set("Resp", "NOMBRE");
		row.set("Cliente", "RESPONSABLE");
		row.set("Concepto", "TELEFONO");
		row.set("Tipo Movimiento", "F.VENTA");
		row.set("Importe abonado", "DEUDA");
		rowModel.add(row);			

		
		for (ClienteView iterable_element : clienteExportList) {
			row= new com.institucion.controller.RowModel();
			
			row.set("Fecha y Hora", iterable_element.getApellido());
			row.set("Resp", iterable_element.getNombre());
			

			if( iterable_element.getResponsable() != null)
				row.set("Cliente", iterable_element.getResponsable());
			else
				row.set("Cliente", " ");

			
			String telefonos=null;
			if(iterable_element.getTelefonoFijo() != null)
				telefonos=iterable_element.getTelefonoFijo();

			if(iterable_element.getCelular() != null){
				if(telefonos != null)
					telefonos= telefonos + "/"+iterable_element.getCelular();
				else
					telefonos=iterable_element.getCelular();
			}
			if(telefonos == null){
				row.set("Concepto", " ");	
			}else{
				row.set("Concepto", telefonos);
			}	
			
			if( iterable_element.getFechaVenta() != null){
				Calendar ahoraCal= Calendar.getInstance();
				ahoraCal.setTime(iterable_element.getFechaVenta());
				int mes=ahoraCal.get(Calendar.MONTH) + 1;

				String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
				
				row.set("Tipo Movimiento", fechaNac);
			}else{
				row.set("Tipo Movimiento",  " ");
			}
			
			float cant=getDeuda(inscripcionEJB.findAllSubscripcionesByClient(iterable_element.getId()));
			row.set("Importe abonado", " $"+formateador.format(cant ) );

			total= total+ cant;

			rowModel.add(row);			
		}
		
		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " ");
		row.set("Concepto", " ");
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
		rowModel.add(row);			
		

		row= new com.institucion.controller.RowModel();
		row.set("Fecha y Hora", " ");
		row.set("Resp", " ");
		row.set("Cliente", " DEUDA TOTAL:  ");
		row.set("Concepto", String.valueOf(total));
		row.set("Tipo Movimiento", " ");
		row.set("Importe abonado", " ");
		
		rowModel.add(row);			
		return rowModel;
	}
	
	private int getDeuda(List<Subscripcion> listSubscripcion){
		int cantDeuda= 0;
		
		Calendar calDesde= Calendar.getInstance();
		calDesde.setTime(getClientFilter().getFechaDesde().getValue());
		
		calDesde.set(Calendar.HOUR, 00);
		calDesde.set(Calendar.HOUR_OF_DAY, 00);
		calDesde.set(Calendar.MINUTE, 00);
		calDesde.set(Calendar.SECOND, 00);
		calDesde.set(Calendar.MILLISECOND, 00);
		
		Calendar calHasta= Calendar.getInstance();
		calHasta.setTime(getClientFilter().getFechaHasta().getValue());
		
		calHasta.set(Calendar.HOUR, 00);
		calHasta.set(Calendar.HOUR_OF_DAY, 00);
		calHasta.set(Calendar.MINUTE, 00);
		calHasta.set(Calendar.SECOND, 00);
		calHasta.set(Calendar.MILLISECOND, 00);
		
		if(listSubscripcion != null){
			for (Subscripcion subs : listSubscripcion) {
				
				subs=clienteEJB.loadLazy(subs, true, true, false, false, false);
				
				Calendar cal= Calendar.getInstance();
				cal.setTime(subs.getFechaYHoraCreacion());
				
				cal.set(Calendar.HOUR, 00);
				cal.set(Calendar.HOUR_OF_DAY, 00);
				cal.set(Calendar.MINUTE, 00);
				cal.set(Calendar.SECOND, 00);
				cal.set(Calendar.MILLISECOND, 00);
					
				if ( (cal.getTime().after(calDesde.getTime()) || cal.getTime().equals(calDesde.getTime()) 
						&&	(cal.getTime().before(calHasta.getTime()) || cal.getTime().equals(calHasta.getTime())))){
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
	
	public String getFiltersAdeudados(){
		
		StringBuilder actionConditions= new StringBuilder("select distinct clientexx.id, clientexx.apellido, clientexx.nombre, clientexx.fechanacimiento, " +
				"clientexx.celular, clientexx.telefonofijo, s111.fechayhoracreacion,s111.idusuariocreosubscripcion , " +
				" clientexx.dni, clientexx.fechanacimiento, clientexx.domicilio, clientexx.facebook, clientexx.mail   from cliente clientexx ");
		
		actionConditions.append(" inner join subscripcion s111 on (clientexx.id= s111.idcliente) ");
		actionConditions.append(" inner join cupoactividad ca11 on (ca11.idsubscripcion= s111.id) ");
		actionConditions.append(" where ca11.estado in (0, 1, 10)    ");
		
		if(getClientFilter().getUsuarios() != null && getClientFilter().getUsuarios().getSelectedItem() != null 
					&& !((getClientFilter().getUsuarios().getSelectedItem().getValue()) instanceof String)){
				com.institucion.fm.security.model.User u= ((com.institucion.fm.security.model.User)getClientFilter().getUsuarios().getSelectedItem().getValue());
				actionConditions.append(" and  s111.idusuariocreosubscripcion=" +u.getId());
		}		
		
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

		if (getClientFilter().getFechaDesde().getValue() != null && getClientFilter().getFechaHasta().getValue() != null) {
			String date1 = format1.format(getClientFilter().getFechaDesde().getValue());    
			String date2 = format1.format(getClientFilter().getFechaHasta().getValue()); 

			actionConditions.append(" and  clientexx.id in ( " +
					"	select subs.idcliente from subscripcion subs  " +
					" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+date1 +"'  and" +
							"   to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+date2+"' )");

			
		}else 	if (getClientFilter().getFechaDesde().getValue() != null) {
			String date1 = format1.format(getClientFilter().getFechaDesde().getValue());    
			
			actionConditions.append(" and  clientexx.id in ( " +
					"	select subs.idcliente from subscripcion subs  " +
					" where  to_char(fechayhoracreacion,'YYYY-MM-DD') >='"+date1 +"'  )");
			
		}else 	if (getClientFilter().getFechaHasta().getValue() != null) {
			String date2 = format1.format(getClientFilter().getFechaHasta().getValue());    
			
			actionConditions.append(" and  clientexx.id in ( " +
					"	select subs.idcliente from subscripcion subs  " +
					" where  to_char(fechayhoracreacion,'YYYY-MM-DD')<= '"+date2+"' )");

		}

		actionConditions.append(" order by s111.fechayhoracreacion desc ");
		return actionConditions.toString();
	}
		
}

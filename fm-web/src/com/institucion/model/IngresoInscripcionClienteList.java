package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class IngresoInscripcionClienteList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private CursosDelegate delegate;
	private ClienteDelegate actionComposerDelegate;	
	private ClienteEJB clienteEJB;
	
	public IngresoInscripcionClienteList() {
		super(false);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

		super.addHeader( setHeaders(I18N.getLabel("client.apellido"),"apellido") );
		super.addHeader(setHeaders(I18N.getLabel("client.nombre"),"nombre"));
		super.addHeader(setHeaders(I18N.getLabel("client.dni"),"dni")).setWidth("8%");;
		super.addHeader(setHeaders(I18N.getLabel("client.domicilio"),"domicilio")).setWidth("20%");;
		super.addHeader(setHeaders(I18N.getLabel("client.telefono"),"telefonoFijo")).setWidth("10%");
		super.addHeader(setHeaders(I18N.getLabel("client.celular"),"celular")).setWidth("10%");
		super.addHeader(setHeaders(I18N.getLabel("client.edad"),"fechanacimiento")).setWidth("5%");
		super.addHeader(setHeaders(I18N.getLabel("client.estado"),"estado")).setWidth("10%");   // Adeuda
		super.addHeader(setHeaders(I18N.getLabel("client.deudaPesos"),"deuda")).setWidth("7%");		
		this.setMultiple(false);
//		this.setPageSize(6);
	}

	private Listheader setHeaders (String header, String id){
		
		Listheader hl = new Listheader(header);
		hl.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event event) {
				getActionComposerDelegate().sortEvent(event);
			}
		});
		hl.setAttribute("id", id);
		return hl;
	}
	
	public void actualizoClienteLuegoDePasarCodigoDeBarras(){
		
		if(super.getSelectedItem() != null){
			Iterator<Listitem> itCursos =super.getItems().iterator();
			while (itCursos.hasNext()) {
				Listitem cursosDeLaLista=(Listitem)itCursos.next();

				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();

						if(cel.getAttribute("esEstado") != null){
							Cliente cliente=(Cliente)super.getSelectedItem().getValue();
							
							if(cel.getParent().getAttribute("prodID") != null && 
									((String)cel.getParent().getAttribute("prodID")).equalsIgnoreCase(String.valueOf(cliente.getId()))){
								
								boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cliente.getId());
								if(tieneDeudas){
									if(clienteEJB.isTieneDisponibles(cliente.getId())){
										cel.setValue("Adeuda con Cupos");
										cel.setLabel("Adeuda con Cupos");
										cel.setStyle("color:#FF0000 !important; font-weight:bold; ");
									}else{
										cel.setValue("Adeuda sin Cupos");
										cel.setLabel("Adeuda sin Cupos");
										cel.setStyle("color:#FF0000 !important; font-weight:bold; ");
									}
									
								}else if(clienteEJB.isTieneDisponibles(cliente.getId())){
									cel.setValue("Con clases Disp.");
									cel.setLabel("Con clases Disp.");

									cel.setStyle("color:#0B610B !important; font-weight:bold; ");
								}else{
									cel.setValue("Sin clases Disp.");
									cel.setLabel("Sin clases Disp.");

									cel.setStyle("color:black !important; font-weight:normal;");
								}
//								break;
							}					
						}
						if(cel.getAttribute("deudas") != null){
							Cliente cliente=(Cliente)super.getSelectedItem().getValue();
							if(cel.getParent().getAttribute("prodID") != null && 
									((String)cel.getParent().getAttribute("prodID")).equalsIgnoreCase(String.valueOf(cliente.getId()))){
								
								boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cliente.getId());
								if(!tieneDeudas){
									cel.setValue("$0");
									cel.setLabel("$0");
									cel.setStyle("color:black !important; font-weight:normal;");

								}
							
								
							}

						}
					}
				}
			}					
		}
	}
	
	public void setList(List<Cliente> clienteList, Cliente cliSeleccionadoAnteriormente) {
		super.removeAll();
		if(clienteList != null){
			Iterator<Cliente> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				Cliente cliente = itPharmacy.next();
				Listitem row = new Listitem();
				
				row.setAttribute("prodID", String.valueOf(cliente.getId()));

				row.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){
					public void onEvent(Event evt){
						if(((Listitem)evt.getTarget()).getAttribute("prodID") != null){
							String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
							
							actionComposerDelegate.venderNuevaSubs(evt,Integer.parseInt(prodID));
						}
					}
				});
		
				if(cliSeleccionadoAnteriormente == null){		
				}else{
					if(cliente.getId().intValue() == cliSeleccionadoAnteriormente.getId().intValue()){
						row.setSelected(true);	
						row.setFocus(true);
						delegate.muestraLasSubscripciones();
					}
				}
				row.setValue(cliente);
				if(cliente.getApellido() != null && !cliente.getApellido().trim().equalsIgnoreCase(""))
					row.appendChild(new Listcell(cliente.getApellido().toUpperCase()));
				else
					row.appendChild(new Listcell(" "));

				if(cliente.getNombre() != null && !cliente.getNombre().trim().equalsIgnoreCase(""))
					row.appendChild(new Listcell(cliente.getNombre().toUpperCase()));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getDni() != null)
					row.appendChild(new Listcell(cliente.getDni()));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getDomicilio() != null)
					row.appendChild(new Listcell(cliente.getDomicilio()));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getTelefonoFijo() != null)
					row.appendChild(new Listcell(cliente.getTelefonoFijo()));
				else
					row.appendChild(new Listcell(" "));
				
				
				if(cliente.getCelular() != null)
					row.appendChild(new Listcell(cliente.getCelular()));
				else
					row.appendChild(new Listcell(" "));
				
				
				
				if(cliente.getFechaNacimiento() != null){
					row.appendChild(new Listcell(String.valueOf(EdadViewHelper.calcularEdad(cliente.getFechaNacimiento(), new Date()))));		
					
				}else{
					row.appendChild(new Listcell(" "));
				}
				
				Listcell listCell = null;
				
				boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cliente.getId());
				if(tieneDeudas){
					if(clienteEJB.isTieneDisponibles(cliente.getId())){
						listCell=new Listcell("Adeuda con Cupos");
						listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");
						
					}else{
						listCell=new Listcell("Adeuda sin Cupos");
						listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");
					}
					
				}else if(clienteEJB.isTieneDisponibles(cliente.getId())){
					listCell=new Listcell("Con clases Disp.");
					listCell.setStyle("color:#0B610B !important; font-weight:bold; ");
				}else{
					listCell=new Listcell("Sin clases Disp.");
				}
				listCell.setAttribute("esEstado", true);
				row.appendChild(listCell);

				if(tieneDeudas){
					float cant=getDeuda(cliente);
					Listcell lll= new  Listcell("$"+formateador.format(cant ));
					lll.setAttribute("deudas", true);

					lll.setStyle("color:#FF0000 !important; font-weight:bold; ");
					row.appendChild(lll);
				}else{
					row.appendChild(new Listcell("$"+formateador.format(0)));
				}
								
				Component nullComponent = null;

				row.addForward("onClick",nullComponent,"onClickEvt");

				super.addRow(row);
			}
		}

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

	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}	
	
	
}
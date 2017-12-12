package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.desktop.helper.EdadViewHelper;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class ClienteList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClienteDelegate actionComposerDelegate;	
	private ClienteEJB clienteEJB;
	DecimalFormat formateador = new DecimalFormat("###,###");


	public ClienteList() {
		super(false);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

		super.addHeader( setHeaders(I18N.getLabel("client.apellido"),"apellido") );
		super.addHeader(setHeaders(I18N.getLabel("client.nombre"),"nombre"));
		super.addHeader(setHeaders(I18N.getLabel("client.dni"),"dni")).setWidth("15%");;
		super.addHeader(setHeaders(I18N.getLabel("client.fechaNac"),"fechanacimiento")).setWidth("10%");
		super.addHeader(setHeaders(I18N.getLabel("client.edad"),"fechanacimiento")).setWidth("5%");
		super.addHeader(setHeaders(I18N.getLabel("client.estado"),"estado")).setWidth("10%");   // Adeuda
		super.addHeader(setHeaders(I18N.getLabel("client.deudaPesos"),"deuda")).setWidth("10%");		
		super.addHeader(setHeaders(I18N.getLabel("client.inscriptoEnAlgo"),"inscriptoEnAlgo")).setWidth("10%");
		this.setMultiple(false);
		//this.setCheckmark(false);
	}

	/**
	 * Set event listener and ID to the list headers
	 * @param header
	 */
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
	
	public void setList(List<Cliente> clienteList) {
		super.removeAll();
		if(clienteList != null){

			Iterator<Cliente> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				Cliente cliente = itPharmacy.next();
				Listitem row = new Listitem();
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
				
				if(cliente.getFechaNacimiento() != null){
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(cliente.getFechaNacimiento());
					int mes=ahoraCal.get(Calendar.MONTH) + 1;

					String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					row.appendChild(new Listcell(fechaNac));
					row.appendChild(new Listcell(String.valueOf(EdadViewHelper.calcularEdad(cliente.getFechaNacimiento(), new Date()))));		
					
				}else{
					row.appendChild(new Listcell(" "));
					row.appendChild(new Listcell(" "));
				}
			
				Listcell listCell = null;
				boolean tieneDeudas=clienteEJB.isAdeudaAlgo(cliente.getId());
				boolean tieneDisponibles=clienteEJB.isTieneDisponibles(cliente.getId());
				if(tieneDeudas){
					if(clienteEJB.isTieneDisponibles(cliente.getId())){
						listCell=new Listcell("Adeuda con Cupos");
						listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");
					}else{
						listCell=new Listcell("Adeuda sin Cupos");
						listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");
					}
				}else if(tieneDisponibles){
					listCell=new Listcell("Con clases Disp.");
					listCell.setStyle("color:#0B610B !important; font-weight:bold; ");
				}else{
					listCell=new Listcell("Sin clases Disp.");
				}
				row.appendChild(listCell);

				if(tieneDeudas){
					float cant=getDeuda(cliente);
					Listcell lll= new  Listcell("$"+formateador.format(cant) );
					lll.setStyle("color:#FF0000 !important; font-weight:bold; ");
					row.appendChild(lll);
				}else{
					row.appendChild(new Listcell("$"+formateador.format(0)));
				}
				
				if(clienteEJB.isTieneDisponibles(cliente.getId())){
					Listcell lll= new  Listcell("SI");
					lll.setStyle("color:#01DF01 !important; font-weight:bold; ");
					row.appendChild(lll);	
				}else{
					row.appendChild(new Listcell("NO"));
				}

				super.addRow(row);
			}			
		}
	}
	
	private float getDeuda(Cliente cli){
		float cantDeuda= 0;
		cli=clienteEJB.loadLazy(cli, true, true, true, false);
		if(cli.getSubscripcionesList() != null){
			for (Subscripcion subs : cli.getSubscripcionesList()) {
				
				
				if(subs.getCupoActividadList() != null){
					for (CupoActividad cupoAct: subs.getCupoActividadList()) {
						if(cupoAct.getEstado() != null && 
								cupoAct.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
								((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
										||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
									||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
							float pagosRealizados=0;
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
	
//	private boolean tieneInscripcionesVigentes(Cliente cli){
//		
//		// esto tiene dos conceptos:
//				// puede ser que sea por clases o libre
//				// si es por clase, esta bien validar por cupos.
//				// si es libre, tiene cupos.
//		
//		if(cli.getSubscripcionesList() != null){
//			for (Subscripcion iterable_element : cli.getSubscripcionesList()) {
//				
//				
//				if(iterable_element.getCupoActividadList() != null){
//					for (CupoActividad cupoAct: iterable_element.getCupoActividadList()) {
//						if(cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.C_CUPOS.toInt()
//								||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
////								||  cupoAct.getEstado().toInt() == CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt()
//								){
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
//				}
//			}
//		}
//		return false;		
//	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}
}
package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridList;

public class CumplesList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClienteDelegate actionComposerDelegate;	
	DecimalFormat formateador = new DecimalFormat("###,###");
	private ClienteEJB clienteEJB;

	public CumplesList() {
		super();
		super.addHeader(new Listheader("Fecha Cumpleaños")).setWidth("14%");;
		super.addHeader(new Listheader("Cliente")).setWidth("35%");
		super.addHeader(new Listheader("Fecha contratación")).setWidth("14%");
		super.addHeader(new Listheader("Valor cumpleaños")).setWidth("12%");
		super.addHeader(new Listheader("Valor Final cumpleaños ")).setWidth("12%");
		super.addHeader(new Listheader("Estado")).setWidth("14%");
		super.addHeader(new Listheader("Deuda")).setWidth("12%");
		this.setMultiple(false);
		this.setPageSize(8);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}
	
	
	// si esta la clase en claseParaCompararList la pinto en verde
	public void setSelectedItem(Subscripcion selectedClass) {

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
	public void setList(List<Subscripcion> claseList, List<Subscripcion> claseParaCompararList) {
		super.removeAll();
		if(claseList != null){
			Iterator<Subscripcion> itPharmacy = claseList.iterator();
			while (itPharmacy.hasNext()) {
				Subscripcion subs = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(subs);
				
				subs=clienteEJB.loadLazy(subs, true, true, true, false, false);
				
				for (Concepto concep : subs.getConceptoList()) {
					
					if(concep.getCurso() != null && concep.getCurso().getNombre().contains("CUMPLEA")){
						
						if(concep.getFechaCumple() != null){
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(concep.getFechaCumple());
							int mes=ahoraCal.get(Calendar.MONTH) + 1;

							String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

							row.appendChild(new Listcell(fechaNac));
						}else
							row.appendChild(new Listcell(" "));

						String apellidoYNomCliente="";
						if(subs.getCliente().getApellido() != null && !subs.getCliente().getApellido().trim().equalsIgnoreCase(""))
							apellidoYNomCliente=subs.getCliente().getApellido().toUpperCase(); 

						if(subs.getCliente().getNombre() != null && !subs.getCliente().getNombre().trim().equalsIgnoreCase("")){
							if(!apellidoYNomCliente .equalsIgnoreCase("")){
								apellidoYNomCliente=apellidoYNomCliente +", "+ subs.getCliente().getNombre().toUpperCase();	
							}else
								apellidoYNomCliente=subs.getCliente().getNombre().toUpperCase();
						}
						
						if(subs.getCliente().getCelular() != null && !subs.getCliente().getCelular().trim().equalsIgnoreCase("")){
							if(!apellidoYNomCliente .equalsIgnoreCase("")){
								apellidoYNomCliente=apellidoYNomCliente +". Celu:"+ subs.getCliente().getCelular().trim();	
							}else
								apellidoYNomCliente=subs.getCliente().getCelular();
						}
						
						if(subs.getCliente().getTelefonoFijo() != null && !subs.getCliente().getTelefonoFijo().trim().equalsIgnoreCase("")){
							if(!apellidoYNomCliente .equalsIgnoreCase("")){
								apellidoYNomCliente=apellidoYNomCliente +". Tel:"+ subs.getCliente().getTelefonoFijo().trim();	
							}else
								apellidoYNomCliente=subs.getCliente().getTelefonoFijo();
						}
						row.appendChild(new Listcell(apellidoYNomCliente));
						
						
						
						if(subs.getFechaYHoraCreacion() != null){
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(subs.getFechaYHoraCreacion());
							int mes=ahoraCal.get(Calendar.MONTH) + 1;

							String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

							row.appendChild(new Listcell(fechaNac));
						}else
							row.appendChild(new Listcell(" "));

						
		
						Listcell desccell = new Listcell("$"+formateador.format(concep.getImporteOriginal()));
						row.appendChild(desccell);

						if(subs.getPrecioTOTALadicionalTarjeta() != null){
							Listcell desccella = new Listcell("$"+formateador.format(subs.getPrecioTOTALadicionalTarjeta()));
							row.appendChild(desccella);
						}else{
							Listcell desccella = new Listcell("$"+formateador.format(concep.getImporteOriginal()));
							row.appendChild(desccella);
						}

						
						Listcell listCell = null;
						boolean estaAnulado=getEstado(subs);
						float deuda=getDeuda(subs);
						if(estaAnulado){
							listCell=new Listcell("x-ANULADO-x");
							listCell.setStyle("font-weight:bold; ");
							
						}else if ( deuda >0  ){
							listCell=new Listcell("ADEUDA");
							listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");

						}else{
							listCell=new Listcell("ABONADO.-");
							listCell.setStyle("color:#0B610B !important; font-weight:bold; ");

						}
						row.appendChild(listCell);
						
						

						if(deuda >0 && !estaAnulado){
							Listcell lll= new  Listcell("$"+formateador.format(deuda) );
							lll.setStyle("color:#FF0000 !important; font-weight:bold; ");
							row.appendChild(lll);
						}else{
							row.appendChild(new Listcell("$"+formateador.format(0)));
						}
					
						super.addRow(row);

					}	
					
					
				}
			}
		}
	}	
	
	private boolean getEstado(Subscripcion subs){
			
			if(subs.getCupoActividadList() != null){
				for (CupoActividad cupoAct: subs.getCupoActividadList()) {
					if(cupoAct.getCurso() != null && cupoAct.getCurso().getNombre().contains("CUMPLEA")){
						
						if(cupoAct.getEstado() != null && cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ANULADA.toInt()  )
							return true;
						}
				   }
			}
			return false;
	}
	
	private float getDeuda(Subscripcion subs){
		float cantDeuda= 0;
			
			if(subs.getCupoActividadList() != null){
				for (CupoActividad cupoAct: subs.getCupoActividadList()) {
					if(cupoAct.getCurso() != null && 
							cupoAct.getCurso().getNombre().contains("CUMPLEA")){
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
		return cantDeuda;		
	}
	

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}
}
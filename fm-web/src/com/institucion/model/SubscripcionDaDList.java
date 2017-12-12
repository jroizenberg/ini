package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadList;

public class SubscripcionDaDList  extends DadList<Subscripcion> {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private ClienteEJB clienteEJB;

	
	public ClienteEJB getClienteEJB(){
	  clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");
	  return clienteEJB;
	}
	protected void addHeaders() {
		this.addHeader(new Listheader(I18N.getLabel("crud.group.assigned.title")));
	}

	protected void addRow(Subscripcion item) {
		Listitem row = new Listitem();
		item=getClienteEJB().loadLazy(item, true, true, true, false, false);
		String nombre=getNombreCursosContratados(item);
		String text= null;
		if(nombre == null)
			text = I18N.getStringLabel("Inscripción");
		else
			text = I18N.getStringLabel(nombre);
		
		row.setValue(item);
		Listcell nameCell = new Listcell(text);
		row.appendChild(nameCell);
		this.addRow(row);
	}

	private String getNombreCursosContratados(Subscripcion subs){
		boolean tieneMatricula=false;
		
		String nombresDeCursos= null;
		if(	subs.getConceptoList() != null){
			for (Concepto conce : subs.getConceptoList()) {
				if(conce.getCurso() ==  null)
					tieneMatricula= true;
				else{
					
					if(nombresDeCursos == null)
						nombresDeCursos = conce.getCurso().getNombre().toUpperCase();
					else
						nombresDeCursos = nombresDeCursos +", "+conce.getCurso().getNombre().toUpperCase();
				}
			}
		}
		String descDeuda=null;
		String fecha= null;
		if(subs.getFechaIngresoSubscripcion() != null){
			Calendar ahoraCal= Calendar.getInstance();
			ahoraCal.setTime(subs.getFechaIngresoSubscripcion() );
			int mes=ahoraCal.get(Calendar.MONTH)+ 1;
			fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
		}
			
		if(tieneMatricula){
			descDeuda="Deuda: $"+formateador.format(obtenerDeuda(subs))+ ". Inscrip curso + Matrícula ";
		}else{
			descDeuda="Deuda: $"+formateador.format(obtenerDeuda(subs))+ ". Inscrip curso ";
		}
		if(fecha != null){
			descDeuda= descDeuda + " "+fecha;
		}
//		descDeuda= descDeuda + ". "+ subs.getEstado();
		
		if(nombresDeCursos != null){
			descDeuda= descDeuda+ ". Cursos:"+nombresDeCursos;
		}
		
		return descDeuda;
	}
	
	
	private int obtenerDeuda(Subscripcion subs){
		int fantantePago=0;
		
		subs=getClienteEJB().loadLazy(subs, true, true, false, false, false);

		if(subs.getCupoActividadList() != null ){
			for (CupoActividad cupos : subs.getCupoActividadList()) {
				if(cupos.getEstado() != null &&  cupos.getEstado().toInt() !=  CupoActividadEstadoEnum.ANULADA.toInt() &&
						((cupos.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
							|| (cupos.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
							||(cupos.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt()))){
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
						fantantePago =subs.getPrecioTOTALadicionalTarjeta() - pagosRealizados;
					}
					break;
				}				
			}
		}
		return fantantePago;
	}
	
	protected boolean areEquals(Subscripcion item1, Subscripcion item2) {
		if (item1 == item2 && item1 == null) {
			return true;
		} else if (item1 == null) {
			return false;
		}
		if (item1.getId().equals(item2.getId())) {
			return true;
		}
		return false;
	}

}

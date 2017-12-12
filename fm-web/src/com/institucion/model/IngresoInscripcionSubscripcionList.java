package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.bz.InscripcionEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class IngresoInscripcionSubscripcionList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");
	private ClienteEJB clienteEJB;
	private InscripcionEJB inscripcionEJB;

	public IngresoInscripcionSubscripcionList() {
		super();
		super.addHeader(new Listheader("F. Pago Inscrip")).setWidth("8%");
		super.addHeader(new Listheader(I18N.getLabel("curso.nombre"))).setWidth("24%");
		super.addHeader(new Listheader(I18N.getLabel("curso.vencimiento"))).setWidth("12%");
		super.addHeader(new Listheader("Cupos Disp")).setWidth("6%");;
		super.addHeader(new Listheader("En Clase/Sesion")).setWidth("5%");;
		super.addHeader(new Listheader(I18N.getLabel("client.estado"))).setWidth("11%");
		super.addHeader(new Listheader(I18N.getLabel("client.deudaPesos"))).setWidth("5%");
		super.addHeader(new Listheader("Comentario")).setWidth("16%");

		this.setMultiple(false);
		this.setPageSize(16);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	
		inscripcionEJB = BeanFactory.<InscripcionEJB>getObject("fmEjbInscripcion");

	}
	
	private boolean elCursoDelClienteEsQueElClienteEstaEnEsaClase(Subscripcion subs, Curso curso, List<SubsYCurso> claseParaCompararList){
		
		if(claseParaCompararList != null){
			
			for (SubsYCurso clase2 : claseParaCompararList) {
				if(clase2.getIdCurso() != null && clase2.getIdSubscripcion() != null && curso != null && curso.getId() != null
						 &&  subs.getId().intValue() ==  clase2.getIdSubscripcion().intValue()
						 &&  curso.getId().intValue() ==  clase2.getIdCurso().intValue()){
					return true;
				} 
			}
		}
		return false;
	}

	
	private  String setearColor(int posicion){		
		if(esPar(posicion)){
			return "background-color: #E6E6E6;";  
		}else{
			return "background-color: #FAFAFA;";  
		}
	} 
	
	public boolean esPar(int x) {
		if ((x % 2) == 0) {
			return true;
		}
		return false;
	}
	
	public void actualizoSubscripcionLuegoDePasarCodigoDeBarras(){

		if(super.getSelectedItem() != null){
			Subscripcion subsDeMemoria=(Subscripcion)super.getSelectedItem().getValue();
			Concepto conceptoDeMemoria=(Concepto)super.getSelectedItem().getAttribute("concepto");
			String idSeleccionado=super.getSelectedItem().getId();
			
			// obtengo la misma subscripcion de la base de datos
			if(subsDeMemoria != null && subsDeMemoria.getId() != null){
				Subscripcion subsDeLaBD=inscripcionEJB.findSubscripcionById(subsDeMemoria.getId());
				subsDeLaBD= clienteEJB.loadLazy(subsDeLaBD, true, false, false, false, false);

				Iterator<Listitem> itCursos =super.getItems().iterator();
				while (itCursos.hasNext()) {
					Listitem cursosDeLaLista=(Listitem)itCursos.next();
//					cursosDeLaLista.setValue(subsDeLaBD);
					if(cursosDeLaLista != null && ((Subscripcion)cursosDeLaLista.getValue()).getId().intValue() == subsDeLaBD.getId().intValue()
							&&    cursosDeLaLista.getId().equalsIgnoreCase(idSeleccionado)){
						cursosDeLaLista.setValue(subsDeLaBD);

						if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
							Iterator iteee=cursosDeLaLista.getChildren().iterator();
					
							Curso curso=conceptoDeMemoria.getCurso();							
							boolean hayQuePintarClase=true;
							CupoActividad cupoAct=getActividad(subsDeLaBD, curso);

							while(iteee.hasNext()){
								Listcell cel=	(Listcell)iteee.next();
								
								if(cel.getAttribute("vencimiento") != null){
									if(curso != null && curso.getNombre().contains("CUMPLEA")){
										cel.setValue(" ");
										cel.setLabel(" ");
										// si tiene seteado fecha de inicio y fin pongo eso, sino el texto
									}else if(cupoAct.getFechaComienzo() != null && cupoAct.getFechaFin() != null){
										if((cupoAct.getEstado() != null && cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ANULADA.toInt())){
											cel.setValue(" ");	
											cel.setLabel(" ");

										}else{
											String fechaIniFinVenc= null;
											if(cupoAct.getFechaComienzo() != null){
												Calendar ahoraCal= Calendar.getInstance();
												ahoraCal.setTime(cupoAct.getFechaComienzo() );
												int mes=ahoraCal.get(Calendar.MONTH)+ 1;
												String fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
												
												fechaIniFinVenc=String.valueOf(fecha);
											}else{
											}
											if(cupoAct.getFechaFin() != null){
												Calendar ahoraCal= Calendar.getInstance();
												ahoraCal.setTime(cupoAct.getFechaFin() );
												int mes=ahoraCal.get(Calendar.MONTH)+ 1;
												String fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
												if(fechaIniFinVenc != null)
													fechaIniFinVenc= fechaIniFinVenc+" - "+ String.valueOf(fecha);

											}else{
											}											
											cel.setValue(fechaIniFinVenc);	
											cel.setLabel(fechaIniFinVenc);							

										}
										
									}else{
										cel.setValue(String.valueOf(VencimientoCursoEnum.toStringMes(curso.getVencimiento().toInt())));		
										cel.setLabel(String.valueOf(VencimientoCursoEnum.toStringMes(curso.getVencimiento().toInt())));
									}								
								}	
								
								
								if(cel.getAttribute("cupos") != null){
									int cantClases=cupoAct.getCupos();
									if(curso.getNombre().contains("CUMPLEA")){
										cel.setValue(String.valueOf(" "));	
										cel.setLabel(String.valueOf(" "));						
									}else if(cantClases == 99 ){
										cel.setValue(String.valueOf("LIBRE"));	
										cel.setLabel(String.valueOf("LIBRE"));						
										cel.setStyle("color:#0B610B !important; font-weight:bold; ");		
									}else{
										cel.setValue(String.valueOf(cantClases));
										cel.setLabel(String.valueOf(cantClases));					

										if(cantClases >0)
											cel.setStyle("color:#0B610B !important; font-weight:bold; ");
									}
								}
							
								if(cel.getAttribute("enclase") != null){
									if(hayQuePintarClase){
										cel.setValue("Si");
										cel.setLabel("Si");					

										cel.setStyle("color:#FFB24C !important; font-weight:bold; font-size: 8pt; ");
									}else{
										cel.setValue("No");
										cel.setLabel("No");	
										cel.setStyle("color:black !important; font-weight:normal; font-size: 8pt; ");
									}
								}

							
								if(cel.getAttribute("estado") != null){
									if(cupoAct.getEstado() != null ){
										
										cel.setValue(CupoActividadEstadoEnum.toString(cupoAct.getEstado().toInt()));
										cel.setLabel(CupoActividadEstadoEnum.toString(cupoAct.getEstado().toInt()));
										
										if((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt())){
										
											cel.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
										}else if((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA.toInt())) 
											cel.setStyle(" color:#FF6347 !important; font-weight:bold;  ");
										
									}else{
										cel.setValue(" ");
										cel.setLabel(" ");
									}
								}
								
								if(cel.getAttribute("pesos") != null){
				
									if((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ANULADA.toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_CUPOS .toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.S_CUPOS.toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA.toInt())
											||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_CUPOS .toInt())){
										cel.setValue("$0");
										cel.setLabel("$0");
										cel.setStyle("color:black !important; font-weight:normal; font-size: 8pt; ");
									}
									
								}
							}
						}
						
					}
				}		
			}
		}
	}
	
	public void setList(List<Subscripcion> pharmacyList, Subscripcion selected, ActividadYClase actYClaseSelected, List<SubsYCurso> cursoParaCompararList) {
		super.removeAll();
		this.setMultiple(false);
		
		if(pharmacyList != null){
			Iterator<Subscripcion> itPharmacy = pharmacyList.iterator();
			int i=0;				
			
			while (itPharmacy.hasNext()) {
				Subscripcion subs = itPharmacy.next();
				
				subs=clienteEJB.loadLazy(subs, true, true, true, false, false);
				int numeroDelCupoActividad=0;
				
				if(subs.getConceptoList() != null && subs.getConceptoList().size() > 0){
					i++;
				}
				for (Concepto concepto : subs.getConceptoList()) {
					Curso curso=concepto.getCurso();
					CupoActividad cupoAct = null;
					
					boolean hayQuePintarClase=elCursoDelClienteEsQueElClienteEstaEnEsaClase(subs, curso, cursoParaCompararList);
					
					if(curso != null && concepto.getConcepto() != null && !concepto.getConcepto().contains("Copago")){
						ActividadYClase actYClase = (ActividadYClase)new ArrayList(curso.getActividadYClaseList()).get(0);
								
						
						cupoAct=getActividad(subs, curso);
						numeroDelCupoActividad++;
						Listitem row = new Listitem();
						row.setStyle(setearColor(i));
						row.setCheckable(true);	
						
						if(selected != null && selected.getId().equals(subs.getId()) && 
								actYClaseSelected != null && actYClase.getId().equals(actYClaseSelected.getId()))
							row.setSelected(true);
						
						row.setValue(subs);
						row.setAttribute("concepto", concepto);
						
						Listcell desccell33= null;
						if(subs.getFechaIngresoSubscripcion() != null){
							Calendar ahoraCal= Calendar.getInstance();
							ahoraCal.setTime(subs.getFechaIngresoSubscripcion() );
							int mes=ahoraCal.get(Calendar.MONTH)+ 1;
							String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
							desccell33 = new Listcell(fechaNac);
						}else
							desccell33 = new Listcell(" ");

						row.appendChild(desccell33);
						
						Listcell nombrecell = new Listcell(curso.getNombre().toUpperCase() +"- "+actYClase.getActiv().getNombre());
						row.appendChild(nombrecell);

						if(curso != null && curso.getNombre().contains("CUMPLEA")){
							Listcell pagasubscell = new Listcell(" ");
							pagasubscell.setAttribute("vencimiento", true);
							row.appendChild(pagasubscell);
							
							// si tiene seteado fecha de inicio y fin pongo eso, sino el texto
						}else if(cupoAct.getFechaComienzo() != null && cupoAct.getFechaFin() != null){
							if((cupoAct.getEstado() != null && cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ANULADA.toInt())){
								Listcell pagasubscell = new Listcell(" ");
								pagasubscell.setAttribute("vencimiento", true);
								row.appendChild(pagasubscell);
							
								
							}else{
								String fechaIniFinVenc= null;
								if(cupoAct.getFechaComienzo() != null){
									Calendar ahoraCal= Calendar.getInstance();
									ahoraCal.setTime(cupoAct.getFechaComienzo() );
									int mes=ahoraCal.get(Calendar.MONTH)+ 1;
									String fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
									
									fechaIniFinVenc=String.valueOf(fecha);
								}else{
								}
								if(cupoAct.getFechaFin() != null){
									Calendar ahoraCal= Calendar.getInstance();
									ahoraCal.setTime(cupoAct.getFechaFin() );
									int mes=ahoraCal.get(Calendar.MONTH)+ 1;
									String fecha=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
									if(fechaIniFinVenc != null)
										fechaIniFinVenc= fechaIniFinVenc+" - "+ String.valueOf(fecha);

								}else{
								}	
								
								Listcell pagasubscell = new Listcell(fechaIniFinVenc);
								pagasubscell.setAttribute("vencimiento", true);
								row.appendChild(pagasubscell);							
							}
							
							
						}else{
							Listcell vencsubscell = new Listcell(String.valueOf(VencimientoCursoEnum.toStringMes(curso.getVencimiento().toInt())));
							vencsubscell.setAttribute("vencimiento", true);

							row.appendChild(vencsubscell);
							
						}
			
						int cantClases=cupoAct.getCupos();
						Listcell cantd=null;
						if(curso.getNombre().contains("CUMPLEA")){
							cantd=new Listcell(String.valueOf(" "));
							cantd.setAttribute("cupos", true);

						}else if(cantClases == 99 ){
							cantd=new Listcell(String.valueOf("LIBRE"));
							cantd.setStyle("color:#0B610B !important; font-weight:bold; ");
							cantd.setAttribute("cupos", true);

						}else{
							cantd=new Listcell(String.valueOf(cantClases));
							if(cantClases >0)
								cantd.setStyle("color:#0B610B !important; font-weight:bold; ");
							cantd.setAttribute("cupos", true);

						}
						row.appendChild(cantd);
						
						Listcell vencsubscell222= null ;
						if(hayQuePintarClase){
							vencsubscell222 = new Listcell("Si");
							vencsubscell222.setStyle("color:#FFB24C !important; font-weight:bold; font-size: 8pt; ");
						}else{
							vencsubscell222 = new Listcell("No");
						}
						vencsubscell222.setAttribute("enclase", true);

						row.appendChild(vencsubscell222);

						Listcell vencsubscell22= null ;
						if(cupoAct.getEstado() != null ){
							
							vencsubscell22 = new Listcell(CupoActividadEstadoEnum.toString(cupoAct.getEstado().toInt()));
							
							
							if((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ADEUDA_Y_SIN_CUPOS.toInt())
								||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.C_DEUDAS_Y_C_CUPOS.toInt())
								||(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA_CON_DEUDA.toInt())){
							
									vencsubscell22.setStyle(" color:#FF0000 !important; font-weight:bold;  ");
							}else if((cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.VENCIDA.toInt())) 
									vencsubscell22.setStyle(" color:#FF6347 !important; font-weight:bold;  ");
							
						}else{
							vencsubscell22 = new Listcell(" ");
							
						}
						vencsubscell22.setAttribute("estado", true);
						row.appendChild(vencsubscell22);

						if(cupoAct.getEstado() != null && 
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
				
								int fantantePago =subs.getPrecioTOTALadicionalTarjeta() - pagosRealizados;
								int cantADistributirLoQueSePAgo=obtenerCantidadDeActividadcupoQueNoSeanObraSocial(subs);
								int FaltantePagoDividido=fantantePago /cantADistributirLoQueSePAgo;
				
								// si es el ultimo, le agrego la diferencia de los float que quedaron.
								if(numeroDelCupoActividad == cantADistributirLoQueSePAgo){
									
									int numero=FaltantePagoDividido * cantADistributirLoQueSePAgo;
									int diferencia=fantantePago -  numero;
									FaltantePagoDividido= FaltantePagoDividido+ diferencia;
									
								}
								
								
								Listcell vencsubscell33= null;
								
								if(cupoAct.getEstado().toInt() ==  CupoActividadEstadoEnum.ANULADA.toInt()){
									if(subs.isAnulaSubscripcion() != null && subs.isAnulaSubscripcion()){
										if(subs.isAnulaDevuelveDinero() != null && subs.isAnulaDevuelveDinero()){
											int  valorDevuelvo=subs.getAnulaValorDevuelvo();
											if(valorDevuelvo == pagosRealizados){
												vencsubscell33 = new Listcell("$"+formateador.format(0));		
											}else{
												vencsubscell33 = new Listcell("$");	
											}
													
										}else
											vencsubscell33 = new Listcell("$"+formateador.format(FaltantePagoDividido));
									}else
										vencsubscell33 = new Listcell("$"+formateador.format(FaltantePagoDividido));									
								}else{
									vencsubscell33 = new Listcell("$"+formateador.format(FaltantePagoDividido));
								}
				
								vencsubscell33.setStyle("color:#FF0000 !important; font-weight:bold; ");
								vencsubscell33.setAttribute("pesos", true);
								row.appendChild(vencsubscell33);
							}else{
								Listcell vencsubscell33 = new Listcell("$"+formateador.format(0));
								row.appendChild(vencsubscell33);
							}
								
						}else{
							Listcell vencsubscell33 = new Listcell(String.valueOf("$"+formateador.format(0)));
							row.appendChild(vencsubscell33);
						}	

						// Campo esta en Clase
						
						
						// Campo comentario
						Listcell cant=new Listcell(getConceptoObraSocialYPrecio(subs, actYClase.getActiv(), curso));
						row.appendChild(cant);
						
						row.setAttribute("subs", subs);
						row.setAttribute("curso",curso );
						row.setAttribute("actYClase",actYClase );

						super.addRow(row, "onDoubleClickSubscripcionesEvt");
						super.addRow(row, "onClickSubscripcionesArribaEvt");

						Component nullComponent = null;
						row.addForward("onClick",nullComponent,"onClickSubscripcionesEvt");
						super.addRow2(row);
						
						
				}
		
			}
		}
		}
	}
	
	private int obtenerCantidadDeActividadcupoQueNoSeanObraSocial(Subscripcion subs){
		int cantidad= 0;
		if(subs.getConceptoList() != null){
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getCurso() != null ){
					
					if(iterable_element.getConcepto() != null && !iterable_element.getConcepto().contains("Copago")){

						if(iterable_element.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
							cantidad =cantidad +1;
						}
					}else{
						cantidad =cantidad +1;
					}
				}else{
					cantidad =cantidad +1;
				}
			}
		}
	return subs.getConceptoList().size() - cantidad;
	}
	
	
	private boolean pagaCopagoSegunActividad( Set<ObraSocialesPrecio>  listPrecios, Actividad act){
		
		if(listPrecios != null){
			for (ObraSocialesPrecio obraSocialesPrecio : listPrecios) {
				
				if(obraSocialesPrecio.getActividad().getId().intValue() == act.getId().intValue()){
					if(obraSocialesPrecio.getSePagaUnaUnicaVez() != null)
						return obraSocialesPrecio.getSePagaUnaUnicaVez();
					else
						return false;
				}
			}
		}
		return false;
	}
	
	private String getConceptoObraSocialYPrecio(Subscripcion subs, Actividad act, Curso curso){
		
		if(subs.getConceptoList() != null){
			for (Concepto iterable_element : subs.getConceptoList()) {
				
				if(iterable_element.getConcepto() != null && !iterable_element.getConcepto().contains("Copago")){
					if(iterable_element.getCurso() != null && iterable_element.getCurso().getId() != null && 
							curso != null && curso.getId() != null &&
							iterable_element.getCurso().getId().intValue() == curso.getId().intValue() ){
						
						if(iterable_element.getActividadDelConcepto().getId().intValue() == act.getId().intValue()){
							
							if(iterable_element.getTipoDescuento().toInt() == TipoDescuentosEnum.OBRA_SOCIAL.toInt()){
								if(iterable_element.getPrecioPorClaseOSesionPagaCliente() != null 
										&&  iterable_element.getPrecioPorClaseOSesionPagaCliente() > 0){
								
									iterable_element.setObraSocial(clienteEJB.loadLazy(iterable_element.getObraSocial()));
									
									if(pagaCopagoSegunActividad(iterable_element.getObraSocial().getPreciosActividadesObraSocial(), act)){
										return iterable_element.getObraSocial().getNombre().toUpperCase()+" -Copago de $"+formateador.format(iterable_element.getPrecioPorClaseOSesionPagaCliente()) + "  una única vez";	
									}else{
										return iterable_element.getObraSocial().getNombre().toUpperCase()+" -Copago de $"+formateador.format(iterable_element.getPrecioPorClaseOSesionPagaCliente()) + "  por cada session";				
									}
									
								}else{
									return iterable_element.getObraSocial().getNombre().toUpperCase()+" -no se paga copago";
								}
								
								
							}else{
								return "";
							} 
						}	
					}					
				}
			}
		}
		return "";
	}
	
	private CupoActividad getActividad(Subscripcion subs, Curso curso){
		if(subs.getCupoActividadList() != null){
			for (CupoActividad iterable_element : subs.getCupoActividadList()) {
				if(iterable_element.getCurso().getId().intValue() == curso.getId().intValue() ){

						return iterable_element;
				}
			}
		}
		return null;
	}

}
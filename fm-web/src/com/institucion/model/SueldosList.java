package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class SueldosList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");
	private ClienteEJB cliEJB;

	public SueldosList() {
		super();
		super.addHeader(new Listheader("Empleado")).setWidth("16%");
		super.addHeader(new Listheader("Sucursal")).setWidth("8%");
		super.addHeader(new Listheader("Año y mes / Temporada")).setWidth("12%");
//		super.addHeader(new Listheader("Quincena")).setWidth("10%");
		super.addHeader(new Listheader(I18N.getLabel("gastos.list.dinero"))).setWidth("6%");;
//		super.addHeader(new Listheader("Responsable")).setWidth("10%");
		super.addHeader(new Listheader(I18N.getLabel("gastos.list.comentario")));
		this.setMultiple(false);
		this.setPageSize(15);
		cliEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}

	public void setList(List<Gastos> clienteList, List<GastosMaipu> clienteListMaipu) {
		super.removeAll();
		if(clienteList != null){

			Iterator<Gastos> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				Gastos cliente = itPharmacy.next();
				cliente= cliEJB.loadLazy(cliente);

				
				Listitem row = new Listitem();
				row.setValue(cliente);
	
//				row.appendChild(new Listcell(""));
				if(cliente.getTipoGasto()!= null){
					if(cliente.getTipoGasto().toInt() ==  GastosEnum.SUELDOS.toInt()){
						if((cliente.getPagaSueldosPorMes() != null && cliente.getPagaSueldosPorMes()) 
								|| cliente.getPagaSueldosPorMes() == null ){
							if(cliente.getEmpleado() != null)
								row.appendChild(new Listcell(cliente.getEmpleado().getNombre()));
							else	
								row.appendChild(new Listcell("Sueldos"));	
						}else{
							
//							Calendar ahoraCal= Calendar.getInstance();
//							ahoraCal.setTime(cliente.getFechaDesde());
//							int mes=ahoraCal.get(Calendar.MONTH) + 1;
//
//							String fechaNacDesde=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
//							
//							Calendar ahoraCalHasta= Calendar.getInstance();
//							ahoraCalHasta.setTime(cliente.getFechaHasta());
//							int mes2=ahoraCalHasta.get(Calendar.MONTH) + 1;
//							String fechaNacHasta=ahoraCalHasta.get(Calendar.DATE)+"/"+mes2+"/"+ahoraCalHasta.get(Calendar.YEAR);
//							
							if(cliente.getEmpleado() != null)
								row.appendChild(new Listcell(cliente.getEmpleado().getNombre()));
							else	
								row.appendChild(new Listcell("Sueldos"));	
						}
					}else{
						row.appendChild(new Listcell("Sueldos"));	
					}
				}else
					row.appendChild(new Listcell(" "));
				
				row.appendChild(new Listcell("Centro"));

				row.appendChild(new Listcell(cliente.getAnio()+" "+ I18N.getLabel("dateboxremember."+cliente.getMes())));

				if(cliente.getDinero()!= null)
					row.appendChild(new Listcell("$"+formateador.format(cliente.getDinero())));
				else
					row.appendChild(new Listcell(" "));
				
//				if(cliente != null && cliente.getNombreUsuarioGeneroMovimiento() != null){
//					row.appendChild(new Listcell(cliente.getNombreUsuarioGeneroMovimiento()));
//				}else{
//					row.appendChild(new Listcell(" "));	
//				}

				if(cliente.getTipoGasto().toInt() ==  GastosEnum.SUELDOS.toInt()){
					String sueldosComentarios ="";
					if(cliente.getComentario() != null){
						sueldosComentarios=cliente.getComentario();
					}
					
					if(cliente.getGastosSueldoList() != null){
						
						String comentGastosSueldos=obtenerActividadesQueSePagaronSueldos(cliente);
						if(sueldosComentarios == null || (sueldosComentarios != null && sueldosComentarios.trim().equalsIgnoreCase(""))){
							row.appendChild(new Listcell(sueldosComentarios + "  "+ comentGastosSueldos));	
						}else{
							row.appendChild(new Listcell(sueldosComentarios + ". "+ comentGastosSueldos));
						}
						
					}else{
						row.appendChild(new Listcell(sueldosComentarios));

					}
					
				}else{
					if(cliente.getComentario() != null){
						row.appendChild(new Listcell(cliente.getComentario()));
					}else{
						row.appendChild(new Listcell(" "));
					}		
				}
				super.addRow(row);
			}			
		}
		
		
		if(clienteListMaipu != null){

			Iterator<GastosMaipu> itPharmacy = clienteListMaipu.iterator();
			while (itPharmacy.hasNext()) {
				GastosMaipu cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
				
				if(cliente.getEmpleado() != null){
					row.appendChild(new Listcell(cliente.getEmpleado().getNombre()));	
				}else
					row.appendChild(new Listcell("Sueldos"));

				row.appendChild(new Listcell("Maipu"));
				
				row.appendChild(new Listcell(cliente.getAnio()+"-"+ cliente.getQuincena().getNombre()));
		
//				row.appendChild(new Listcell(cliente.getQuincena().getNombre()));
				

//				if(cliente.getTipoGasto()!= null){
//					String aaa=GastosMaipuEnum.toString(cliente.getTipoGasto().toInt());
//					if(cliente.getTipoGasto().toInt() ==  GastosEnum.SUELDOS.toInt()){
//						
//						if(cliente.getEmpleado() != null){
//							aaa= cliente.getEmpleado().getNombre() + "- "+ aaa;
//						}
//					}
//					row.appendChild(new Listcell(aaa));	
//				}else
//					row.appendChild(new Listcell(" "));
				
				if(cliente.getDinero()!= null)
					row.appendChild(new Listcell("$"+formateador.format(cliente.getDinero())));
				else
					row.appendChild(new Listcell(" "));
				
//				if(cliente != null && cliente.getNombreUsuarioGeneroMovimiento() != null){
//					row.appendChild(new Listcell(cliente.getNombreUsuarioGeneroMovimiento()));
//				}else{
//					row.appendChild(new Listcell(" "));	
//				}

				if(cliente.getComentario() != null){
					row.appendChild(new Listcell(cliente.getComentario()));
				}else{
					row.appendChild(new Listcell(" "));
				}		
				super.addRow(row);
			}			
		}
	}
	
	public String obtenerActividadesQueSePagaronSueldos(Gastos cliente){
		String sueldosComentarios="";
		if(cliente != null && cliente.getGastosSueldoList() != null){
			for (GastosSueldos gastos : cliente.getGastosSueldoList()) {
				if(gastos.getDinero()  != null && gastos.getDinero() > 0){
					sueldosComentarios= sueldosComentarios+ gastos.getActividad().getNombre().toUpperCase()+": $"+formateador.format(gastos.getDinero())+"- ";			
				}
			}		
		}
	return sueldosComentarios;	
	}
}
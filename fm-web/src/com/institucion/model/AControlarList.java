package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class AControlarList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public AControlarList() {
		super();
		super.addHeader(new Listheader(I18N.getLabel("a.controlar.fecha"))).setWidth("13%");
		super.addHeader(new Listheader(I18N.getLabel("a.controlar.tipo"))).setWidth("13%");
		super.addHeader(new Listheader(I18N.getLabel("a.controlar.resposable"))).setWidth("15%");
		super.addHeader(new Listheader(I18N.getLabel("a.controlar.cantDinero"))).setWidth("8%");
		super.addHeader(new Listheader(I18N.getLabel("a.controlar.comentario")));
		this.setMultiple(false);
		this.setPageSize(15);
	}

	public void setList(List<CajaMovimientoView> clienteList) {
		super.removeAll();
		if(clienteList != null && clienteList.size() > 0){

			Iterator<CajaMovimientoView> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				CajaMovimientoView cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
			
				if(cliente.getFecha() != null)
					row.appendChild(new Listcell(cliente.getFecha()));
				else
					row.appendChild(new Listcell(" "));		
				
				if(cliente.getTipo() != null)
					row.appendChild(new Listcell(cliente.getTipo()));
				else
					row.appendChild(new Listcell(" "));		
				
				if(cliente.getResponsable() != null)
					row.appendChild(new Listcell(cliente.getResponsable()));
				else
					row.appendChild(new Listcell(" "));		

				if(cliente.getValor() != null){
					row.appendChild(new Listcell("$"+formateador.format(Integer.parseInt(cliente.getValor()))));
				}else
					row.appendChild(new Listcell(" "));			

				if(cliente.getConcepto() != null){
					String comentario= "";
				
					if(cliente.getTipo() != null){
						if(cliente.getTipo().equalsIgnoreCase("Anula Inscripcion")){
							
							if(cliente.getCliente() != null)
								comentario= "Cliente: "+ cliente.getCliente() + ". "+comentario;	
						
							String cantCupos=null;
							if(cliente.getCantCuposTenia() != null && cliente.getCantCuposTenia().equalsIgnoreCase("99") )
								cantCupos="LIBRE";
							else
								cantCupos=cliente.getCantCuposTenia();
							
							if(cantCupos != null)
								comentario= comentario+ "Cupos que tenia antes de la anulacion: "+ cantCupos;	
							
							comentario= comentario+ cliente.getConcepto();
							row.appendChild(new Listcell(comentario));

						}else 	if(cliente.getTipo().equalsIgnoreCase("Se Devolvio Cupo")){
 
							if(cliente.getCliente() != null)
								comentario= "Cliente: "+ cliente.getCliente() + ". "+comentario;	
								comentario= comentario+ "Se lo quito de clase y devolvio el Cupo. NO SE Devolvio dinero.";;	
							
							comentario= comentario+ cliente.getConcepto();
							row.appendChild(new Listcell(comentario));
						
						}else{
							comentario= comentario+ cliente.getConcepto();
							row.appendChild(new Listcell(comentario));
						}
					}else{
						row.appendChild(new Listcell(cliente.getConcepto()));
					}
				}else{
					row.appendChild(new Listcell(" "));	
				}		
				super.addRow(row);
			}			
		}
	}
}
package com.institucion.model;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class GastosMaipuList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public GastosMaipuList() {
		super();
		
		super.addHeader(new Listheader("Temporada")).setWidth("10%");
		super.addHeader(new Listheader("Quincena")).setWidth("10%");
		super.addHeader(new Listheader(I18N.getLabel("gastos.list.gasto"))).setWidth("30%");
		super.addHeader(new Listheader(I18N.getLabel("gastos.list.dinero"))).setWidth("5%");;
		super.addHeader(new Listheader(I18N.getLabel("gastos.list.comentario")));
		this.setMultiple(false);
		this.setPageSize(15);
	}

	public void setList(List<GastosMaipu> clienteList) {
		super.removeAll();
		if(clienteList != null){

			Iterator<GastosMaipu> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				GastosMaipu cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
				
				row.appendChild(new Listcell(cliente.getAnio()+""));
		
				row.appendChild(new Listcell(cliente.getQuincena().getNombre()));
				
				if(cliente.getTipoGasto()!= null){
					row.appendChild(new Listcell(GastosMaipuEnum.toString(cliente.getTipoGasto().toInt())));	
				}else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getDinero()!= null)
					row.appendChild(new Listcell("$"+formateador.format(cliente.getDinero())));
				else
					row.appendChild(new Listcell(" "));
				

				if(cliente.getComentario() != null){
					row.appendChild(new Listcell(cliente.getComentario()));
				}else{
					row.appendChild(new Listcell(" "));
				}		
				super.addRow(row);
			}			
		}
	}
}
package com.institucion.model;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class MatriculaList extends GridList {
	private static final long serialVersionUID = 1L;

	public MatriculaList() {
		super();
		this.setCheckmark(false);
		super.addHeader(new Listheader(I18N.getLabel("matricula.fechaI")));
		super.addHeader(new Listheader(I18N.getLabel("matricula.fechaV")));
		super.addHeader(new Listheader(I18N.getLabel("matricula.valor"))).setWidth("15%");;
		this.setMultiple(false);
	}

	public void setList(List<Inscripcion> clienteList) {
		super.removeAll();
		if(clienteList != null){

			Iterator<Inscripcion> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				Inscripcion cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
				
				if(cliente.getFecha() != null){
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(cliente.getFecha());
					int mes=ahoraCal.get(Calendar.MONTH) + 1;

					String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					row.appendChild(new Listcell(fechaNac));
					
				}else{
					row.appendChild(new Listcell(" "));
				}
			
				if(cliente.getFechaVencimiento() != null){
					Calendar ahoraCal= Calendar.getInstance();
					ahoraCal.setTime(cliente.getFechaVencimiento());
					int mes=ahoraCal.get(Calendar.MONTH) + 1;

					String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);
					
					row.appendChild(new Listcell(fechaNac));
					
				}else{
					row.appendChild(new Listcell(" "));
				}

				row.appendChild(new Listcell(String.valueOf(cliente.getSaldoAbonado())));
			
				
				super.addRow(row);
			}			
		}
	}
}
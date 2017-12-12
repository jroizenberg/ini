package com.institucion.fm.security.model;

import java.util.Calendar;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.dad.DadList;
import com.institucion.model.Cliente;

public class ClienteDaDList  extends DadList<Cliente> {
	private static final long serialVersionUID = 1L;

	protected void addHeaders() {
		this.addHeader(new Listheader(I18N.getLabel("client.list")));
	}

	protected void addRow(Cliente item) {
		Listitem row = new Listitem();
		String nombres= null;
		if(item.getApellido() != null)
			nombres= item.getApellido();
		if(item.getNombre() != null){
			if(nombres != null)
				nombres= nombres + " " +item.getNombre();
		}
		if(item.getFechaNacimiento() != null){
			Calendar ahoraCal= Calendar.getInstance();
			ahoraCal.setTime(item.getFechaNacimiento());
			int mes=ahoraCal.get(Calendar.MONTH) + 1;

			String fechaNac=ahoraCal.get(Calendar.DATE)+"/"+mes+"/"+ahoraCal.get(Calendar.YEAR);

			if(nombres != null)
				nombres= nombres + " - " +fechaNac;
		}
		if(nombres != null){
//			String text = I18N.getStringLabel(nombres);
			//log.debug("permission [" + item.getDescription() + "][" + text +"]");
			row.setValue(item);
			Listcell nameCell = new Listcell(nombres);
			
			row.appendChild(nameCell);
			this.addRow(row);
		}
	}

	protected boolean areEquals(Cliente item1, Cliente item2) {
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

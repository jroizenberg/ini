package com.institucion.model;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridList;

public class ObraSocialesList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClienteEJB clienteEJB;

	public ObraSocialesList() {
		super();
		super.addHeader(new Listheader("Nombre")).setWidth("80%");;
		super.addHeader(new Listheader("Actividades Disponibles")).setWidth("20%");;
		this.setMultiple(false);
		this.setPageSize(8);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}

	public void setList(List<ObraSocial> claseList) {
		super.removeAll();
		if(claseList != null){
			Iterator<ObraSocial> itPharmacy = claseList.iterator();
			while (itPharmacy.hasNext()) {
				ObraSocial clase = itPharmacy.next();
				clase=clienteEJB.loadLazy(clase);
				Listitem row = new Listitem();
				row.setValue(clase);
				Listcell companyCell = new Listcell(clase.getNombre().toUpperCase());
				row.appendChild(companyCell);
							
				Listcell companyCell2 = new Listcell(""+cantidadActividades(clase));
				row.appendChild(companyCell2);
				
				
				super.addRow(row);
			}
	
		}
	}
	
	private int cantidadActividades(ObraSocial clase){
		int cantidad=0;
		if(clase != null && clase.getPreciosActividadesObraSocial() != null){
			for (ObraSocialesPrecio ospre : clase.getPreciosActividadesObraSocial()) {
				if(ospre.getDisponible() != null && ospre.getDisponible())
					cantidad= cantidad +1;
			}
		}
	return cantidad;
	}
}
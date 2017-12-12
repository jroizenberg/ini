package com.institucion.model;

import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridList;

public class EmpleadoList extends GridList {
	private static final long serialVersionUID = 1L;
	private ClienteEJB clienteEJB;

	public EmpleadoList() {
		super();
		super.addHeader(new Listheader("Nombre")).setWidth("40%");;
		super.addHeader(new Listheader("Actividades")).setWidth("40%");;
//		super.addHeader(new Listheader("Disponible")).setWidth("10%");;
		this.setMultiple(false);
		this.setPageSize(8);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}

	public void setList(List<Empleado> claseList) {
		super.removeAll();
		if(claseList != null){
			Iterator<Empleado> itPharmacy = claseList.iterator();
			while (itPharmacy.hasNext()) {
				Empleado clase = itPharmacy.next();
				clase=clienteEJB.loadLazy(clase);
				Listitem row = new Listitem();
				row.setValue(clase);
				Listcell companyCell = new Listcell(clase.getNombre().toUpperCase());
				row.appendChild(companyCell);
							
				Listcell companyCell2 = new Listcell(""+cantidadActividades(clase));
				row.appendChild(companyCell2);
				
//				if(clase.isDisponible()){
//					Listcell companyCell3 = new Listcell("SI");
//					row.appendChild(companyCell3);
//				}else{
//					Listcell companyCell3 = new Listcell("NO");
//					row.appendChild(companyCell3);
//				}
				super.addRow(row);
			}
	
		}
	}
	
	private String cantidadActividades(Empleado clase){
		String var=null;
		if(clase != null && clase.getActividades() != null){
			for (EmpleadoActividades ospre : clase.getActividades()) {
				if(ospre != null && ospre.isDisponible() && ospre.getActividad().getNombre() != null){
					if(var== null)
						var= ospre.getActividad().getNombre();
					else
						var= var +", "+ ospre.getActividad().getNombre();
				}
			}
		}
	return var;
	}
}
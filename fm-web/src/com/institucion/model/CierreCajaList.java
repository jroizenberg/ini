package com.institucion.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.security.bz.SecurityEJB;
import com.institucion.fm.security.model.User;

public class CierreCajaList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	private SecurityEJB securityEjb = null;

	private SecurityEJB getService()
	{
		if (this.securityEjb == null) {
			this.securityEjb = BeanFactory.<SecurityEJB>getObject("fm.ejb.securityService");
		}
		return this.securityEjb;
	}
	
	
	public CierreCajaList() {
		super();
		super.addHeader(new Listheader("Sucursal")).setWidth("15%");
		super.addHeader(new Listheader("Fecha")).setWidth("13%");
		super.addHeader(new Listheader("Usuario cerro la caja")).setWidth("15%");
		super.addHeader(new Listheader("Monto que dejo")).setWidth("15%");;
		super.addHeader(new Listheader("Comentario" ));
		this.setMultiple(false);
		this.setPageSize(15);
	}

	public void setList(List<CierreCaja> clienteList) {
		super.removeAll();
		if(clienteList != null && clienteList.size() > 0){

			Iterator<CierreCaja> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				CierreCaja cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
			
				
				if(cliente.getSucursal() != null){
					String aa=cliente.getSucursal().toString(cliente.getSucursal().toInt()); 
					row.appendChild(new Listcell(aa));
				}else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getFecha() != null){
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String date1 = format1.format(cliente.getFecha());    
					row.appendChild(new Listcell(date1));
				}else
					row.appendChild(new Listcell(" "));
			
				if(cliente.getIdUsuarioGeneroMovimiento() != null){
				User u=getService().getUserById(null, new Long(cliente.getIdUsuarioGeneroMovimiento()));
					row.appendChild(new Listcell(String.valueOf(u.getFirstName() + " " +  u.getLastName())));
				}else
					row.appendChild(new Listcell(" "));
				
				Listcell plata = null;
				String dada=String.valueOf(cliente.getValor() );

				if(dada!= null && dada.endsWith(",0")){
					String sas=dada.substring(0, dada.indexOf(","));
					int www= Integer.parseInt(sas);
					plata= new Listcell("$"+ formateador.format(www));
				}else if(dada != null){
					if(dada.contains(",")){
						dada= dada.replace(",", "");
					}

					plata= new Listcell("$" +formateador.format(Integer.parseInt(dada)));
				}else{
					plata= new Listcell("$"+formateador.format(0));
				}
				if(cliente.getValor()  >= 0){
					plata .setStyle("color:#0B610B !important; font-weight:bold; ");
				}else{
					plata.setStyle("color:#FF0000 !important; font-weight:bold; ");
				}	
				row.appendChild(plata);
				
				if(cliente.getComentario() != null)
					row.appendChild(new Listcell(cliente.getComentario()));
				else
					row.appendChild(new Listcell(" "));
			
				super.addRow(row);
			}			
		}
	}
}
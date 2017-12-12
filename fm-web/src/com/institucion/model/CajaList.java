package com.institucion.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class CajaList extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public CajaList() {
		super();
		super.addHeader(new Listheader(I18N.getLabel("client.tipo.curso.fechass"))).setWidth("13%");
		super.addHeader(new Listheader(I18N.getLabel("caja.responsable"))).setWidth("15%");
		super.addHeader(new Listheader(I18N.getLabel("caja.cliente"))).setWidth("15%");;
		super.addHeader(new Listheader(I18N.getLabel("caja.concepto")));
		super.addHeader(new Listheader(I18N.getLabel("caja.tipo.movimiento"))).setWidth("8%");;
		super.addHeader(new Listheader(I18N.getLabel("caja.precio.abonado"))).setWidth("8%");   
		this.setMultiple(false);
		this.setPageSize(15);
	}

	public void setList(List<CajaMovimiento> clienteList, Date fechaYHoraDesdePorCierreCaja) {
		super.removeAll();
		if(clienteList != null && clienteList.size() > 0){

			Iterator<CajaMovimiento> itPharmacy = clienteList.iterator();
			while (itPharmacy.hasNext()) {
				CajaMovimiento cliente = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(cliente);
			
				if(cliente.getFecha() != null){
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String date1 = format1.format(cliente.getFecha());    
					row.appendChild(new Listcell(date1));
				}else
					row.appendChild(new Listcell(" "));
			
				if(cliente.getIdUsuarioGeneroMovimiento() != null)
					row.appendChild(new Listcell(String.valueOf(cliente.getNombreUsuarioGeneroMovimiento())));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getCliente() != null)
					row.appendChild(new Listcell(String.valueOf(cliente.getCliente().getApellido().toUpperCase() +" "+ cliente.getCliente().getNombre().toUpperCase())));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getConcepto() != null)
					row.appendChild(new Listcell(cliente.getConcepto()));
				else
					row.appendChild(new Listcell(" "));
				
				if(cliente.getTipoMovimiento() != null){
					// ver si es de caja anterior
					if(fechaYHoraDesdePorCierreCaja != null && cliente.getFecha().before(fechaYHoraDesdePorCierreCaja)){
						// aviso que es del cierre anterior
						if(cliente.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
							Listcell aa=new Listcell("INGRESO del Cierre anterior");
							aa.setStyle("color:#B8860B !important; font-weight:bold; ");
							row.appendChild(aa);	
						}else{
							Listcell aa=new Listcell("EGRESO del Cierre anterior");
							aa.setStyle("color:#B8860B !important; font-weight:bold; ");
							row.appendChild(aa);
						}
					}else{
						if(cliente.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
							Listcell aa=new Listcell(cliente.getTipoMovimiento().toString());
							aa.setStyle("color:#0B610B !important; font-weight:bold; ");
							row.appendChild(aa);	
						}else{
							Listcell aa=new Listcell(cliente.getTipoMovimiento().toString());
							aa.setStyle("color:#FF0000 !important; font-weight:bold; ");
							row.appendChild(aa);
						}
					}
					
				}else
					row.appendChild(new Listcell(" "));
				
				Listcell plata = null;
				String dada=String.valueOf(cliente.getValor() );

				if(dada!= null && dada.endsWith(".0")){
					String sas=dada.substring(0, dada.indexOf("."));
					int aaa= Integer.parseInt(sas);
					plata= new Listcell("$"+formateador.format(aaa));
				}else if(dada!= null && dada.endsWith(",0")){
					String sas=dada.substring(0, dada.indexOf(","));
					int aaa= Integer.parseInt(sas);
					plata= new Listcell("$"+formateador.format(aaa));
				}else if(cliente.getValor() != null){
					String aaa= String.valueOf(cliente.getValor() );
					if(aaa.contains(",")){
						aaa= aaa.replace(",", "");
					}							
					if(aaa.contains(".")){
				        double a= Double.parseDouble(aaa);
				        long nue=Math.round(a);
						plata= new Listcell("$"+formateador.format(nue));
					}else{
						plata= new Listcell("$"+formateador.format(Integer.parseInt(aaa)));
					}
				}else{
					plata= new Listcell("$"+formateador.format(0));

				}
				if(fechaYHoraDesdePorCierreCaja != null && cliente.getFecha().before(fechaYHoraDesdePorCierreCaja)){
					// aviso que es del cierre anterior
					if(cliente.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
						plata .setStyle("color:#B8860B !important; font-weight:bold; ");
					}else{
						plata.setStyle("color:#B8860B !important; font-weight:bold; ");
					}	

				}else{
					if(cliente.getTipoMovimiento().toInt() == TipoMovimientoCajaEnum.INGRESO.toInt()){
						plata .setStyle("color:#0B610B !important; font-weight:bold; ");
					}else{
						plata.setStyle("color:#FF0000 !important; font-weight:bold; ");
					}	

				}
				row.appendChild(plata);

				super.addRow(row);
			}			
		}
	}
}
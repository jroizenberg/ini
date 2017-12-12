package com.institucion.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.bz.ClienteEJB;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class ProductoVentaDelDiaList2 extends GridList {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");
	private ClienteEJB clienteEJB;

	public ProductoVentaDelDiaList2() {
		super();
		
		super.addHeader(new Listheader(I18N.getLabel("venta.productos.fechayHora"))).setWidth("13%");;
		super.addHeader(new Listheader(I18N.getLabel("venta.productos.usuario.creo"))).setWidth("13%");;
		super.addHeader(new Listheader("Importe")).setWidth("7%");;
		super.addHeader(new Listheader(I18N.getLabel("venta.productos.detalle")));
		this.setMultiple(false);
		this.setPageSize(6);
		clienteEJB = BeanFactory.<ClienteEJB>getObject("fmEjbCliente");	

	}

	public void setList(List<VentaProducto> productosList) {
		super.removeAll();
		
		if(productosList != null){
			Iterator<VentaProducto> itPharmacy = productosList.iterator();
			while (itPharmacy.hasNext()) {
				VentaProducto prod = itPharmacy.next();
				Listitem row = new Listitem();
				row.setValue(prod);
				
				if(prod.getFechayhoracompra() != null){
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String date1 = format1.format(prod.getFechayhoracompra());  
					row.appendChild(new Listcell(date1));
					
				}else
					row.appendChild(new Listcell(" "));
			
				if(prod.getNombreUsuarioCreoVenta() != null)
					row.appendChild(new Listcell(prod.getNombreUsuarioCreoVenta().toString()));
				else
					row.appendChild(new Listcell(" "));

				// Cantidad vendida
				int canti=obtenerPagos(prod);
				row.appendChild(new Listcell("$"+formateador.format(canti)));
				
				if(prod.getVentaComentario() != null){
					if(prod.getAnulaVenta() != null && prod.getAnulaVenta()){
						String addenComentario= "Venta ANULADA ";
						
						if(prod.getNombreUsuarioAnuloVenta() != null)
							addenComentario= addenComentario + " por usuario: "+ prod.getNombreUsuarioAnuloVenta();
						
						if(prod.getNombreUsuarioAnuloVenta() != null){
							SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							String date1 = format1.format(prod.getFechaYHoraAnulacion());  
							addenComentario= addenComentario + " . Fecha ANULACION: "+ date1;
						}
						
						row.appendChild(new Listcell(addenComentario +". "+ prod.getVentaComentario().toString()));	

					}else{
						row.appendChild(new Listcell(prod.getVentaComentario().toString()));	
					}
				}else
					row.appendChild(new Listcell(" "));
			
				super.addRow2(row);
			}
	
		}
	}
	
	public int obtenerPagos(VentaProducto  producto){
		int pagosRealizados=0;
		int adicionales= 0;
		producto= clienteEJB.loadLazy(producto, true, false);
		if(producto.getPagosPorSubscripcionList() != null){
			
			for (PagosPorSubscripcion pagoPorSubscripcion: 	producto.getPagosPorSubscripcionList()) {
				if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_15.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else if(pagoPorSubscripcion.getIdTipoDePago().toInt() == FormasDePagoSubscripcionEnum.TARJETA_20.toInt()){
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
					adicionales= adicionales+ pagoPorSubscripcion.getAdicional();
				}else{
					pagosRealizados= pagosRealizados+ pagoPorSubscripcion.getCantidadDinero();
				}		
			}					
		}
		return adicionales+ pagosRealizados;
	} 
}
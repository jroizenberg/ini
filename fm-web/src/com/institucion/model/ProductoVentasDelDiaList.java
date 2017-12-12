package com.institucion.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class ProductoVentasDelDiaList extends PanelRoutingCrud<VentaProducto> {
	private static final long serialVersionUID = 1L;
	
	public ProductoVentasDelDiaList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);
	}
	
	@Override
	protected boolean setPanelTitle() {
		this.addAuxheader(" ", 1,1, "2px");
		this.addAuxheader(I18N.getLabel("venta.productos.fechayHora")		, 1,1, "8%");
		this.addAuxheader(I18N.getLabel("venta.productos.usuario.creo")	, 1,1, "10%");
		this.addAuxheader(I18N.getLabel("venta.productos.detalle")			, 1,1);
		
		return false;
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	public void setField(List<VentaProducto> productosList) {
		Checkbox check = null;
		cleanRows();

		if(productosList != null){
			for (VentaProducto prod : productosList) {
				check = new Checkbox();
				check.setId(String.valueOf(prod.getId()));
				check.setAttribute("check", prod);
				check.setWidth("2%");
				this.addField(check);

				
				if(prod.getFechayhoracompra() != null){
					SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					String date1 = format1.format(prod.getFechayhoracompra());  
					this.addField(new Label(date1));
					
				}else
					this.addField(new Label(" "));
			
				if(prod.getNombreUsuarioCreoVenta() != null)
					this.addField(new Label(prod.getNombreUsuarioCreoVenta().toString()));
				else
					this.addField(new Label(" "));

				if(prod.getVentaComentario() != null){
					if(prod.getAnulaVenta() != null && prod.getAnulaVenta()){
						String addenComentario= "Venta Anulada ";
						
						if(prod.getNombreUsuarioAnuloVenta() != null)
							addenComentario= addenComentario + " por usuario: "+ prod.getNombreUsuarioAnuloVenta();
						
						if(prod.getNombreUsuarioAnuloVenta() != null){
							SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							String date1 = format1.format(prod.getFechaYHoraAnulacion());  
							addenComentario= addenComentario + " . Fecha Anulacion: "+ date1;
						}
						
						this.addField(new Label(addenComentario +". "+ prod.getVentaComentario().toString()));	

					}else{
						this.addField(new Label(prod.getVentaComentario().toString()));	
					}
				}else
					this.addField(new Label(" "));
				
				super.addRow();
				
			}		
		}
	}

	@Override
	protected void setUIEvent() {
		this.setUievent(true);

	}

	@Override
	protected void setCheckbox() {
		this.setCheckbox(false);
	}


	@SuppressWarnings("unchecked")
	public void deleteRow() {

	}
	
	@SuppressWarnings("unchecked")
	public void cleanRows() {
		Rows rows =super.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
		} 
		super.addRow();
	}
}
package com.institucion.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class ProductoVentaList extends PanelRoutingCrud<Producto> {
	private static final long serialVersionUID = 1L;
	DecimalFormat formateador = new DecimalFormat("###,###");

	public ProductoVentaList() {
		super();
//		this.setCheckbox();
//		this.setUIEvent();
//		this.setDisable(false);

	}
	@Override
	protected boolean setPanelTitle() {
		this.addAuxheader2(" ", 1,1, "1%");
		this.addAuxheader2(I18N.getLabel("producto.imagen")		, 1,1, "3%");
		this.addAuxheader2(I18N.getLabel("producto.codigo")			, 1,1, "4%");
//		this.addAuxheader(I18N.getLabel("producto.nombre")			, 1,1);
		this.addAuxheader2(I18N.getLabel("producto.descripcion")	, 1,1, "4%");
		this.addAuxheader2(I18N.getLabel("producto.precio")	, 1,1, "4%");
		this.addAuxheader2(I18N.getLabel("producto.stock")	, 1,1, "4%");
		this.addAuxheader2(I18N.getLabel("producto.cantidad.a.comprar")	, 1,1, "3%");

		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	public void setField(List<Producto> productosList, boolean isFromVenta) {
		Checkbox check = null;
		cleanRows();

		if(productosList != null){
			for (Producto prod : productosList) {
				check = new Checkbox();
				check.setId(String.valueOf(prod.getId()));
				check.setAttribute("check", prod);
//				check.setWidth("2px");
//				check.setStyle("width:2px !important;");
				this.addField(check);

				if(prod.getImagen() != null){
					InputStream in = new ByteArrayInputStream(prod.getImagen());
					BufferedImage bImageFromConvert;
					try {
						bImageFromConvert = ImageIO.read(in);
						if(bImageFromConvert != null){
							Image imm0 =new Image(); 
							imm0.setContent(bImageFromConvert);
							imm0.setWidth("45px");
							imm0.setHeight("55px");

							imm0.applyProperties();
							this.addField(imm0);
						}else{
							Label lbl = new Label("");
							this.addField(lbl);
						}

					} catch (IOException e) {
						e.printStackTrace();
						Label lbl = new Label("");
						this.addField(lbl);
					}
				}else{
					Label lbl = new Label("");
					this.addField(lbl);
					
				}
				
				if(prod.getCodigo() != null)
					this.addField(new Label(prod.getCodigo().toUpperCase()));
				else
					this.addField(new Label(" "));

				if(prod.getDescripcion() != null)
					this.addField(new Label(prod.getDescripcion().toString()));
				else
					this.addField(new Label(" "));
				
				
				this.addField(new Label("$"+formateador.format(prod.getPrecio())));
				this.addField(new Label(String.valueOf(prod.getStock())));
				
				if(!isFromVenta){  // se selecciona desde el selector
					Intbox aa=new Intbox(prod.getCantidad());
					aa.setAttribute("idCheck", String.valueOf(prod.getId()));
					aa.addForward(Events.ON_CHANGE,aa,"onChangeClickEvt");
					Component com = null;
					aa.addForward(Events.ON_CHANGE,com,"onChange2ClickEvt", check);

					this.addField22(aa, check);  // deberia poner todo en 0
				}else
					this.addField(new Label(String.valueOf(prod.getCantidad())));  // deberia poner para las los que se van a comprar el valor de la cantidad
				
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

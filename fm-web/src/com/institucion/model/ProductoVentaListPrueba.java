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

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.institucion.desktop.delegated.ClienteDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.GridList;

public class ProductoVentaListPrueba extends GridList {
	private static final long serialVersionUID = 1L;
	private ClienteDelegate actionComposerDelegate;	
	DecimalFormat formateador = new DecimalFormat("###,###");


	public ProductoVentaListPrueba() {
		super(false);
		super.addHeader( setHeaders(I18N.getLabel("producto.imagen")	,null) ).setWidth("5%");
		super.addHeader(setHeaders(I18N.getLabel("producto.codigo")	,null)).setWidth("10%");
		super.addHeader(setHeaders(I18N.getLabel("producto.descripcion"),null)).setWidth("20%");;
		super.addHeader(setHeaders(I18N.getLabel("producto.precio"),null)).setWidth("6%");
		super.addHeader(setHeaders(I18N.getLabel("producto.stock"),null)).setWidth("6%");
		super.addHeader(setHeaders(I18N.getLabel("producto.cantidad.a.comprar"),null)).setWidth("10%");   // Adeuda
		this.setMultiple(false);
		this.setCheckmark(false);
	}

	/**
	 * Set event listener and ID to the list headers
	 * @param header
	 */
	private Listheader setHeaders (String header, String id){
			
			Listheader hl = new Listheader(header);
			hl.setAttribute("id", id);
			return hl;
	}
	
	public void setList(List<Producto> productosList, boolean isFromVenta) {
		super.removeAll();
		
		if(productosList != null){
			for (Producto prod : productosList) {
				
				Listitem row = new Listitem();
				row.setAttribute("prodID", String.valueOf(prod.getId()));
				row.addEventListener(Events.ON_CLICK, new EventListener(){
					public void onEvent(Event evt){
						String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
						actualizarIntbos(Integer.parseInt(prodID), "resta");
						actionComposerDelegate.actualizarPaneldePrecioProducto();
					}
				});
				row.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){
					public void onEvent(Event evt){
						String prodID=(String)((Listitem)evt.getTarget()).getAttribute("prodID");
						actualizarIntbos(Integer.parseInt(prodID), "suma");
						actionComposerDelegate.actualizarPaneldePrecioProducto();
					}
				});

				row.setValue(prod);
				if(prod.getImagen() != null){
					InputStream in = new ByteArrayInputStream(prod.getImagen());
					BufferedImage bImageFromConvert;
					try {
						bImageFromConvert = ImageIO.read(in);
						if(bImageFromConvert != null){
							
							Listcell pagasubscell2 = new Listcell();
					
							Image imm0 =new Image(); 
							imm0.setContent(bImageFromConvert);
							imm0.setAttribute("prodID", String.valueOf(prod.getId()));
							imm0.addEventListener(Events.ON_CLICK, new EventListener(){
								public void onEvent(Event evt){
									String prodID=(String)((Image)evt.getTarget()).getAttribute("prodID");
									actualizarIntbos(Integer.parseInt(prodID), "resta");
									actionComposerDelegate.actualizarPaneldePrecioProducto();
								}
							});

							imm0.addEventListener(Events.ON_RIGHT_CLICK, new EventListener(){
								public void onEvent(Event evt){
									String prodID=(String)((Image)evt.getTarget()).getAttribute("prodID");
									actualizarIntbos(Integer.parseInt(prodID), "suma");
									actionComposerDelegate.actualizarPaneldePrecioProducto();
								}
							});

							imm0.setWidth("45px");
							imm0.setHeight("55px");
							imm0.applyProperties();
							pagasubscell2.appendChild(imm0);

							row.appendChild(pagasubscell2);
						}else{
							row.appendChild(new Listcell(""));
						}

					} catch (IOException e) {
						e.printStackTrace();
						row.appendChild(new Listcell(""));
					}
				}else{
					row.appendChild(new Listcell(""));
				
				}
				
				if(prod.getCodigo() != null)
					row.appendChild(new Listcell(prod.getCodigo().toUpperCase()));
				else
					row.appendChild(new Listcell(""));

				if(prod.getDescripcion() != null)
					row.appendChild(new Listcell(prod.getDescripcion().toString()));
				else
					row.appendChild(new Listcell(""));

				row.appendChild(new Listcell("$"+formateador.format(prod.getPrecio())));
				row.appendChild(new Listcell(String.valueOf(prod.getStock())));
				
				if(!isFromVenta){  // se selecciona desde el selector
					
					Listcell pagasubscell2 = new Listcell();

					Intbox aa=new Intbox(prod.getCantidad());
					aa.setReadonly(true); //Disabled(true);
					aa.setWidth("30px");
					Image imas= new Image("/img/mas.png");
					imas.setAttribute("prodID", String.valueOf(prod.getId()));
					imas.addEventListener(Events.ON_CLICK, new EventListener(){
						public void onEvent(Event evt){
							String prodID=(String)((Image)evt.getTarget()).getAttribute("prodID");
							actualizarIntbos(Integer.parseInt(prodID), "suma");
							actionComposerDelegate.actualizarPaneldePrecioProducto();
						}
					});

					
					Image imenos= new Image("/img/menos.png");
					imenos.setAttribute("prodID", String.valueOf(prod.getId()));
					imenos.addEventListener(Events.ON_CLICK, new EventListener(){
						public void onEvent(Event evt){
							String prodID=(String)((Image)evt.getTarget()).getAttribute("prodID");
							actualizarIntbos(Integer.parseInt(prodID), "resta");
							actionComposerDelegate.actualizarPaneldePrecioProducto();
						}
					});
					pagasubscell2.appendChild(imenos);
					pagasubscell2.appendChild(aa);
					pagasubscell2.appendChild(imas);

					row.appendChild(pagasubscell2);
				}else
					row.appendChild(new Listcell(String.valueOf(prod.getCantidad())));
				super.addRow(row);
				
			}		
		}
	
	}
	
	public void actualizarIntbos(int prodID, String type){
		
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Producto cursoSeleccionado=(Producto)cursosDeLaLista.getValue();

			// busco y actualizo el campo quincena
			if(cursoSeleccionado != null  && cursoSeleccionado.getId().intValue() == prodID){
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						Iterator iteee2=cel.getChildren().iterator();
						while(iteee2.hasNext()){
							Object obj=	(Object)iteee2.next();

							if(obj instanceof Intbox){
								if(((Intbox)obj).getValue() == null || (((Intbox)obj).getValue() != null && ((Intbox)obj).getValue() == 0)){
									if(type.equalsIgnoreCase("suma")){
										(((Intbox)obj)).setValue(1);
//										seteaColorARegistros(prodID, "  color: red !important;font-weight: bold !important;");
										( (Intbox)obj).setStyle("color: red;font-weight: bold;");
										cel.setStyle("color: red;font-weight: bold;");
										cursosDeLaLista.setStyle("color: red;font-weight: bold;");
									}else{
//										seteaColorARegistros(prodID, " color: #000000 !important;font-weight: normal !important;");
										( (Intbox)obj).setStyle("color: black;font-weight: normal;");
										cel.setStyle("color: black;font-weight: normal;");
										cursosDeLaLista.setStyle("color: black;font-weight: normal;");
									}
								}else{
									if(type.equalsIgnoreCase("suma")){
										( (Intbox)obj).setStyle("color: red;font-weight: bold;");
										cel.setStyle("color: red;font-weight: bold;");
										cursosDeLaLista.setStyle("color: red;font-weight: bold;");

										(((Intbox)obj)).setValue( (((Intbox)obj)).getValue() +1);
									}else{
										(((Intbox)obj)).setValue( (((Intbox)obj)).getValue() -1);
										if(( (Intbox)obj).getValue() == 0){
											( (Intbox)obj).setStyle("color: black;font-weight: normal;");
											cel.setStyle("color: black;font-weight: normal;");
											cursosDeLaLista.setStyle("color: black;font-weight: normal;");
										}
									}
								}
								break;
							}							
						}
					}
				}
			}
		}		
		
	}

	public void actualizarIntbosaNegros(){
		
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Producto cursoSeleccionado=(Producto)cursosDeLaLista.getValue();

			// busco y actualizo el campo quincena
				if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
					Iterator iteee=cursosDeLaLista.getChildren().iterator();
					while(iteee.hasNext()){
						Listcell cel=	(Listcell)iteee.next();
						Iterator iteee2=cel.getChildren().iterator();
						while(iteee2.hasNext()){
							Object obj=	(Object)iteee2.next();

							if(obj instanceof Intbox){
								( (Intbox)obj).setStyle("color: black;font-weight: normal;");
								cel.setStyle("color: black;font-weight: normal;");
								cursosDeLaLista.setStyle("color: black;font-weight: normal;");

							}							
						}
					}
			}
		}		
		
	}
	@SuppressWarnings("unchecked")
	public List<Producto>getListProductsSelectedConCantidadActualizada(){
		List<Producto>productoss = new ArrayList<Producto>();
		Iterator<Listitem> itCursos =super.getItems().iterator();
		while (itCursos.hasNext()) {
			Listitem cursosDeLaLista=(Listitem)itCursos.next();
			Producto cursoSeleccionado=(Producto)cursosDeLaLista.getValue();

			// busco y actualizo el campo quincena
			if(cursosDeLaLista != null && cursosDeLaLista.getChildren() != null){
				Iterator iteee=cursosDeLaLista.getChildren().iterator();
				while(iteee.hasNext()){
					Listcell cel=	(Listcell)iteee.next();
					Iterator iteee2=cel.getChildren().iterator();
					while(iteee2.hasNext()){
						Object obj=	(Object)iteee2.next();

						if(obj instanceof Intbox){
							if(((Intbox)obj).getValue() == null){
								cursoSeleccionado.setCantidad(0);
							}else{
								cursoSeleccionado.setCantidad((((Intbox)obj)).getValue());
							}
							
							if(cursoSeleccionado.getCantidad() >0)
								productoss.add(cursoSeleccionado);
						}	
					}
				}
			}
		}		
		return productoss;
	}

	public ClienteDelegate getActionComposerDelegate() {
		return actionComposerDelegate;
	}

	public void setActionComposerDelegate(ClienteDelegate actionComposerDelegate) {
		this.actionComposerDelegate = actionComposerDelegate;
	}
}
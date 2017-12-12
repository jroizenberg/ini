package com.institucion.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJBException;
import javax.imageio.ImageIO;

import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;

import com.institucion.bz.ProductoEJB;
import com.institucion.fm.conf.Session;
import com.institucion.fm.conf.exception.WebExceptionHandler;
import com.institucion.fm.conf.spring.BeanFactory;
import com.institucion.fm.desktop.service.CrudComposer;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.MessageBox;
import com.institucion.fm.desktop.view.PanelCrud;
import com.institucion.model.Producto;
import com.institucion.model.ProductoCrud;

public class ProductosCrudComposer extends CrudComposer{

	public final String idCurso = "idProducto";
	private Producto producto;
	private ProductoEJB productoEJB;
	private PanelCrud crud;
	
	public ProductosCrudComposer() {
		productoEJB = BeanFactory.<ProductoEJB>getObject("fmEjbProducto");
	}

	private ProductoCrud getProductoCrud() {
		return (ProductoCrud)(crud.getGridCrud());
	}
 	
    public BufferedImage resize(BufferedImage bufferedImage, int newW, int newH) {
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();
        BufferedImage bufim = new BufferedImage(newW, newH, bufferedImage.getType());
        Graphics2D g = bufim.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(bufferedImage, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return bufim;
    }
    
	public void onCreate() {
		Producto prod= (Producto) Session.getAttribute(idCurso);
		setProducto(prod);
		fromModelToView();
		
		getProductoCrud().getArchivofile().addEventListener(Events.ON_UPLOAD, new EventListener() {
			public void onEvent(Event evt) {
				
				org.zkoss.util.media.Media media = 	((UploadEvent)evt).getMedia();
				if (media!=null){
					if(media instanceof AImage){
						java.io.InputStream in = media.getStreamData();
						BufferedImage bImageFromConvert;
						try {
							bImageFromConvert = ImageIO.read(in);
							if(bImageFromConvert != null){
								
								BufferedImage bImageFromConvertNueva= resize(bImageFromConvert, 45, 55);
								
								 getProductoCrud().getImagenProducto().setContent(bImageFromConvertNueva);
								 getProductoCrud().getImagenProducto().setVisible(true);
								 
//								 getProductoCrud().getImagenProducto().setWidth("45px");
//								 getProductoCrud().getImagenProducto().setHeight("55px");
								 getProductoCrud().getImagenProducto().applyProperties();
							}

						} catch (IOException e) {
							e.printStackTrace();
						
						}		
						
					}else{
						MessageBox.info(" Error - Se permiten importar solamente Imagenes", I18N.getLabel("selector.actionwithoutitem.title"));
						return;	
					}
			}
		}		
	  });	
		
	}
	
	private void fromModelToView() {
		// si lo que obtuve de bd es != null
		if(producto != null){

			if(producto.getDescripcion() != null){
				getProductoCrud().getDescripcionCb().setValue(producto.getDescripcion());
			}
			
			if(producto.getCodigo() != null){
				getProductoCrud().getCodigoCb().setValue(producto.getCodigo());
			}
			
			getProductoCrud().getPrecioCb().setValue(producto.getPrecio());

			getProductoCrud().getPrecioCostoCb().setValue(producto.getPrecioCosto());
			
			getProductoCrud().getStockCb().setValue(producto.getStock());
		
			if(producto.getImagen() != null){
				InputStream in = new ByteArrayInputStream(producto.getImagen());
				BufferedImage bImageFromConvert;
				try {
					bImageFromConvert = ImageIO.read(in);
					if(bImageFromConvert != null){
						getProductoCrud().getImagenProducto().setContent(bImageFromConvert);
						 getProductoCrud().getImagenProducto().setWidth("45px");
						 getProductoCrud().getImagenProducto().setHeight("55px");

						getProductoCrud().getImagenProducto().applyProperties();
						getProductoCrud().getImagenProducto().setVisible(true);
					}

				} catch (IOException e) {
					e.printStackTrace();			
				}
			}
		}			
	}
		
	public void onSave(Event event) throws Exception {
		try{
			if (Sessions.getCurrent().getAttribute(idCurso) != null) {
				// es una modificacion
					producto = (Producto) Sessions.getCurrent().getAttribute(idCurso);
					this.fromViewToModel(producto);
					if(!validateCrud()){
						MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
						return;
					}
					productoEJB.save(producto);
					
					if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
						Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
						super.gotoPage("/institucion/productos-selector.zul");
					}else	
						super.gotoPage("/institucion/productos-selector.zul");

			} else {
				// es nuevo 
				producto= new Producto();
				this.fromViewToModel(producto);
				if(!validateCrud()){
					MessageBox.info(I18N.getLabel("curso.error"), I18N.getLabel("selector.actionwithoutitem.title"));
					return;
				}
				productoEJB.save(producto);
		
				if (Sessions.getCurrent().getAttribute("isClienteFromCurso") != null){ 
					Sessions.getCurrent().setAttribute("isClienteFromCurso", null);
					super.gotoPage("/institucion/productos-selector.zul");
				}else	
					super.gotoPage("/institucion/productos-selector.zul");
			}
		} catch (EJBException ex) {
			WebExceptionHandler.handleThis(ex);
		}
	}
		
	public void onBack(Event event) {
		
		super.gotoPage("/institucion/productos-selector.zul");
	}


	private Producto fromViewToModel(Producto producto) {
		
		if(getProductoCrud().getDescripcionCb().getValue() != null)
			producto.setDescripcion(getProductoCrud().getDescripcionCb().getValue());
	
		if(getProductoCrud().getPrecioCb().getValue() != null)
			producto.setPrecio(getProductoCrud().getPrecioCb().getValue());	

		if(getProductoCrud().getPrecioCostoCb().getValue() != null)
			producto.setPrecioCosto(getProductoCrud().getPrecioCostoCb().getValue());	
		
		if(getProductoCrud().getStockCb().getValue() != null)
			producto.setStock(getProductoCrud().getStockCb().getValue());
		
		if(getProductoCrud().getCodigoCb().getValue() != null)
			producto.setCodigo(getProductoCrud().getCodigoCb().getValue());

		if(getProductoCrud().getImagenProducto() != null && getProductoCrud().getImagenProducto().getContent() != null){
			
			org.zkoss.zul.Image aa= (org.zkoss.zul.Image) getProductoCrud().getImagenProducto();
			Image bImageFromConvert =aa.getContent();
			producto.setImagen(bImageFromConvert.getByteData());
		}else
			producto.setImagen(null);

		return producto;
	}
	
	private boolean validateCrud() {
			
		if(getProductoCrud().getCodigoCb().getValue() == null)
			throw new WrongValueException(getProductoCrud().getCodigoCb(), I18N.getLabel("error.empty.field"));
	
		if(getProductoCrud().getPrecioCb().getValue() == null)
			throw new WrongValueException(getProductoCrud().getPrecioCb(), I18N.getLabel("error.empty.field"));

		if(getProductoCrud().getPrecioCostoCb().getValue() == null)
			throw new WrongValueException(getProductoCrud().getPrecioCostoCb(), I18N.getLabel("error.empty.field"));
		
		if(getProductoCrud().getStockCb().getValue() == null)
			throw new WrongValueException(getProductoCrud().getStockCb(), I18N.getLabel("error.empty.field"));
		
		return true;
	}


	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}
}

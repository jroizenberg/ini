package com.institucion.model;

import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ProductoCrud extends GridCrud{

	private static final long serialVersionUID = 1L;
	
	private Textbox descripcionCb;
	private Textbox codigoCb;
	private Intbox precioCb;
	private Intbox precioCostoCb;
	private Intbox stockCb;
	private Image imagenProducto;
    private Fileupload archivofile;

	public ProductoCrud (){
		super();
		this.makeFields();
	}
	
	private void makeFields(){
		codigoCb= new Textbox();
    	codigoCb.setMaxlength(50);
    	codigoCb.setConstraint(new TextConstraint());
		this.addField(new RequiredLabel(I18N.getLabel("producto.codigo")), codigoCb);
		
		descripcionCb= new Textbox();
		descripcionCb.setMaxlength(50);
		descripcionCb.setConstraint(new TextConstraint());
		this.addField(new Label(I18N.getLabel("producto.descripcion")), descripcionCb);
		
		precioCostoCb = new Intbox();
		this.addField(new RequiredLabel("Precio Costo"), precioCostoCb);
		
		precioCb = new Intbox();
		this.addField(new RequiredLabel(I18N.getLabel("producto.precio")), precioCb);
		
		stockCb = new Intbox();
		this.addField(new RequiredLabel(I18N.getLabel("producto.stock")), stockCb);
	
		Label ll = new Label();
		this.addField(new RequiredLabel(" "), ll);
		
		Label ll2 = new Label();
		this.addField(new RequiredLabel(" "), ll2);
		
		Label ll3 = new Label();
		this.addField(new RequiredLabel(" "), ll3);
		
		archivofile = new Fileupload();
		archivofile.setId("archivofile");
		archivofile.setFocus(true);
		this.addField(new RequiredLabel("Importar Foto"), archivofile);

		imagenProducto = new Image();
		imagenProducto.setId("marketOKsasas");

		imagenProducto.setWidth("45px");
		imagenProducto.setHeight("55px");
		imagenProducto.setVisible(false);
        this.addField(imagenProducto);
        
        this.addField(new Label(""));
		
	}

	public Textbox getDescripcionCb() {
		return descripcionCb;
	}

	public void setDescripcionCb(Textbox descripcionCb) {
		this.descripcionCb = descripcionCb;
	}

	public Intbox getPrecioCb() {
		return precioCb;
	}

	public void setPrecioCb(Intbox precioCb) {
		this.precioCb = precioCb;
	}

	public Intbox getStockCb() {
		return stockCb;
	}

	public void setStockCb(Intbox stockCb) {
		this.stockCb = stockCb;
	}

	public Textbox getCodigoCb() {
		return codigoCb;
	}

	public void setCodigoCb(Textbox codigoCb) {
		this.codigoCb = codigoCb;
	}

	public Image getImagenProducto() {
		return imagenProducto;
	}

	public void setImagenProducto(Image imagenProducto) {
		this.imagenProducto = imagenProducto;
	}

	public Fileupload getArchivofile() {
		return archivofile;
	}

	public void setArchivofile(Fileupload archivofile) {
		this.archivofile = archivofile;
	}

	public Intbox getPrecioCostoCb() {
		return precioCostoCb;
	}

	public void setPrecioCostoCb(Intbox precioCostoCb) {
		this.precioCostoCb = precioCostoCb;
	}


}

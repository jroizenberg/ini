package com.institucion.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.institucion.desktop.delegated.CursosDelegate;
import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.validator.TextConstraint;
import com.institucion.fm.desktop.view.GridCrud;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.PanelEntityCrudList;
import com.institucion.fm.desktop.view.RequiredLabel;

public class ClientesNoEncontradosEnListaPresenteCrud extends PanelEntityCrudList<ClienteNoEncontradoEnPileta>{

	private static final long serialVersionUID = 1L;
	public static final String ID = "gridproduct";
	private CursosDelegate delegate;
	private GridCrud productCrudGrid;
	private GridList productListBox;
	
	private Textbox nombre;
	private Textbox telefono;
	private Textbox celular;
	private Textbox comentario;
	
	public ClientesNoEncontradosEnListaPresenteCrud(){
	}

	private String updateMethod="onDoubleClickEvt";

	/**
	 * @return the productAmount
	 */

	public CursosDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CursosDelegate delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void cleanCrud() {
		Constraint c = nombre.getConstraint();
		nombre.setConstraint("");
		nombre.setText("");
		nombre.setConstraint(c);
		
		Constraint brandC = telefono.getConstraint();
		telefono.setConstraint("");
		telefono.setText("");
		telefono.setConstraint(brandC);
		
		Constraint presentationC = celular.getConstraint();
		celular.setConstraint("");
		celular.setText("");
		celular.setConstraint(presentationC);
		
		Constraint presentationC2 = comentario.getConstraint();
		comentario.setConstraint("");
		comentario.setText("");
		comentario.setConstraint(presentationC2);
	}
	
	@Override
	protected Listitem createItem(ClienteNoEncontradoEnPileta item) {
		
		Listitem rowProduct = new Listitem();
		
		if(item.getNombre() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getNombre())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getTelefono() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getTelefono())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getCelular() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getCelular())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getComentario() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getComentario())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		
		rowProduct.setValue(item);
		rowProduct.addForward(Events.ON_DOUBLE_CLICK,(Component) null,getUpdateMethod());
		addIDUEvent(rowProduct);
		return rowProduct;
	}
	
	protected Listitem createItem2(ClienteNoEncontradoEnPiletaHistorico item) {
		
		Listitem rowProduct = new Listitem();
		
		if(item.getNombre() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getNombre())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getTelefono() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getTelefono())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getCelular() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getCelular())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		if(item.getComentario() != null)
			rowProduct.appendChild(new Listcell(String.valueOf(item.getComentario())));
		else
			rowProduct.appendChild(new Listcell(" "));
		
		
		rowProduct.setValue(item);
		rowProduct.addForward(Events.ON_DOUBLE_CLICK,(Component) null,getUpdateMethod());
		addIDUEvent(rowProduct);
		return rowProduct;
	}
	@Override
	protected ClienteNoEncontradoEnPileta createObject() {
		ClienteNoEncontradoEnPileta dpp = new ClienteNoEncontradoEnPileta();
		if(getNombre().getValue() != null)
			dpp.setNombre(getNombre().getValue());
		
		if(getTelefono().getValue() != null)
			dpp.setTelefono(getTelefono().getValue());
		
		if(getCelular().getValue() != null)
			dpp.setCelular(getCelular().getValue());
		
		if(getComentario().getValue() != null)
			dpp.setComentario(getComentario().getValue());
		
		dpp.setFecha(new Date());
		
		if (Sessions.getCurrent().getAttribute("idClase") != null) {
			Clase cur=(Clase)Sessions.getCurrent().getAttribute("idClase"); 
			dpp.setClase(null);
		}
		
		return dpp;
	}
	
	@Override
	protected ClienteNoEncontradoEnPileta fillObject(ClienteNoEncontradoEnPileta item) {
		return item;
	}
	
	@Override
	public GridList getGridList() {
		return productListBox;
	}
	@Override
	protected String getPanelTitle() {
		return I18N.getLabel("curso.alumnosNoEnco");
	}
	
	@Override
	protected boolean itemExists(Listitem item) {

		return false;
	}
	
	@Override
	protected boolean insert(){
		super.insert();
		return true;
	}
	
	@Override
	protected void delete(){
		super.delete();
	}

	public Set<ClienteNoEncontradoEnPileta> getProducts() {
		ClienteNoEncontradoEnPileta dpp = null;
		Set<ClienteNoEncontradoEnPileta> dpps = new HashSet<ClienteNoEncontradoEnPileta>();
		for (Object row : productListBox.getItems()) {
			dpp = (ClienteNoEncontradoEnPileta)((Listitem)row).getValue();
			dpps.add(dpp);
		}
		return dpps;
	}
	
	@Override
	protected GridCrud makeFields() {
		
		productCrudGrid = new GridCrud();
		productCrudGrid.setFixedLayout(true);
		productCrudGrid.setWidth("auto");
				
		nombre= new Textbox();
		nombre.setConstraint(new TextConstraint());
		nombre.setMaxlength(50);
		productCrudGrid.addField(new RequiredLabel(I18N.getLabel("curso.cliente.no.encontrados.nomyape")), nombre);

		telefono= new Textbox();
		telefono.setConstraint(new TextConstraint());
		telefono.setMaxlength(50);
		productCrudGrid.addField(new RequiredLabel(I18N.getLabel("curso.cliente.no.encontrados.nomyape.telefono")), telefono);

		celular= new Textbox();
		celular.setConstraint(new TextConstraint());
		celular.setMaxlength(50);
		productCrudGrid.addField(new RequiredLabel(I18N.getLabel("curso.cliente.no.encontrados.nomyape.celular")), celular);
		
		comentario= new Textbox();
		comentario.setConstraint(new TextConstraint());
		comentario.setMaxlength(100);
		productCrudGrid.addField(new Label(I18N.getLabel("curso.cliente.no.encontrados.nomyape.comentario")), comentario);
	
		return productCrudGrid;
	}
	
	@Override
	protected GridList makeList() {
		productListBox = new GridList();
		productListBox.setMultiple(false);
		productListBox.setPageSize(15);
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.cliente.no.encontrados.nomyape"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.cliente.no.encontrados.nomyape.telefono"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.cliente.no.encontrados.nomyape.celular"))));
		productListBox.addHeader((new Listheader(I18N.getLabel("curso.cliente.no.encontrados.nomyape.comentario"))));
		productListBox.setHeight("150px"); 
		return productListBox;
	}
	
	@Override
	public void updateFields() {
		if (productListBox.getSelectedItem() == null)
			return;
		ClienteNoEncontradoEnPileta dpp =(ClienteNoEncontradoEnPileta)productListBox.getSelectedItem().getValue();
		
		nombre.setValue(dpp.getNombre());
		telefono.setValue(dpp.getTelefono());
		celular.setValue(dpp.getCelular());
		comentario.setValue(dpp.getComentario());

	}
	
	public void updateProductTotalDelivery() {
		Set<ClienteNoEncontradoEnPileta> dppBackup = getProducts();
		this.getGridList().removeAll();
		getProducts().addAll(dppBackup);
		this.fillListBox(dppBackup);
	}

	@Override
	protected ClienteNoEncontradoEnPileta updateList() {
		
		if (productListBox.getSelectedItem() == null)
			return null;
		
		ClienteNoEncontradoEnPileta newActionProduct =(ClienteNoEncontradoEnPileta)productListBox.getSelectedItem().getValue();
			
		newActionProduct.setNombre(nombre.getValue());
		newActionProduct.setTelefono(telefono.getValue());
		newActionProduct.setCelular(celular.getValue());
		newActionProduct.setComentario(comentario.getValue());
		
		if (Sessions.getCurrent().getAttribute("idClase") != null) {
			Clase cur=(Clase)Sessions.getCurrent().getAttribute("idClase"); 
			newActionProduct.setClase(null);
		}
		
		return newActionProduct;
	}
	
	@Override
	protected boolean validateCrud() {
		getNombre().clearErrorMessage();
		getTelefono().clearErrorMessage();
		if (getNombre().getValue()==null || 
				(getNombre().getValue()!=null && getNombre().getValue().equalsIgnoreCase(""))) {
				throw new WrongValueException(getNombre(), I18N.getLabel("error.empty.field"));
		}
		if (getTelefono().getValue()==null && getCelular().getValue()==null 
				|| 
			(getTelefono().getValue()!=null && getCelular().getValue()!=null
				&& getTelefono().getValue().equalsIgnoreCase("") && getCelular().getValue().equalsIgnoreCase("")	)	) {
			throw new WrongValueException(getTelefono(), I18N.getLabel("error.empty.field"));
		}
		return true;
	}
	
	public String getUpdateMethod() {
		return updateMethod;
	}
	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

	public Textbox getTelefono() {
		return telefono;
	}

	public void setTelefono(Textbox telefono) {
		this.telefono = telefono;
	}

	public Textbox getCelular() {
		return celular;
	}

	public void setCelular(Textbox celular) {
		this.celular = celular;
	}

	public Textbox getComentario() {
		return comentario;
	}

	public void setComentario(Textbox comentario) {
		this.comentario = comentario;
	}
}
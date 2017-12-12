package com.institucion.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class ActividadesDeEmpleadosList extends PanelRoutingCrud<ObraSocialesPrecio> {
	private static final long serialVersionUID = 1L;
	
	public ActividadesDeEmpleadosList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);

	}
	@Override
	protected boolean setPanelTitle() {
		this.addAuxheader("Actividad", 1,1);
		this.addAuxheader("Ejerce?", 1,1);
//		this.addAuxheader("Se paga una únca vez?", 1,1);
//		this.addAuxheader("Copago", 1,1, "20%");
//		this.addAuxheader("Precio que Paga Obra Social(Por Sesion)", 1,1, "20%");

		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	public void setField(List<EmpleadoActividades> productosList) {
		cleanRows();

		if(productosList != null){
			for (EmpleadoActividades prod : productosList) {

				if(prod.getActividad() != null){
					Label lab= new Label(prod.getActividad().getNombre());
					lab.setAttribute("valorObjeto", prod);
					this.addField(lab);
				}else
					this.addField(new Label(" "));
				
				if(prod.isDisponible())
					this.addField(getComboBox(prod.isDisponible()));
				else
					this.addField(getComboBox(false));
				super.addRow();
				
			}		
		}
	}

	
	public Combobox getComboBox(boolean selected) {
		Combobox cb = new Combobox();
		cb.setConstraint("strict");

		Comboitem  item = new Comboitem(I18N.getLabel("boolean.true"));
		item.setValue(true);
		cb.appendChild(item);

		Comboitem   item2 = new Comboitem(I18N.getLabel("boolean.false"));
		item2.setValue(false);
		cb.appendChild(item2);


		if(selected)
			cb.setSelectedItem(item);
		else
			cb.setSelectedItem(item2);
		
		return cb;
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

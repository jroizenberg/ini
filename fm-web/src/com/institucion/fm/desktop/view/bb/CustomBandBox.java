package com.institucion.fm.desktop.view.bb;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;

import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CustomBandBox<T> extends Bandbox {
	private static final long serialVersionUID = 1L;
	private Bandpopup popup = null;
	private BandBoxPanel<T> panel = null;
	private T value;

	public CustomBandBox(BandBoxPanel<T> panel) {
		super();
		this.setCtrlKeys("#del"); // atrapo la tecla DEL
		if (panel == null) {
			throw new RuntimeException("'No se ha seteado el panel del CustomBandBox");
		}
		this.setReadonly(true);
		this.panel = panel;
		this.panel.getListFromPanel().addForward("onSelect", this, "onSelecto");
		this.addForward("onCtrlKey", this, "onCtrlKeyo");
		this.setHeight("auto");
		this.popup = new Bandpopup();
		if (!Executions.getCurrent().isBrowser("ie6-")) {
			this.popup.setHeight("100%");
		}
		this.popup.appendChild(panel);

		this.appendChild(this.popup);
	}
	
	@SuppressWarnings("unchecked")
	public void onSelecto(Event event) throws Exception {
		T t = (T)this.getListFromPanel().getSelectedItem().getValue();
		this.setObjectValue(t);
		this.close();
		this.closeDropdown();
	}

	public void onCtrlKeyo(Event event) throws Exception {
		this.setObjectValue(null);
	}
	
	public void setObjectValue(T value) {
		this.value = value;
		if (this.value == null) {
			// FIXME Debe ser null, pero por alguna razon no me esta limpiando el bandbox aun haciendole un setText("")
			this.setValue(" "); 
		} else {
			this.setValue(this.panel.stringValue(this.value));
		}
	}
	
	public T getObjectValue() {
		this.getValue(); // workaround para que salte el constraint
		return this.value;
	}
	
	// Delegadores de BandBoxPanel
	public CriteriaClause getCriteriaFilters() {
		return this.panel.getCriteriaFilters();
	}

	public GridList getListFromPanel() {
		return this.panel.getListFromPanel();
	}

	public void setList(List<T> list) {
		this.panel.setList(list);
	}
	
}

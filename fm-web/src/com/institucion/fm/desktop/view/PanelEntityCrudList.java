package com.institucion.fm.desktop.view;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Separator;
import org.zkoss.zul.api.Toolbar;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.model.ClienteNoEncontradoEnPileta;
import com.institucion.model.ClienteNoEncontradoEnPiletaHistorico;

public abstract class PanelEntityCrudList<T> extends Panel implements AfterCompose{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ID = "panelEntityCrudList";
//	private Button updateButton;
	private Button deleteButton;
	private Button insertButton;
	private Button cleanButton;
	private Set<T>itemsBCK;
	protected boolean disable;

	public PanelEntityCrudList() {
		super();
		setWidth("auto");
		setId(ID);
		setHeight("auto");
		Panelchildren panelchildren = new Panelchildren();
		this.appendChild(panelchildren);
	}

//	public Button getUpdateButton() {
//		return updateButton;
//	}
//
//	public void setUpdateButton(Button updateButton) {
//		this.updateButton = updateButton;
//	}

	public void setButtonsVisible(boolean visible) {
		this.deleteButton.setVisible(visible);
		this.insertButton.setVisible(visible);
		this.cleanButton.setVisible(visible);
	}
	
	@Override
	public void afterCompose() {
		makeTitle();
		this.getPanelchildren().appendChild(makeFields());
		this.getPanelchildren().appendChild(new Separator());
		this.getPanelchildren().appendChild(makeList());
		this.getPanelchildren().appendChild(makeToolbar());
		setInsertMode();
	}
	
	protected abstract GridCrud makeFields();
	protected abstract GridList makeList();
	protected abstract void cleanCrud();
	protected abstract GridList getGridList();
	protected abstract boolean validateCrud();
	protected abstract T createObject();
	protected abstract Listitem createItem(T item);
	protected abstract Listitem createItem2(ClienteNoEncontradoEnPiletaHistorico item);
	protected abstract boolean itemExists(Listitem item);
	protected abstract T fillObject (T item);
	public abstract void updateFields();
	protected abstract String getPanelTitle();
	protected abstract T updateList();
	
	
	protected void makeTitle(){
		String title = getPanelTitle();
		if (title !=null){
			this.setTitle(title);
		}
	}
	
	protected Toolbar makeToolbar(){
		org.zkoss.zul.Toolbar toolbar = new org.zkoss.zul.Toolbar();
		toolbar.setAlign("end");

		// boton Limpiar 
		cleanButton = new Button();
		cleanButton.setLabel(I18N.getLabel("toolbar.clean"));
		cleanButton.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				cleanCrud();
			}
			}); 
		toolbar.appendChild(cleanButton);

		
		// boton modificar 
//		updateButton = new Button();
//		updateButton.setLabel(I18N.getLabel("toolbar.update"));
//		updateButton.addEventListener(Events.ON_CLICK, new EventListener() {
//			public void onEvent(Event evt){
//				update();
//			}
//			}); 
//		toolbar.appendChild(updateButton);

		// boton eliminar
		deleteButton = new Button();
		deleteButton.setLabel(I18N.getLabel("toolbar.delete"));
		
		deleteButton.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				delete();
			}
			});
		toolbar.appendChild(deleteButton);

		// boton eliminar 
		insertButton = new Button();
		insertButton.setLabel(I18N.getLabel("toolbar.insert"));
		insertButton.addEventListener(Events.ON_CLICK, new EventListener() {
			public void onEvent(Event evt){
				insert();
			}
		});
		toolbar.appendChild(insertButton);
		return toolbar;
	}
	
	protected void addIDUEvent(Listitem row){
		if (!disable){
			row.addEventListener(Events.ON_CLICK, new EventListener() { public void onEvent(Event evt) { setDeleteMode(); } });
			row.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() { public void onEvent(Event evt) { setUpdateMode();} });
		}
	}
	
	protected void addIDUEvent(Listitem row, boolean ismodification){
		if (!disable){
			if(ismodification){
				row.addEventListener(Events.ON_CLICK, new EventListener() { public void onEvent(Event evt) { setButtonsVisible(false); } });
				row.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() { public void onEvent(Event evt) { setButtonsVisible(false);} });	
			}else{
				row.addEventListener(Events.ON_CLICK, new EventListener() { public void onEvent(Event evt) { setDeleteMode(); } });
				row.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener() { public void onEvent(Event evt) { setUpdateMode();} });	
			}
		}
	}
	
	protected void delete(){
		Listitem lisItem = getGridList().getSelectedItem();
		
		if (lisItem == null)
			return;
		
		getGridList().removeChild(lisItem);
		setInsertMode();
	}

	protected boolean insert(){
		if(validateCrud()){
			Listitem item = createItem(createObject());
			
//			if (itemExists(item)){
//				MessageBox.validation(I18N.getLabel("duplicate.message"), I18N.getLabel("duplicate.error"));
//				return false;
//			}
			if(item != null){
				getGridList().addRow(item);
				setInsertMode();
				cleanCrud();
			}
		}
		return true;
		
	}
	
	public void fillListBox( Set <T>list){
		if (list!=null && list.size()>0){
			for (T t : list) {
				if(t instanceof ClienteNoEncontradoEnPileta){
					Listitem item=createItem(t);
					if(item != null)
						getGridList().addRow(item);
				}else if(t instanceof ClienteNoEncontradoEnPiletaHistorico){
					Listitem item=createItem2((ClienteNoEncontradoEnPiletaHistorico)t);
					if(item != null)
						getGridList().addRow(item);
				}else{
					Listitem item=createItem(t);
					if(item != null)
						getGridList().addRow(item);	
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Set<T> getItems(Set<T>items){
		itemsBCK = new HashSet<T>();
		if (items == null) 
			items = new LinkedHashSet<T>();
		items.clear();
		
		List<?> rows = getGridList().getItems();
		for (int i = 0; i < rows.size(); i++){
			Listitem row = (Listitem) rows.get(i);
			itemsBCK.add(fillObject((T)row.getValue()));
		}
		
		items.addAll(itemsBCK);
//		if (items!=null && !items.isEmpty()) {
//			items.clear();	
//		}
		return items;
	}
	
	protected void update() {
		
		if (getGridList().getSelectedItem() == null)
			return;
	
		validateCrud();
		
		Listitem item=createItem(updateList());
		if (item != null){
			getGridList().addRow(item);
			getGridList().removeChild(getGridList().getSelectedItem());
			setInsertMode();
			cleanCrud();
		}
	}
	
	protected void setInsertMode(){
		if (!disable){
//			updateButton.setDisabled(true);
			deleteButton.setDisabled(true);
			insertButton.setDisabled(false);
			cleanButton.setDisabled(true);
		}
	}

	protected void setDeleteMode(){
		if (!disable){
//			updateButton.setDisabled(true);
			deleteButton.setDisabled(false);
			insertButton.setDisabled(false);
			cleanButton.setDisabled(true);
		}
	}

	protected void setUpdateMode(){
		if (!disable){
//			updateButton.setDisabled(false);
			deleteButton.setDisabled(true);
			insertButton.setDisabled(false);
			cleanButton.setDisabled(false);
		}
	}
	
	protected void disableMode(boolean conf){
//		updateButton.setDisabled(conf);
		deleteButton.setDisabled(conf);
		insertButton.setDisabled(conf);
		cleanButton.setDisabled(conf);
		disable = conf;
	}
}
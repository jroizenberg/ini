package com.institucion.fm.desktop.view;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;

public class GridList extends Listbox implements ListDataListener {
	private static final long serialVersionUID = 1L;
	private String ID = "gridList";

	public GridList(){
		this.setId(ID);
		this.setWidth("auto");
		this.setHeight("auto");
		this.setFixedLayout(true);
		this.setMultiple(true);
		this.setCheckmark(true);
		this.setMold("paging");
		this.setPageSize(10);
		Listhead head = new Listhead();
//		head.setSizable(true);
		head.setSizable(false);
		this.appendChild(head);
		this.setDisabled(true);
		this.getPagingChild().setAutohide(false);
	}

	//para hacer paging manual
	public GridList(Boolean nopaging){
		this.setId(ID);
		this.setWidth("auto");
		this.setHeight("auto");
		this.setFixedLayout(true);
		this.setMultiple(true);
		this.setCheckmark(true);
		
		Listhead head = new Listhead();
//		head.setSizable(true);
		head.setSizable(false);
		this.appendChild(head);
		this.setDisabled(true);
	
	}
	
	public Listheader addHeader(Listheader header){
		header.setSort("auto");
		this.getListhead().appendChild(header);
		return header;
	}

	public Listheader addHeaderSinSort(Listheader header){
		this.getListhead().appendChild(header);
		return header;
	}

	public void addHeaderComponent(Component header){
		this.getListhead().appendChild(header);
	//	return header;
	}

	public void addRow(Listitem item){
		item.setClass("cells");
		Component nullComponent = null;
		item.addForward("onDoubleClick",nullComponent,"onDoubleClickEvt");
		this.appendChild(item);
	}

	public void addRow(Listitem item, String nameMethod){
		item.setClass("cells");
		Component nullComponent = null;
		item.addForward("onDoubleClick",nullComponent,nameMethod);
		this.appendChild(item);
	}

	
	public void addRowCursoList(Listitem item){
		item.setClass("cells");
		Component nullComponent = null;
		item.addForward("onClick",nullComponent,"onClickEvt");
		this.appendChild(item);
	}
	
	public void addRow2(Listitem item){
		this.appendChild(item);
	}
	
	public void removeAll(){
		this.getItems().clear();
	}

	public void disable(){
		for(Object item: this.getItems()){
			((Listitem)item).setDisabled(true);
		}
	}
	
	public void enable(){
		for(Object item: this.getItems()){
			((Listitem)item).setDisabled(false);
		}
	}

//	/**
//	 * @deprecated usar ORDER BY en Entity.find(CriteriaClause)
//	 */

	public void sort(boolean check) {
		Listheader header = ((Listheader)this.getListhead().getFirstChild());
		String sortDirection = header.getSortDirection();
		if (check) {
			header.sort("ascending".equals(sortDirection), true);
		} else {
			header.sort(true);
		}
	}
	/**
	 * sirve para setear a una lista los elementos seleccionados.
	 * @param item
	 */
	public void setSelectedListItem(Listitem item){
		this.setSelectedItem(item);
	}
	
	public void disableSort(Listheader header){
		header.setSort("none");
	}
	public void enableSort(Listheader header){
		header.setSort("auto");
	}

	@Override
	public void onChange(ListDataEvent event) {
		setModel(getModel());
		
	}
	
}
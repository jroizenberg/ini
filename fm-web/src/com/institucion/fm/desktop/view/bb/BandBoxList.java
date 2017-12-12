package com.institucion.fm.desktop.view.bb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.view.GridList;


public abstract class BandBoxList<T> extends GridList {
	private static final long serialVersionUID = 1L;

	public BandBoxList() {
		super();
	}

	public void initPanel(int width) {
		this.setWidth(width + "px");
		this.setMultiple(false);
		this.setCheckmark(false);
		this.addHeaders();
		this.sort(false);
	}
	

	public List<T> getList() {
		List<T> list = new ArrayList<T>();
		for (Iterator<?> it = this.getItems().iterator(); it.hasNext();) {
			Listitem li = (Listitem)it.next();
			@SuppressWarnings("unchecked")
			T item = (T)li.getValue();
			list.add(item);
		}
		return list;
	}

	public void setList(List<T> list) {
		this.removeAll();
		addList(list);
	}

	private void addList(List<T> list) {
		for (T item : list) {
			addRow(item);
		}
		sort();		
	}
	 
	private void sort() {
		this.sort(true);
	}

	abstract protected void addHeaders();
	abstract protected void addRow(T item);

}
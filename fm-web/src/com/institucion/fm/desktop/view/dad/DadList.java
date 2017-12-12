package com.institucion.fm.desktop.view.dad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.wf.model.User;

public abstract class DadList<T> extends GridList {
	private static final long serialVersionUID = 1L;

	private boolean sorteable=true; 
	
	public DadList() {
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

	public void removeAllList() {
		this.removeAll();
	}
	public void setList(List<T> list) {
		this.removeAll();
		addList(list);
	}

	public void addList(List<T> list) {
		if(list != null){
			for (T item : list) {
				if (!exist(item)) {
					addRow(item);
				}
			}
			if (sorteable)
				sort();			
		}
	}
	
	public List<T> getSelectedAndRemoveThem() {
		List<T> items = new ArrayList<T>();
		for (Iterator<?> it = this.getItems().iterator(); it.hasNext();) {
			Listitem listitem = (Listitem) it.next();
			if (listitem.isSelected()) {
				@SuppressWarnings("unchecked")
				T item = (T) listitem.getValue();
				items.add(item);
				it.remove();
			}
		}
		return items;
	}
	
	public void setSelectedItems(List<T> items) {
		boolean set;
		for(T item: items){
			if(item instanceof User){
				set = false;
				Iterator<?> listIterator = this.getItems().iterator();
				while(!set && listIterator.hasNext()){
					Listitem listitem = (Listitem) listIterator.next();
					if(listitem.getLabel().equals(((User)item).getUser().getFullName())){
						set = true;
						listitem.setSelected(true);
					}
				}
			}
			
		}
	}
	 

    public List<T> getSelectedItemsFromList() {
        List<T> items = new ArrayList<T>();
        for (Iterator<?> it = this.getItems().iterator(); it.hasNext();) {
            Listitem listitem = (Listitem) it.next();
            if (listitem.isSelected()) {
                @SuppressWarnings("unchecked")
                T item = (T) listitem.getValue();
                items.add(item);
            //    it.remove();
            }
        }
        return items;
    }
    
	
	
	private boolean exist(T newItem)	{
		for (Iterator<?> it = this.getItems().iterator(); it.hasNext();) {
			Listitem li = (Listitem) it.next();
			@SuppressWarnings("unchecked")
			T item = (T)li.getValue();
			if (this.areEquals(item, newItem)) {
				return true;
			}
		}
		return false;
	}
	
	private void sort() {
		this.sort(true);
	}

	abstract protected void addHeaders();
	abstract protected void addRow(T item);
	abstract protected boolean areEquals(T item1, T item2);
	
	public boolean isSorteable() {
		return sorteable;
	}

	public void setSorteable(boolean sorteable) {
		this.sorteable = sorteable;
	}
}

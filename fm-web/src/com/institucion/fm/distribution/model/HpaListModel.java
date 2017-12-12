package com.institucion.fm.distribution.model;

import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataListener;

import com.institucion.fm.cobj.UserClObj;

/**
 * The Class HpaComboModel.
 */
public class HpaListModel implements ListModel {

	/** The users. */
	private List<UserClObj> users;

	/**
	 * Instantiates a new hpa combo model.
	 * 
	 * @param users
	 *            the users
	 */
	public HpaListModel(List<UserClObj> users) {
		this.users = users;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zkoss.zul.ListModel#addListDataListener(org.zkoss.zul.event.
	 * ListDataListener)
	 */
	@Override
	public void addListDataListener(ListDataListener arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.zkoss.zul.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt(int position) {
		return users.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.zkoss.zul.ListModel#getSize()
	 */
	@Override
	public int getSize() {
		return users.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zkoss.zul.ListModel#removeListDataListener(org.zkoss.zul.event.
	 * ListDataListener)
	 */
	@Override
	public void removeListDataListener(ListDataListener arg0) {

	}

}
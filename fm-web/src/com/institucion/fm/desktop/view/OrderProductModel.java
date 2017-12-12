package com.institucion.fm.desktop.view;

import java.util.List;

import org.zkoss.zul.ListModel;
import org.zkoss.zul.event.ListDataListener;

import com.institucion.fm.cobj.PromotionLineCObj;

public class OrderProductModel implements ListModel {
	private List<PromotionLineCObj>lp;
	
	
	public OrderProductModel(List<PromotionLineCObj>lp) {
		this.lp=lp;	
	}
	
	@Override
	public void addListDataListener(ListDataListener l) 
	{
		
	}

	@Override
	public Object getElementAt(int index) {
		return lp.get(index);
	}

	@Override
	public int getSize() {
		return lp.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		// TODO Auto-generated method stub

	}
	

}

package com.institucion.fm.desktop.service;

import java.util.Iterator;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.MessageBox;

public abstract class SelectorFEComposer extends SelectorComposer{
	
	protected boolean hasAprovalPendingItems(GridList gridList){
//		boolean withAprovalPending = false;
//
//		@SuppressWarnings("unchecked")
//		Iterator<Listitem> itemsIt = gridList.getSelectedItems().iterator();
//		while (itemsIt.hasNext() && !withAprovalPending){
//			FEBase febase = (FEBase) itemsIt.next().getValue();
//			if (febase.getState().equals(FEState.APROVAL_PENDING)){
//				withAprovalPending = true;
//			}
//		}
//
//		if (withAprovalPending){
//			MessageBox.info(I18N.getLabel("selector.aprovpend.information"), I18N.getLabel("selector.aprovpend.title"));
//			return true;
//		}
		return false;
	}
//	protected boolean hasAprovalPendingItems(List<ContactView> gridList){
//		boolean withAprovalPending = false;

//		@SuppressWarnings("unchecked")
//		Iterator<ContactView> itemsIt = gridList.iterator();
//		while (itemsIt.hasNext() && !withAprovalPending){
//			ContactView febase = (ContactView) itemsIt.next();
//			if (febase.getFeState().equals(FEState.APROVAL_PENDING)){
//				withAprovalPending = true;
//			}
//		}

//		if (withAprovalPending){
//			MessageBox.info(I18N.getLabel("selector.aprovpend.information"), I18N.getLabel("selector.aprovpend.title"));
//			return true;
//		}
//		return false;
//	}
	
	
	
	protected boolean hasInactiveItems(GridList gridList){
		boolean withInactive = false;

//		@SuppressWarnings("unchecked")
//		Iterator<Listitem> itemsIt = gridList.getSelectedItems().iterator();
//		while (itemsIt.hasNext() && !withInactive){
//			FEBase febase = (FEBase) itemsIt.next().getValue();
//			if (febase.getState().equals(FEState.INACTIVE)){
//				withInactive = true;
//			}
//		}

		if (withInactive){
			MessageBox.info(I18N.getLabel("selector.inactive.information"), I18N.getLabel("selector.aprovpend.title"));
			return true;
		}
		return false;
	}
	
	protected boolean hasActiveOneItem(GridList gridList) {
		if (hasSelectedOneItem(gridList)){
//			FEBase febase = (FEBase)  gridList.getSelectedItem().getValue();
//			if (febase.getState().equals(FEState.ACTIVE)){
//				return true;
//			}
			MessageBox.info(I18N.getLabel("selector.hasactive.information"), I18N.getLabel("selector.hasactive.title"));
			return false;
		}
		return false;
	}


	protected boolean doAction(GridList  gridlist){
		if (!hasAprovalPendingItems(gridlist)){
			if (gridlist.getSelectedItems().size() != 0){
				return true;
			}
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
		}
		return false;
	}


	protected boolean doAction(Listbox  gridlist){
		if (!hasAprovalPendingItems(gridlist)){
			if (gridlist.getSelectedItems().size() != 0){
				return true;
			}
			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
		}
		return false;
	}
//	protected boolean doAction(List<ContactView> list){
//		if (!hasAprovalPendingItems(list)){
//			if (list.size() != 0){
//				return true;
//			}
//			MessageBox.info(I18N.getLabel("selector.actionwithoutitem.information"), I18N.getLabel("selector.actionwithoutitem.title"));
//		}
//		return false;
//	}

	protected boolean hasAprovalPendingItems(Listbox gridList){
		boolean withAprovalPending = false;

		@SuppressWarnings("unchecked")
		Iterator<Listitem> itemsIt = gridList.getSelectedItems().iterator();
		while ((!withAprovalPending) && (itemsIt.hasNext())){
//			ContactView contactView = (ContactView) itemsIt.next().getValue();
//			if (contactView.getFeState().equals(FEState.APROVAL_PENDING)){
//				withAprovalPending = true;
//			}
		}

		if (withAprovalPending){
			MessageBox.info(I18N.getLabel("selector.aprovpend.information"), I18N.getLabel("selector.aprovpend.title"));
			return true;
		}
		return false;
	}
}

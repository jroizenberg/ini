package com.institucion.fm.desktop.view.dad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.desktop.view.GridList;
import com.institucion.fm.desktop.view.PanelDaDFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public abstract class DadPanel<T> extends Panel implements AfterCompose {
	private static final long serialVersionUID = 1L;
	private boolean hasFilter = false;
	private static Log log = LogFactory.getLog(DadPanel.class);
	private DadList<T> daDList;
	public PanelDaDFilter panelDaDFilter; 

	public DadPanel() {
		super();
	}
	
	public void setFilter(String hasFilter) { 
		this.hasFilter = Boolean.valueOf(hasFilter); 
	}
	
	public String getFilter() { 
		return Boolean.toString(hasFilter); 
	}

	protected void createPanel(String panelId, String filterType) {
		setId(panelId);		
		setWidth("auto");
		setBorder("normal");
		setWidth("auto");
		Panelchildren panelchildren = new Panelchildren();
		this.appendChild(panelchildren);
		
		if (hasFilter) {
			panelDaDFilter = new PanelDaDFilter();
			panelDaDFilter.setUsefilter(filterType == null ? getDadFilterClassName() : filterType);
			panelDaDFilter.setId(getId()+"_filter");
			panelDaDFilter.afterCompose();
			this.getPanelchildren().appendChild(panelDaDFilter);
		}
		daDList = getDadList();
		daDList.setId(getId()+"_list");
		this.getPanelchildren().appendChild(daDList);		

	}

	public void afterCompose() {
		log.info("DadPanel.afterCompose");
		this.createPanel(this.getId(), null);
	}

	abstract protected String getDadFilterClassName();
	abstract protected DadList<T> getDadList();

	// Delegadores de Filter/List
	public GridList getListFromPanel() {
		return this.daDList;
	}	

	public void setList(List<T> list) {
		this.daDList.setList(list);
	}

	public void setSet(Set<T> set) {
		List<T> list = new ArrayList<T>();
		for (T t : set) {
			list.add(t);
		}
		this.daDList.setList(list);
	}

	public void addList(List<T> list) {
		this.daDList.addList(list);
	}

//	public void addList(List<BrandAndSegmentCObj> list,BrandAndSegmentCObj value)
//	{	
//		this.daDList.getItems().clear();
//		Iterator<BrandAndSegmentCObj> it = list.iterator();
//		while (it.hasNext()){
//			BrandAndSegmentCObj aux = it.next();
//			if (!exist(aux))
//				add(aux,value);
//		}
//		sort();		
//	}

//	private boolean exist(BrandAndSegmentCObj newValue){
//		BrandSegmentDaDPanel right = (BrandSegmentDaDPanel)this.getFellow("rightPanelBrandSegment");
//		Iterator<?> it = right.getListFromPanel().getItems().iterator();
//		
//		while (it.hasNext()) {
//			Listitem item = (Listitem)it.next();
//			BrandAndSegmentCObj brandSegment = (BrandAndSegmentCObj) item.getValue();
//			if (brandSegment.getBrand().getId().equals(newValue.getBrand().getId()) &&
//					brandSegment.getSegment().getId().equals(newValue.getSegment().getId())) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
	public void sort()
	{
		((Listheader) this.daDList.getListhead().getFirstChild()).sort(true);		
	}
	
//	private void add(BrandAndSegmentCObj aux,BrandAndSegmentCObj brandSegment)
//	{
//		Listitem row = new Listitem();
//		row.setValue(aux);
//		Listcell nameCell = new Listcell(aux.getBrand().getDescription());
//		Listcell nameCell2 = new Listcell(aux.getSegment().getDescription());
//	
//		row.appendChild(nameCell);
//		row.appendChild(nameCell2);
//		this.daDList.addRow(row);
//		if (aux.getBrand().getDescription().equals(brandSegment.getBrand().getDescription()) &&
//				aux.getSegment().getDescription().equals(brandSegment.getSegment().getDescription()) ){
//			this.daDList.setSelectedListItem(row);
//		}
//	}	
	
	public List<T> getList() {
		return this.daDList.getList();
	}
	
	public void removeAll() {
		this.daDList.removeAll();
	}
	
	public List<T> getSelectedAndRemoveThem() {
		return this.daDList.getSelectedAndRemoveThem();
	}
	
	public void setSelectedItems(List<T> items){
		this.daDList.setSelectedItems(items);
	}

   public List<T> getSelectedItems() {
       return  this.daDList.getSelectedItemsFromList();
    }

   public <T> List getItemsList(){
       return this.daDList.getItems();
   }
   
   public void selectAllItems() {
		this.daDList.selectAll();
	}
	
	public CriteriaClause getCriteriaFilters() {
		return this.panelDaDFilter.getGridFilter().getCriteriaFilters();
	}

	public GridFilter getPanelDaDFilter() {
		return panelDaDFilter.getGridFilter();
	}
	
//	public String getMasterUser(){
//		return ((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).getMasterUser();
//	}
//	
//	public void setMasterUser(String masterUser){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).setMasterUser(masterUser);
//	}
//	
//	public void setPromotionLines(List<PromotionLine> list){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).setPromotionLines(list);
//	}
//	
//	public void setSupervisors(List<User> list){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).setSupervisors(list);
//	}
//	
//	public void setSelectedSupervisor(User selectedSupervisor){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).setSelectedSupervisor(selectedSupervisor);
//	}
//	
//	public PromotionLine getPromotionLine(){
//		return ((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).getPromotionLineSelected();
//	}
//	
//	public void setSelectedPromotionLine(PromotionLine selectedPromotionLine){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).setSelectedPromotionLine(selectedPromotionLine);
//	}
//	public User getSupervisor(){
//		return ((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).getSupervisorSelected();
//	}
//	
//	public List<User> getSupervisors(){
//		return ((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).getSupervisors();
//	}
//	
//	public void clearFilters(){
//		((CycleDaDWFUserFilter) this.panelDaDFilter.getGridFilter()).clearFilters();
//		this.daDList.removeAll();
//	}

}

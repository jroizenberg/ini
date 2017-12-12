package com.institucion.model;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import com.institucion.desktop.delegated.CerrarCajaDelegate;
import com.institucion.desktop.helper.SucursalViewHelper;
import com.institucion.fm.conf.Session;
import com.institucion.fm.desktop.view.GridFilter;
import com.institucion.fm.filteradv.model.CriteriaClause;

public class CerrarCajaFilter extends GridFilter {
	private static final long serialVersionUID = 1L;

	private Combobox sucursal;
	private CerrarCajaDelegate delegate;	

	public CerrarCajaFilter()	{
		super();
		buildFilter();
		
	}

	private void buildFilter() {
		
		Row row1 = new Row();
		row1.appendChild(new Label("Sucursal"));
		
		sucursal = SucursalViewHelper.getComboBox();
		sucursal.setConstraint("strict");
		sucursal.addEventListener(Events.ON_CHANGE, new EventListener() {
			public void onEvent(Event evt){
//				if(sucursal.getSelectedItem() != null){
					
					delegate.onfindFromChangeSucursal();
//				}
			}
		});	

		
		if(Session.getAttribute("sucursalPrincipalSeleccionada") != null){
			SucursalEnum suc= null;
			if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof SucursalEnum ){
				suc=((SucursalEnum)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc.equals(SucursalEnum.CENTRO)){
					sucursal.setSelectedIndex(0);
				}else if(suc.equals(SucursalEnum.MAIPU)){
					sucursal.setSelectedIndex(1);
				}

			}else if((Session.getAttribute("sucursalPrincipalSeleccionada")) instanceof String ){
				String suc2=((String)Session.getAttribute("sucursalPrincipalSeleccionada"));
				if(suc2 != null && suc2.equalsIgnoreCase("TodasCentro")){
					suc= SucursalEnum.CENTRO;
				}else if(suc2 != null && suc2.equalsIgnoreCase("TodasMaipu")){
					suc= SucursalEnum.MAIPU;
				}
				if(suc.equals(SucursalEnum.CENTRO)){
					sucursal.setSelectedIndex(0);
				}else if(suc.equals(SucursalEnum.MAIPU)){
					sucursal.setSelectedIndex(1);
				}
			}
			sucursal.setDisabled(true);
		}

		row1.appendChild(sucursal);
		addRow(row1);
	}


	
	public CriteriaClause getCriteriaFilters(){
		CriteriaClause criteria = new CriteriaClause();

//		super.setPredicate(criteria, nombre, "companyName");

		return criteria;
	}

	public void clear()
	{
		Constraint c;
		c= sucursal.getConstraint();
		sucursal.setConstraint("");
		sucursal.setSelectedItem(null);
		sucursal.setConstraint(c);
	}

	public Combobox getSucursal() {
		return sucursal;
	}

	public void setSucursal(Combobox sucursal) {
		this.sucursal = sucursal;
	}

	public CerrarCajaDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(CerrarCajaDelegate delegate) {
		this.delegate = delegate;
	}

}
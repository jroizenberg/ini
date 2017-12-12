package com.institucion.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Rows;

import com.institucion.fm.desktop.service.I18N;
import com.institucion.fm.desktop.view.PanelRoutingCrud;

public class PulsoClinicaResumenList extends PanelRoutingCrud<PulsoClinica> {
	private static final long serialVersionUID = 1L;
	
	public PulsoClinicaResumenList() {
		super();
		this.setCheckbox();
		this.setUIEvent();
		this.setDisable(false);

	}
	@Override
	protected boolean setPanelTitle() {

		this.addAuxheader(I18N.getLabel("reporte.mes")		, 1,1);
		this.addAuxheader(I18N.getLabel("reporte.ingreso")		, 1,1);
		this.addAuxheader(I18N.getLabel("reporte.egreso.rrhh")			, 1,1);
		this.addAuxheader("Gastos y Ing/Egre caja", 1,1, "5%");
		this.addAuxheader(I18N.getLabel("reporte.total")	, 1,1, "3%");

		return false;//esto se usa para no dibujar el ruteo
	}
	
	@Override
	protected void doEvent(Event event) {

	}
	
	public void setField(List<PulsoClinica> productosList) {
		cleanRows();
		 DecimalFormat formateador = new DecimalFormat("###,###"); 
		if(productosList != null){
			for (PulsoClinica prod : productosList) {
				
				if(prod.getMes() != null){
					Label total=new Label(prod.getMes().toString());
					total.setStyle("font-weight:bold;");
					this.addField(total);
			
				}else
					this.addField(new Label(" "));
				
				if(prod.getCantidadIngreso() != null){
					
					Label listCell =null;
					listCell= new Label("$"+formateador.format (prod.getCantidadIngreso())); 
					this.addField(listCell);
				}else
					this.addField(new Label("$"+formateador.format(0)));
					
				if(prod.getCantidadEgresoRRHH() != null){
					
					this.addField(new Label("$"+formateador.format (prod.getCantidadEgresoRRHH()))); 
				}else
					this.addField(new Label("$"+formateador.format(0)));
				
				if(prod.getCantidadEgresoGastosMes() != null){
					this.addField(new Label("$"+formateador.format (prod.getCantidadEgresoGastosMes()))); 
				}else
					this.addField(new Label("$"+formateador.format(0)));
				
				Label listCell =null;
				if(prod.getCantidadTotal() != null){
					
					listCell= new Label("$"+formateador.format (prod.getCantidadTotal())); 

					if(prod.getCantidadTotal() > 0){
						listCell.setStyle("color:#0B610B !important; font-weight:bold; ");
						
					}else if(prod.getCantidadTotal() == 0){
						listCell.setStyle("font-weight:bold; ");
						
					} else{
						listCell.setStyle("color:#FF0000 !important; font-weight:bold; ");
					}

					this.addField(listCell);
				}else{
					listCell =new Label("$"+formateador.format(0));
					listCell.setStyle("font-weight:bold; ");
					this.addField(listCell);
				}
				super.addRow();
				
			}		
		}
	}

	@Override
	protected void setUIEvent() {
		this.setUievent(true);

	}

	@Override
	protected void setCheckbox() {
		this.setCheckbox(false);
	}


	@SuppressWarnings("unchecked")
	public void deleteRow() {

	}
	
	@SuppressWarnings("unchecked")
	public void cleanRows() {
		Rows rows =super.getRows();
		for (Iterator it = new ArrayList(rows.getChildren()).iterator(); it.hasNext();) {
			rows.removeChild((Component)it.next());
		} 
		super.addRow();
	}
	
}